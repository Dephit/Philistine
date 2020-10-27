package com.sergeenko.alexey.noble

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.sergeenko.alexey.noble.dataclasses.Client

import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sergeenko.alexey.noble.dataclasses.Language
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.client_card.view.*
import kotlinx.android.synthetic.main.clients_edit_second_page_fragment.*
import kotlinx.android.synthetic.main.training_card.view.*

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
        viewModel = ModelFactory(requireActivity().application, arguments?.getSerializable("client") as Client).create(ClientsEditSecondPageViewModel::class.java)
        observeOnView()
    }

    private fun observeOnView() {
        viewModel.apply {
            val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            language.observe(viewLifecycleOwner, { language ->
                val userAdapter = ProductAdapter()
                Toast.makeText(context, client.trainingList!!.size.toString(), Toast.LENGTH_LONG).show()
                training_list.layoutManager = linearLayoutManager
                training_list.adapter = userAdapter
                userList!!.observe(viewLifecycleOwner, {
                    userAdapter.submitList(it)
                })
            })
        }
    }

}


class ProductAdapter() : PagedListAdapter<TrainingItem, RecyclerView.ViewHolder>(UserDiffCallback) {

    class ClientHolder(private val textView: ConstraintLayout) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientHolder {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.training_card, parent, false) as ConstraintLayout
        return ClientHolder(textView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.apply {
            Log.i("ASDASDASDASD", name)
            holder.itemView.training_name_shown.text = name

            holder.itemView.training_dur_text2.text =  "${convertStringToDate(totalMinutes)}:${convertStringToDate(totalSeconds)}"

            holder.itemView.closed_image.setOnClickListener {
                holder.itemView.closed_image.isActivated = !holder.itemView.closed_image.isActivated
            }
        }
    }

    companion object {
        val UserDiffCallback = object : DiffUtil.ItemCallback<TrainingItem>() {
            override fun areItemsTheSame(oldItem: TrainingItem, newItem: TrainingItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TrainingItem, newItem: TrainingItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
