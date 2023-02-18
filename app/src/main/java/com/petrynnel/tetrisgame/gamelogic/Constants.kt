package com.petrynnel.tetrisgame.gamelogic


object Constants {
    /* Размер одной плитки */
    const val CELL_SIZE = 80F
    const val CELL_TOP_OFFSET = 1F
    const val CELL_BOTTOM_OFFSET = 76F
    const val CELL_LEFT_OFFSET = 1F
    const val CELL_RIGHT_OFFSET = 76F

    /* Количество ячеек на экране по горизонтали и вертикали */
    const val COUNT_CELLS_X = 10
    const val COUNT_CELLS_Y = 2 * COUNT_CELLS_X

    /*Невидимое пространство сверху, в котором создаются фигуры*/
    const val OFFSET_TOP = 3

    /* Параметры окна */
    const val GAME_FIELD_WIDTH = COUNT_CELLS_X * CELL_SIZE
    const val GAME_FIELD_HEIGHT = COUNT_CELLS_Y * CELL_SIZE

    /* Количество раз, в которое увеличивается скорость падения,
     * если пользователь нажал соответсвующую клавишу
     */
    const val BOOST_MULTIPLIER = 5

    /* Количество клеток, на которое в секунду смещается вниз фигура*/
    const val MOVEDOWNS_PER_SECOND = 2

    /* Количество игровых циклов в секунду*/
    const val FPS = 60

    /* Количество игровых циклов, за которое фигура сместится вниз на одну клетку */
    const val FRAMES_PER_MOVE = FPS / MOVEDOWNS_PER_SECOND

    const val FRAME_TIME = 1000L / FPS
    const val DEFAULT_GAME_SPEED = FRAME_TIME
    const val FAST_DOWN_SPEED = DEFAULT_GAME_SPEED / BOOST_MULTIPLIER

    /* Количество линий, заполненных блоками в начале */
    const val BLOCKS_INITIAL_LEVEL = COUNT_CELLS_Y / 3

    /* Максимальное и минимальное количества отсутствующих блоков в линиях, созданных в начале игры */
    const val MISSNG_BLOCKS_IN_INITIAL_LINE_MIN = COUNT_CELLS_X / 3
    const val MISSNG_BLOCKS_IN_INITIAL_LINE_MAX = COUNT_CELLS_X / 2

    /* Максимально возможная ширина фигуры */
    const val MAX_FIGURE_WIDTH = 4


    const val SWIPE_MIN_DISTANCE = 120
    const val SWIPE_MAX_OFF_PATH = 250
    const val SWIPE_THRESHOLD_VELOCITY = 200
}