package cu.anonymouscode.posts.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import cu.anonymouscode.posts.models.Contact
import java.lang.Exception

class ContactViewModel @ViewModelInject constructor(
        application: Application
) : AndroidViewModel(application) {
    lateinit var uriFinal: Uri

    private val cols = listOf<String>(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
    ).toTypedArray()

    private val contacts: MutableLiveData<ArrayList<Contact>> = MutableLiveData()
    private val contactList = arrayListOf<Contact>()

    fun editContact(contactList: ArrayList<Contact>){
        contacts.value = contactList
    }

    suspend fun readContact(): MutableLiveData<ArrayList<Contact>> {
        contactList.clear()

        var result: Cursor? = getApplication<Application>().applicationContext.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                cols,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        )

        while (result!!.moveToNext()) {
            var uri: Uri = getUriContact(result.getString(0), result.getString(1))
            contactList.add(Contact(
                    result.getString(0),
                    result.getString(1),
                    uri
            ))
        }

        contacts.value = contactList

        return contacts

    }

    private fun getUriContact(name: String, phoneNumber: String): Uri{
        val contactUri: Uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber)
        )

        val cursor: Cursor? = getApplication<Application>().applicationContext.contentResolver
                .query(
                        contactUri,
                        null, null, null, null)

        while (cursor!!.moveToNext()){
            if (cursor.getString(cursor
                            .getColumnIndex(
                                    ContactsContract.PhoneLookup.DISPLAY_NAME))
                    == name
            ) {
                val lookupKey: String = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))
                uriFinal = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey)
            }
        }

        return uriFinal
    }

}

