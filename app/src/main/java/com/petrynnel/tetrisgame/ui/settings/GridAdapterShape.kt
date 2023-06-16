package com.petrynnel.tetrisgame.ui.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.petrynnel.tetrisgame.R
import com.petrynnel.tetrisgame.TetrisApp.Companion.prefHelper
import com.petrynnel.tetrisgame.gamelogic.BlockShape
import com.petrynnel.tetrisgame.gamelogic.BlockShape.Companion.blockShapes
import com.petrynnel.tetrisgame.gamelogic.Constants.BLOCK_CORNER_RADIUS_DISABLED
import com.petrynnel.tetrisgame.gamelogic.Constants.BLOCK_CORNER_RADIUS_HUGE
import com.petrynnel.tetrisgame.gamelogic.Constants.BLOCK_CORNER_RADIUS_SMALL
import com.petrynnel.tetrisgame.isVisibleOrInvisible

class GridAdapterShape(
    private val mContext: Context,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<GridAdapterShape.GridViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(blockShape: BlockShape)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        return GridViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.item_block_shape, parent, false),
            mContext,
            itemClickListener
        )
    }

    override fun getItemCount(): Int {
        return blockShapes.size
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        holder.bind(blockShapes[position])
    }

    class GridViewHolder(
        view: View,
        private val mContext: Context,
        private val listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(view) {
        private var cvShape: MaterialCardView = view.findViewById(R.id.cvShape)
        private var check: MaterialCardView = view.findViewById(R.id.cvCheck)
        private var ivBlockForm: ImageView = view.findViewById(R.id.ivBitmap)
        private var cvBlockForm: MaterialCardView = view.findViewById(R.id.cvBlockForm)
        private var gradient: View = view.findViewById(R.id.vGradient)


        fun bind(shape: BlockShape) {
            cvShape.setCardBackgroundColor(mContext.getColor(R.color.black))
            ivBlockForm.setImageResource(R.drawable.l_block)
            cvBlockForm.setCardBackgroundColor(mContext.getColor(R.color.L_FORM))
            when (shape) {
                prefHelper.loadBlockShape() -> {
                    cvShape.strokeColor = mContext.getColor(R.color.colorPrimary)
                    check.visibility = View.VISIBLE
                }
                else -> {
                    cvShape.strokeColor = mContext.getColor(R.color.text_color)
                    check.visibility = View.INVISIBLE
                }
            }

            val hasShader = prefHelper.loadHasShader()
            when (shape) {
                BlockShape.SQUARE -> {
                    cvBlockForm.radius = BLOCK_CORNER_RADIUS_DISABLED
                    ivBlockForm.visibility = View.INVISIBLE
                    cvBlockForm.visibility = View.VISIBLE
                    gradient.isVisibleOrInvisible(hasShader)
                }
                BlockShape.ROUNDED_SQUARE -> {
                    cvBlockForm.radius = BLOCK_CORNER_RADIUS_SMALL
                    ivBlockForm.visibility = View.INVISIBLE
                    cvBlockForm.visibility = View.VISIBLE
                    gradient.isVisibleOrInvisible(hasShader)
                }
                BlockShape.CIRCLE -> {
                    cvBlockForm.radius = BLOCK_CORNER_RADIUS_HUGE
                    ivBlockForm.visibility = View.INVISIBLE
                    cvBlockForm.visibility = View.VISIBLE
                    gradient.isVisibleOrInvisible(hasShader)
                }
                BlockShape.BITMAP -> {
                    ivBlockForm.visibility = View.VISIBLE
                    cvBlockForm.visibility = View.INVISIBLE
                    gradient.visibility = View.INVISIBLE
                }
            }
            cvShape.setOnClickListener {
                listener.onItemClick(shape)
            }
        }
    }
}