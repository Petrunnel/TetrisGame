package com.petrynnel.tetrisgame.gamelogic

enum class ShiftDirection {
    /** Направление не определено (сдвиг не требуется) */
    AWAITING,

    /** Требуется сдвиг влево */
    LEFT,

    /** Требуется сдвиг вправо */
    RIGHT
}