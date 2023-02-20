package com.petrynnel.tetrisgame

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat.getColor
import com.petrynnel.tetrisgame.MainActivity.Companion.getField
import com.petrynnel.tetrisgame.gamelogic.Constants.CELL_BOTTOM_OFFSET
import com.petrynnel.tetrisgame.gamelogic.Constants.CELL_LEFT_OFFSET
import com.petrynnel.tetrisgame.gamelogic.Constants.CELL_RIGHT_OFFSET
import com.petrynnel.tetrisgame.gamelogic.Constants.CELL_SIZE
import com.petrynnel.tetrisgame.gamelogic.Constants.CELL_TOP_OFFSET
import com.petrynnel.tetrisgame.gamelogic.Constants.COUNT_CELLS_X
import com.petrynnel.tetrisgame.gamelogic.Constants.COUNT_CELLS_Y
import com.petrynnel.tetrisgame.gamelogic.Constants.GAME_FIELD_HEIGHT
import com.petrynnel.tetrisgame.gamelogic.Constants.GAME_FIELD_WIDTH
import com.petrynnel.tetrisgame.gamelogic.Constants.OFFSET_TOP
import com.petrynnel.tetrisgame.gamelogic.Coord
import com.petrynnel.tetrisgame.gamelogic.Figure
import com.petrynnel.tetrisgame.gamelogic.FigureColor

class CanvasView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val figurePaint = Paint()
    private val levelPaint = Paint()
    private val bestTextPaint = Paint()
    private val scoreTextPaint = Paint()
    private val levelTextPaint = Paint()

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val field = getField()
        figurePaint.isAntiAlias = true

        levelPaint.style = Paint.Style.STROKE
        levelPaint.strokeWidth = 1f
        levelPaint.color = Color.GRAY

        bestTextPaint.color = Color.LTGRAY
        bestTextPaint.textSize = 100f
        val best = maxOf(field.best, field.score)
        canvas?.drawText(best.toString(), 850f, 190f, bestTextPaint)

        scoreTextPaint.color = Color.LTGRAY
        scoreTextPaint.textSize = 100f
        canvas?.drawText(field.score.toString(), 850f, 410f, scoreTextPaint)

        levelTextPaint.color = Color.LTGRAY
        levelTextPaint.textSize = 100f
        canvas?.drawText(field.level.toString(), 850f, 635f, levelTextPaint)

        val nextFigure = Figure(Coord(0, 0), form = field.nextFigureForm)

        setPaintColor(nextFigure.color)
        for (i in 0 until nextFigure.coords.size) {
            val blockXCoord = 730f + (nextFigure.coords[i]!!.x + 1) * CELL_SIZE
            val blockYCoord = 935f + (nextFigure.coords[i]!!.y + 1) * CELL_SIZE
            val shader = LinearGradient(
                blockXCoord,
                blockYCoord,
                blockXCoord + CELL_SIZE,
                blockYCoord + CELL_SIZE,
                getColor(resources, R.color.GRADIENT_START, null),
                figurePaint.color,
                Shader.TileMode.MIRROR
            )
            figurePaint.shader = shader
            canvas?.drawRoundRect(
                blockXCoord + CELL_LEFT_OFFSET,
                blockYCoord + CELL_TOP_OFFSET,
                blockXCoord + CELL_RIGHT_OFFSET,
                blockYCoord + CELL_BOTTOM_OFFSET,
                20F,
                20F,
                figurePaint
            )
        }

        figurePaint.shader = null
        canvas?.drawRect(1f, 1f, 797f, 1597f, levelPaint)

        val gameField = field.getTheField()
        for (x in 0 until COUNT_CELLS_X)
            for (y in 0 until COUNT_CELLS_Y + OFFSET_TOP) {
                setPaintColor(gameField[x][y])
                val blockXCoord = GAME_FIELD_WIDTH - (x + 1) * CELL_SIZE
                val blockYCoord = GAME_FIELD_HEIGHT - (y + 1) * CELL_SIZE
                val shader = LinearGradient(
                    blockXCoord,
                    blockYCoord,
                    blockXCoord + CELL_SIZE,
                    blockYCoord + CELL_SIZE,
                    getColor(resources, R.color.GRADIENT_START, null),
                    figurePaint.color,
                    Shader.TileMode.MIRROR
                )
                figurePaint.shader = shader
                canvas?.drawRoundRect(
                    blockXCoord + CELL_LEFT_OFFSET,
                    blockYCoord + CELL_TOP_OFFSET,
                    blockXCoord + CELL_RIGHT_OFFSET,
                    blockYCoord + CELL_BOTTOM_OFFSET,
                    20F,
                    20F,
                    figurePaint
                )
            }
        figurePaint.shader = null
        val figureGhost = field.figureGhost
        figureGhost?.let {
            setPaintColor(it.color)
            for (i in 0 until it.coords.size) {
                val blockXCoord = GAME_FIELD_WIDTH - (figureGhost.coords[i]!!.x + 1) * CELL_SIZE
                val blockYCoord = GAME_FIELD_HEIGHT - (figureGhost.coords[i]!!.y + 1) * CELL_SIZE
                canvas?.drawRoundRect(
                    blockXCoord + CELL_LEFT_OFFSET,
                    blockYCoord + CELL_TOP_OFFSET,
                    blockXCoord + CELL_RIGHT_OFFSET,
                    blockYCoord + CELL_BOTTOM_OFFSET,
                    20F,
                    20F,
                    figurePaint
                )
            }
        }

        val figure: Figure = field.figure
        setPaintColor(figure.color)
        for (i in 0 until figure.coords.size) {
            val blockXCoord = GAME_FIELD_WIDTH - (figure.coords[i]!!.x + 1) * CELL_SIZE
            val blockYCoord = GAME_FIELD_HEIGHT - (figure.coords[i]!!.y + 1) * CELL_SIZE
            val shader = LinearGradient(
                blockXCoord,
                blockYCoord,
                blockXCoord + CELL_SIZE,
                blockYCoord + CELL_SIZE,
                getColor(resources, R.color.GRADIENT_START, null),
                figurePaint.color,
                Shader.TileMode.MIRROR
            )
            figurePaint.shader = shader
            canvas?.drawRoundRect(
                blockXCoord + CELL_LEFT_OFFSET,
                blockYCoord + CELL_TOP_OFFSET,
                blockXCoord + CELL_RIGHT_OFFSET,
                blockYCoord + CELL_BOTTOM_OFFSET,
                20F,
                20F,
                figurePaint
            )
        }
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
}