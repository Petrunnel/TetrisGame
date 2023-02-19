package com.petrynnel.tetrisgame.gamelogic

import com.petrynnel.tetrisgame.R
import java.util.*

enum class FigureColor(val color: Int) {

    EMPTY_BLOCK(android.R.color.transparent),
    SHADOW_BLOCK(R.color.SHADOW_BLOCK),
    I_FORM(R.color.I_FORM),
    O_FORM(R.color.O_FORM),
    T_FORM(R.color.T_FORM),
    J_FORM(R.color.J_FORM),
    L_FORM(R.color.L_FORM),
    S_FORM(R.color.S_FORM),
    Z_FORM(R.color.Z_FORM);
    companion object {
        private val colorByNumber = arrayOf(I_FORM, J_FORM, L_FORM, O_FORM, S_FORM, Z_FORM, T_FORM)

        val randomColor: FigureColor
            get() {
                val formNumber = Random().nextInt(colorByNumber.size)
                return colorByNumber[formNumber]
            }
    }

}