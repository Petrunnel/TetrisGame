package com.petrynnel.tetrisgame.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.petrynnel.tetrisgame.databinding.ActivityMainBinding
import com.petrynnel.tetrisgame.gamelogic.*
import com.petrynnel.tetrisgame.gamelogic.Constants.BOOST_MULTIPLIER
import com.petrynnel.tetrisgame.gamelogic.Constants.DEFAULT_GAME_SPEED
import com.petrynnel.tetrisgame.gamelogic.Constants.FRAMES_PER_MOVE
import kotlinx.coroutines.*
import kotlin.math.abs

@SuppressLint("ClickableViewAccessibility")
class MainActivity : AppCompatActivity() {

    companion object {
        private lateinit var gameField: GameField

        private var endOfGame = false
        private var shiftDirection: ShiftDirection? = null
        private var isRotateRequested = false
        private var isDownToBottomRequested = false
        private var isBoostRequested = false
        private var loopNumber = 0

        fun requestRotate() {
            isRotateRequested = true
        }

        fun requestDownToBottom() {
            isDownToBottomRequested = true
        }

        fun getField(): GameField = gameField

        fun initFields() {
            loopNumber = 0
            endOfGame = false
            shiftDirection = ShiftDirection.AWAITING
            isRotateRequested = false
            gameField = GameField()
        }

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

            if (isDownToBottomRequested) { //Если есть запрос на мгновенное падение фигуры
                /*Сдвигаем фигуру до низа*/
                gameField.letFallDown(isDownToBottom = true)
                /* Ожидаем нового запроса */
                isDownToBottomRequested = false
            }

            /* Падение фигуры вниз происходит если loopNumber % FRAMES_PER_MOVE == 0
             * Т.е. 1 раз за FRAMES_PER_MOVE итераций или
             * FRAMES_PER_MOVE / BOOST_MULTIPLIER если есть запрос на ускорение падения
             */
            if (loopNumber % (FRAMES_PER_MOVE / if (isBoostRequested) BOOST_MULTIPLIER else 1) == 0) {
                gameField.letFallDown(isDownToBottom = false)
            }

            /* Увеличение номера итерации (по модулю FPM)*/
            loopNumber = (loopNumber + 1) % FRAMES_PER_MOVE

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

        initGame()
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

    private fun initControls() {
        with(binding) {
            canvas.setOnTouchListener(gestureListener)
            btnPause.setOnClickListener {
                btnPause.changeState()
                if (btnPause.isPaused()) {
                    gameStop()
                    pause.visibility = View.VISIBLE
                } else {
                    gameStart()
                    pause.visibility = View.GONE
                }
            }
        }
    }

    private fun initGame() {
        initControls()
        initFields()
        setBest()
    }

    private fun gameStart() {
        binding.btnPause.setState(PlayPauseView.STATE_PLAY)
        job = CoroutineScope(Dispatchers.IO).launch {

            /* Основной цикл игры*/
            while (!endOfGame) {
                logic()
                gameSpeed = getField().gameSpeed
                delay(gameSpeed)
                binding.canvas.invalidate()
            }
            withContext(NonCancellable) {
                CoroutineScope(Dispatchers.Main).launch {
                    resetBest()
                    showGameOver()
                }
            }
        }
    }

    private fun gameStop() {
        job?.cancel()
        resetBest()
    }

    private fun gameRestart() {
        gameStop()
        initFields()
        setBest()
        gameStart()
    }

    private fun showGameOver() {
        with(binding) {
            btnPause.visibility = View.GONE
            gameOver.visibility = View.VISIBLE
            btnRestart.setOnClickListener {
                gameRestart()
                btnPause.visibility = View.VISIBLE
                gameOver.visibility = View.GONE
            }
            btnExit.setOnClickListener {
                this@MainActivity.finish()
            }
        }
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