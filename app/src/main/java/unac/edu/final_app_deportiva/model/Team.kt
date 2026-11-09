package unac.edu.final_app_deportiva.model

/**
 * Respuesta de la API al buscar un equipo por nombre.
 * Contiene una lista de objetos Team que coinciden con el término de búsqueda.
 */
data class TeamResponse(
    val teams: List<Team>?
)

/**
 * Representa un equipo de fútbol obtenido de TheSportsDB API.
 * Contiene información básica como nombre, liga, identificadores y el escudo del equipo.
 */
data class Team(
    val idTeam: String,
    val strTeam: String,
    val strLeague: String,
    val idLeague: String,
    val strBadge: String?
)