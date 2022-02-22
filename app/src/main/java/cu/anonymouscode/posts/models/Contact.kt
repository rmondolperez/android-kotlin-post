package cu.anonymouscode.posts.models

import android.net.Uri

data class Contact(
    val name: String,
    val phoneNumber: String,
    val uri: Uri
)