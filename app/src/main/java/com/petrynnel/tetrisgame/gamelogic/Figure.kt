package com.petrynnel.tetrisgame.gamelogic

import com.google.gson.Gson

open class Figure @JvmOverloads constructor(
    private val metaPointCoords: Coord,
    private var currentRotation: RotationMode = RotationMode.NORMAL,
    private val form: FigureForm = FigureForm.randomForm
) {
    val coords: Array<Coord?>
        get() = form.mask.generateFigure(metaPointCoords, currentRotation)

    val rotatedCoords: Array<Coord?>
        get() = form.mask.generateFigure(
            metaPointCoords,
            RotationMode.getNextRotationFrom(currentRotation)
        )

    fun rotate() {
        currentRotation = RotationMode.getNextRotationFrom(currentRotation)
    }

    fun clone(): Figure {
        val stringFigure = Gson().toJson(this, Figure::class.java)
        return Gson().fromJson(stringFigure, Figure::class.java)
    }

    fun getShiftedCoords(direction: ShiftDirection?): Array<Coord?> {
        var newFirstCell: Coord? = null
        when (direction) {
            ShiftDirection.LEFT -> newFirstCell = Coord(metaPointCoords.x - 1, metaPointCoords.y)
            ShiftDirection.RIGHT -> newFirstCell = Coord(metaPointCoords.x + 1, metaPointCoords.y)
            else -> ErrorCatcher.wrongParameter("direction (for getShiftedCoords)", "Figure")
        }
        return form.mask.generateFigure(newFirstCell!!, currentRotation)
    }

    fun shift(direction: ShiftDirection?) {
        when (direction) {
            ShiftDirection.LEFT -> metaPointCoords.x--
            ShiftDirection.RIGHT -> metaPointCoords.x++
            else -> ErrorCatcher.wrongParameter("direction (for shift)", "Figure")
        }
    }

    fun isOForm() = form == FigureForm.O_FORM

    val fallenCoords: Array<Coord?>
        get() {
            val newFirstCell = Coord(metaPointCoords.x, metaPointCoords.y - 1)
            return form.mask.generateFigure(newFirstCell, currentRotation)
        }

    fun fall() {
        metaPointCoords.y--
    }

    var color: FigureColor = form.color
}