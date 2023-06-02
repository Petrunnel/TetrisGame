package com.petrynnel.tetrisgame.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.petrynnel.tetrisgame.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding

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
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun startSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun startRecords() {
        startActivity(Intent(this, RecordsActivity::class.java))
    }

    private fun exit() {
        this.finish()
    }
}