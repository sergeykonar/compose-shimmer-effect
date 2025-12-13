package konar.ui.compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import konar.ui.compose.data.Photo
import konar.ui.compose.repository.PhotosApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class MainViewModel(
    private val api: PhotosApi,
) : ViewModel() {
    companion object {
        private const val INIT_PAGE = 1
    }

    private val _currentPage = MutableStateFlow(INIT_PAGE)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()
    private val _photos = MutableStateFlow<List<Photo>>(emptyList())
    val photos: StateFlow<List<Photo>> = _photos

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchPhotosByPage(sol: Int) {
        _currentPage.value = sol
        viewModelScope.launch {
            try {
                val response = api.getImagesList(page = sol)
                _photos.value = response
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

    fun incrementPage() {
        fetchPhotosByPage(_currentPage.value + 1)
    }

    fun decrementPage() {
        fetchPhotosByPage((_currentPage.value - 1).coerceAtLeast(0))
    }
}

class MainViewModelFactory(
    private val api: PhotosApi,
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
