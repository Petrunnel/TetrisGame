package com.petrynnel.tetrisgame.gamelogic

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.petrynnel.tetrisgame.TetrisApp.Companion.prefHelper
import com.petrynnel.tetrisgame.gamelogic.Vibration.VIBRATION_DURATION_DEFAULT


object Vibration {
    /* Продолжительность вибрации */
    const val VIBRATION_DURATION_SHORT = 20L
    const val VIBRATION_DURATION_DEFAULT = 100L
}

fun Context.vibrate(duration: Long = VIBRATION_DURATION_DEFAULT) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            this.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    val canVibrate: Boolean = vibrator.hasVibrator()

    if (canVibrate && prefHelper.loadHasVibration()) {
        vibrator.vibrate(
            VibrationEffect.createOneShot(
                duration,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
    }
}