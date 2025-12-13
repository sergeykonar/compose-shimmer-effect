package konar.ui.compose.repository

import konar.ui.compose.data.Photo
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotosApi {
    @GET("v2/list")
    suspend fun getImagesList(
        @Query("page") page: Int = 1,
    ): List<Photo>
}
