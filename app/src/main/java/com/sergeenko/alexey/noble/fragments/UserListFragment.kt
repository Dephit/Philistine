package com.sergeenko.alexey.noble.fragments

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.sergeenko.alexey.noble.*
import com.sergeenko.alexey.noble.adapter.ClientListAdapter
import com.sergeenko.alexey.noble.utills.SpacesItemDecoration
import com.sergeenko.alexey.noble.utills.hideKeyboard
import com.sergeenko.alexey.noble.utills.makeAndShowToast
import com.sergeenko.alexey.noble.view_models.SortType
import com.sergeenko.alexey.noble.view_models.UserListViewModel
import kotlinx.android.synthetic.main.user_list_fragment.*
import java.lang.ref.WeakReference

class UserListFragment : Fragment() {

    companion object {
        fun newInstance() = UserListFragment()
    }

    private lateinit var viewModel: UserListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.user_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserListViewModel::class.java)
        setListeners()
        bringAlphaToFront()
        observeOnViewModel()
    }

    private fun setListeners() {
        search_edit.setOnFocusChangeListener { v, hasFocus ->
            search_input.setStartIconDrawable(if (hasFocus) R.drawable.ic_close else R.drawable.ic_search)
        }
        search_input.setStartIconOnClickListener {
            hideKeyboard(WeakReference(activity))
            search_edit.apply {
                if(text.toString().trim().isNotEmpty())
                    viewModel.searchClients("")
                setText("")
                clearFocus()
            }
        }
        search_edit.setOnKeyListener { _, _, event ->
            if((event.action == KeyEvent.ACTION_DOWN) && (event.keyCode == KeyEvent.KEYCODE_ENTER)){
                hideKeyboard(WeakReference(activity))
                viewModel.searchClients(search_edit.text.toString())
                return@setOnKeyListener true
            }
            false
        }
        by_alphabet_button.setOnClickListener { sortByAlpha() }
        by_date_button.setOnClickListener { sortByDate() }
    }

    private fun sortByAlpha(){
        bringAlphaToFront()
        viewModel.sortBy(search_edit.text.toString(), SortType.Alpha)
        showSortToast()
    }

    private fun bringAlphaToFront() {
        by_alphabet_button.apply {
            bringToFront()
            isActivated = true
        }
        by_date_button.apply {
            isActivated = false
        }
    }

    private fun sortByDate(){
        bringDateToFront()
        viewModel.sortBy(search_edit.text.toString(), SortType.Date)
        showSortToast()
    }

    private fun showSortToast() {
        viewModel.getLanguage()?.apply {
            context?.let {
                makeAndShowToast(context = WeakReference(context), message = when (viewModel.sortBy) {
                    SortType.SirnameDesc -> sorting_by_alphabet_desc
                    SortType.SirnameAsc -> sorting_by_alphabet_asc
                    SortType.DateDesc -> sorting_by_date_desc
                    SortType.DateAsc -> sorting_by_date_asc
                    else -> sorting_by_alphabet_asc
                })
            }
        }
    }

    private fun bringDateToFront() {
        by_date_button.apply {
            bringToFront()
            isActivated = true
        }
        by_alphabet_button.apply {
            isActivated = false
        }
    }

    private fun observeOnViewModel() {
        viewModel.apply {
            language.observe(viewLifecycleOwner, {
                it.apply {
                    by_alphabet_button.text = by_alphabet
                    by_date_button.text = by_date
                    search_edit.hint = find_client
                }
            })
            val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
            users_list.addItemDecoration(SpacesItemDecoration(3, spacingInPixels))
            clientList.observe(viewLifecycleOwner, {clientList->
                val userAdapter = ClientListAdapter()
                users_list.apply {
                    layoutManager = GridLayoutManager(requireContext(), 3)
                    adapter = userAdapter
                }
                getLanguage()?.let { lang ->
                    userAdapter.initAdapter(clientList, lang)
                }
            })
            onDataChanged.observe(viewLifecycleOwner, {
                viewModel.searchClients(search_edit.text.toString())
            })
        }
    }
}

