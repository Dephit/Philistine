package com.sergeenko.alexey.noble.modules

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class NoConnectionInterceptor(context: Context? = null) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return /*if (!isConnectionOn()) {
            throw NoConnectivityException()
        } else */if(!isInternetAvailable()) {
            throw NoInternetException()
        } else {
            chain.proceed(chain.request())
        }
    }

    private fun isInternetAvailable(): Boolean {
        return try {
            val timeoutMs = 1500
            val sock = Socket()
            val sockaddr = InetSocketAddress("8.8.8.8", 53)
            sock.connect(sockaddr, timeoutMs)
            sock.close()
            true
        } catch (e: IOException) {
            false
        }
    }

}

class NoConnectivityException : IOException() {
    override val message: String
        get() =
            "No network available, please check your WiFi or Data connection"
}

class NoInternetException() : IOException() {
    override val message: String
        get() =
            "No internet available, please check your connected WIFi or Data"
}