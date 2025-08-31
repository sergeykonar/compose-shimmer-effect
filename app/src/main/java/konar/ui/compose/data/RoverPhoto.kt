package konar.ui.compose.data

import com.google.gson.annotations.SerializedName

// Data classes for the full NASA response
data class RoverPhoto(
    val id: Long,
    val sol: Int,
    val camera: Camera,
    @SerializedName("img_src")
    val imgSrc: String,
    @SerializedName("earth_date")
    val earthDate: String,
    val rover: Rover,
)

data class Camera(
    val id: Int,
    val name: String,
    @SerializedName("rover_id")
    val roverId: Int,
    @SerializedName("full_name")
    val fullName: String,
)

data class Rover(
    val id: Int,
    val name: String,
    @SerializedName("landing_date")
    val landingDate: String,
    @SerializedName("launch_date")
    val launchDate: String,
    val status: String,
)

data class RoverPhotosResponse(
    val photos: List<RoverPhoto>,
)
