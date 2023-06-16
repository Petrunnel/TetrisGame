package com.petrynnel.tetrisgame.ui.main

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat.getColor
import com.petrynnel.tetrisgame.R
import com.petrynnel.tetrisgame.TetrisApp.Companion.prefHelper
import com.petrynnel.tetrisgame.gamelogic.*
import com.petrynnel.tetrisgame.gamelogic.Constants.BLOCK_CORNER_RADIUS_DISABLED
import com.petrynnel.tetrisgame.gamelogic.Constants.BLOCK_CORNER_RADIUS_HUGE
import com.petrynnel.tetrisgame.gamelogic.Constants.BLOCK_CORNER_RADIUS_SMALL
import com.petrynnel.tetrisgame.gamelogic.Constants.BLOCK_SIZE_DRAWABLE
import com.petrynnel.tetrisgame.gamelogic.Constants.CELL_BOTTOM_OFFSET
import com.petrynnel.tetrisgame.gamelogic.Constants.CELL_LEFT_OFFSET
import com.petrynnel.tetrisgame.gamelogic.Constants.CELL_RIGHT_OFFSET
import com.petrynnel.tetrisgame.gamelogic.Constants.CELL_SIZE
import com.petrynnel.tetrisgame.gamelogic.Constants.CELL_SIZE_NO_MARGINS
import com.petrynnel.tetrisgame.gamelogic.Constants.CELL_TOP_OFFSET
import com.petrynnel.tetrisgame.gamelogic.Constants.COLOR_ALPHA_FIGURE
import com.petrynnel.tetrisgame.gamelogic.Constants.COLOR_ALPHA_FIGURE_GHOST
import com.petrynnel.tetrisgame.gamelogic.Constants.COUNT_CELLS_X
import com.petrynnel.tetrisgame.gamelogic.Constants.COUNT_CELLS_Y
import com.petrynnel.tetrisgame.gamelogic.Constants.FIELD_BOUND_BOTTOM
import com.petrynnel.tetrisgame.gamelogic.Constants.FIELD_BOUND_LEFT
import com.petrynnel.tetrisgame.gamelogic.Constants.FIELD_BOUND_RIGHT
import com.petrynnel.tetrisgame.gamelogic.Constants.FIELD_BOUND_TOP
import com.petrynnel.tetrisgame.gamelogic.Constants.FIELD_BOUND_WIDTH
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
import com.petrynnel.tetrisgame.gamelogic.Logic.getField

