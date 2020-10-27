package com.sergeenko.alexey.noble

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sergeenko.alexey.noble.dataclasses.Client
import kotlinx.android.synthetic.main.clients_edit_second_page_fragment.*

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { return inflater.inflate(R.layout.clients_edit_second_page_fragment, container, false) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ModelFactory(requireActivity().application, arguments?.getSerializable("client") as Client).create(ClientsEditSecondPageViewModel::class.java)
        observeOnView()
    }

    private fun observeOnView() {
        viewModel.apply {
            val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            language.observe(viewLifecycleOwner, { language ->
                val userAdapter = TrainingListAdapter(language)
                training_list.layoutManager = linearLayoutManager
                training_list.adapter = userAdapter
                userList!!.observe(viewLifecycleOwner, {
                    userAdapter.submitList(it)
                })
            })
        }
    }

}


