package unac.edu.final_app_deportiva.network

import unac.edu.final_app_deportiva.model.PlayerResponse
import unac.edu.final_app_deportiva.model.TableResponse
import unac.edu.final_app_deportiva.model.TeamResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaz que define los endpoints de TheSportsDB API utilizados en la aplicación.
 * Todos los métodos son funciones suspend para uso con corrutinas de Kotlin.
 */
interface SportsApiService {

    // 1. Buscar equipo por su nombre (searchteams.php)
    // Parámetro "t": término de búsqueda del nombre del equipo
    @GET("searchteams.php")
    suspend fun searchTeams(@Query("t") teamName: String): TeamResponse

    // 2. Obtener lista de jugadores del equipo (lookup_all_players.php)
    // Parámetro "id": ID del equipo para filtrar jugadores
    @GET("lookup_all_players.php")
    suspend fun getPlayersByTeam(@Query("id") teamId: String): PlayerResponse

    // 3. Obtener tabla de posiciones por liga (lookuptable.php)
    // Parámetro "l": ID de la liga para obtener su tabla de clasificación
    @GET("lookuptable.php")
    suspend fun getLeagueTable(@Query("l") leagueId: String): TableResponse

    // 4. Obtener detalle/foto de un jugador específico (lookupplayer.php)
    // Parámetro "id": ID del jugador para obtener información detallada
    @GET("lookupplayer.php")
    suspend fun getPlayerDetails(@Query("id") playerId: String): PlayerResponse
}