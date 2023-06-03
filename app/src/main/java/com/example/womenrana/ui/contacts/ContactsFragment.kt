package com.example.womenrana.ui.contacts

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.womenrana.R
import com.example.womenrana.databinding.FragmentContactsBinding

class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null

    private lateinit var textView: TextView


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ContactsFragment()
    }

    private lateinit var viewModel: ContactsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contactsViewModel =
            ViewModelProvider(this)[ContactsViewModel::class.java]

        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val addBtn: ImageButton = root.findViewById(R.id.addBtn) as ImageButton

        val fragmentManager = parentFragmentManager
        addBtn.setOnClickListener {
            fragmentManager.commit {
                replace<AddContactFragment>(R.id.container)
                setReorderingAllowed(true)
                fragmentManager.saveBackStack("contacts")
            }
        }

        var sharedPreferences = requireContext().getSharedPreferences("MyContacts",
            Context.MODE_PRIVATE
        )
        val firstNames = sharedPreferences.getStringSet("first names", emptySet())?.toMutableSet()
        val lastNames = sharedPreferences.getStringSet("last names", emptySet())?.toMutableSet()
        val phoneNums = sharedPreferences.getStringSet("phone numbers", emptySet())?.toMutableSet()


        textView = root.findViewById(R.id.oneContact)

        val firstNamesList = firstNames?.toList()
        val lastNamesList = lastNames?.toList()
        val phoneNumsList = phoneNums?.toList()

        if (firstNamesList!=null && lastNamesList!=null && phoneNumsList!=null){
            var contactString = ""
            for (i in firstNamesList.indices){
                contactString += firstNamesList[i] + " "
                contactString += lastNamesList[i] + " "
                contactString += phoneNumsList[i] + "\n\n"
            }
            textView.text = contactString
        }




        /*textView = TextView(requireContext())
        textView.text = "Hello, World!"
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val containerLayout = view?.findViewById<LinearLayout>(R.id.contactsLayout)
        containerLayout?.addView(textView, layoutParams)
        return view*/




        /*val textView: TextView = binding.textContacts
        contactsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root }

        /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ContactsViewModel::class.java)
        // TODO: Use the ViewModel
    }*/

}