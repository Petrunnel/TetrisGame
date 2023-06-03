package com.petrynnel.tetrisgame.gamelogic

import com.petrynnel.tetrisgame.R

enum class GameFieldBackgroundColor(val color: Int) {

    BLACK(R.color.background_black),
    GRAY(R.color.background_gray),
    WHITE(R.color.background_white),
    RED(R.color.background_red),
    GREEN(R.color.background_green),
    BLUE(R.color.background_blue),
    YELLOW(R.color.background_yellow);

    companion object {
        val backgroundColors = arrayOf(BLACK, GRAY, WHITE, RED, GREEN, BLUE, YELLOW)
    }
}