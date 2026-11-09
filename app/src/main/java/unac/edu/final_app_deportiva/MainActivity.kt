package unac.edu.final_app_deportiva

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kotlinx.coroutines.launch
import unac.edu.final_app_deportiva.adapter.PlayerAdapter
import unac.edu.final_app_deportiva.adapter.TableAdapter
import unac.edu.final_app_deportiva.model.Player
import unac.edu.final_app_deportiva.model.Team
import unac.edu.final_app_deportiva.network.RetrofitClient

/**
 * Actividad principal de la aplicación "RETRO FOOTBALL CLUB".
 * Funciona como punto de control para la búsqueda de equipos, carga de plantillas
 * de jugadores y tablas de posiciones, además de la interfaz del diálogo de detalle
 * de jugadores con imagen en alta resolución.
 */
class MainActivity : AppCompatActivity() {

    // Componentes de la interfaz de usuario vinculados desde el layout XML
    private lateinit var etSearchTeam: EditText   // Campo de búsqueda de equipo
    private lateinit var btnSearch: Button       // Botón para buscar equipo
    private lateinit var progressBar: ProgressBar // Indicador de carga de datos
    private lateinit var layoutTeamInfo: LinearLayout  // Contenedor info del equipo encontrado
    private lateinit var tvTeamName: TextView    // Texto con nombre del equipo
    private lateinit var btnViewPlayers: Button  // Botón para ver plantilla de jugadores
    private lateinit var btnViewTable: Button    // Botón para ver tabla de posiciones
    private lateinit var recyclerView: RecyclerView // Lista para resultados en vista permanente

    // Estado: el equipo actualmente seleccionado en la interfaz
    private var currentTeam: Team? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Vincular componentes de la vista XML
        etSearchTeam = findViewById(R.id.etSearchTeam)
        btnSearch = findViewById(R.id.btnSearch)
        progressBar = findViewById(R.id.progressBar)
        layoutTeamInfo = findViewById(R.id.layoutTeamInfo)
        tvTeamName = findViewById(R.id.tvTeamName)
        btnViewPlayers = findViewById(R.id.btnViewPlayers)
        btnViewTable = findViewById(R.id.btnViewTable)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // 2. Acción del botón BUSCAR (API searchteams.php)
        btnSearch.setOnClickListener {
            val query = etSearchTeam.text.toString().trim()
            if (query.isNotEmpty()) {
                searchTeam(query)
            } else {
                Toast.makeText(this, "Ingresa un nombre de equipo", Toast.LENGTH_SHORT).show()
            }
        }

        // 3. Acción para ver la Plantilla de Jugadores (API lookup_all_players.php)
        btnViewPlayers.setOnClickListener {
            currentTeam?.idTeam?.let { teamId ->
                loadPlayers(teamId)
            }
        }

        // 4. Acción para ver la Tabla de Posiciones (API lookuptable.php)
        btnViewTable.setOnClickListener {
            currentTeam?.idLeague?.let { leagueId ->
                loadLeagueTable(leagueId)
            }
        }
    }

    private fun searchTeam(teamName: String) {
        progressBar.visibility = View.VISIBLE
        layoutTeamInfo.visibility = View.GONE
        recyclerView.adapter = null

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.searchTeams(teamName)
                progressBar.visibility = View.GONE

                val teamList = response.teams
                if (!teamList.isNullOrEmpty()) {
                    currentTeam = teamList[0]
                    tvTeamName.text = currentTeam?.strTeam
                    layoutTeamInfo.visibility = View.VISIBLE
                    Toast.makeText(this@MainActivity, "Equipo encontrado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "No se encontró el equipo", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadPlayers(teamId: String) {
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getPlayersByTeam(teamId)
                progressBar.visibility = View.GONE

                val players = response.player
                if (!players.isNullOrEmpty()) {
                    recyclerView.adapter = PlayerAdapter(players) { selectedPlayer ->
                        showPlayerDetailDialog(selectedPlayer)
                    }
                } else {
                    Toast.makeText(this@MainActivity, "No hay jugadores registrados", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@MainActivity, "Error al cargar jugadores", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadLeagueTable(leagueId: String) {
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getLeagueTable(leagueId)
                progressBar.visibility = View.GONE

                val table = response.table
                if (!table.isNullOrEmpty()) {
                    recyclerView.adapter = TableAdapter(table)
                } else {
                    Toast.makeText(this@MainActivity, "Tabla no disponible para esta liga", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@MainActivity, "Error al cargar la tabla", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 5. Diálogo con estilo retro para ver la foto en grande (API lookupplayer.php / Coil)
    private fun showPlayerDetailDialog(player: Player) {
        lifecycleScope.launch {
            var playerPhotoUrl = player.strCutout ?: player.strThumb

            // Consultamos detalle si falta información
            try {
                val response = RetrofitClient.apiService.getPlayerDetails(player.idPlayer)
                val detail = response.player?.firstOrNull()
                if (detail != null) {
                    playerPhotoUrl = detail.strCutout ?: detail.strThumb ?: playerPhotoUrl
                }
            } catch (_: Exception) { }

            val dialogView = layoutInflater.inflate(R.layout.item_player, null)
            val img = dialogView.findViewById<android.widget.ImageView>(R.id.imgPlayer)
            val tvName = dialogView.findViewById<TextView>(R.id.tvPlayerName)
            val tvPos = dialogView.findViewById<TextView>(R.id.tvPlayerPosition)
            val tvNat = dialogView.findViewById<TextView>(R.id.tvPlayerNationality)

            tvName.text = player.strPlayer
            tvPos.text = "Posición: ${player.strPosition ?: "N/A"}"
            tvNat.text = "Nacionalidad: ${player.strNationality ?: "N/A"}"

            if (!playerPhotoUrl.isNullOrEmpty()) {
                img.load(playerPhotoUrl)
            }

            AlertDialog.Builder(this@MainActivity)
                .setTitle("Ficha de Jugador")
                .setView(dialogView)
                .setPositiveButton("Cerrar", null)
                .show()
        }
    }
}