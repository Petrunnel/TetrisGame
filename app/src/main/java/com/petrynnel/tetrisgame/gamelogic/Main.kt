package com.petrynnel.tetrisgame.gamelogic

import com.petrynnel.tetrisgame.gamelogic.Constants.BOOST_MULTIPLIER
import com.petrynnel.tetrisgame.gamelogic.Constants.FRAMES_PER_MOVE

object Main {
    /** Флаг для завершения основного цикла программы  */
    var endOfGame = false

    /** Игровое поле. См. документацию GameField  */
    private lateinit var gameField: GameField

    /** Направление для сдвига, полученное за последнюю итерацию  */
    var shiftDirection: ShiftDirection? = null

    /** Был ли за последнюю итерацию запрошен поворот фигуры  */
    var isRotateRequested = false

    /** Было ли за последнюю итерацию запрошено ускорение падения */
    private var isBoostRequested = false

    /** Номер игровой итерации по модулю FRAMES_PER_MOVE.
     * Падение фигуры вниз происходит если loopNumber % FRAMES_PER_MOVE == 0
     * Т.е. 1 раз за FRAMES_PER_MOVE итераций.
     */
    private var loopNumber = 0

    fun getField(): GameField {
        return gameField
    }

    /**
     * Задаёт значения полей для начала игры
     */
    fun initFields() {
        loopNumber = 0
        endOfGame = false
        shiftDirection = ShiftDirection.AWAITING
        isRotateRequested = false
        gameField = GameField()
    }

    /**
     * Чтение пользовательских команд. Их выполнение реализовано в logic().
     */
    private fun input() {
//        keyboardModule.update()
//        shiftDirection = keyboardModule.getShiftDirection()
//        isRotateRequested = keyboardModule.wasRotateRequested()
//        isBoostRequested = keyboardModule.wasBoostRequested()
//        endOfGame = endOfGame || keyboardModule.wasEscPressed() || graphicsModule.isCloseRequested()
    }

    /**
     * Здесь происходят основные игровые действия --
     * запросы пользователя передаются на исполнение,
     * обновляется игровое поле (и фигура)
     */
    fun logic() {

        if (shiftDirection != ShiftDirection.AWAITING) { // Если есть запрос на сдвиг фигуры

            /* Пробуем сдвинуть */
            gameField.tryShiftFigure(shiftDirection)

            /* Ожидаем нового запроса */
            shiftDirection = ShiftDirection.AWAITING
        }
        if (isRotateRequested) { // Если есть запрос на поворот фигуры

            /* Пробуем повернуть */
            gameField.tryRotateFigure()

            /* Ожидаем нового запроса */
            isRotateRequested = false
        }

        /* Падение фигуры вниз происходит если loopNumber % FRAMES_PER_MOVE == 0
         * Т.е. 1 раз за FRAMES_PER_MOVE итераций.
         */
        if (loopNumber % (FRAMES_PER_MOVE / if (isBoostRequested) BOOST_MULTIPLIER else 1) == 0) gameField.letFallDown()


//        gameField.letFallDown()
        /* Увеличение номера итерации (по модулю FPM)*/
        loopNumber = (loopNumber + 1) % FRAMES_PER_MOVE

        /* Если поле переполнено, игра закончена */endOfGame = endOfGame || gameField.isOverfilled
    }
}