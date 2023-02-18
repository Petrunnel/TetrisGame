package com.petrynnel.tetrisgame.gamelogic

import java.util.*

enum class FigureForm(
    val mask: CoordMask,
    val color: FigureColor
) {
    I_FORM(CoordMask.I_FORM, FigureColor.I_FORM),
    J_FORM(CoordMask.J_FORM, FigureColor.J_FORM),
    L_FORM(CoordMask.L_FORM, FigureColor.L_FORM),
    O_FORM(CoordMask.O_FORM, FigureColor.O_FORM),
    S_FORM(CoordMask.S_FORM, FigureColor.S_FORM),
    Z_FORM(CoordMask.Z_FORM, FigureColor.Z_FORM),
    T_FORM(CoordMask.T_FORM, FigureColor.T_FORM);

    companion object {
        private val formByNumber = arrayOf(I_FORM, J_FORM, L_FORM, O_FORM, S_FORM, Z_FORM, T_FORM)

        val randomForm: FigureForm
            get() {
                val formNumber = Random().nextInt(formByNumber.size)
                return formByNumber[formNumber]
            }
    }
}