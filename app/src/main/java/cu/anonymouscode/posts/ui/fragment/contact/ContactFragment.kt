package cu.anonymouscode.posts.ui.fragment.contact

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cu.anonymouscode.posts.adapters.ContactAdapter
import cu.anonymouscode.posts.databinding.FragmentContactBinding
import cu.anonymouscode.posts.models.Contact
import cu.anonymouscode.posts.util.Constants
import cu.anonymouscode.posts.viewmodels.ContactViewModel
import kotlinx.coroutines.launch

class ContactFragment : Fragment(), ContactAdapter.OnEditContactClickListener {

    private var _binding: FragmentContactBinding? = null
    private val binding get() = _binding!!

    private lateinit var contactViewModel: ContactViewModel

    private val contactAdapter by lazy { ContactAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactViewModel = ViewModelProvider(requireActivity()).get(ContactViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentContactBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        setupPermissions()

    }


    private fun setupRecyclerView() {
        binding.contactRecyclerView.adapter = contactAdapter
        binding.contactRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun editClick(currentContact: Contact) {
        showDialogEdit(requireContext(), currentContact.uri)
    }

    fun showDialogEdit(context: Context, uri: Uri) {
        val addContactDialog = AlertDialog.Builder(context)
            .setTitle("Editar Contacto")
            .setMessage("Está seguro que desea editar el contacto?")
            .setPositiveButton("Yes") { _, _ ->
                val editIntent = Intent(Intent.ACTION_EDIT).apply {
                    setDataAndType(uri, ContactsContract.Contacts.CONTENT_ITEM_TYPE)
                }

                startActivityForResult(editIntent, 8610)
            }
            .setNegativeButton("No") { _, _ ->

            }
            .create()
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 8610) {
            refreshContact()
        }
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_CONTACTS
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        } else {
            readContact()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS),
            Constants.CONTACT_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            Constants.CONTACT_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    readContact()
                }
            }
        }
    }

    private fun refreshContact(){
        lifecycleScope.launch {
            contactViewModel.readContact().observe(viewLifecycleOwner, Observer { contacts ->
                contactAdapter.editContact(contacts)
            })
        }
    }

    private fun readContact(){
        lifecycleScope.launch {
            contactViewModel.readContact().observe(viewLifecycleOwner, Observer { contacts ->
                contactAdapter.setData(contacts)
            })
        }
    }
}