package com.example.womenrana.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import com.example.womenrana.R
import com.example.womenrana.databinding.FragmentDashboardBinding
import com.example.womenrana.ui.contacts.ContactsFragment
import com.example.womenrana.ui.contacts.DevicesFragment


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val TAG = "dashBoard"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Log.d(TAG, "onCreate: Starting")

        val contactButton: Button = root.findViewById(R.id.contact_button) as Button
        val devicesButton: Button = root.findViewById(R.id.DevicesBtn) as Button

        val fragmentManager = parentFragmentManager
        contactButton.setOnClickListener {
            fragmentManager.commit {
                replace<ContactsFragment>(R.id.container)
                setReorderingAllowed(true)
                fragmentManager.saveBackStack("dashboard")
            }
        }
        devicesButton.setOnClickListener {
            fragmentManager.commit {
                replace<DevicesFragment>(R.id.container)
                setReorderingAllowed(true)
                fragmentManager.saveBackStack("dashboard")
            }
        }
        return root
    }

    /*val textView: TextView = binding.textDashboard
    dashboardViewModel.text.observe(viewLifecycleOwner) {
        textView.text = it
    }
    return root
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
