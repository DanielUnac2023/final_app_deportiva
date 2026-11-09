package unac.edu.final_app_deportiva.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente singleton de Retrofit para acceder a TheSportsDB API.
 * Proporciona una instancia lazy del servicio API con la URL base configurada
 * y Gson como convertidor para serialización/deserialización JSON.
 */
object RetrofitClient {
    private const val BASE_URL = "https://www.thesportsdb.com/api/v1/json/123/"

    val apiService: SportsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SportsApiService::class.java)
    }
}