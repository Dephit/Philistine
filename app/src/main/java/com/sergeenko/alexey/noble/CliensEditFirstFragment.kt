package com.sergeenko.alexey.noble

import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sergeenko.alexey.noble.dataclasses.Client
import kotlinx.android.synthetic.main.activity_client_edit.*
import kotlinx.android.synthetic.main.cliens_edit_first_fragment.*
import kotlinx.android.synthetic.main.personal_info_layout.*
import kotlin.system.measureTimeMillis

class CliensEditFirstFragment : BaseFragment() {

    companion object {
        fun newInstance(client: Client): CliensEditFirstFragment{
            val args = Bundle()
            args.putSerializable("client", client)
            val fragment = CliensEditFirstFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var viewModel: CliensEditFirstViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.cliens_edit_first_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ModelFactory(requireActivity().application, arguments?.getSerializable("client") as Client).create(CliensEditFirstViewModel::class.java)
        observeOnView()
    }



    private fun observeOnView() {
        viewModel.apply {
            language.observe(viewLifecycleOwner, {
                personal_info.setPersonalInfo(viewModel, it)
                personal_info.fillPersonalInfo(client)
                save_changes_button.text = it.save
            })
            isClientSuccessivelyAdded.observe(viewLifecycleOwner, {
                if(it)
                    activity?.onBackPressed()
                else
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            })
        }
    }

    fun saveClientChanges() {
        viewModel.saveClientChanges()
    }

    fun setImage(bitmap: Bitmap?) {
        viewModel.setImage(bitmap)
    }


}