package tn.esprit.miniprojectforintegratedprojectsmanagement.utils

import okhttp3.Interceptor
import okhttp3.Response
import tn.esprit.miniprojectforintegratedprojectsmanagement.sharedPrefs.SharedPrefs

class RequestInterceptor constructor(private val pref: SharedPrefs) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = pref.getToken()
        return if (token != null) {
            val newRequest = chain.request().newBuilder()
                .addHeader("jwt", token).build()
            println("request: $newRequest")
            chain.proceed(newRequest)
        } else {
            val newRequest = chain.request().newBuilder().build()
            println("request: $newRequest")
            chain.proceed(newRequest)
        }
    }
}
