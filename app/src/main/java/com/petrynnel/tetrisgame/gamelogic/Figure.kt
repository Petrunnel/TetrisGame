package com.petrynnel.tetrisgame.gamelogic

class Figure @JvmOverloads constructor(
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

    val fallenCoords: Array<Coord?>
        get() {
            val newFirstCell = Coord(metaPointCoords.x, metaPointCoords.y - 1)
            return form.mask.generateFigure(newFirstCell, currentRotation)
        }

    fun fall() {
        metaPointCoords.y--
    }

    val color: FigureColor
        get() = form.color
}