package com.example.modul3xml.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.modul3xml.R
import com.example.modul3xml.data.Game

class HighlightAdapter(
    private val games: List<Game>
) : RecyclerView.Adapter<HighlightAdapter.HighlightViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighlightViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_highlight_game, parent, false)
        return HighlightViewHolder(view)
    }

    override fun onBindViewHolder(holder: HighlightViewHolder, position: Int) {
        holder.bind(games[position])
    }

    override fun getItemCount(): Int = games.size

    class HighlightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageGame: ImageView = itemView.findViewById(R.id.image_highlight)

        fun bind(game: Game) {
            imageGame.setImageResource(game.imageRes)
        }
    }
}