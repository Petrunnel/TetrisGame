package com.petrynnel.tetrisgame.gamelogic

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import com.petrynnel.tetrisgame.R
import com.petrynnel.tetrisgame.TetrisApp.Companion.instance
import com.petrynnel.tetrisgame.TetrisApp.Companion.prefHelper


class SoundEffects {

    private var priority = 1
    private var priorityMusic = 2
    private var noLoop = 0
    private var loopForever = -1
    private var normalPlaybackRate = 1f
    private val audioManager = instance.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val attributes: AudioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_GAME)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()
    private val soundPool = SoundPool.Builder()
        .setAudioAttributes(attributes)
        .setMaxStreams(3)
        .build()

    private val soundPoolMusic = SoundPool.Builder()
        .setAudioAttributes(attributes)
        .setMaxStreams(1)
        .build()

    private val soundMoveId = soundPool.load(instance, R.raw.se_game_move, priority)
    private val soundRotateId = soundPool.load(instance, R.raw.se_game_rotate, priority)
    private val soundPauseId = soundPool.load(instance, R.raw.se_game_pause, priority)
    private val soundBlockLandingId = soundPool.load(instance, R.raw.se_game_landing, priority)
    private val soundBlockFallId = soundPool.load(instance, R.raw.se_game_bfall, priority)
    private val soundScoreId = soundPool.load(instance, R.raw.se_game_score, priority)
    private val soundSelectId = soundPool.load(instance, R.raw.se_sys_select, priority)
    private val soundOKId = soundPool.load(instance, R.raw.se_sys_ok, priority)
    private val soundAlertId = soundPool.load(instance, R.raw.se_sys_alert, priority)
    private val musicMainId = soundPoolMusic.load(instance, R.raw.mu_game_main, priority)


    private fun getCurVolume() = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
    private fun getMaxVolume() =
        audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()

    private fun getVolume() = getCurVolume() / getMaxVolume()


    fun playMoveEffect() {
        playEffect(soundMoveId)
    }

    fun playPauseEffect() {
        playEffect(soundPauseId)
    }

    fun playRotateEffect() {
        playEffect(soundRotateId)
    }

    fun playBlockLandingEffect() {
        playEffect(soundBlockLandingId)
    }

    fun playBlockFallEffect() {
        playEffect(soundBlockFallId)
    }

    fun playScoreEffect() {
        playEffect(soundScoreId)
    }

    fun playSelectEffect() {
        playEffect(soundSelectId)
    }

    fun playOKEffect() {
        playEffect(soundOKId)
    }

    fun playAlertEffect() {
        playEffect(soundAlertId)
    }

    fun playMusic() {
        soundPoolMusic.setOnLoadCompleteListener { soundPool, sampleId, status ->
            if (prefHelper.loadHasMusic()) {
                soundPool.play(
                    musicMainId,
                    getVolume(),
                    getVolume(),
                    priorityMusic,
                    loopForever,
                    normalPlaybackRate
                )
            }
            soundPool.autoPause()
        }
    }

    fun autoPause() {
        soundPoolMusic.autoPause()
    }

    fun autoResume() {
        soundPoolMusic.autoResume()
    }

    private fun playEffect(soundId: Int) {
        if (prefHelper.loadHasSoundEffects())
            soundPool.play(soundId, getVolume(), getVolume(), priority, noLoop, normalPlaybackRate)
    }
}