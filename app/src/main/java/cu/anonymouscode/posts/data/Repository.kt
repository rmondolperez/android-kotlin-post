package cu.anonymouscode.posts.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
        remoteDatasource: RemoteDatasource
){
    val remote = remoteDatasource
}