package com.sergeenko.alexey.noble.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.sergeenko.alexey.noble.R
import com.sergeenko.alexey.noble.activities.ClientEditActivity
import com.sergeenko.alexey.noble.dataclasses.Client
import com.sergeenko.alexey.noble.dataclasses.Language
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.client_card.view.*

class ClientListAdapter() : RecyclerView.Adapter<ClientListAdapter.ClientHolder>()  {

    private var clients: List<Client> = listOf()
    var language: Language? = null

    fun initAdapter(clients: List<Client>, language: Language){
        this.language = language
        this.clients = clients
    }

    class ClientHolder(textView: ConstraintLayout) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientHolder {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.client_card, parent, false) as ConstraintLayout
        return ClientHolder(textView)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
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

                user_age.text = getClientAge(language!!)
                user_last_seen.text = getClientLastVisit(language!!)
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