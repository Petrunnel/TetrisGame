package com.petrynnel.tetrisgame.gamelogic

import android.graphics.Color
import java.util.*

enum class FigureColor(val color: Int) {

    EMPTY_BLOCK(Color.TRANSPARENT),
    SHADOW_BLOCK(Color.parseColor("#202020")),
    I_FORM(Color.parseColor("#00FFFF")),
    O_FORM(Color.parseColor("#FFFF00")),
    T_FORM(Color.parseColor("#800080")),
    J_FORM(Color.parseColor("#0000FF")),
    L_FORM(Color.parseColor("#FF8000")),
    S_FORM(Color.parseColor("#00FF00")),
    Z_FORM(Color.parseColor("#FF0000"));

    companion object {
        private val colorByNumber = arrayOf(I_FORM, J_FORM, L_FORM, O_FORM, S_FORM, Z_FORM, T_FORM)

        val randomColor: FigureColor
            get() {
                val formNumber = Random().nextInt(colorByNumber.size)
                return colorByNumber[formNumber]
            }
    }

}