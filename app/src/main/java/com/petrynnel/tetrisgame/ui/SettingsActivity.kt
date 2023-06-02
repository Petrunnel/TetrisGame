package com.petrynnel.tetrisgame.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import androidx.core.view.isVisible
import com.petrynnel.tetrisgame.TetrisApp.Companion.prefHelper
import com.petrynnel.tetrisgame.databinding.ActivitySettingsBinding
import com.petrynnel.tetrisgame.gamelogic.Constants.BLOCK_CORNER_RADIUS_DISABLED
import com.petrynnel.tetrisgame.gamelogic.Constants.BLOCK_CORNER_RADIUS_HUGE
import com.petrynnel.tetrisgame.gamelogic.Constants.BLOCK_CORNER_RADIUS_SMALL
import com.petrynnel.tetrisgame.gamelogic.Constants.DEFAULT_BLOCKS_INITIAL_LEVEL
import com.petrynnel.tetrisgame.gamelogic.Constants.DEFAULT_INITIAL_LEVEL
import com.petrynnel.tetrisgame.gamelogic.GameFieldBackgroundColor

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private var cornerRadius = BLOCK_CORNER_RADIUS_DISABLED
    private var initialLevel = DEFAULT_INITIAL_LEVEL
    private var initialBlocksLevel = DEFAULT_BLOCKS_INITIAL_LEVEL
    private var hasShader = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSettings()
    }

    override fun onPause() {
        saveSettings()
        super.onPause()
    }

    private fun initSettings() {
        initBackgroundColor()
        initBlockForm()
        initHasShader()
        initBlocksLevel()
        initLevel()
    }

    private fun saveSettings() {
        prefHelper.saveSettings(
            backgroundColor = GameFieldBackgroundColor.BLACK,
            cornerRadius = cornerRadius,
            hasShader = hasShader,
            initialLevel = initialLevel,
            blockInitialLevel = initialBlocksLevel
        )
    }

    private fun initBackgroundColor() {}

    private fun initBlockForm() {
        with(binding) {
            cornerRadius = prefHelper.loadCornerRadius()
            val progress = when (cornerRadius) {
                BLOCK_CORNER_RADIUS_DISABLED -> {
                    cvBlockForm.radius = BLOCK_CORNER_RADIUS_DISABLED
                    0
                }
                BLOCK_CORNER_RADIUS_SMALL -> {
                    cvBlockForm.radius = BLOCK_CORNER_RADIUS_SMALL
                    1
                }
                BLOCK_CORNER_RADIUS_HUGE -> {
                    cvBlockForm.radius = BLOCK_CORNER_RADIUS_HUGE
                    2
                }
                else -> 0
            }
            sbBlockForm.setProgress(progress, false)
            sbBlockForm.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    cornerRadius = when (progress) {
                        0 -> {
                            cvBlockForm.radius = BLOCK_CORNER_RADIUS_DISABLED
                            BLOCK_CORNER_RADIUS_DISABLED
                        }
                        1 -> {
                            cvBlockForm.radius = BLOCK_CORNER_RADIUS_SMALL
                            BLOCK_CORNER_RADIUS_SMALL
                        }
                        2 -> {
                            cvBlockForm.radius = BLOCK_CORNER_RADIUS_HUGE
                            BLOCK_CORNER_RADIUS_HUGE
                        }
                        else -> BLOCK_CORNER_RADIUS_DISABLED
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    private fun initHasShader() {
        with(binding) {
            hasShader = prefHelper.loadHasShader()
            swHasShader.isChecked = hasShader
            vGradient.isVisible = hasShader
            swHasShader.setOnCheckedChangeListener { _, isChecked ->
                hasShader = isChecked
                vGradient.isVisible = isChecked
            }
        }
    }

    private fun initBlocksLevel() {
        with(binding) {
            initialBlocksLevel = prefHelper.loadBlocksInitialLevel()
            tvBlockInitialLevel.text = initialBlocksLevel.toString()
            sbInitialBlocksLevel.setProgress(initialBlocksLevel, false)
            sbInitialBlocksLevel.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    initialBlocksLevel = progress
                    tvBlockInitialLevel.text = progress.toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    private fun initLevel() {
        with(binding) {
            initialLevel = prefHelper.loadInitialLevel()
            tvInitialLevel.text = initialLevel.toString()
            sbInitialLevel.setProgress(initialLevel, false)
            sbInitialLevel.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    initialLevel = progress
                    tvInitialLevel.text = progress.toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }
}