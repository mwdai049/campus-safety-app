package com.example.womenrana.ui.campus

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.womenrana.R
import android.content.Intent
import android.net.Uri
import android.widget.ImageButton

class CampusContactsFragment : Fragment() {
    //TESTTING ABOVE
    companion object {
        fun newInstance() = CampusContactsFragment()
    }

    private lateinit var viewModel: CampusContactsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_campus_contacts, container, false)

        // Find the button and set the click listener
        val callButton: ImageButton = view.findViewById(R.id.callbtn)
        callButton.setOnClickListener {
            val phoneNumber = "8585344357" // Replace with the desired phone number
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
            startActivity(intent)
        }

        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CampusContactsViewModel::class.java)
        // TODO: Use the ViewModel
    }


}