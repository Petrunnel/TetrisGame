package com.petrynnel.tetrisgame

import android.app.Application

class TetrisApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        prefHelper = PreferenceHelper()
    }

    companion object {
        lateinit var instance: TetrisApp
            private set
        lateinit var prefHelper: PreferenceHelper
            private set
    }
}