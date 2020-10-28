package com.sergeenko.alexey.noble

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sergeenko.alexey.noble.dataclasses.LangList
import com.sergeenko.alexey.noble.dataclasses.Language
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.lang_text_view.view.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class AuthActivity : BaseActivity() {

    private lateinit var userViewModel: AuthActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        userViewModel = ViewModelProvider(this).get(AuthActivityViewModel::class.java)
        forgot_password_text.underline()
        nameInputListener()
        observeOnViewModel()
    }

    private fun observeOnViewModel() {
        userViewModel.apply {
            language.observe(this@AuthActivity, {
                setLanguage(it)
            })
            langList.observe(this@AuthActivity, {
                it?.let {
                    lang_list_recycler_view.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this@AuthActivity)
                        adapter = MyAdapter(userViewModel, it)
                    }
                }
            })
            isListOpen.observe(this@AuthActivity, {
                lang_list_recycler_view.visibility = if(it) View.VISIBLE else View.GONE
                chosen_measure_icon.isActivated = it
            })
            nameErrorLiveData.observe(this@AuthActivity, {
                name_input.error = it
            })
            passwordErrorLiveData.observe(this@AuthActivity, {
                password_input.error = it
            })
            isAuthSuccessful.observe(this@AuthActivity, {
                if(it){
                    startActivity(Intent(this@AuthActivity, StartActivity::class.java))
                }else {
                    Toast.makeText(this@AuthActivity, "not cool", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    fun agreeToAgreements(view: View){
        userViewModel.agree(agreement_switch.isChecked)
    }

    private fun setLanguage(language: Language) {
        this.language.text = userViewModel.getConfigLanguage()
        language.apply {
            textView3.text = auth
            agr_text_1.text = i_read_and_agry_with
            agr_text_2.text = conditions_of_user_agreement
            agr_text_3.text = and
            agr_text_4.text = agreement_on_data_processing
            password_edit.hint = password
            name_edit.hint = email
            sign_in_button.text = come_in
            forgot_password_text.text = forgot_password
        }
    }

    private fun nameInputListener() {
        name_edit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                /*if(!isValidEmailAddress(s.toString())) {
                    name_input.isErrorEnabled = true
                    name_input.error = "Неверный email"
                }else {
                    name_input.isErrorEnabled = false
                    name_input.error = ""
                }*/
            }
        })
    }

    fun signIn(view: View){
        userViewModel.signIn(name_edit.text.toString(), password_edit.text.toString())
    }

    fun showLangList(view: View){
        userViewModel.changeListVisibility()
    }

    fun showUserAgreement(view: View){
        startActivity(
            Intent(this, AgreementActivity::class.java)
                .putExtra("text", userViewModel.getLanguage()?.user_agreement_2)
        )
    }

    fun showUserExperience(view: View){
        startActivity(
            Intent(this, AgreementActivity::class.java)
                .putExtra("text", userViewModel.getLanguage()?.user_agreement_3)
        )
    }

    fun goToPasswordRecovery(view: View){
        startActivity(Intent(this, PasswordRecoveryActivity::class.java))
    }
}

fun TextView.underline() {
    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

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
            viewModelScope.launch(IO) {
                language.postValue(langList.body)
                config.selectedLanguageCode = langList.name
                configDao?.insertConfig(config)
            }
        }
    }

    override fun getItemCount() = langList.size
}