package unac.edu.final_app_deportiva.model

/**
 * Respuesta de la API para la tabla de posiciones de una liga.
 * Contiene una lista de objetos TableEntry que representan la clasificación de equipos.
 */
data class TableResponse(
    val table: List<TableEntry>?
)

/**
 * Representa una fila en la tabla de posiciones de la liga.
 * Contiene información de clasificación, nombre de equipo, juegos jugados, goles,
 * y puntos acumulados. Algunos campos pueden ser nulos dependiendo de los datos
 * disponibles en la respuesta de la API.
 */
data class TableEntry(
    val intRank: String?,
    val strTeam: String?,
    val intPlayed: String?,
    val intGoalsFor: String?,      // ⚠️ No utilizado actualmente en la aplicación
    val intGoalsAgainst: String?,  // ⚠️ No utilizado actualmente en la aplicación
    val intPoints: String?
)