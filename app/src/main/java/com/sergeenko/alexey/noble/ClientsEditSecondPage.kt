package com.sergeenko.alexey.noble

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sergeenko.alexey.noble.dataclasses.Client

class ClientsEditSecondPage : Fragment() {

    companion object {
        fun newInstance(client: Client): ClientsEditSecondPage{
            val args = Bundle()
            args.putSerializable("client", client)
            val fragment = ClientsEditSecondPage()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var viewModel: ClientsEditSecondPageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.clients_edit_second_page_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ClientsEditSecondPageViewModel::class.java)
        // TODO: Use the ViewModel
    }

}