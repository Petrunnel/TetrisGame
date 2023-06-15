package com.petrynnel.tetrisgame.gamelogic

object Logic {

    private var gameField: GameField? = null

    private var endOfGame = false
    private var shiftDirection: ShiftDirection? = null
    private var isRotateRequested = false
    private var isDownToBottomRequested = false
    private var isBoostRequested = false
    private var loopNumber = 0
    private var isNewGame = true

    fun requestRotate() {
        isRotateRequested = true
    }

    fun requestDownToBottom() {
        isDownToBottomRequested = true
    }

    fun boostRequested(isRequested: Boolean) {
        isBoostRequested = isRequested
    }

    fun setShiftDirection(shiftDirection: ShiftDirection) {
        this.shiftDirection = shiftDirection
    }

    fun isEndOfGame() = endOfGame

    fun isNewGame() = isNewGame

    fun setNewGame(isNewGame: Boolean) {
        this.isNewGame = isNewGame
    }

    fun getGameField() = gameField

    fun setGameField(gameField: GameField?) {
        this.gameField = gameField
    }

    fun getField(): GameField = gameField ?: GameField()

    fun initFields() {
        loopNumber = 0
        endOfGame = false
        shiftDirection = ShiftDirection.AWAITING
        isRotateRequested = false
        gameField = getField()
    }

    fun logic() {

        if (shiftDirection != ShiftDirection.AWAITING) { // Если есть запрос на сдвиг фигуры

            /* Пробуем сдвинуть */
            getField().tryShiftFigure(shiftDirection)

            /* Ожидаем нового запроса */
            shiftDirection = ShiftDirection.AWAITING
        }
        if (isRotateRequested) { // Если есть запрос на поворот фигуры

            /* Пробуем повернуть */
            getField().tryRotateFigure()

            /* Ожидаем нового запроса */
            isRotateRequested = false
        }

        if (isDownToBottomRequested) { //Если есть запрос на мгновенное падение фигуры
            /*Сдвигаем фигуру до низа*/
            getField().letFallDown(isDownToBottom = true)
            /* Ожидаем нового запроса */
            isDownToBottomRequested = false
        }

        /* Падение фигуры вниз происходит если loopNumber % FRAMES_PER_MOVE == 0
         * Т.е. 1 раз за FRAMES_PER_MOVE итераций или
         * FRAMES_PER_MOVE / BOOST_MULTIPLIER если есть запрос на ускорение падения
         */
        val framesPerMove = Constants.FPS / (Constants.DEFAULT_GAME_SPEED + getField().level)
        if (loopNumber % (framesPerMove / if (isBoostRequested) Constants.BOOST_MULTIPLIER else 1) == 0) {
            getField().letFallDown(isDownToBottom = false)
        }

        /* Увеличение номера итерации (по модулю FPM)*/
        loopNumber = (loopNumber + 1) % framesPerMove

        /* Если поле переполнено, игра закончена */
        endOfGame = endOfGame || getField().isOverfilled == true
    }
}