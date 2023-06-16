package com.petrynnel.tetrisgame.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.petrynnel.tetrisgame.TetrisApp.Companion.prefHelper
import com.petrynnel.tetrisgame.databinding.ActivityMainBinding
import com.petrynnel.tetrisgame.gamelogic.*
import com.petrynnel.tetrisgame.gamelogic.Constants.FIELD_LABEL_LEVEL_Y
import com.petrynnel.tetrisgame.gamelogic.Constants.FIELD_LABEL_NEXT_Y
import com.petrynnel.tetrisgame.gamelogic.Constants.FIELD_LABEL_SCORE_Y
import com.petrynnel.tetrisgame.gamelogic.Constants.FIELD_TEXT_X
import com.petrynnel.tetrisgame.gamelogic.Constants.GAME_TICK_DELAY
import com.petrynnel.tetrisgame.gamelogic.Logic.boostRequested
import com.petrynnel.tetrisgame.gamelogic.Logic.getField
import com.petrynnel.tetrisgame.gamelogic.Logic.getGameField
import com.petrynnel.tetrisgame.gamelogic.Logic.initFields
import com.petrynnel.tetrisgame.gamelogic.Logic.isEndOfGame
import com.petrynnel.tetrisgame.gamelogic.Logic.isNewGame
import com.petrynnel.tetrisgame.gamelogic.Logic.logic
import com.petrynnel.tetrisgame.gamelogic.Logic.setGameField
import com.petrynnel.tetrisgame.gamelogic.Logic.setNewGame
import com.petrynnel.tetrisgame.gamelogic.Logic.setShiftDirection
import com.petrynnel.tetrisgame.setMargin
import com.petrynnel.tetrisgame.ui.main.PlayPauseView.Companion.STATE_PLAY
import kotlinx.coroutines.*
import kotlin.math.abs

@SuppressLint("ClickableViewAccessibility")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var mDownX = 0F
    private var mDownY = 0F
    private var job: Job? = null
    private val soundEffects = SoundEffects()

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
                    boostRequested(false)
                    if (!isEndOfGame())
                        binding.canvas.invalidate()
                    return@OnTouchListener true
                }

                MotionEvent.ACTION_MOVE -> {
                    val deltaX: Float = motionEvent.rawX - mDownX
                    val deltaY: Float = motionEvent.rawY - mDownY
                    if (abs(deltaX) > 60 && abs(deltaY) < 160) {
                        mDownX = motionEvent.rawX
                        if (deltaX > 0) {
                            setShiftDirection(ShiftDirection.LEFT)
                            binding.canvas.invalidate()
                        }
                        if (deltaX < 0) {
                            setShiftDirection(ShiftDirection.RIGHT)
                            binding.canvas.invalidate()
                        }
                    }
                    if (deltaY > 250) {
                        boostRequested(true)
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
        if (!binding.btnPause.isPaused()) binding.btnPause.performClick()
        gameStop()
        if (isEndOfGame()) resetField()
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
        soundEffects.playMusic()
        loadField()
        if (getGameField() != null) setNewGame(false)
        initControls()
        initFields()
        binding.tvBest.setMargin(left = FIELD_TEXT_X.toInt())
        binding.tvScore.setMargin(top = FIELD_LABEL_SCORE_Y)
        binding.tvLevel.setMargin(top = FIELD_LABEL_LEVEL_Y)
        binding.tvNext.setMargin(top = FIELD_LABEL_NEXT_Y)
        setBest()
    }

    private fun gameStart() {
        setStatePlay()
        job = CoroutineScope(Dispatchers.IO).launch {
            withContext(NonCancellable) {
                CoroutineScope(Dispatchers.Main).launch {
                    if (!isNewGame()) showContinueGame()
                    setNewGame(true)
                }
            }
            /* Основной цикл игры*/
            while (!isEndOfGame()) {
                soundEffects.autoResume()
                logic()
                delay(GAME_TICK_DELAY)
                binding.canvas.invalidate()
            }
                CoroutineScope(Dispatchers.Main).launch {
                    showGameOver()
            }
        }
    }

    private fun gameStop() {
        saveField()
        job?.cancel()
        soundEffects.autoPause()
    }

    private fun gameRestart() {
        gameStop()
        resetField()
        initFields()
        setBest()
        gameStart()
    }

    private fun showGameOver() {
        with(binding) {
            gameStop()
            soundEffects.playAlertEffect()
            resetBest()
            btnPause.visibility = View.GONE
            gameOver.visibility = View.VISIBLE
            resetSavedField()
            btnRestart.setOnClickListener {
                soundEffects.playOKEffect()
                gameRestart()
                setStatePlay()
                gameOver.visibility = View.GONE
            }
            btnExit.setOnClickListener {
                soundEffects.playOKEffect()
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
                setStatePlay()
                continueGame.visibility = View.GONE
            }
            btnNewGame.setOnClickListener {
                resetBest()
                gameRestart()
                setStatePlay()
                continueGame.visibility = View.GONE
            }
        }
    }

    private fun setStatePlay() {
        with(binding) {
            btnPause.setState(STATE_PLAY)
            pause.visibility = View.GONE
            btnPause.visibility = View.VISIBLE
        }
    }

    private fun setBest() {
        getField().setBest(prefHelper.loadRecords().max())
    }

    private fun resetBest() {
        val bestScores = prefHelper.loadRecords()
        val minBestScore = bestScores.min()
        if (getField().score > minBestScore && !bestScores.contains(getField().score)) {
            bestScores[bestScores.indexOf(minBestScore)] = getField().score
            prefHelper.saveRecords(bestScores)
            getField().setBest(bestScores.max())
        }
    }

    private fun saveField() {
        prefHelper.saveField(getGameField())
    }

    private fun loadField() {
        setGameField(prefHelper.loadField())
        getGameField()?.initSoundEffects()
    }

    private fun resetField() {
        setGameField(null)
        resetSavedField()
    }

    private fun resetSavedField() {
        prefHelper.saveField(null)
    }
}