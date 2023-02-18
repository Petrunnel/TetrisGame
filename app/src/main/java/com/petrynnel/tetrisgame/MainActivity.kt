package com.petrynnel.tetrisgame

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.petrynnel.tetrisgame.databinding.ActivityMainBinding
import com.petrynnel.tetrisgame.gamelogic.Constants.DEFAULT_GAME_SPEED
import com.petrynnel.tetrisgame.gamelogic.Constants.FAST_DOWN_SPEED
import com.petrynnel.tetrisgame.gamelogic.Constants.SWIPE_MAX_OFF_PATH
import com.petrynnel.tetrisgame.gamelogic.Constants.SWIPE_MIN_DISTANCE
import com.petrynnel.tetrisgame.gamelogic.Constants.SWIPE_THRESHOLD_VELOCITY
import com.petrynnel.tetrisgame.gamelogic.Main.endOfGame
import com.petrynnel.tetrisgame.gamelogic.Main.initFields
import com.petrynnel.tetrisgame.gamelogic.Main.isRotateRequested
import com.petrynnel.tetrisgame.gamelogic.Main.logic
import com.petrynnel.tetrisgame.gamelogic.Main.shiftDirection
import com.petrynnel.tetrisgame.gamelogic.ShiftDirection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@SuppressLint("ClickableViewAccessibility")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var mDownX = 0F
    private var mDownY = 0F
    private var isActionRotate: Boolean = false
    private var gameSpeed: Long = DEFAULT_GAME_SPEED

    private val gestureDetector by lazy { GestureDetector(this, MyGestureDetector()) }
    private val gestureListener by lazy {
        View.OnTouchListener { _, motionEvent ->
            gestureDetector.onTouchEvent(motionEvent)
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    mDownX = motionEvent.rawX
                    mDownY = motionEvent.rawY
                    isActionRotate = true
                    return@OnTouchListener true
                }
                MotionEvent.ACTION_UP -> {
                    gameSpeed = DEFAULT_GAME_SPEED
                    if (isActionRotate) {
                        isRotateRequested = true
                        binding.canvas.invalidate()
                    }
                    return@OnTouchListener true
                }

                MotionEvent.ACTION_MOVE -> {
                    val deltaX: Float = motionEvent.rawX - mDownX
                    val deltaY: Float = motionEvent.rawY - mDownY
                    if (abs(deltaX) > 80 && abs(deltaY) < 160) {
                        mDownX = motionEvent.rawX
                        isActionRotate = false
                        if (deltaX > 0) {
                            shiftDirection = ShiftDirection.LEFT
                            binding.canvas.invalidate()
                        }
                        if (deltaX < 0) {
                            shiftDirection = ShiftDirection.RIGHT
                            binding.canvas.invalidate()
                        }
                    }
                    if (deltaY > 160) {
                        isActionRotate = false
                        gameSpeed = FAST_DOWN_SPEED
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
        game()
    }

    private fun game() {

        CoroutineScope(Dispatchers.IO).launch {
            initFields()
            // gameplay infinite
            while (!endOfGame) {
                logic()
                //game speed in millisecond
                delay(gameSpeed)
                binding.canvas.invalidate()
            }
        }

    }

    internal class MyGestureDetector : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            try {
                if (abs(e1.x - e2.x) > SWIPE_MAX_OFF_PATH) return false
                if (e2.y - e1.y > SWIPE_MIN_DISTANCE && abs(velocityY) > SWIPE_THRESHOLD_VELOCITY)
//                    while (!Falling.willLanding(1)) {
//                        Falling.fallingStep()
//                    }
                    return false
            } catch (e: Exception) {
                // nothing
            }
            return false
        }

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
    }

}