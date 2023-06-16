package com.petrynnel.tetrisgame.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.petrynnel.tetrisgame.databinding.ActivityStartBinding
import com.petrynnel.tetrisgame.gamelogic.SoundEffects
import com.petrynnel.tetrisgame.ui.main.MainActivity
import com.petrynnel.tetrisgame.ui.settings.SettingsActivity

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding
    private val soundEffects = SoundEffects()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnStart.setOnClickListener { startGame() }
            btnRecords.setOnClickListener { startRecords() }
            btnSettings.setOnClickListener { startSettings() }
            btnExit.setOnClickListener { exit() }
        }
    }

    private fun startGame() {
        soundEffects.playSelectEffect()
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun startSettings() {
        soundEffects.playSelectEffect()
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun startRecords() {
        soundEffects.playSelectEffect()
        startActivity(Intent(this, RecordsActivity::class.java))
    }

    private fun exit() {
        soundEffects.playSelectEffect()
        this.finish()
    }
}