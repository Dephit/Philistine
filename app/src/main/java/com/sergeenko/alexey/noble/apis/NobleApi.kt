package com.sergeenko.alexey.noble.apis


import com.sergeenko.alexey.noble.dataclasses.Client
import com.sergeenko.alexey.noble.dataclasses.Club
import com.sergeenko.alexey.noble.dataclasses.Language
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

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
    suspend fun auth(
            @Field("api_key") apiKey: Int = Const.apiKey,
            @Field("api_event") apiEvent: String = Const.clubGet,
            @Field("user_email") email: String,
            @Field("user_pass") password: String
    ): Club

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

    @Multipart
    @POST("query/clients.php")
    fun addClient(
            @Part apiKeys: MutableList<MultipartBody.Part> = MultipartBody.Builder()
                    .addFormDataPart("api_key", Const.apiKey.toString())
                    .addFormDataPart("api_event", Const.clientAdd)
                    .build()
                    .parts(),
            @Part fields: MutableList<MultipartBody.Part>,
            @Part body: MutableList<MultipartBody.Part>,
    ): Call<Int>

    @Multipart
    @POST("query/clients.php")
    fun editClient(
        @Part apiKeys: MutableList<MultipartBody.Part> = MultipartBody.Builder()
            .addFormDataPart("api_key", Const.apiKey.toString())
            .addFormDataPart("api_event", Const.clientRedact)
            .build()
            .parts(),
        @Part fields: MutableList<MultipartBody.Part>,
        @Part body: MutableList<MultipartBody.Part>,
    ): Call<Int>

    @FormUrlEncoded
    @POST("query/clients.php")
    fun deleteClient(
            @FieldMap fields: Map<String, String>,
            @Field("api_key") apiKey: Int = Const.apiKey,
            @Field("api_event") apiEvent: String = Const.clientDelete,
            @Field("client_id") clientId: String,
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
        const val clientDelete = "client_delete"
        const val clientRedact = "client_redact"

        const val sortBySurname = "sirname"
        const val sortByDate = "dateS"
    }
}