class CanvasView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val figurePaint = Paint(ANTI_ALIAS_FLAG)
    private val levelPaint = Paint(ANTI_ALIAS_FLAG)
    private val textPaint = Paint(ANTI_ALIAS_FLAG)
    private val backgroundPaint = Paint(ANTI_ALIAS_FLAG)

    private val backgroundColor = resources.getColor(prefHelper.loadBackgroundColor(), null)
    private val cornerRadius = when (prefHelper.loadBlockShape()) {
        BlockShape.SQUARE -> BLOCK_CORNER_RADIUS_DISABLED
        BlockShape.ROUNDED_SQUARE -> BLOCK_CORNER_RADIUS_SMALL
        BlockShape.CIRCLE -> BLOCK_CORNER_RADIUS_HUGE
        else -> BLOCK_CORNER_RADIUS_DISABLED
    }
    private val hasShader = prefHelper.loadHasShader()
    private val hasShadow = prefHelper.loadHasShadow()
    private val hasBitmap = prefHelper.hasBitmap()

    private val matrix = Matrix()
    private val iBlockBitmapSource = BitmapFactory.decodeResource(resources, R.drawable.i_block)
    private val jBlockBitmapSource = BitmapFactory.decodeResource(resources, R.drawable.j_block)
    private val lBlockBitmapSource = BitmapFactory.decodeResource(resources, R.drawable.l_block)
    private val oBlockBitmapSource = BitmapFactory.decodeResource(resources, R.drawable.o_block)
    private val sBlockBitmapSource = BitmapFactory.decodeResource(resources, R.drawable.s_block)
    private val tBlockBitmapSource = BitmapFactory.decodeResource(resources, R.drawable.t_block)
    private val zBlockBitmapSource = BitmapFactory.decodeResource(resources, R.drawable.z_block)
    private val shadowBlockBitmapSource =
        BitmapFactory.decodeResource(resources, R.drawable.shadow_block)

    private var iBlockBitmap: Bitmap
    private var jBlockBitmap: Bitmap
    private var lBlockBitmap: Bitmap
    private var oBlockBitmap: Bitmap
    private var sBlockBitmap: Bitmap
    private var tBlockBitmap: Bitmap
    private var zBlockBitmap: Bitmap
    private var shadowBlockBitmap: Bitmap

    init {
        val destiny = resources.displayMetrics.density
        // Для расчета масштаба bitmap
        val scale: Float = CELL_SIZE_NO_MARGINS / ( destiny * BLOCK_SIZE_DRAWABLE )
        matrix.postScale(scale, scale)
        matrix.postRotate(0F)
        iBlockBitmap = createBitmapFromSource(iBlockBitmapSource)
        jBlockBitmap = createBitmapFromSource(jBlockBitmapSource)
        lBlockBitmap = createBitmapFromSource(lBlockBitmapSource)
        oBlockBitmap = createBitmapFromSource(oBlockBitmapSource)
        sBlockBitmap = createBitmapFromSource(sBlockBitmapSource)
        tBlockBitmap = createBitmapFromSource(tBlockBitmapSource)
        zBlockBitmap = createBitmapFromSource(zBlockBitmapSource)
        shadowBlockBitmap = createBitmapFromSource(shadowBlockBitmapSource)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val field = getField()

        backgroundPaint.apply {
            style = Paint.Style.FILL
            color = backgroundColor
        }

        levelPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = FIELD_BOUND_WIDTH
            color = Color.GRAY
        }

        canvas.drawRect(
            FIELD_BOUND_LEFT,
            FIELD_BOUND_TOP,
            FIELD_BOUND_RIGHT,
            FIELD_BOUND_BOTTOM,
            backgroundPaint
        )

        canvas.drawRect(
            FIELD_BOUND_LEFT,
            FIELD_BOUND_TOP,
            FIELD_BOUND_RIGHT,
            FIELD_BOUND_BOTTOM,
            levelPaint
        )

        textPaint.apply {
            color = Color.LTGRAY
            textSize = FIELD_TEXT_SIZE
        }
        val best = maxOf(field.best, field.score)
        canvas.drawText(best.toString(), FIELD_TEXT_X, FIELD_TEXT_BEST_Y, textPaint)
        canvas.drawText(field.score.toString(), FIELD_TEXT_X, FIELD_TEXT_SCORE_Y, textPaint)
        canvas.drawText(field.level.toString(), FIELD_TEXT_X, FIELD_TEXT_LEVEL_Y, textPaint)

        drawGameField(canvas, field, figurePaint)
        if (hasShadow) {
            drawFigure(canvas, field.figureGhost, figurePaint, COLOR_ALPHA_FIGURE_GHOST)
        }
        drawFigure(canvas, field.figure, figurePaint, hasShader = hasShader)
        drawFigure(
            canvas,
            generateNextFigure(),
            figurePaint,
            hasShader = hasShader,
            isNextFigure = true
        )
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

    private fun getBitmap(color: FigureColor?): Bitmap? {
        return when (color) {
            FigureColor.T_FORM -> tBlockBitmap
            FigureColor.S_FORM -> sBlockBitmap
            FigureColor.I_FORM -> iBlockBitmap
            FigureColor.J_FORM -> jBlockBitmap
            FigureColor.Z_FORM -> zBlockBitmap
            FigureColor.O_FORM -> oBlockBitmap
            FigureColor.L_FORM -> lBlockBitmap
            FigureColor.SHADOW_BLOCK -> shadowBlockBitmap
            else -> null
        }
    }

    private fun generateNextFigure() = Figure(Coord(0, 0), form = getField().nextFigureForm)

    private fun drawGameField(canvas: Canvas, field: GameField, paint: Paint) {
        val gameField = field.getTheField()
        for (x in 0 until COUNT_CELLS_X) for (y in 0 until COUNT_CELLS_Y + OFFSET_TOP) {
            setPaintColor(gameField[x][y])
            val currentBitmap = getBitmap(gameField[x][y])
            val blockXCoord = GAME_FIELD_WIDTH - (x + 1) * CELL_SIZE
            val blockYCoord = GAME_FIELD_HEIGHT - (y + 1) * CELL_SIZE
            if (hasShader)
                paint.shader = shader(blockXCoord, blockYCoord, paint)
            else
                paint.shader = null
            if (currentBitmap != null && hasBitmap)
                drawBlockBitmap(canvas, currentBitmap, blockXCoord, blockYCoord, paint)
            else
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
            val currentBitmap = getBitmap(figure.color)
            val startCoordX = if (isNextFigure) NEXT_FIGURE_X else GAME_FIELD_WIDTH
            val startCoordY = if (isNextFigure) NEXT_FIGURE_Y else GAME_FIELD_HEIGHT
            for (i in 0 until figure.coords.size) {
                val blockXCoord = startCoordX - (figure.coords[i]!!.x + 1) * CELL_SIZE
                val blockYCoord = startCoordY - (figure.coords[i]!!.y + 1) * CELL_SIZE
                paint.alpha = alpha
                paint.shader = if (hasShader) shader(blockXCoord, blockYCoord, paint) else null
                if (currentBitmap != null && hasBitmap)
                    drawBlockBitmap(canvas, currentBitmap, blockXCoord, blockYCoord, paint)
                else
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

    private fun createBitmapFromSource(source: Bitmap): Bitmap {
        return Bitmap.createBitmap(
            source,
            0,
            0,
            source.width,
            source.height,
            matrix,
            true
        )
    }

    private fun drawBlockBitmap(canvas: Canvas, bitmap: Bitmap, x: Float, y: Float, paint: Paint) {
        canvas.drawBitmap(
            bitmap,
            x + CELL_LEFT_OFFSET,
            y + CELL_TOP_OFFSET,
            paint
        )
    }

    private fun drawBlock(canvas: Canvas, x: Float, y: Float, paint: Paint) {
        canvas.drawRoundRect(
            x + CELL_LEFT_OFFSET,
            y + CELL_TOP_OFFSET,
            x + CELL_RIGHT_OFFSET,
            y + CELL_BOTTOM_OFFSET,
            cornerRadius,
            cornerRadius,
            paint
        )
    }
}