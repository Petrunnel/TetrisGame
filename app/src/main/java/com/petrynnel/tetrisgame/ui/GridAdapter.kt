package com.petrynnel.tetrisgame.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.petrynnel.tetrisgame.R
import com.petrynnel.tetrisgame.TetrisApp.Companion.prefHelper
import com.petrynnel.tetrisgame.gamelogic.GameFieldBackgroundColor
import com.petrynnel.tetrisgame.gamelogic.GameFieldBackgroundColor.Companion.backgroundColors

class GridAdapter(
    private val mContext: Context,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<GridAdapter.GridViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(gameFieldBackgroundColor: GameFieldBackgroundColor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        return GridViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.item_background_color, parent, false),
            mContext,
            itemClickListener
        )
    }

    override fun getItemCount(): Int {
        return backgroundColors.size
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        holder.bind(backgroundColors[position], position)
    }

    class GridViewHolder(
        view: View,
        private val mContext: Context,
        private val listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(view) {
        private var backgroundColor: MaterialCardView = view.findViewById(R.id.cvBackgroundColor)
        private var check: MaterialCardView = view.findViewById(R.id.cvCheck)

        fun bind(gameFieldBackgroundColor: GameFieldBackgroundColor, position: Int) {
            backgroundColor.setCardBackgroundColor(mContext.getColor(gameFieldBackgroundColor.color))
            when (backgroundColors[position].color) {
                prefHelper.loadBackgroundColor() -> {
                    backgroundColor.strokeColor = mContext.getColor(R.color.colorPrimary)
                    check.visibility = View.VISIBLE
                }
                else -> {
                    backgroundColor.strokeColor = mContext.getColor(R.color.text_color)
                    check.visibility = View.INVISIBLE
                }
            }
            backgroundColor.setOnClickListener {
                listener.onItemClick(gameFieldBackgroundColor)
            }
        }
    }
}