package com.sergeenko.alexey.noble


import com.sergeenko.alexey.noble.dataclasses.Client
import com.sergeenko.alexey.noble.dataclasses.Club
import com.sergeenko.alexey.noble.dataclasses.Language
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface NobleApi {

    @FormUrlEncoded
    @POST("query/lang.php")
    fun getLangList(
        @Field("api_key") apiKey: Int = Const.apiKey,
        @Field("api_event") apiEvent: String = Const.allLangApiEvent
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("query/lang.php")
    fun getLanguage(
            @Field("api_key") apiKey: Int = Const.apiKey,
            @Field("api_event") apiEvent: String = Const.langsGets,
            @Field("variable") variable: String
    ): Call<Language>

    @FormUrlEncoded
    @POST("query/clients.php")
    fun auth(
        @Field("api_key") apiKey: Int = Const.apiKey,
        @Field("api_event") apiEvent: String = Const.clubGet,
        @Field("user_email") email: String,
        @Field("user_pass") password: String
    ): Call<Club>

    @FormUrlEncoded
    @POST("query/clients.php")
    fun passwordRecovery(
            @Field("api_key") apiKey: Int = Const.apiKey,
            @Field("api_event") apiEvent: String = Const.clubPasswordRecovery,
            @Field("user_email") email: String,
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("query/clients.php")
    fun getClients(
            @FieldMap fields: Map<String, String>,
            @Field("api_key") apiKey: Int = Const.apiKey,
            @Field("api_event") apiEvent: String = Const.clientGet,
            @Field("api_filter_ids") apiFilterIds: Array<String> = arrayOf(),
            @Field("api_filter_names") apiFilterNames: Array<String> = arrayOf(),
            @Field("api_sort") apiSort: String = Const.sortBySurname,
    ): Call<List<Client>>

    @FormUrlEncoded
    @POST("query/clients.php")
    fun addClient(
        @FieldMap fields: Map<String, String>,
        @Field("api_key") apiKey: Int = Const.apiKey,
        @Field("api_event") apiEvent: String = Const.clientAdd,
        @Field("client_name") clientName: String,
        @Field("client_sirname") client_sirname: String,
        @Field("client_patronymic") client_patronymic: String? = null,
        @Field("client_height") client_height: Int? = null,
        @Field("client_weight") client_weight: Int? = null,
        @Field("client_sex") client_sex: String? = null,
        @Field("client_age") client_age: Long?,
        @Field("client_visit") client_visit: String? = null,
        @Field("client_phone") client_phone: String? = null,
        @Field("client_measurements") client_measurements: String? = null,
        @Field("client_foto") client_foto: String? = null,
    ): Call<Int>


}

class Const{
    companion object {
        const val apiKey = 1111
        const val allLangApiEvent = "all_langs"
        const val langsGets = "langs_gets"
        const val clubGet = "club_get"
        const val clientGet = "client_get"
        const val clubPasswordRecovery = "club_password_recovery"
        const val clientAdd = "client_add"


        const val sortBySurname = "sirname"
        const val sortByDate = "dateS"
    }
}
