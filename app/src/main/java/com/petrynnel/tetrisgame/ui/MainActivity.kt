package com.petrynnel.tetrisgame.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.petrynnel.tetrisgame.TetrisApp.Companion.prefHelper
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
        private var gameField: GameField? = null

        private var endOfGame = false
        private var shiftDirection: ShiftDirection? = null
        private var isRotateRequested = false
        private var isDownToBottomRequested = false
        private var isBoostRequested = false
        private var loopNumber = 0
        private var isNewGame = true

        fun requestRotate() {
            isRotateRequested = true
        }

        fun requestDownToBottom() {
            isDownToBottomRequested = true
        }

        fun getField(): GameField = gameField ?: GameField()

        fun initFields() {
            loopNumber = 0
            endOfGame = false
            shiftDirection = ShiftDirection.AWAITING
            isRotateRequested = false
            gameField = getField()
        }

        fun logic() {

            if (shiftDirection != ShiftDirection.AWAITING) { // Если есть запрос на сдвиг фигуры

                /* Пробуем сдвинуть */
                getField().tryShiftFigure(shiftDirection)

                /* Ожидаем нового запроса */
                shiftDirection = ShiftDirection.AWAITING
            }
            if (isRotateRequested) { // Если есть запрос на поворот фигуры

                /* Пробуем повернуть */
                getField().tryRotateFigure()

                /* Ожидаем нового запроса */
                isRotateRequested = false
            }

            if (isDownToBottomRequested) { //Если есть запрос на мгновенное падение фигуры
                /*Сдвигаем фигуру до низа*/
                getField().letFallDown(isDownToBottom = true)
                /* Ожидаем нового запроса */
                isDownToBottomRequested = false
            }

            /* Падение фигуры вниз происходит если loopNumber % FRAMES_PER_MOVE == 0
             * Т.е. 1 раз за FRAMES_PER_MOVE итераций или
             * FRAMES_PER_MOVE / BOOST_MULTIPLIER если есть запрос на ускорение падения
             */
            if (loopNumber % (FRAMES_PER_MOVE / if (isBoostRequested) BOOST_MULTIPLIER else 1) == 0) {
                getField().letFallDown(isDownToBottom = false)
            }

            /* Увеличение номера итерации (по модулю FPM)*/
            loopNumber = (loopNumber + 1) % FRAMES_PER_MOVE

            /* Если поле переполнено, игра закончена */
            endOfGame = endOfGame || getField().isOverfilled == true
        }
    }

    private lateinit var binding: ActivityMainBinding

    private var mDownX = 0F
    private var mDownY = 0F
    private var gameSpeed = getField().gameSpeed
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
                    if (!endOfGame)
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
        loadField()
        if (gameField != null) isNewGame = false
        initControls()
        initFields()
        setBest()
    }

    private fun gameStart() {
        binding.btnPause.setState(PlayPauseView.STATE_PLAY)
        job = CoroutineScope(Dispatchers.IO).launch {
            withContext(NonCancellable) {
                CoroutineScope(Dispatchers.Main).launch {
                    if (!isNewGame) showContinueGame()
                    else gameSpeed = DEFAULT_GAME_SPEED - getField().initialLevel
                    isNewGame = true
                }
            }
            /* Основной цикл игры*/
            while (!endOfGame) {
                logic()
                gameSpeed = getField().gameSpeed
                delay(gameSpeed)
                binding.canvas.invalidate()
            }
            withContext(NonCancellable) {
                CoroutineScope(Dispatchers.Main).launch {
                    showGameOver()
                }
            }
        }
    }

    private fun gameStop() {
        saveField()
        job?.cancel()
    }

    private fun gameRestart() {
        gameStop()
        resetSavedField()
        initFields()
        gameSpeed = DEFAULT_GAME_SPEED - prefHelper.loadInitialLevel()
        setBest()
        gameStart()
    }

    private fun showGameOver() {
        with(binding) {
            gameStop()
            resetBest()
            btnPause.visibility = View.GONE
            gameOver.visibility = View.VISIBLE
            resetSavedField()
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

    private fun showContinueGame() {
        with(binding) {
            gameStop()
            btnPause.visibility = View.GONE
            continueGame.visibility = View.VISIBLE
            btnContinue.setOnClickListener {
                gameStart()
                btnPause.visibility = View.VISIBLE
                continueGame.visibility = View.GONE
            }
            btnNewGame.setOnClickListener {
                resetBest()
                gameRestart()
                btnPause.visibility = View.VISIBLE
                continueGame.visibility = View.GONE
            }
        }
    }

    private fun setBest() {
        getField().best = prefHelper.loadRecords().max()
    }

    private fun resetBest() {
        val bestScores = prefHelper.loadRecords()
        val minBestScore = bestScores.min()
        if (getField().score > minBestScore && !bestScores.contains(getField().score)) {
            bestScores[bestScores.indexOf(minBestScore)] = getField().score
            prefHelper.saveRecords(bestScores)
            getField().best = bestScores.max()
        }
    }

    private fun saveField() {
        prefHelper.saveField(gameField)
    }

    private fun loadField() {
        gameField = prefHelper.loadField()
    }

    private fun resetSavedField() {
        gameField = null
        prefHelper.saveField(gameField)
    }
}