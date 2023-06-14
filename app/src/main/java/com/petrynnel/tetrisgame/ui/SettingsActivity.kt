package com.petrynnel.tetrisgame.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.petrynnel.tetrisgame.TetrisApp.Companion.prefHelper
import com.petrynnel.tetrisgame.databinding.ActivitySettingsBinding
import com.petrynnel.tetrisgame.gamelogic.Constants.BLOCK_CORNER_RADIUS_DISABLED
import com.petrynnel.tetrisgame.gamelogic.Constants.BLOCK_CORNER_RADIUS_HUGE
import com.petrynnel.tetrisgame.gamelogic.Constants.BLOCK_CORNER_RADIUS_SMALL
import com.petrynnel.tetrisgame.gamelogic.GameFieldBackgroundColor

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private var backgroundColor = prefHelper.loadBackgroundColor()
    private var cornerRadius = prefHelper.loadCornerRadius()
    private var hasShader = prefHelper.loadHasShader()
    private var hasShadow = prefHelper.loadHasShadow()
    private var initialLevel = prefHelper.loadInitialLevel()
    private var initialBlocksLevel = prefHelper.loadBlocksInitialLevel()
    private var hasVibration = prefHelper.loadHasVibration()
    private var hasSoundEffects = prefHelper.loadHasSoundEffects()
    private var hasMusic = prefHelper.loadHasMusic()
    private var mAdapter: GridAdapter? = null

    private val itemOnClick = object : GridAdapter.OnItemClickListener {
        @SuppressLint("NotifyDataSetChanged")
        override fun onItemClick(gameFieldBackgroundColor: GameFieldBackgroundColor) {
            backgroundColor = gameFieldBackgroundColor.color
            saveSettings()
            setBackgroundColorPreview()
            mAdapter?.notifyDataSetChanged()
        }
    }

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
        initHasShadow()
        initBlocksLevel()
        initLevel()
        initHasVibration()
        initHasSoundEffects()
        initHasMusic()
    }

    private fun saveSettings() {
        prefHelper.saveSettings(
            backgroundColor = backgroundColor,
            cornerRadius = cornerRadius,
            hasShader = hasShader,
            hasShadow = hasShadow,
            initialLevel = initialLevel,
            blockInitialLevel = initialBlocksLevel,
            hasVibration = hasVibration,
            hasSoundEffects = hasSoundEffects,
            hasMusic = hasMusic
        )
    }

    private fun initBackgroundColor() {
        with(binding) {
            setBackgroundColorPreview()
            cvBackgroundColor.setOnClickListener {
                cvBackgroundColorDialog.visibility = View.VISIBLE
                llSettings.visibility = View.INVISIBLE
            }
            btnClose.setOnClickListener {
                cvBackgroundColorDialog.visibility = View.INVISIBLE
                llSettings.visibility = View.VISIBLE
            }
            mAdapter = GridAdapter(this@SettingsActivity, itemOnClick)
            rvBackgroundColors.adapter = mAdapter
        }
    }

    private fun initBlockForm() {
        with(binding) {
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
            swHasShader.isChecked = hasShader
            vGradient.isVisible = hasShader
            swHasShader.setOnCheckedChangeListener { _, isChecked ->
                hasShader = isChecked
                vGradient.isVisible = isChecked
            }
        }
    }

    private fun initHasShadow() {
        with(binding) {
            swHasShadow.isChecked = hasShadow
            swHasShadow.setOnCheckedChangeListener { _, isChecked ->
                hasShadow = isChecked
            }
        }
    }

    private fun initBlocksLevel() {
        with(binding) {
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

    private fun setBackgroundColorPreview() {
        binding.cvBackgroundColorPreview.setCardBackgroundColor(
            this@SettingsActivity.getColor(prefHelper.loadBackgroundColor())
        )
    }

    private fun initHasVibration() {
        with(binding) {
            swHasVibration.isChecked = hasVibration
            swHasVibration.setOnCheckedChangeListener { _, isChecked ->
                hasVibration = isChecked
            }
        }
    }

    private fun initHasSoundEffects() {
        with(binding) {
            swHasSoundEffects.isChecked = hasSoundEffects
            swHasSoundEffects.setOnCheckedChangeListener { _, isChecked ->
                hasSoundEffects = isChecked
            }
        }
    }

    private fun initHasMusic() {
        with(binding) {
            swHasMusic.isChecked = hasMusic
            swHasMusic.setOnCheckedChangeListener { _, isChecked ->
                hasMusic = isChecked
            }
        }
    }
}