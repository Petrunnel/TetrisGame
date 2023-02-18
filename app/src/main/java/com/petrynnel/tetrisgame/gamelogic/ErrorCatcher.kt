package com.petrynnel.tetrisgame.gamelogic

import kotlin.system.exitProcess

object ErrorCatcher {

    @JvmStatic
    fun wrongParameter(eparameter: String, eclass: String) {
        System.err.println("Wrong parameter $eparameter occured in class $eclass")
        exitProcess(-2)
    }

    fun graphicsFailure(e: Exception) {
        System.err.println("GraphicsModule failed.")
        e.printStackTrace()
        exitProcess(-3)
    }
}