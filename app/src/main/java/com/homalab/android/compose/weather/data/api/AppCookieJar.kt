package com.homalab.android.compose.weather.data.api

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class AppCookieJar : CookieJar {

    companion object {
        private var INSTANCE: AppCookieJar? = null
        fun getInstance(): AppCookieJar {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = AppCookieJar()
                }
                return instance
            }
        }
    }

    private val cookies = ArrayList<Cookie>()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookies.ifEmpty { emptyList() }
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        if (this.cookies.isNotEmpty()) {
            this.cookies.clear()
        }
        this.cookies.addAll(cookies)
    }
}