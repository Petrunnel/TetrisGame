package com.petrynnel.tetrisgame

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.petrynnel.tetrisgame.databinding.ActivityMainBinding
import com.petrynnel.tetrisgame.gamelogic.*
import com.petrynnel.tetrisgame.gamelogic.Constants.DEFAULT_GAME_SPEED
import kotlinx.coroutines.*
import kotlin.math.abs

@SuppressLint("ClickableViewAccessibility")
class MainActivity : AppCompatActivity() {
    companion object {
        /** Флаг для завершения основного цикла программы  */
        private var endOfGame = false

        /** Игровое поле. См. документацию GameField  */
        private lateinit var gameField: GameField

        /** Направление для сдвига, полученное за последнюю итерацию  */
        private var shiftDirection: ShiftDirection? = null

        /** Был ли за последнюю итерацию запрошен поворот фигуры  */
        private var isRotateRequested = false

        private var isDownToBottomRequested = false

        /** Было ли за последнюю итерацию запрошено ускорение падения */
        private var isBoostRequested = false

        /** Номер игровой итерации по модулю FRAMES_PER_MOVE.
         * Падение фигуры вниз происходит если loopNumber % FRAMES_PER_MOVE == 0
         * Т.е. 1 раз за FRAMES_PER_MOVE итераций.
         */
        private var loopNumber = 0

        fun requestRotate() {
            isRotateRequested = true
        }

        fun requestDownToBottom() {
            isDownToBottomRequested = true
        }

        fun getField(): GameField {
            return gameField
        }

        /**
         * Задаёт значения полей для начала игры
         */
        fun initFields() {
            loopNumber = 0
            endOfGame = false
            shiftDirection = ShiftDirection.AWAITING
            isRotateRequested = false
            gameField = GameField()
        }

        /**
         * Здесь происходят основные игровые действия --
         * запросы пользователя передаются на исполнение,
         * обновляется игровое поле (и фигура)
         */
        fun logic() {

            if (shiftDirection != ShiftDirection.AWAITING) { // Если есть запрос на сдвиг фигуры

                /* Пробуем сдвинуть */
                gameField.tryShiftFigure(shiftDirection)

                /* Ожидаем нового запроса */
                shiftDirection = ShiftDirection.AWAITING
            }
            if (isRotateRequested) { // Если есть запрос на поворот фигуры

                /* Пробуем повернуть */
                gameField.tryRotateFigure()

                /* Ожидаем нового запроса */
                isRotateRequested = false
            }

            if (isDownToBottomRequested) {
                gameField.letFallDown(isDownToBottom = true)
                isDownToBottomRequested = false
            }

            /* Падение фигуры вниз происходит если loopNumber % FRAMES_PER_MOVE == 0
             * Т.е. 1 раз за FRAMES_PER_MOVE итераций.
             */
            if (loopNumber % (Constants.FRAMES_PER_MOVE / if (isBoostRequested) Constants.BOOST_MULTIPLIER else 1) == 0) {
                gameField.letFallDown(isDownToBottom = false)
            }

            /* Увеличение номера итерации (по модулю FPM)*/
            loopNumber = (loopNumber + 1) % Constants.FRAMES_PER_MOVE

            /* Если поле переполнено, игра закончена */
            endOfGame = endOfGame || gameField.isOverfilled
        }
    }

    private lateinit var binding: ActivityMainBinding

    private var mDownX = 0F
    private var mDownY = 0F
    private var gameSpeed = DEFAULT_GAME_SPEED
    private var job: Job? = null

    private val gestureDetector by lazy { GestureDetector(this, MyGestureDetector()) }
    private val gestureListener by lazy {
        View.OnTouchListener { _, motionEvent ->
            gestureDetector.onTouchEvent(motionEvent)
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    mDownX = motionEvent.rawX
                    mDownY = motionEvent.rawY
                    return@OnTouchListener true
                }
                MotionEvent.ACTION_UP -> {
                    isBoostRequested = false
                    binding.canvas.invalidate()
                    return@OnTouchListener true
                }

                MotionEvent.ACTION_MOVE -> {
                    val deltaX: Float = motionEvent.rawX - mDownX
                    val deltaY: Float = motionEvent.rawY - mDownY
                    if (abs(deltaX) > 80 && abs(deltaY) < 160) {
                        mDownX = motionEvent.rawX
                        if (deltaX > 0) {
                            shiftDirection = ShiftDirection.LEFT
                            binding.canvas.invalidate()
                        }
                        if (deltaX < 0) {
                            shiftDirection = ShiftDirection.RIGHT
                            binding.canvas.invalidate()
                        }
                    }
                    if (deltaY > 250) {
                        isBoostRequested = true
                    }
                    return@OnTouchListener true
                }
                else -> false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.canvas.setOnTouchListener(gestureListener)
        binding.btnPause.setOnClickListener {
            binding.btnPause.changeState()
            if (binding.btnPause.isPaused()) {
                gameStop()
                binding.pause.visibility = View.VISIBLE
            } else {
                gameStart()
                binding.pause.visibility = View.GONE
            }
        }
        initFields()
        setBest()
    }

    override fun onResume() {
        super.onResume()
        if (!binding.btnPause.isPaused())
            gameStart()
    }

    override fun onPause() {
        if (!binding.btnPause.isPaused())
            binding.btnPause.performClick()
        gameStop()
        super.onPause()
    }

    private fun gameRestart() {
        gameStop()
        initFields()
        setBest()
        gameStart()
    }

    private fun showEndGameAlertDialog() {
        AlertDialog
            .Builder(this, R.style.YDialog)
            .setTitle("Game Over")
            .setCancelable(false)
            .setNegativeButton("Exit") { dialog, _ ->
                dialog.cancel()
                this.finish()
            }
            .setPositiveButton("Restart Game") { dialog, _ ->
                gameRestart()
                dialog.dismiss()
            }
            .show()
    }

    private fun gameStart() {
        binding.btnPause.setState(PlayPauseView.STATE_PLAY)
        job = CoroutineScope(Dispatchers.IO).launch {

            // gameplay infinite
            while (!endOfGame) {
                logic()
                //game speed in millisecond
                gameSpeed = getField().gameSpeed
                delay(gameSpeed)
                binding.canvas.invalidate()
            }
            withContext(NonCancellable) {
                CoroutineScope(Dispatchers.Main).launch {
                    resetBest()
                    showEndGameAlertDialog()
                }
            }
        }

    }

    private fun gameStop() {
        job?.cancel()
        resetBest()
    }

    private fun setBest() {
        val sharedPreference = getSharedPreferences("HIGH_SCORE", Context.MODE_PRIVATE)
        getField().best = sharedPreference.getInt("high_score", 0)
    }

    private fun resetBest() {
        val sharedPreference = getSharedPreferences("HIGH_SCORE", Context.MODE_PRIVATE)
        if (getField().score > sharedPreference.getInt("high_score", 0)) {
            val editor = sharedPreference.edit()
            editor.putInt("high_score", getField().score)
            editor.apply()
            getField().best = sharedPreference.getInt("high_score", 0)
        }
    }
}