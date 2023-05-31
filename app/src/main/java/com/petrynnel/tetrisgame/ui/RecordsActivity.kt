package com.petrynnel.tetrisgame.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.petrynnel.tetrisgame.R
import com.petrynnel.tetrisgame.databinding.ActivityRecordsBinding

class RecordsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecordsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvRecords.adapter = RecordsAdapter(this)
    }

    class RecordsAdapter(context: Context) : RecyclerView.Adapter<RecordsAdapter.ViewHolder>() {
        private var mContext: Context = context
        private val bestScores = getBestScores().filter { it > 0 }.sortedDescending()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item, parent, false))
        }

        override fun getItemCount(): Int = bestScores.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = "${position + 1}.\t ${bestScores[position]}"
            holder.item.text = item
        }

        private fun getBestScores(): IntArray {
            val sharedPreference = mContext.getSharedPreferences("HIGH_SCORE", Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreference.getString("high_score", gson.toJson(IntArray(10)))
            return gson.fromJson(json, IntArray::class.java) ?: IntArray(10)
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var item: TextView = view.findViewById(R.id.item)
        }
    }
}