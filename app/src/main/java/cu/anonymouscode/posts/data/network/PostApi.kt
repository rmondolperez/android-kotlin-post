package cu.anonymouscode.posts.data.network

import cu.anonymouscode.posts.models.Comment
import cu.anonymouscode.posts.models.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PostApi {
    @GET("/posts")
    suspend fun getPosts(): Response<List<Post>>

    @GET("/comments")
    suspend fun searchComments(
            @Query("postId") postId: Int
    ): Response<List<Comment>>
}