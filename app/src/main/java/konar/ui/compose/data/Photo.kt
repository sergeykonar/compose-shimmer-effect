package konar.ui.compose.data

import com.google.gson.annotations.SerializedName

// Data classes for the full NASA response
data class Photo(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    @SerializedName("download_url")
    val downloadUrl: String,
)
