package com.petrynnel.tetrisgame.gamelogic


object Constants {
    /* Размер одной плитки */
    const val CELL_SIZE = 80F
    const val CELL_TOP_OFFSET = 1F
    const val CELL_BOTTOM_OFFSET = 76F
    const val CELL_LEFT_OFFSET = 1F
    const val CELL_RIGHT_OFFSET = 76F
    const val NEXT_FIGURE_X = 1050F
    const val NEXT_FIGURE_Y = 960F

    const val COLOR_ALPHA_FIGURE = 255
    const val COLOR_ALPHA_FIGURE_GHOST = 80

    const val FIELD_TEXT_X = 850F
    const val FIELD_TEXT_BEST_Y = 190F
    const val FIELD_TEXT_SCORE_Y = 410F
    const val FIELD_TEXT_LEVEL_Y = 635F
    const val FIELD_TEXT_SIZE = 100F

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
    private const val MOVEDOWNS_PER_SECOND = 2

    /* Количество игровых циклов в секунду*/
    private const val FPS = 60

    /* Количество игровых циклов, за которое фигура сместится вниз на одну клетку */
    const val FRAMES_PER_MOVE = FPS / MOVEDOWNS_PER_SECOND

    const val DEFAULT_GAME_SPEED = 16L

    /* Количество линий, заполненных блоками в начале */
    const val BLOCKS_INITIAL_LEVEL = COUNT_CELLS_Y / 3

    /* Максимальное и минимальное количества отсутствующих блоков в линиях, созданных в начале игры */
    const val MISSNG_BLOCKS_IN_INITIAL_LINE_MIN = COUNT_CELLS_X / 3
    const val MISSNG_BLOCKS_IN_INITIAL_LINE_MAX = COUNT_CELLS_X / 2

    /* Максимально возможная ширина фигуры */
    const val MAX_FIGURE_WIDTH = 4

    const val SWIPE_MIN_DISTANCE = 560
    const val SWIPE_MAX_OFF_PATH = 700
    const val SWIPE_THRESHOLD_VELOCITY = 200
}