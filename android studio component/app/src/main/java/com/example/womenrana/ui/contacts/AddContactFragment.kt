package com.example.womenrana.ui.contacts

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import com.example.womenrana.R
import com.example.womenrana.databinding.FragmentAddContactBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AddContactFragment : Fragment() {


    private var _binding: FragmentAddContactBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = AddContactFragment()
    }

    private lateinit var viewModel: AddContactViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddContactBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var sharedPreferences = requireContext().getSharedPreferences("MyContacts", MODE_PRIVATE)

        val fragmentManager = parentFragmentManager


        fun addContact(view: View){
            var etFirstName: EditText = root.findViewById(R.id.etFirstName)
            var etLastName: EditText = root.findViewById(R.id.etLastName)
            var etContactNumber: EditText = root.findViewById(R.id.etPhoneNumber)
            var firstName: String = etFirstName.text.toString()
            var lastName: String = etLastName.text.toString()
            var phone: String = etContactNumber.text.toString()

            val retrievedPhoneNums = sharedPreferences.getStringSet("phone numbers", emptySet())?.toMutableSet()

            val gson = Gson()

            var json = sharedPreferences.getString("first names", null)
            var type = object : TypeToken<List<String>>() {}.type
            val retrievedFirstNames: List<String>? = gson.fromJson(json, type)

            json = sharedPreferences.getString("last names", null)
            type = object : TypeToken<List<String>>() {}.type
            val retrievedLastNames: List<String>? = gson.fromJson(json, type)


            if (retrievedFirstNames.isNullOrEmpty()) {
                val newFirstName = mutableSetOf<String>()
                newFirstName.add(firstName)

                // Store the new set in SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putStringSet("last names", newFirstName)
                editor.apply()
            } else {
                // If the "contacts" key is not empty, create a copy of the set
                val modifiedFirstName = retrievedFirstNames.toMutableSet()

                // Add new contact information
                modifiedFirstName.add(firstName)

                // Store the modified set back into SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putStringSet("first names", modifiedFirstName)
                editor.apply()
            }

            if (retrievedLastNames.isNullOrEmpty()) {
                // If the "contacts" key is empty, create a new key with a set of contacts
                val newLastName = mutableSetOf<String>()
                newLastName.add(lastName)

                // Store the new set in SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putStringSet("last names", newLastName)
                editor.apply()
            } else {
                // If the "contacts" key is not empty, create a copy of the set
                val modifiedLastName = retrievedLastNames.toMutableSet()

                // Add new contact information
                modifiedLastName.add(lastName)

                // Store the modified set back into SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putStringSet("last names", modifiedLastName)
                editor.apply()
            }

            if (retrievedPhoneNums.isNullOrEmpty()) {
                // If the "contacts" key is empty, create a new key with a set of contacts
                val newPhoneNums = mutableSetOf<String>()
                newPhoneNums.add(phone)

                // Store the new set in SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putStringSet("phone numbers", newPhoneNums)
                editor.apply()
            } else {
                // If the "contacts" key is not empty, create a copy of the set
                val modifiedPhoneNums = retrievedPhoneNums.toMutableSet()

                // Add new contact information
                modifiedPhoneNums.add(phone)

                // Store the modified set back into SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putStringSet("phone numbers", modifiedPhoneNums)
                editor.apply()
            }


            /*val editor = sharedPreferences.edit()
            val setString = mutableSetOf<String>()
            setString.add(firstName)
            setString.add(lastName)
            setString.add(phone)
            editor.putStringSet("contacts", setString)
            editor.apply() // Commit the changes*/
            Toast.makeText(requireContext(), "added", Toast.LENGTH_SHORT).show()
            fragmentManager.commit {
                replace<ContactsFragment>(R.id.container)
                setReorderingAllowed(true)
                fragmentManager.saveBackStack("add contacts")
            }
            /*
            var contactIntent: Intent = Intent(ContactsContract.Intents.Insert. ACTION )
            contactIntent.type = ContactsContract.RawContacts. CONTENT_TYPE
            contactIntent
                .putExtra(ContactsContract.Intents.Insert.NAME, name)
                .putExtra(ContactsContract.Intents.Insert.PHONE, phone)
            resultLauncher.launch(contactIntent)*/
        }

        val addButton: Button = root.findViewById(R.id.addButton) as Button
        val cancelBtn: Button = root.findViewById(R.id.cancelButton) as Button

        cancelBtn.setOnClickListener {
            fragmentManager.commit {
                replace<ContactsFragment>(R.id.container)
                setReorderingAllowed(true)
                fragmentManager.saveBackStack("add contacts")
            }
        }

        addButton.setOnClickListener{
            view?.let { it1 -> addContact(it1) }
        }



        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddContactViewModel::class.java)
        // TODO: Use the ViewModel
    }

}