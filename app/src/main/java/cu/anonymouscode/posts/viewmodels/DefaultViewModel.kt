
package cu.anonymouscode.posts.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cu.anonymouscode.posts.data.Repository
import cu.anonymouscode.posts.models.Comment
import cu.anonymouscode.posts.models.Post
import cu.anonymouscode.posts.util.NetworkResult
import kotlinx.coroutines.launch
import retrofit2.Response

@Suppress("DEPRECATION")
class DefaultViewModel @ViewModelInject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application) {

    var postsResponse: MutableLiveData<NetworkResult<List<Post>>> = MutableLiveData()
    var commentsResponse: MutableLiveData<NetworkResult<List<Comment>>> = MutableLiveData()

    fun getPosts() = viewModelScope.launch {
        getPostsSafeCall()
    }

    fun getComments(postId: Int) = viewModelScope.launch {
        getCommentsSafeCall(postId)
    }

    private suspend fun getPostsSafeCall() {
        postsResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()){
            try {
                val response = repository.remote.getPosts()
                postsResponse.value = handlePostsResponse(response)
            }catch (e: Exception){
                postsResponse.value = NetworkResult.Error("No se ha obtenido información...")
            }
        } else {
            postsResponse.value = NetworkResult.Error("No existe conexión a Internet.")
        }
    }

    private suspend fun getCommentsSafeCall(postId: Int) {
        commentsResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()){
            try {
                val response = repository.remote.searchComments(postId)
                commentsResponse.value = handleCommentResponse(response)
            }catch (e: Exception){
                commentsResponse.value = NetworkResult.Error("No se ha obtenido información...")
            }
        } else {
            commentsResponse.value = NetworkResult.Error("No existe conexión a Internet.")
        }
    }

    private fun handleCommentResponse(response: Response<List<Comment>>): NetworkResult<List<Comment>>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Tiempo agotado.")
            }
            response.body()!!.get(0).id.toString().isNullOrEmpty() -> {
                return NetworkResult.Error("No se ha encontrado ningún comentario.")
            }
            response.isSuccessful -> {
                val commentsList = response.body()
                return NetworkResult.Success(commentsList!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun handlePostsResponse(response: Response<List<Post>>): NetworkResult<List<Post>>{
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Tiempo agotado")
            }
            response.body()!!.get(0).id.toString().isNullOrEmpty() -> {
                return NetworkResult.Error("No se ha encontrado ningún post.")
            }
            response.isSuccessful -> {
                val postsList = response.body()
                return NetworkResult.Success(postsList!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            return connectivityManager.activeNetworkInfo?.isConnected?: false
        }

    }

}