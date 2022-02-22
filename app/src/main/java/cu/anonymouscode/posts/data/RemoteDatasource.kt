package cu.anonymouscode.posts.data

import cu.anonymouscode.posts.data.network.PostApi
import cu.anonymouscode.posts.models.Comment
import cu.anonymouscode.posts.models.Post
import retrofit2.Response
import javax.inject.Inject

class RemoteDatasource @Inject constructor(
        private val postApi: PostApi
){

    suspend fun getPosts(): Response<List<Post>>{
        return postApi.getPosts()
    }

    suspend fun searchComments(postId: Int): Response<List<Comment>>{
        return postApi.searchComments(postId)
    }

}