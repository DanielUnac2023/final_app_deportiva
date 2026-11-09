package unac.edu.final_app_deportiva.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import unac.edu.final_app_deportiva.R
import unac.edu.final_app_deportiva.model.TableEntry

/**
 * Adaptador RecyclerView para mostrar una lista de entradas de tabla (equipos y posiciones).
 * Utiliza el diseño item_table.xml para cada fila y muestra clasificación, nombre de equipo,
 * juegos jugados y puntos.
 */
class TableAdapter(
    private val tableList: List<TableEntry>
) : RecyclerView.Adapter<TableAdapter.TableViewHolder>() {

    class TableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRank: TextView = itemView.findViewById(R.id.tvRank)
        val tvTeam: TextView = itemView.findViewById(R.id.tvTableTeam)
        val tvPlayed: TextView = itemView.findViewById(R.id.tvPlayed)
        val tvPoints: TextView = itemView.findViewById(R.id.tvPoints)
    }

    // Infla el diseño del item de tabla para crear un nuevo ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_table, parent, false)
        return TableViewHolder(view)
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        val entry = tableList[position]
        holder.tvRank.text = entry.intRank ?: "-"
        holder.tvTeam.text = entry.strTeam ?: "Equipo"
        holder.tvPlayed.text = entry.intPlayed ?: "0"
        holder.tvPoints.text = entry.intPoints ?: "0"
    }

    override fun getItemCount(): Int = tableList.size
}