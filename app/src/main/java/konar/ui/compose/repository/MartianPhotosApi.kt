package konar.ui.compose.repository

import dev.skonar.BuildConfig
import konar.ui.compose.data.RoverPhotosResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MartianPhotosApi {
    @GET("rovers/{rover}/photos")
    suspend fun getPhotosBySol(
        @Path("rover") roverName: String,
        @Query("sol") sol: Int,
        @Query("camera") camera: String? = null,
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY,
    ): RoverPhotosResponse

    @GET("rovers/{rover}/photos")
    suspend fun getPhotosByEarthDate(
        @Path("rover") roverName: String,
        @Query("earth_date") earthDate: String,
        @Query("camera") camera: String? = null,
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY,
    ): RoverPhotosResponse
}
