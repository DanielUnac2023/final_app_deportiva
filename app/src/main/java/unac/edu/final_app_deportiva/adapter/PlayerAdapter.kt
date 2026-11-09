package unac.edu.final_app_deportiva.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import unac.edu.final_app_deportiva.R
import unac.edu.final_app_deportiva.model.Player

/**
 * Adaptador RecyclerView para mostrar una lista de jugadores.
 * Utiliza el diseño item_player.xml para cada elemento y carga imágenes con Coil.
 * Permite manejar clics en cada elemento de jugador a través del callback onPlayerClick.
 */
class PlayerAdapter(
    private val playerList: List<Player>,
    private val onPlayerClick: (Player) -> Unit
) : RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPlayer: ImageView = itemView.findViewById(R.id.imgPlayer)
        val tvName: TextView = itemView.findViewById(R.id.tvPlayerName)
        val tvPosition: TextView = itemView.findViewById(R.id.tvPlayerPosition)
        val tvNationality: TextView = itemView.findViewById(R.id.tvPlayerNationality)
    }

    // Infla el diseño del item de jugador para crear un nuevo ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_player, parent, false)
        return PlayerViewHolder(view)
    }

    // Vincula los datos del jugador en la posición dada al ViewHolder correspondiente
    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val player = playerList[position]
        holder.tvName.text = player.strPlayer
        holder.tvPosition.text = player.strPosition ?: "Posición desconocida"
        holder.tvNationality.text = player.strNationality ?: ""

        // Cargar foto usando Coil (probamos cutout primero, luego thumb)
        val imageUrl = player.strCutout ?: player.strThumb
        if (!imageUrl.isNull_or_Empty()) {
            holder.imgPlayer.load(imageUrl) {
                crossfade(true)
                placeholder(android.R.drawable.ic_menu_gallery)
                error(android.R.drawable.ic_menu_report_image)
            }
        } else {
            holder.imgPlayer.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        // Listener al hacer clic en el jugador
        holder.itemView.setOnClickListener { onPlayerClick(player) }
    }

    override fun getItemCount(): Int = playerList.size
}

// Extensión rápida para validar texto nulo o vacío
private fun String?.isNull_or_Empty(): Boolean = this == null || this.trim().isEmpty()