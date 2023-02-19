package com.petrynnel.tetrisgame

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.petrynnel.tetrisgame.databinding.ActivityMainBinding
import com.petrynnel.tetrisgame.gamelogic.Constants.DEFAULT_GAME_SPEED
import com.petrynnel.tetrisgame.gamelogic.Constants.SWIPE_MAX_OFF_PATH
import com.petrynnel.tetrisgame.gamelogic.Constants.SWIPE_MIN_DISTANCE
import com.petrynnel.tetrisgame.gamelogic.Constants.SWIPE_THRESHOLD_VELOCITY
import com.petrynnel.tetrisgame.gamelogic.Main
import com.petrynnel.tetrisgame.gamelogic.Main.endOfGame
import com.petrynnel.tetrisgame.gamelogic.Main.initFields
import com.petrynnel.tetrisgame.gamelogic.Main.isBoostRequested
import com.petrynnel.tetrisgame.gamelogic.Main.isDownToBottomRequested
import com.petrynnel.tetrisgame.gamelogic.Main.isRotateRequested
import com.petrynnel.tetrisgame.gamelogic.Main.logic
import com.petrynnel.tetrisgame.gamelogic.Main.shiftDirection
import com.petrynnel.tetrisgame.gamelogic.ShiftDirection
import kotlinx.coroutines.*
import kotlin.math.abs

@SuppressLint("ClickableViewAccessibility")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var mDownX = 0F
    private var mDownY = 0F
    private var isActionRotate: Boolean = false
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
                    isActionRotate = true
                    return@OnTouchListener true
                }
                MotionEvent.ACTION_UP -> {
                    isBoostRequested = false
                    if (isActionRotate) {
                        isRotateRequested = true
                        binding.canvas.invalidate()
                    }
                    return@OnTouchListener true
                }

                MotionEvent.ACTION_MOVE -> {
                    val deltaX: Float = motionEvent.rawX - mDownX
                    val deltaY: Float = motionEvent.rawY - mDownY
                    if (abs(deltaX) > 50 && abs(deltaY) < 160) {
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
        hideSystemUI()
        binding.canvas.setOnTouchListener(gestureListener)
        initFields()
        setBest()

    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun showSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, binding.root).show(WindowInsetsCompat.Type.systemBars())
    }

    override fun onResume() {
        super.onResume()
        gameStart()
    }

    override fun onPause() {
        gameStop()
        super.onPause()
    }

    private fun gameStart() {
        job = CoroutineScope(Dispatchers.IO).launch {

            // gameplay infinite
            while (!endOfGame) {
                logic()
                //game speed in millisecond
                gameSpeed = Main.getField().gameSpeed
                delay(gameSpeed)
                binding.canvas.invalidate()
            }
            resetBest()
        }
    }

    private fun gameStop() {
        job?.cancel()
        resetBest()
    }

    private fun setBest() {
        val sharedPreference = getSharedPreferences("HIGH_SCORE", Context.MODE_PRIVATE)
        Main.getField().best = sharedPreference.getInt("high_score", 0)
    }

    private fun resetBest() {
        val sharedPreference = getSharedPreferences("HIGH_SCORE", Context.MODE_PRIVATE)
        if (Main.getField().score > sharedPreference.getInt("high_score", 0)) {
            val editor = sharedPreference.edit()
            editor.putInt("high_score", Main.getField().score)
            editor.apply()
            Main.getField().best = sharedPreference.getInt("high_score", 0)
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
                    isDownToBottomRequested = true
            } catch (e: Exception) {
                Log.e("MyGestureDetector", "$e")
            }
            return false
        }

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
    }

}