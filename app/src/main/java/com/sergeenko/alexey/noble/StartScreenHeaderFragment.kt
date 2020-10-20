package com.sergeenko.alexey.noble

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.start_screen_header_fragment.*
import kotlinx.android.synthetic.main.user_list_fragment.*

class StartScreenHeaderFragment : BaseFragment() {

    companion object {
        fun newInstance() = StartScreenHeaderFragment()
    }

    private lateinit var viewModel: StartScreenHeaderViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.start_screen_header_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StartScreenHeaderViewModel::class.java)
        viewModel.apply {
            language.observe(viewLifecycleOwner, {
                it.apply {
                    add_client_button.text = add_new_client_button_text
                    connect_device.text = connect_new_device_button
                    statistic_button.text = statistic_text
                    demo_button.text = demo_text
                }
            })
        }
    }

}