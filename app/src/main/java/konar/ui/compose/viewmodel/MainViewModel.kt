package konar.ui.compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import konar.ui.compose.data.RoverPhoto
import konar.ui.compose.repository.MartianPhotosApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class MainViewModel(
    private val api: MartianPhotosApi,
) : ViewModel() {
    companion object {
        private const val INIT_SOL = 1000
    }

    private val _currentSol = MutableStateFlow(INIT_SOL)
    val currentSol: StateFlow<Int> = _currentSol.asStateFlow()
    private val _photos = MutableStateFlow<List<RoverPhoto>>(emptyList())
    val photos: StateFlow<List<RoverPhoto>> = _photos

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchPhotosBySol(sol: Int) {
        _currentSol.value = sol
        viewModelScope.launch {
            try {
                val response = api.getPhotosBySol("curiosity", sol)
                _photos.value = response.photos
                _error.value = null
            } catch (e: IOException) {
                // Network error
                _error.value = "Network error: ${e.message}"
            } catch (e: HttpException) {
                // Non-2xx HTTP response
                _error.value = "HTTP error ${e.code()}: ${e.message()}"
            }
        }
    }

    fun fetchPhotosByEarthDate(
        roverName: String = "curiosity",
        earthDate: String,
        camera: String? = null,
        page: Int = 1,
    ) {
        viewModelScope.launch {
            try {
                val response = api.getPhotosByEarthDate(roverName, earthDate, camera, page)
                _photos.value = response.photos
                _error.value = null
            } catch (e: IOException) {
                _error.value = "Network error: ${e.message}"
            } catch (e: HttpException) {
                _error.value = "HTTP error ${e.code()}: ${e.message()}"
            }
        }
    }

    fun incrementSol() {
        fetchPhotosBySol(_currentSol.value + 1)
    }

    fun decrementSol() {
        fetchPhotosBySol((_currentSol.value - 1).coerceAtLeast(0))
    }
}

class MainViewModelFactory(
    private val api: MartianPhotosApi,
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
