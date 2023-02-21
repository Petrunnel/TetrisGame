package com.petrynnel.tetrisgame.gamelogic


object Constants {
    /* Размер одной плитки */
    const val CELL_SIZE = 80F

    /* Количество ячеек на экране по горизонтали и вертикали */
    const val COUNT_CELLS_X = 10
    const val COUNT_CELLS_Y = 2 * COUNT_CELLS_X

    /*Невидимое пространство сверху, в котором создаются фигуры*/
    const val OFFSET_TOP = 3

    /* Параметры окна */
    const val GAME_FIELD_WIDTH = COUNT_CELLS_X * CELL_SIZE
    const val GAME_FIELD_HEIGHT = COUNT_CELLS_Y * CELL_SIZE

    /* Отступы от начальных координат для рисования блоков */
    const val CELL_TOP_OFFSET = 1F
    const val CELL_BOTTOM_OFFSET = 76F
    const val CELL_LEFT_OFFSET = 1F
    const val CELL_RIGHT_OFFSET = 76F

    /* Радиус скругления блоков*/
    const val BLOCK_CORNER_RADIUS = 20F

    /* Координаты начальной точки для отрисовки следующей фигуры */
    const val NEXT_FIGURE_X = 1050F
    const val NEXT_FIGURE_Y = 960F

    /* Значения прозрачности для блока*/
    const val COLOR_ALPHA_FIGURE = 255
    const val COLOR_ALPHA_FIGURE_GHOST = 80

    /* Координаты для отрисовки текстовой информации об игре*/
    const val FIELD_TEXT_X = 850F
    const val FIELD_TEXT_BEST_Y = 190F
    const val FIELD_TEXT_SCORE_Y = 410F
    const val FIELD_TEXT_LEVEL_Y = 635F

    /* Размер текста информации об игре*/
    const val FIELD_TEXT_SIZE = 100F

    /* Отступы и толщина для отрисовки границ игрового поля */
    const val FIELD_BOUND_TOP = 1F
    const val FIELD_BOUND_BOTTOM = 1597F
    const val FIELD_BOUND_LEFT = 1F
    const val FIELD_BOUND_RIGHT = 797F
    const val FIELD_BOUND_WIDTH = 1F

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

    /* Скорость игры по умолчанию */
    const val DEFAULT_GAME_SPEED = 16L

    /* Количество линий, заполненных блоками в начале */
    const val BLOCKS_INITIAL_LEVEL = COUNT_CELLS_Y / 3

    /* Максимальное и минимальное количества отсутствующих блоков в линиях, созданных в начале игры */
    const val MISSNG_BLOCKS_IN_INITIAL_LINE_MIN = COUNT_CELLS_X / 3
    const val MISSNG_BLOCKS_IN_INITIAL_LINE_MAX = COUNT_CELLS_X / 2

    /* Максимально возможная ширина фигуры */
    const val MAX_FIGURE_WIDTH = 4

    /* Значение параметров дистанции и скорости для срабатывания свайпа вниз */
    const val SWIPE_MIN_DISTANCE = 560
    const val SWIPE_MAX_OFF_PATH = 700
    const val SWIPE_THRESHOLD_VELOCITY = 200
}