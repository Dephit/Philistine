package com.sergeenko.alexey.noble.utills

import com.sergeenko.alexey.noble.dataclasses.User
import okhttp3.*


class ServiceInterceptor : Interceptor {

    var token : String = ""
    var user : User? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if(request.header("No-Authentication") == null){
            if(token.isNotEmpty()) {
                val finalToken = "Bearer $token"
                request = request.newBuilder()
                        .addHeader("Authorization", finalToken)
                        .build()
            }
        }


        if(user != null){
            val body = FormBody.Builder()
                    .add("user_email",  user!!.email)
                    .add("user_pass", user!!.pass)
                    .add("club_id", user!!.club?.id)
                    .build()

            return chain.proceed(request.newBuilder()
                    .post(request.body()!!)
                    .post(body)
                    .build())
        }
        return chain.proceed(request)
    }

}