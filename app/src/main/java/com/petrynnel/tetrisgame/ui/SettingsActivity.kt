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
    private var initialLevel = prefHelper.loadInitialLevel()
    private var initialBlocksLevel = prefHelper.loadBlocksInitialLevel()
    private var hasShader = prefHelper.loadHasShader()
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
        initBlocksLevel()
        initLevel()
    }

    private fun saveSettings() {
        prefHelper.saveSettings(
            backgroundColor = backgroundColor,
            cornerRadius = cornerRadius,
            hasShader = hasShader,
            initialLevel = initialLevel,
            blockInitialLevel = initialBlocksLevel
        )
    }

    private fun initBackgroundColor() {
        with(binding) {
            setBackgroundColorPreview()
            cvBackgroundColor.setOnClickListener {
                cvBackgroundColorDialog.visibility = View.VISIBLE
            }
            btnClose.setOnClickListener {
                cvBackgroundColorDialog.visibility = View.INVISIBLE
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
}