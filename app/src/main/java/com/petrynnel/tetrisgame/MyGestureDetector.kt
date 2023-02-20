package com.petrynnel.tetrisgame

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import com.petrynnel.tetrisgame.MainActivity.Companion.requestDownToBottom
import com.petrynnel.tetrisgame.MainActivity.Companion.requestRotate
import com.petrynnel.tetrisgame.gamelogic.Constants
import kotlin.math.abs

class MyGestureDetector : GestureDetector.SimpleOnGestureListener() {
    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        try {
            if (abs(e1.x - e2.x) > Constants.SWIPE_MAX_OFF_PATH) return false
            if (e2.y - e1.y > Constants.SWIPE_MIN_DISTANCE && abs(velocityY) > Constants.SWIPE_THRESHOLD_VELOCITY)
                requestDownToBottom()
        } catch (e: Exception) {
            Log.e("MyGestureDetector", "$e")
        }
        return false
    }

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        requestRotate()
        return super.onSingleTapConfirmed(e)
    }

    override fun onDoubleTap(e: MotionEvent): Boolean {
        requestRotate()
        return super.onDoubleTapEvent(e)
    }

    override fun onDown(e: MotionEvent): Boolean {
        return true
    }
}