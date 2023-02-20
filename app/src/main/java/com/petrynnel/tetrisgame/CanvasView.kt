package com.petrynnel.tetrisgame

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat.getColor
import com.petrynnel.tetrisgame.MainActivity.Companion.getField
import com.petrynnel.tetrisgame.gamelogic.Constants.CELL_BOTTOM_OFFSET
import com.petrynnel.tetrisgame.gamelogic.Constants.CELL_LEFT_OFFSET
import com.petrynnel.tetrisgame.gamelogic.Constants.CELL_RIGHT_OFFSET
import com.petrynnel.tetrisgame.gamelogic.Constants.CELL_SIZE
import com.petrynnel.tetrisgame.gamelogic.Constants.CELL_TOP_OFFSET
import com.petrynnel.tetrisgame.gamelogic.Constants.COLOR_ALPHA_FIGURE
import com.petrynnel.tetrisgame.gamelogic.Constants.COLOR_ALPHA_FIGURE_GHOST
import com.petrynnel.tetrisgame.gamelogic.Constants.COUNT_CELLS_X
import com.petrynnel.tetrisgame.gamelogic.Constants.COUNT_CELLS_Y
import com.petrynnel.tetrisgame.gamelogic.Constants.FIELD_TEXT_BEST_Y
import com.petrynnel.tetrisgame.gamelogic.Constants.FIELD_TEXT_LEVEL_Y
import com.petrynnel.tetrisgame.gamelogic.Constants.FIELD_TEXT_SCORE_Y
import com.petrynnel.tetrisgame.gamelogic.Constants.FIELD_TEXT_SIZE
import com.petrynnel.tetrisgame.gamelogic.Constants.FIELD_TEXT_X
import com.petrynnel.tetrisgame.gamelogic.Constants.GAME_FIELD_HEIGHT
import com.petrynnel.tetrisgame.gamelogic.Constants.GAME_FIELD_WIDTH
import com.petrynnel.tetrisgame.gamelogic.Constants.NEXT_FIGURE_X
import com.petrynnel.tetrisgame.gamelogic.Constants.NEXT_FIGURE_Y
import com.petrynnel.tetrisgame.gamelogic.Constants.OFFSET_TOP
import com.petrynnel.tetrisgame.gamelogic.Coord
import com.petrynnel.tetrisgame.gamelogic.Figure
import com.petrynnel.tetrisgame.gamelogic.FigureColor
import com.petrynnel.tetrisgame.gamelogic.GameField

class CanvasView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val figurePaint = Paint(ANTI_ALIAS_FLAG)
    private val levelPaint = Paint(ANTI_ALIAS_FLAG)
    private val textPaint = Paint(ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val field = getField()

        levelPaint.style = Paint.Style.STROKE
        levelPaint.strokeWidth = 1f
        levelPaint.color = Color.GRAY
        canvas.drawRect(1f, 1f, 797f, 1597f, levelPaint)

        textPaint.color = Color.LTGRAY
        textPaint.textSize = FIELD_TEXT_SIZE
        val best = maxOf(field.best, field.score)

        canvas.drawText(best.toString(), FIELD_TEXT_X, FIELD_TEXT_BEST_Y, textPaint)
        canvas.drawText(field.score.toString(), FIELD_TEXT_X, FIELD_TEXT_SCORE_Y, textPaint)
        canvas.drawText(field.level.toString(), FIELD_TEXT_X, FIELD_TEXT_LEVEL_Y, textPaint)

        drawGameField(canvas, field, figurePaint)
        drawFigure(canvas, field.figureGhost, figurePaint, COLOR_ALPHA_FIGURE_GHOST)
        drawFigure(canvas, field.figure, figurePaint, hasShader = true)
        drawFigure(canvas, generateNextFigure(), figurePaint, hasShader = true, isNextFigure = true)
    }

    private fun setPaintColor(color: FigureColor?) {
        when (color) {
            FigureColor.T_FORM -> figurePaint.color =
                getColor(resources, FigureColor.T_FORM.color, null)
            FigureColor.S_FORM -> figurePaint.color =
                getColor(resources, FigureColor.S_FORM.color, null)
            FigureColor.I_FORM -> figurePaint.color =
                getColor(resources, FigureColor.I_FORM.color, null)
            FigureColor.J_FORM -> figurePaint.color =
                getColor(resources, FigureColor.J_FORM.color, null)
            FigureColor.Z_FORM -> figurePaint.color =
                getColor(resources, FigureColor.Z_FORM.color, null)
            FigureColor.O_FORM -> figurePaint.color =
                getColor(resources, FigureColor.O_FORM.color, null)
            FigureColor.L_FORM -> figurePaint.color =
                getColor(resources, FigureColor.L_FORM.color, null)
            FigureColor.SHADOW_BLOCK -> figurePaint.color =
                getColor(resources, FigureColor.SHADOW_BLOCK.color, null)
            else -> figurePaint.color = getColor(resources, FigureColor.EMPTY_BLOCK.color, null)
        }
    }

    private fun generateNextFigure() = Figure(Coord(0, 0), form = getField().nextFigureForm)

    private fun drawGameField(canvas: Canvas, field: GameField, paint: Paint ) {
        val gameField = field.getTheField()
        for (x in 0 until COUNT_CELLS_X)
            for (y in 0 until COUNT_CELLS_Y + OFFSET_TOP) {
                setPaintColor(gameField[x][y])
                val blockXCoord = GAME_FIELD_WIDTH - (x + 1) * CELL_SIZE
                val blockYCoord = GAME_FIELD_HEIGHT - (y + 1) * CELL_SIZE
                paint.shader = shader(blockXCoord, blockYCoord, paint)
                drawBlock(canvas, blockXCoord, blockYCoord, paint)
            }
    }

    private fun drawFigure(
        canvas: Canvas,
        figure: Figure?,
        paint: Paint,
        alpha: Int = COLOR_ALPHA_FIGURE,
        hasShader: Boolean = false,
        isNextFigure: Boolean = false
    ) {
        figure?.let {
            setPaintColor(figure.color)
            val startCoordX = if (isNextFigure) NEXT_FIGURE_X else GAME_FIELD_WIDTH
            val startCoordY = if (isNextFigure) NEXT_FIGURE_Y else GAME_FIELD_HEIGHT
            for (i in 0 until figure.coords.size) {
                val blockXCoord = startCoordX - (figure.coords[i]!!.x + 1) * CELL_SIZE
                val blockYCoord = startCoordY - (figure.coords[i]!!.y + 1) * CELL_SIZE
                paint.alpha = alpha
                paint.shader = if (hasShader) shader(blockXCoord, blockYCoord, paint) else null
                drawBlock(canvas, blockXCoord, blockYCoord, paint)
            }
        }
    }

    private fun shader(x: Float, y: Float, paint: Paint): LinearGradient {
        return LinearGradient(
            x,
            y,
            x + CELL_SIZE,
            y + CELL_SIZE,
            getColor(resources, R.color.GRADIENT_START, null),
            paint.color,
            Shader.TileMode.MIRROR
        )
    }

    private fun drawBlock(canvas: Canvas, x: Float, y: Float, paint: Paint) {
        canvas.drawRoundRect(
            x + CELL_LEFT_OFFSET,
            y + CELL_TOP_OFFSET,
            x + CELL_RIGHT_OFFSET,
            y + CELL_BOTTOM_OFFSET,
            20F,
            20F,
            paint
        )
    }
}