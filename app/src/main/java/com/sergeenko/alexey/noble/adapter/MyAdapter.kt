package com.sergeenko.alexey.noble.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.sergeenko.alexey.noble.R
import com.sergeenko.alexey.noble.dataclasses.LangList
import com.sergeenko.alexey.noble.view_models.BaseViewModel
import kotlinx.android.synthetic.main.activity_password_recovery.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyAdapter(var config: BaseViewModel, private val langList: List<LangList>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(private val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val textView = LayoutInflater.from(parent.context).inflate(R.layout.lang_text_view, parent, false) as TextView
            return MyViewHolder(textView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.textView.apply {
            text = langList[position].name
            setOnClickListener {
                changeLanguage(langList[position])
            }
        }
    }

    private fun changeLanguage(langList: LangList) {
        config.apply {
            viewModelScope.launch(Dispatchers.IO) {
                language.postValue(langList.body)
                config.selectedLanguageCode = langList.name
                configDao?.insertConfig(config)
            }
        }
    }

    override fun getItemCount() = langList.size
}