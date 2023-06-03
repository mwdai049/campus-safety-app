package com.example.womenrana.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import com.example.womenrana.R
import com.example.womenrana.databinding.FragmentHomeBinding
import com.example.womenrana.ui.campus.CampusContactsFragment
import com.example.womenrana.ui.contacts.ContactsFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mapViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val campusContactBtn: Button = root.findViewById(R.id.CampusContactBtn) as Button
        val yourContactBtn: Button = root.findViewById(R.id.YourContactBtn) as Button

        val fragmentManager = parentFragmentManager
        campusContactBtn.setOnClickListener {
            fragmentManager.commit {
                replace<CampusContactsFragment>(R.id.container)
                setReorderingAllowed(true)
                fragmentManager.saveBackStack("dashboard")
            }
        }
        yourContactBtn.setOnClickListener {
            fragmentManager.commit {
                replace<ContactsFragment>(R.id.container)
                setReorderingAllowed(true)
                fragmentManager.saveBackStack("dashboard")
            }
        }
        /*val textView: TextView = binding.textHome
        mapViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
