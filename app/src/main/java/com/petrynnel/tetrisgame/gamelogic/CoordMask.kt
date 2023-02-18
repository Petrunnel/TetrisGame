package com.petrynnel.tetrisgame.gamelogic

enum class CoordMask(private val forms: GenerationDelegate) {
    /* Кирпичик [][][][] */
    I_FORM(
        object : GenerationDelegate {
            override fun generateFigure(
                initialCoord: Coord,
                rotation: RotationMode?
            ): Array<Coord?> {
                val ret = arrayOfNulls<Coord>(4)
                when (rotation) {
                    RotationMode.NORMAL, RotationMode.INVERT -> {
                        ret[0] = initialCoord
                        ret[1] = Coord(initialCoord.x, initialCoord.y - 1)
                        ret[2] = Coord(initialCoord.x, initialCoord.y - 2)
                        ret[3] = Coord(initialCoord.x, initialCoord.y - 3)
                    }
                    RotationMode.FLIP_CCW, RotationMode.FLIP_CW -> {
                        ret[0] = initialCoord
                        ret[1] = Coord(initialCoord.x + 1, initialCoord.y)
                        ret[2] = Coord(initialCoord.x + 2, initialCoord.y)
                        ret[3] = Coord(initialCoord.x + 3, initialCoord.y)
                    }
                    else -> ErrorCatcher.wrongParameter("rotation", "CoordMask")
                }
                return ret
            }
        }
    ),
    /* Кирпичик  []
     *           [][][]
     */
    J_FORM(
        object : GenerationDelegate {
            override fun generateFigure(
                initialCoord: Coord,
                rotation: RotationMode?
            ): Array<Coord?> {
                val ret = arrayOfNulls<Coord>(4)
                when (rotation) {
                    RotationMode.NORMAL -> {
                        ret[0] = Coord(initialCoord.x + 1, initialCoord.y)
                        ret[1] = Coord(initialCoord.x + 1, initialCoord.y - 1)
                        ret[2] = Coord(initialCoord.x + 1, initialCoord.y - 2)
                        ret[3] = Coord(initialCoord.x, initialCoord.y - 2)
                    }
                    RotationMode.INVERT -> {
                        ret[0] = Coord(initialCoord.x + 1, initialCoord.y)
                        ret[1] = initialCoord
                        ret[2] = Coord(initialCoord.x, initialCoord.y - 1)
                        ret[3] = Coord(initialCoord.x, initialCoord.y - 2)
                    }
                    RotationMode.FLIP_CCW -> {
                        ret[0] = initialCoord
                        ret[1] = Coord(initialCoord.x + 1, initialCoord.y)
                        ret[2] = Coord(initialCoord.x + 2, initialCoord.y)
                        ret[3] = Coord(initialCoord.x + 2, initialCoord.y - 1)
                    }
                    RotationMode.FLIP_CW -> {
                        ret[0] = initialCoord
                        ret[1] = Coord(initialCoord.x, initialCoord.y - 1)
                        ret[2] = Coord(initialCoord.x + 1, initialCoord.y - 1)
                        ret[3] = Coord(initialCoord.x + 2, initialCoord.y - 1)
                    }
                    else -> ErrorCatcher.wrongParameter("rotation", "CoordMask")
                }
                return ret
            }
        }
    ),
    /* Кирпичик     []
     *          [][][]
     */
    L_FORM(
        object : GenerationDelegate {
            override fun generateFigure(
                initialCoord: Coord,
                rotation: RotationMode?
            ): Array<Coord?> {
                val ret = arrayOfNulls<Coord>(4)
                when (rotation) {
                    RotationMode.NORMAL -> {
                        ret[0] = initialCoord
                        ret[1] = Coord(initialCoord.x, initialCoord.y - 1)
                        ret[2] = Coord(initialCoord.x, initialCoord.y - 2)
                        ret[3] = Coord(initialCoord.x + 1, initialCoord.y - 2)
                    }
                    RotationMode.INVERT -> {
                        ret[0] = initialCoord
                        ret[1] = Coord(initialCoord.x + 1, initialCoord.y)
                        ret[2] = Coord(initialCoord.x + 1, initialCoord.y - 1)
                        ret[3] = Coord(initialCoord.x + 1, initialCoord.y - 2)
                    }
                    RotationMode.FLIP_CCW -> {
                        ret[0] = Coord(initialCoord.x, initialCoord.y - 1)
                        ret[1] = Coord(initialCoord.x + 1, initialCoord.y - 1)
                        ret[2] = Coord(initialCoord.x + 2, initialCoord.y - 1)
                        ret[3] = Coord(initialCoord.x + 2, initialCoord.y)
                    }
                    RotationMode.FLIP_CW -> {
                        ret[0] = Coord(initialCoord.x, initialCoord.y - 1)
                        ret[1] = initialCoord
                        ret[2] = Coord(initialCoord.x + 1, initialCoord.y)
                        ret[3] = Coord(initialCoord.x + 2, initialCoord.y)
                    }
                    else -> ErrorCatcher.wrongParameter("rotation", "CoordMask")
                }
                return ret
            }
        }
    ),
    /* Кирпичик [][]
     *          [][]
     */
    O_FORM(
        object : GenerationDelegate {
            override fun generateFigure(
                initialCoord: Coord,
                rotation: RotationMode?
            ): Array<Coord?> {
                val ret = arrayOfNulls<Coord>(4)
                ret[0] = initialCoord
                ret[1] = Coord(initialCoord.x, initialCoord.y - 1)
                ret[2] = Coord(initialCoord.x + 1, initialCoord.y - 1)
                ret[3] = Coord(initialCoord.x + 1, initialCoord.y)
                return ret
            }
        }
    ),
    /* Кирпичик   [][]
     *          [][]
     */
    S_FORM(
        object : GenerationDelegate {
            override fun generateFigure(
                initialCoord: Coord,
                rotation: RotationMode?
            ): Array<Coord?> {
                val ret = arrayOfNulls<Coord>(4)
                when (rotation) {
                    RotationMode.NORMAL, RotationMode.INVERT -> {
                        ret[0] = Coord(initialCoord.x, initialCoord.y - 1)
                        ret[1] = Coord(initialCoord.x + 1, initialCoord.y - 1)
                        ret[2] = Coord(initialCoord.x + 1, initialCoord.y)
                        ret[3] = Coord(initialCoord.x + 2, initialCoord.y)
                    }
                    RotationMode.FLIP_CCW, RotationMode.FLIP_CW -> {
                        ret[0] = initialCoord
                        ret[1] = Coord(initialCoord.x, initialCoord.y - 1)
                        ret[2] = Coord(initialCoord.x + 1, initialCoord.y - 1)
                        ret[3] = Coord(initialCoord.x + 1, initialCoord.y - 2)
                    }
                    else -> ErrorCatcher.wrongParameter("rotation", "CoordMask")
                }
                return ret
            }
        }
    ),
    /* Кирпичик [][]
     *            [][]
     */
    Z_FORM(
        object : GenerationDelegate {
            override fun generateFigure(
                initialCoord: Coord,
                rotation: RotationMode?
            ): Array<Coord?> {
                val ret = arrayOfNulls<Coord>(4)
                when (rotation) {
                    RotationMode.NORMAL, RotationMode.INVERT -> {
                        ret[0] = initialCoord
                        ret[1] = Coord(initialCoord.x + 1, initialCoord.y)
                        ret[2] = Coord(initialCoord.x + 1, initialCoord.y - 1)
                        ret[3] = Coord(initialCoord.x + 2, initialCoord.y - 1)
                    }
                    RotationMode.FLIP_CCW, RotationMode.FLIP_CW -> {
                        ret[0] = Coord(initialCoord.x + 1, initialCoord.y)
                        ret[1] = Coord(initialCoord.x + 1, initialCoord.y - 1)
                        ret[2] = Coord(initialCoord.x, initialCoord.y - 1)
                        ret[3] = Coord(initialCoord.x, initialCoord.y - 2)
                    }
                    else -> ErrorCatcher.wrongParameter("rotation", "CoordMask")
                }
                return ret
            }
        }
    ),
    /* Кирпичик [][][]
     *            []
     */
    T_FORM(
        object : GenerationDelegate {
            override fun generateFigure(
                initialCoord: Coord,
                rotation: RotationMode?
            ): Array<Coord?> {
                val ret = arrayOfNulls<Coord>(4)
                when (rotation) {
                    RotationMode.NORMAL -> {
                        ret[0] = initialCoord
                        ret[1] = Coord(initialCoord.x + 1, initialCoord.y)
                        ret[2] = Coord(initialCoord.x + 1, initialCoord.y - 1)
                        ret[3] = Coord(initialCoord.x + 2, initialCoord.y)
                    }
                    RotationMode.INVERT -> {
                        ret[0] = Coord(initialCoord.x, initialCoord.y - 1)
                        ret[1] = Coord(initialCoord.x + 1, initialCoord.y - 1)
                        ret[2] = Coord(initialCoord.x + 1, initialCoord.y)
                        ret[3] = Coord(initialCoord.x + 2, initialCoord.y - 1)
                    }
                    RotationMode.FLIP_CCW -> {
                        ret[0] = initialCoord
                        ret[1] = Coord(initialCoord.x, initialCoord.y - 1)
                        ret[2] = Coord(initialCoord.x + 1, initialCoord.y - 1)
                        ret[3] = Coord(initialCoord.x, initialCoord.y - 2)
                    }
                    RotationMode.FLIP_CW -> {
                        ret[0] = Coord(initialCoord.x + 1, initialCoord.y)
                        ret[1] = Coord(initialCoord.x + 1, initialCoord.y - 1)
                        ret[2] = Coord(initialCoord.x, initialCoord.y - 1)
                        ret[3] = Coord(initialCoord.x + 1, initialCoord.y - 2)
                    }
                    else -> ErrorCatcher.wrongParameter("rotation", "CoordMask")
                }
                return ret
            }
        }
    );

    private interface GenerationDelegate {
        fun generateFigure(initialCoord: Coord, rotation: RotationMode?): Array<Coord?>
    }

    fun generateFigure(initialCoord: Coord, rotation: RotationMode?): Array<Coord?> {
        return forms.generateFigure(initialCoord, rotation)
    }
}