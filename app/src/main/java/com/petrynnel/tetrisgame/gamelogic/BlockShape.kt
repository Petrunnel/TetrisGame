package com.petrynnel.tetrisgame.gamelogic

enum class BlockShape {
    SQUARE,
    ROUNDED_SQUARE,
    CIRCLE,
    BITMAP;

    companion object {
        val blockShapes = arrayOf(SQUARE, ROUNDED_SQUARE, CIRCLE, BITMAP)
    }
}