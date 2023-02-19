package com.petrynnel.tetrisgame.gamelogic

import com.petrynnel.tetrisgame.gamelogic.Constants.BLOCKS_INITIAL_LEVEL
import com.petrynnel.tetrisgame.gamelogic.Constants.COUNT_CELLS_X
import com.petrynnel.tetrisgame.gamelogic.Constants.COUNT_CELLS_Y
import com.petrynnel.tetrisgame.gamelogic.Constants.MAX_FIGURE_WIDTH
import com.petrynnel.tetrisgame.gamelogic.Constants.MISSNG_BLOCKS_IN_INITIAL_LINE_MAX
import com.petrynnel.tetrisgame.gamelogic.Constants.MISSNG_BLOCKS_IN_INITIAL_LINE_MIN
import com.petrynnel.tetrisgame.gamelogic.Constants.OFFSET_TOP
import java.util.*

class GameField {
    private val theField: Array<Array<FigureColor?>>

    private val countFilledCellsInLine: IntArray
    lateinit var figure: Figure
        private set

    var figureGhost: Figure? = null
        private set

    var score = 0

    init {
        spawnNewFigure()
        theField = Array(COUNT_CELLS_X) {
            arrayOfNulls(COUNT_CELLS_Y + OFFSET_TOP)
        }
        countFilledCellsInLine = IntArray(COUNT_CELLS_Y + OFFSET_TOP)
        val rnd = Random()

        /* До определённого уровня создаём начальные блоки с пустотами в каждой линии */
        for (y in 0 until BLOCKS_INITIAL_LEVEL) {

            /* Количество пустых блоков в линии */
            val missingBlocksCount: Int = (MISSNG_BLOCKS_IN_INITIAL_LINE_MIN
                    + rnd.nextInt(MISSNG_BLOCKS_IN_INITIAL_LINE_MAX - MISSNG_BLOCKS_IN_INITIAL_LINE_MIN))

            /* X-координаты пустых блоков линии*/
            val missingBlocksXCoords = IntArray(missingBlocksCount)

            /*
             * Пустые блоки генерируются слева направо.
             * Каждый следующий пустой блок не может быть к концу координат ближе,
             * чем на количество оставшихся для генерации блоков.
             */
            missingBlocksXCoords[0] = rnd.nextInt(COUNT_CELLS_X - (missingBlocksCount - 1))
            for (i in 1 until missingBlocksCount) {
                val previousCoord = missingBlocksXCoords[i - 1]
                val offset = rnd.nextInt(COUNT_CELLS_X - (previousCoord - 1))
                missingBlocksXCoords[i] = previousCoord + offset
            }

            /*
             * Если Х-координата не попала в missingBlocksXCoords,
             * создаём в ней кирпичик случайного цвета
             */
            var q = 0
            for (x in 0 until COUNT_CELLS_X) {
                if (q < missingBlocksCount && missingBlocksXCoords[q] == x) {
                    theField[x][y] = FigureColor.EMPTY_BLOCK
                    q++
                } else {
                    theField[x][y] = FigureColor.randomColor
                    countFilledCellsInLine[y]++
                }
            }
        }

        /* Остальное пространство заполняем пустыми блоками */
        for (y in BLOCKS_INITIAL_LEVEL until COUNT_CELLS_Y + OFFSET_TOP) {
            for (x in 0 until COUNT_CELLS_X) {
                theField[x][y] = FigureColor.EMPTY_BLOCK
            }
        }
    }

    private fun spawnNewFigure() {
        val randomX = Random().nextInt(COUNT_CELLS_X - MAX_FIGURE_WIDTH)
        figure = Figure(Coord(randomX, COUNT_CELLS_Y + OFFSET_TOP - 1))
    }

    fun isEmpty(x: Int, y: Int): Boolean {
        return theField[x][y] == FigureColor.EMPTY_BLOCK
    }

    fun getTheField(): Array<Array<FigureColor?>> {
        return theField
    }

    fun tryShiftFigure(shiftDirection: ShiftDirection?) {
        val shiftedCoords = figure.getShiftedCoords(shiftDirection)
        var canShift = true
        for (coord in shiftedCoords) {
            if (coord!!.y < 0 || coord.y >= COUNT_CELLS_Y + OFFSET_TOP || coord.x < 0 || coord.x >= COUNT_CELLS_X
                || !isEmpty(coord.x, coord.y)
            ) {
                canShift = false
            }
        }
        if (canShift) {
            figure.shift(shiftDirection)
            ghostRefresh()
        }
    }

