package com.sergeenko.alexey.noble.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sergeenko.alexey.noble.view_models.ClientEditThirdViewModel
import com.sergeenko.alexey.noble.R
import com.sergeenko.alexey.noble.dataclasses.Client

class ClientEditThirdFragment : Fragment() {

    companion object {
        fun newInstance(client: Client): ClientEditThirdFragment {
            val args = Bundle()
            args.putSerializable("client", client)
            val fragment = ClientEditThirdFragment()
            fragment.arguments = args
            return fragment
        }
    }


    private lateinit var viewModel: ClientEditThirdViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.client_edit_third_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ClientEditThirdViewModel::class.java)
        // TODO: Use the ViewModel
    }

}