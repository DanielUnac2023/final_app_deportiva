package unac.edu.final_app_deportiva.model

/**
 * Respuesta de la API al obtener la lista de jugadores de un equipo.
 * Contiene una lista de objetos Player que pertenecen al equipo consultado.
 */
data class PlayerResponse(
    val player: List<Player>?
)

/**
 * Representa a un jugador de fútbol obtenido de TheSportsDB API.
 * Contiene información del jugador como nombre, posición, nacionalidad y URLs
 * de imagen (strCutout para foto de alta resolución y strThumb como alternativa).
 */
data class Player(
    val idPlayer: String,
    val strPlayer: String,
    val strPosition: String?,
    val strCutout: String?,
    val strThumb: String?,
    val strNationality: String?
)