    fun tryRotateFigure() {
        val rotatedCoords = figure.rotatedCoords
        var canRotate = true
        for (coord in rotatedCoords) {
            if (coord!!.y < 0 || coord.y >= COUNT_CELLS_Y + OFFSET_TOP || coord.x < 0 || coord.x >= COUNT_CELLS_X
                || !isEmpty(coord.x, coord.y)
            ) {
                canRotate = false
            }
        }
        if (canRotate) {
            figure.rotate()
            ghostRefresh()
        }
    }

    private fun ghostRefresh() {
        val figure = figure.clone()
        while (canFall(figure.fallenCoords)) {
            figure.fall()
        }
        figureGhost = figure.clone()
        figureGhost!!.color = FigureColor.SHADOW_BLOCK
    }

    private fun canFall(fallenCoords: Array<Coord?>): Boolean {
        var canFall = true
        for (coord in fallenCoords) {
            if (coord!!.y < 0 || coord.y >= COUNT_CELLS_Y + OFFSET_TOP || coord.x < 0 || coord.x >= COUNT_CELLS_X
                || !isEmpty(coord.x, coord.y)
            ) {
                canFall = false
            }
        }
        return canFall
    }

    fun letFallDown(isDownToBottom: Boolean) {
        ghostRefresh()

        while (isDownToBottom && canFall(figure.fallenCoords))
            figure.fall()

        if (canFall(figure.fallenCoords)) {
            figure.fall()
        } else {
            figureGhost = null
            val figureCoords = figure.coords

            /* Флаг, говорящий о том, что после будет необходимо сместить линии вниз
             * (т.е. какая-то линия была уничтожена)
             */
            var haveToShiftLinesDown = false
            for (coord in figureCoords) {
                theField[coord!!.x][coord.y] = figure.color

                /* Увеличиваем информацию о количестве статичных блоков в линии*/
                countFilledCellsInLine[coord.y]++

                /* Проверяем, полностью ли заполнена строка Y
                 * Если заполнена полностью, устанавливаем  haveToShiftLinesDown в true
                 */
                haveToShiftLinesDown = tryDestroyLine(coord.y) || haveToShiftLinesDown
            }

            /* Если это необходимо, смещаем линии на образовавшееся пустое место */
            if (haveToShiftLinesDown) {
                score++
                shiftLinesDown()
            }

            /* Создаём новую фигуру взамен той, которую мы перенесли*/
            spawnNewFigure()
        }
    }

    /**
     * Если на поле есть полностю пустые линии, сдвигает вышестоящие непустые линии на их место.
     */
    private fun shiftLinesDown() {

        /* Номер обнаруженной пустой линии (-1, если не обнаружена) */
        var fallTo = -1

        /* Проверяем линии снизу вверх*/
        for (y in 0 until COUNT_CELLS_Y) {
            if (fallTo == -1) { //Если пустот ещё не обнаружено
                if (countFilledCellsInLine[y] == 0) fallTo = y //...пытаемся обнаружить (._.)
            } else { //А если обнаружено
                if (countFilledCellsInLine[y] != 0) { // И текущую линию есть смысл сдвигать...

                    /* Сдвигаем... */
                    for (x in 0 until COUNT_CELLS_X) {
                        theField[x][fallTo] = theField[x][y]
                        theField[x][y] = FigureColor.EMPTY_BLOCK
                    }

                    /* Не забываем обновить мета-информацию*/countFilledCellsInLine[fallTo] =
                        countFilledCellsInLine[y]
                    countFilledCellsInLine[y] = 0

                    /*
                     * В любом случае линия сверху от предыдущей пустоты пустая.
                     * Если раньше она не была пустой, то сейчас мы её сместили вниз.
                     * Если раньше она была пустой, то и сейчас пустая -- мы её ещё не заполняли.
                     */fallTo++
                }
            }
        }
    }

    /**
     * Если линия полностю заполнена, уничтожает её (но не сдвигает остальные!)
     *
     * @param y Номер проверяемой линии
     * @return Была ли линия уничтожена
     */
    private fun tryDestroyLine(y: Int): Boolean {
        if (countFilledCellsInLine[y] < COUNT_CELLS_X) {
            return false
        }
        for (x in 0 until COUNT_CELLS_X) {
            theField[x][y] = FigureColor.EMPTY_BLOCK
        }

        /* Не забываем обновить мета-информацию! */
        countFilledCellsInLine[y] = 0
        return true
    }

    /**
     * @return Есть ли в невидимой зоне над полем статичные блоки
     */
    val isOverfilled: Boolean
        get() {
            var ret = false
            for (i in 0 until OFFSET_TOP) {
                if (countFilledCellsInLine[COUNT_CELLS_Y + i] != 0) ret = true
            }
            return ret
        }
}