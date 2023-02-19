package com.petrynnel.tetrisgame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.petrynnel.tetrisgame.gamelogic.FigureColor
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
import com.petrynnel.tetrisgame.gamelogic.Figure
import com.petrynnel.tetrisgame.gamelogic.Main

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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        figurePaint.color = Color.GREEN

        levelPaint.style = Paint.Style.STROKE
        levelPaint.strokeWidth = 1f
        levelPaint.color = Color.GRAY

        bestTextPaint.color = Color.LTGRAY
        bestTextPaint.textSize = 100f
        canvas?.drawText("best", 815f, 180f, bestTextPaint)

        scoreTextPaint.color = Color.LTGRAY
        scoreTextPaint.textSize = 100f
        canvas?.drawText(Main.getField().score.toString(), 815f, 390f, scoreTextPaint)

        levelTextPaint.color = Color.LTGRAY
        levelTextPaint.textSize = 100f
        canvas?.drawText("level", 815f, 605f, levelTextPaint)

        canvas?.drawRect(1f, 1f, 797f, 1597f, levelPaint)

        val field = Main.getField().getTheField()
        for (x in 0 until COUNT_CELLS_X)
            for (y in 0 until COUNT_CELLS_Y + OFFSET_TOP) {
                setPaintColor(field[x][y])
                val blockXCoord = GAME_FIELD_WIDTH - (x + 1) * CELL_SIZE
                val blockYCoord = GAME_FIELD_HEIGHT - (y + 1) * CELL_SIZE
                figurePaint.alpha = 1000
                canvas?.drawRect(
                    blockXCoord + CELL_LEFT_OFFSET,
                    blockYCoord + CELL_TOP_OFFSET,
                    blockXCoord + CELL_RIGHT_OFFSET,
                    blockYCoord + CELL_BOTTOM_OFFSET,
                    figurePaint
                )
            }
        val figureGhost = Main.getField().figureGhost
        figureGhost?.let {
            figurePaint.alpha = 1000
            setPaintColor(it.color)
            for (i in 0 until it.coords.size) {
                val blockXCoord = GAME_FIELD_WIDTH - (figureGhost.coords[i]!!.x + 1) * CELL_SIZE
                val blockYCoord = GAME_FIELD_HEIGHT - (figureGhost.coords[i]!!.y + 1) * CELL_SIZE
                canvas?.drawRect(
                    blockXCoord + CELL_LEFT_OFFSET,
                    blockYCoord + CELL_TOP_OFFSET,
                    blockXCoord + CELL_RIGHT_OFFSET,
                    blockYCoord + CELL_BOTTOM_OFFSET,
                    figurePaint
                )
            }
        }

        val figure: Figure = Main.getField().figure
            figurePaint.alpha = 1000
            setPaintColor(figure.color)
            for (i in 0 until figure.coords.size) {
                val blockXCoord = GAME_FIELD_WIDTH - (figure.coords[i]!!.x + 1) * CELL_SIZE
                val blockYCoord = GAME_FIELD_HEIGHT - (figure.coords[i]!!.y + 1) * CELL_SIZE
                canvas?.drawRect(
                    blockXCoord + CELL_LEFT_OFFSET,
                    blockYCoord + CELL_TOP_OFFSET,
                    blockXCoord + CELL_RIGHT_OFFSET,
                    blockYCoord + CELL_BOTTOM_OFFSET,
                    figurePaint
                )
            }
    }

    private fun setPaintColor(color: FigureColor?) {
        when (color) {
            FigureColor.T_FORM -> figurePaint.color = FigureColor.T_FORM.color
            FigureColor.S_FORM -> figurePaint.color = FigureColor.S_FORM.color
            FigureColor.I_FORM -> figurePaint.color = FigureColor.I_FORM.color
            FigureColor.J_FORM -> figurePaint.color = FigureColor.J_FORM.color
            FigureColor.Z_FORM -> figurePaint.color = FigureColor.Z_FORM.color
            FigureColor.O_FORM -> figurePaint.color = FigureColor.O_FORM.color
            FigureColor.L_FORM -> figurePaint.color = FigureColor.L_FORM.color
            FigureColor.SHADOW_BLOCK -> figurePaint.color = FigureColor.SHADOW_BLOCK.color
            else -> figurePaint.color = FigureColor.EMPTY_BLOCK.color
        }
    }
}