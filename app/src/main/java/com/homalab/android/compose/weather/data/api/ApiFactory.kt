package com.homalab.android.compose.weather.data.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.homalab.android.compose.weather.data.common.DataConstants.BASE_OPEN_WEATHER_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

interface ApiFactory {

    companion object {
        inline fun <reified T> createRetrofit(): T {
            val builder = OkHttpClient().newBuilder()

            builder.cookieJar(AppCookieJar.getInstance())
            builder.connectTimeout(30, TimeUnit.SECONDS)
            builder.readTimeout(60, TimeUnit.SECONDS)
            builder.writeTimeout(60, TimeUnit.SECONDS)

            builder.addInterceptor { chain ->

                val request = chain.request().newBuilder().build()

                val response = chain.proceed(request)

                response
            }

            val client = builder.build()

            val gson = GsonBuilder().serializeNulls()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setLenient()
                .create()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_OPEN_WEATHER_URL)
                .client(client)
                .build()

            return retrofit.create(T::class.java)
        }
    }
}