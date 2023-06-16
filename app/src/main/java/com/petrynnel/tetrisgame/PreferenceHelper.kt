package com.petrynnel.tetrisgame

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.petrynnel.tetrisgame.gamelogic.BlockShape
import com.petrynnel.tetrisgame.gamelogic.Constants
import com.petrynnel.tetrisgame.gamelogic.Constants.DEFAULT_BLOCKS_INITIAL_LEVEL
import com.petrynnel.tetrisgame.gamelogic.Constants.DEFAULT_INITIAL_LEVEL
import com.petrynnel.tetrisgame.gamelogic.GameField
import com.petrynnel.tetrisgame.gamelogic.GameFieldBackgroundColor

class PreferenceHelper {

    private val sharedPreferencesSettings = sharedPrefs("SETTINGS")
    private val sharedPreferencesGameField = sharedPrefs("GAME_FIELD")
    private val sharedPreferencesRecords = sharedPrefs("HIGH_SCORE")

    fun saveSettings(
        backgroundColor: Int,
        blockShape: BlockShape,
        hasShader: Boolean,
        hasShadow: Boolean,
        initialLevel: Int,
        blockInitialLevel: Int,
        hasVibration: Boolean,
        hasSoundEffects: Boolean,
        hasMusic: Boolean,
    ) {
        sharedPreferencesSettings.edit {
            it.putInt("background_color", backgroundColor)
            it.putString("block_shape", saveBlockShape(blockShape))
            it.putBoolean("has_shader", hasShader)
            it.putBoolean("has_shadow", hasShadow)
            it.putInt("initial_level", initialLevel)
            it.putInt("blocks_initial_level", blockInitialLevel)
            it.putBoolean("has_vibration", hasVibration)
            it.putBoolean("has_sounds_effects", hasSoundEffects)
            it.putBoolean("has_music", hasMusic)
        }
    }

    fun loadBackgroundColor() =
        sharedPreferencesSettings.getInt("background_color", GameFieldBackgroundColor.BLACK.color)

    fun loadBlockShape(): BlockShape? {
        val gson = Gson()
        val json = sharedPreferencesSettings.getString("block_shape", "")
        return gson.fromJson(json, BlockShape::class.java)
    }

    fun loadHasShader() = sharedPreferencesSettings.getBoolean("has_shader", false)

    fun loadHasShadow() = sharedPreferencesSettings.getBoolean("has_shadow", true)

    fun loadInitialLevel() =
        sharedPreferencesSettings.getInt("initial_level", DEFAULT_INITIAL_LEVEL)

    fun loadBlocksInitialLevel() =
        sharedPreferencesSettings.getInt("blocks_initial_level", DEFAULT_BLOCKS_INITIAL_LEVEL)

    fun loadHasVibration() =
        sharedPreferencesSettings.getBoolean("has_vibration", true)

    fun loadHasSoundEffects() =
        sharedPreferencesSettings.getBoolean("has_sounds_effects", true)

    fun loadHasMusic() =
        sharedPreferencesSettings.getBoolean("has_music", false)

    fun hasBitmap(): Boolean {
        return when (loadBlockShape()) {
            BlockShape.BITMAP -> true
            else -> false
        }
    }

    fun loadRecords(): IntArray {
        val gson = Gson()
        val json = sharedPreferencesRecords.getString(
            "high_score",
            gson.toJson(IntArray(Constants.NUMBER_OF_RECORDS))
        )
        return gson.fromJson(json, IntArray::class.java)
    }

    fun saveRecords(best: IntArray) {
        val gson = Gson()
        val json = gson.toJson(best)
        sharedPreferencesRecords.edit {
            it.putString("high_score", json)
        }
    }

    fun saveField(gameField: GameField?) {
        val gson = Gson()
        val json = gson.toJson(gameField)
        sharedPreferencesGameField.edit {
            it.putString("game_field", json)
        }
    }

    fun loadField(): GameField? {
        val gson = Gson()
        val json =  sharedPreferencesGameField.getString("game_field", "")
        return gson.fromJson(json, GameField::class.java)
    }

    private fun sharedPrefs(name: String): SharedPreferences =
        TetrisApp.instance.getSharedPreferences(name, Context.MODE_PRIVATE)


    private fun saveBlockShape(shape: BlockShape): String {
        val gson = Gson()
        return gson.toJson(shape) ?: ""
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }
}