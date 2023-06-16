package com.petrynnel.tetrisgame.ui.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.petrynnel.tetrisgame.TetrisApp.Companion.prefHelper
import com.petrynnel.tetrisgame.databinding.ActivitySettingsBinding
import com.petrynnel.tetrisgame.gamelogic.BlockShape
import com.petrynnel.tetrisgame.gamelogic.Constants.BLOCK_CORNER_RADIUS_DISABLED
import com.petrynnel.tetrisgame.gamelogic.Constants.BLOCK_CORNER_RADIUS_HUGE
import com.petrynnel.tetrisgame.gamelogic.Constants.BLOCK_CORNER_RADIUS_SMALL
import com.petrynnel.tetrisgame.gamelogic.GameFieldBackgroundColor
import com.petrynnel.tetrisgame.isVisibleOrInvisible

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private var backgroundColor = prefHelper.loadBackgroundColor()
    private var blockShape = prefHelper.loadBlockShape() ?: BlockShape.SQUARE
    private var hasShader = prefHelper.loadHasShader()
    private var hasShadow = prefHelper.loadHasShadow()
    private var initialLevel = prefHelper.loadInitialLevel()
    private var initialBlocksLevel = prefHelper.loadBlocksInitialLevel()
    private var hasVibration = prefHelper.loadHasVibration()
    private var hasSoundEffects = prefHelper.loadHasSoundEffects()
    private var hasMusic = prefHelper.loadHasMusic()
    private var mAdapter: GridAdapter? = null
    private var mAdapterShape: GridAdapterShape? = null

    private val itemOnClick = object : GridAdapter.OnItemClickListener {
        @SuppressLint("NotifyDataSetChanged")
        override fun onItemClick(gameFieldBackgroundColor: GameFieldBackgroundColor) {
            backgroundColor = gameFieldBackgroundColor.color
            saveSettings()
            setBackgroundColorPreview()
            mAdapter?.notifyDataSetChanged()
        }
    }

    private val itemShapeOnClick = object : GridAdapterShape.OnItemClickListener {
        @SuppressLint("NotifyDataSetChanged")
        override fun onItemClick(blockShape: BlockShape) {
            this@SettingsActivity.blockShape = blockShape
            saveSettings()
            setShapePreview()
            mAdapterShape?.notifyDataSetChanged()
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
            blockShape = blockShape,
            hasShader = hasShader,
            hasShadow = hasShadow,
            initialLevel = initialLevel,
            blockInitialLevel = initialBlocksLevel,
            hasVibration = hasVibration,
            hasSoundEffects = hasSoundEffects,
            hasMusic = hasMusic,
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
            setShapePreview()
            cvBlockShape.setOnClickListener {
                cvBlockShapeDialog.visibility = View.VISIBLE
                llSettings.visibility = View.INVISIBLE
            }
            btnCloseBlockShapeDialog.setOnClickListener {
                cvBlockShapeDialog.visibility = View.INVISIBLE
                llSettings.visibility = View.VISIBLE
            }
            mAdapterShape = GridAdapterShape(this@SettingsActivity, itemShapeOnClick)
            rvBlockShape.adapter= mAdapterShape
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initHasShader() {
        with(binding) {
            swHasShader.isChecked = hasShader
            swHasShader.setOnCheckedChangeListener { _, isChecked ->
                hasShader = isChecked
                saveSettings()
                mAdapterShape?.notifyDataSetChanged()
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

    private fun setShapePreview() {
        with(binding) {
            when (blockShape) {
                BlockShape.SQUARE -> {
                    cvBlockForm.radius = BLOCK_CORNER_RADIUS_DISABLED
                    ivBitmap.visibility = View.INVISIBLE
                    cvBlockForm.visibility = View.VISIBLE
                    vGradient.isVisibleOrInvisible(hasShader)
                }
                BlockShape.ROUNDED_SQUARE -> {
                    cvBlockForm.radius = BLOCK_CORNER_RADIUS_SMALL
                    ivBitmap.visibility = View.INVISIBLE
                    cvBlockForm.visibility = View.VISIBLE
                    vGradient.isVisibleOrInvisible(hasShader)
                }
                BlockShape.CIRCLE -> {
                    cvBlockForm.radius = BLOCK_CORNER_RADIUS_HUGE
                    ivBitmap.visibility = View.INVISIBLE
                    cvBlockForm.visibility = View.VISIBLE
                    vGradient.isVisibleOrInvisible(hasShader)
                }
                BlockShape.BITMAP -> {
                    cvBlockForm.radius = BLOCK_CORNER_RADIUS_DISABLED
                    ivBitmap.visibility = View.VISIBLE
                    vGradient.visibility = View.INVISIBLE
                }
            }
        }
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