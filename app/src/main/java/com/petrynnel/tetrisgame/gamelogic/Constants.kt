package com.petrynnel.tetrisgame.gamelogic


object Constants {
    /* Размер одной плитки */
    const val CELL_SIZE = 80F

    /* Размер одной плитки без полей */
    const val CELL_SIZE_NO_MARGINS = 76F

    /* Разрешение картинок png для блоков */
    const val BLOCK_SIZE_DRAWABLE = 48F

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
    const val BLOCK_CORNER_RADIUS_DISABLED = 0F
    const val BLOCK_CORNER_RADIUS_SMALL = 20F
    const val BLOCK_CORNER_RADIUS_HUGE = 200F

    /* Координаты начальной точки для отрисовки следующей фигуры */
    const val NEXT_FIGURE_X = 1050F
    const val NEXT_FIGURE_Y = 960F

    /* Значения прозрачности для блока*/
    const val COLOR_ALPHA_FIGURE = 255
    const val COLOR_ALPHA_FIGURE_GHOST = 80

    /* Координаты для отрисовки текстовой информации об игре*/
    const val FIELD_TEXT_X = 850F
    const val FIELD_TEXT_BEST_Y = 190F
    const val FIELD_LABEL_SCORE_Y = 210
    const val FIELD_TEXT_SCORE_Y = 410F
    const val FIELD_LABEL_LEVEL_Y = 435
    const val FIELD_TEXT_LEVEL_Y = 635F
    const val FIELD_LABEL_NEXT_Y = 660

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
    const val BOOST_MULTIPLIER = 10

    /* Задержка между тиками в игре */
    const val GAME_TICK_DELAY = 1L

    /* Скорость игры по умолчанию */
    const val DEFAULT_GAME_SPEED = 1

    /* Количество игровых циклов в секунду*/
    const val FPS = 1000

    /* Количество линий, заполненных блоками в начале */
    const val DEFAULT_BLOCKS_INITIAL_LEVEL = COUNT_CELLS_Y / 3

    /* Начальный уровень по умолчанию */
    const val DEFAULT_INITIAL_LEVEL = 1

    /* Максимальный уровень */
    const val MAX_LEVEL = 10

    /* Очков до повышения уровня */
    const val SCORE_TO_NEXT_LEVEL = 50

    /* Количество очков за удаление 1-4 линий */
    const val SCORE_FOR_ONE_LINE = 1
    const val SCORE_FOR_DOUBLE_LINE = 3
    const val SCORE_FOR_TRIPLE_LINE = 7
    const val SCORE_FOR_QUADRUPLE_LINE = 15

    /* Максимальное и минимальное количества отсутствующих блоков в линиях, созданных в начале игры */
    const val MISSNG_BLOCKS_IN_INITIAL_LINE_MIN = COUNT_CELLS_X / 3
    const val MISSNG_BLOCKS_IN_INITIAL_LINE_MAX = COUNT_CELLS_X / 2

    /* Максимально возможная ширина фигуры */
    const val MAX_FIGURE_WIDTH = 4

    /* Значение параметров дистанции и скорости для срабатывания свайпа вниз */
    const val SWIPE_MIN_DISTANCE = 400
    const val SWIPE_MAX_OFF_PATH = 700
    const val SWIPE_THRESHOLD_VELOCITY = 200

    /* Количество рекордов */
    const val NUMBER_OF_RECORDS = 10
}