package com.sergeenko.alexey.noble

import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.sergeenko.alexey.noble.dataclasses.Client
import com.sergeenko.alexey.noble.dataclasses.Language
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.client_card.view.*
import kotlinx.android.synthetic.main.user_list_fragment.*

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
        search_edit.setOnFocusChangeListener { v, hasFocus ->
            search_input.setStartIconDrawable(if (hasFocus) R.drawable.ic_close else R.drawable.ic_search)
        }

        search_input.setStartIconOnClickListener {
            hideKeyboard(activity)
            search_edit.apply {
                if(text.toString().trim().isNotEmpty())
                    viewModel.searchClients("")
                setText("")
                clearFocus()
            }
        }
        search_edit.setOnKeyListener { _, _, event ->
            if((event.action == KeyEvent.ACTION_DOWN) && (event.keyCode == KeyEvent.KEYCODE_ENTER)){
                hideKeyboard(activity)
                viewModel.searchClients(search_edit.text.toString())
                return@setOnKeyListener true
            }
            false
        }
        bringAlphaToFront()
        by_alphabet_button.setOnClickListener { sortByAlpha() }
        by_date_button.setOnClickListener { sortByDate() }
        observeOnViewModel()
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
            Toast.makeText(context,
                    when (viewModel.sortBy) {
                        SortType.SirnameDesc -> sorting_by_alphabet_desc
                        SortType.SirnameAsc -> sorting_by_alphabet_asc
                        SortType.DateDesc -> sorting_by_date_desc
                        SortType.DateAsc -> sorting_by_date_asc
                        else -> sorting_by_alphabet_asc
                    }, Toast.LENGTH_SHORT
            ).show()
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
            clientList.observe(viewLifecycleOwner, {
                val userAdapter = getLanguage()?.let { it1 ->
                    ClientListAdapter(it1, it)
                }
                users_list.apply {
                    layoutManager = GridLayoutManager(requireContext(), 3)
                    adapter = userAdapter
                }
            })
            onDataChanged.observe(viewLifecycleOwner, {
                viewModel.searchClients(search_edit.text.toString())
            })
        }
    }
}

class ClientListAdapter(val language: Language, private val clients: List<Client>) : RecyclerView.Adapter<ClientListAdapter.ClientHolder>()  {

    class ClientHolder(private val textView: ConstraintLayout) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientHolder {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.client_card, parent, false) as ConstraintLayout
        return ClientHolder(textView)
    }

    override fun onBindViewHolder(holder: ClientHolder, position: Int) {
        holder.itemView.apply {
            clients[position].apply {
                Picasso.with(context)
                        .load("http://noble.gensol.ru/files/${foto}")
                        .fit()
                        .placeholder(R.drawable.client_image_place_holder)
                        .error(bitmap?.let {
                            BitmapDrawable(resources, bitmap)
                        } ?: context.getDrawable(R.drawable.client_image_place_holder))
                        .into(user_picture)

                user_picture.clipToOutline = true

                user_age.text = getClientAge(language)
                user_last_seen.text = getClientLastVisit(language)
                user_name.text = getName()
                setOnClickListener {
                    context.startActivity(
                            Intent(context, ClientEditActivity::class.java)
                                    .putExtra("client", this)
                    )
                }
            }
        }
    }


    override fun getItemCount() = clients.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

}

class SpacesItemDecoration(private val spanCount: Int, private val spacing: Int) : ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column: Int = position % spanCount // item column

        outRect.left = column * spacing / spanCount
        outRect.right = spacing - (column + 1) * spacing / spanCount
    }
}