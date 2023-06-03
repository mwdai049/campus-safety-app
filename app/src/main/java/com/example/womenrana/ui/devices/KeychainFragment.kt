package com.example.womenrana.ui.devices

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.womenrana.R

class KeychainFragment : Fragment() {

    companion object {
        fun newInstance() = KeychainFragment()
    }

    private lateinit var viewModel: KeychainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_keychain, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(KeychainViewModel::class.java)
        // TODO: Use the ViewModel
    }

}