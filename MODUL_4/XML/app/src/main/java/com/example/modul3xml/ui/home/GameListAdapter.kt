package com.example.modul3xml.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.modul3xml.R
import com.example.modul3xml.data.Game
import com.example.modul3xml.util.LanguageManager

class GameListAdapter(
    private val games: List<Game>,
    private val onBrowserClick: (Game) -> Unit,
    private val onDetailClick: (Game) -> Unit
) : RecyclerView.Adapter<GameListAdapter.GameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.bind(game, onBrowserClick, onDetailClick)
    }

    override fun getItemCount(): Int = games.size

    class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageGame: ImageView = itemView.findViewById(R.id.image_game)
        private val textTitle: TextView = itemView.findViewById(R.id.text_title)
        private val textYear: TextView = itemView.findViewById(R.id.text_year)
        private val textPlot: TextView = itemView.findViewById(R.id.text_plot)
        private val buttonBrowser: Button = itemView.findViewById(R.id.button_browser)
        private val buttonDetail: Button = itemView.findViewById(R.id.button_detail)

        fun bind(
            game: Game,
            onBrowserClick: (Game) -> Unit,
            onDetailClick: (Game) -> Unit
        ) {
            val isIndonesian = LanguageManager.isIndonesian(itemView.context)

            imageGame.setImageResource(game.imageRes)
            textTitle.text = game.title
            textYear.text = game.year
            textPlot.text = if (isIndonesian) game.plotId else game.plotEn

            buttonBrowser.setOnClickListener { onBrowserClick(game) }
            buttonDetail.setOnClickListener { onDetailClick(game) }
        }
    }
}