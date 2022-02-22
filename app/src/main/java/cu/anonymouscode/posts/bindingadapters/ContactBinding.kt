package cu.anonymouscode.posts.bindingadapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModelProvider
import cu.anonymouscode.posts.models.Contact
import cu.anonymouscode.posts.viewmodels.ContactViewModel
import cu.anonymouscode.posts.viewmodels.DefaultViewModel
import dagger.hilt.android.AndroidEntryPoint

class ContactBinding {

    companion object {
        @BindingAdapter( "uriDelete")
        @JvmStatic
        fun deleteImageView(
                imageView: ImageView,
                uri: Uri
        ) {
            imageView.setOnClickListener { currentContext ->
                showDialogDelete(currentContext.context, uri)
            }
        }

        @BindingAdapter("uriEdit")
        @JvmStatic
        fun editImageView(
                imageView: ImageView,
                uri: Uri
        ) {
            imageView.setOnClickListener { currentContext ->
                showDialogEdit(currentContext.context, uri)
            }
        }

        fun showDialogEdit(context: Context, uri: Uri){
            val addContactDialog = AlertDialog.Builder(context)
                .setTitle("Editar Contacto")
                .setMessage("Está seguro que desea editar el contacto?")
                .setPositiveButton("Yes"){ _, _ ->
                    val editIntent = Intent(Intent.ACTION_EDIT).apply {
                        setDataAndType(uri, ContactsContract.Contacts.CONTENT_ITEM_TYPE)
                    }

                    context.startActivity(editIntent)
                }
                .setNegativeButton("No"){ _, _ ->

                }
                .create()
                .show()

        }

        fun showDialogDelete(context: Context, uri: Uri){
            val deleteContactDialog = AlertDialog.Builder(context)
                .setTitle("Eliminar contacto")
                .setMessage("Esta seguro que desea eliminar el contacto")
                .setPositiveButton("Yes"){ _, _ ->
                    context.contentResolver.delete(uri, null, null)
                }
                .setNegativeButton("No"){ _, _ ->

                }
                .create()
                .show()
        }
    }
}