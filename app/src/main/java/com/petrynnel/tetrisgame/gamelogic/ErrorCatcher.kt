package com.petrynnel.tetrisgame.gamelogic

import kotlin.system.exitProcess

object ErrorCatcher {

    @JvmStatic
    fun wrongParameter(eparameter: String, eclass: String) {
        System.err.println("Wrong parameter $eparameter occured in class $eclass")
        exitProcess(-2)
    }
}