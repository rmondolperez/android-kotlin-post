package cu.anonymouscode.posts.adapters

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cu.anonymouscode.posts.databinding.ContactRowLayoutBinding
import cu.anonymouscode.posts.models.Contact
import cu.anonymouscode.posts.util.DefaultDiffUtil
import cu.anonymouscode.posts.viewmodels.ContactViewModel
import kotlinx.android.synthetic.main.contact_row_layout.view.*

class ContactAdapter(private val onEditContactClickListener: OnEditContactClickListener) : RecyclerView.Adapter<ContactAdapter.MyViewHolder>() {

    private var contacts = arrayListOf<Contact>()

    class MyViewHolder(private val binding: ContactRowLayoutBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            binding.contact = contact
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ContactRowLayoutBinding.inflate(layoutInflater, parent, false)

                return MyViewHolder(binding)
            }
        }
    }

    interface OnEditContactClickListener {
        fun editClick(contact: Contact)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentContact = contacts.get(position)
		holder.bind(currentContact)

        holder.itemView.deleteImageView.setOnClickListener {
            showDialogDelete(it.context, currentContact)
        }

        holder.itemView.editImageView.setOnClickListener {
            onEditContactClickListener.editClick(currentContact)
        }
    }

    fun deleteContact(currentContact: Contact){
        contacts.remove(currentContact)
        notifyDataSetChanged()
    }

    fun editContact(newData: List<Contact>){
        contacts.clear()
        contacts.addAll(newData)
        notifyDataSetChanged()
    }

    fun setData(newData: List<Contact>){
        contacts.addAll(newData)
        notifyDataSetChanged()
    }

    fun showDialogDelete(context: Context, contact: Contact){
        val deleteContactDialog = AlertDialog.Builder(context)
            .setTitle("Eliminar contacto")
            .setMessage("Esta seguro que desea eliminar el contacto")
            .setPositiveButton("Yes"){ _, _ ->
                context.contentResolver.delete(contact.uri, null, null)
                deleteContact(contact)
            }
            .setNegativeButton("No"){ _, _ ->

            }
            .create()
            .show()
    }
}