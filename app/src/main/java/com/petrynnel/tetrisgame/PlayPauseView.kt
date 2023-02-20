package com.petrynnel.tetrisgame

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat


class PlayPauseView : AppCompatImageView {
    private var mPlayToPauseAnim: AnimatedVectorDrawableCompat? = null
    private var mPauseToPlay: AnimatedVectorDrawableCompat? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        mPlayToPauseAnim = AnimatedVectorDrawableCompat.create(context, R.drawable.play_to_pause)
        mPauseToPlay = AnimatedVectorDrawableCompat.create(context, R.drawable.pause_to_play)
        setState(STATE_PLAY)
    }

    fun setState(state: Int) {
        when (state) {
            STATE_PLAY -> {
                setImageDrawable(mPlayToPauseAnim)
                mPlayToPauseAnim!!.start()
                isActivated = false
            }
            STATE_PAUSE -> {
                setImageDrawable(mPauseToPlay)
                mPauseToPlay!!.start()
                isActivated = true
            }
        }
    }

    fun changeState() {
        if (isPaused()) setState(STATE_PLAY) else setState(STATE_PAUSE)
    }

    fun isPaused() = isActivated

    companion object {
        const val STATE_PLAY = 1
        const val STATE_PAUSE = 2
    }

}