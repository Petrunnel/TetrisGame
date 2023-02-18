package com.petrynnel.tetrisgame.gamelogic

enum class RotationMode(private val number: Int) {
    /** Начальное положение  */
    NORMAL(0),

    /** Положение, соответствующее повороту против часовой стрелки */
    FLIP_CCW(1),

    /** Положение, соответствующее зеркальному отражению */
    INVERT(2),

    /** Положение, соответствующее повороту по часовой стрелке (или трём поворотам против) */
    FLIP_CW(3);

    companion object {
        private val rotationByNumber = arrayOf(NORMAL, FLIP_CCW, INVERT, FLIP_CW)

        fun getNextRotationFrom(previousRotation: RotationMode): RotationMode {
            val newRotationIndex = (previousRotation.number + 1) % rotationByNumber.size
            return rotationByNumber[newRotationIndex]
        }
    }
}