package com.homalab.android.compose.weather.data.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkChecker(private val context: Context) {

    fun getConnectionType(): Int {
        var result = NONE
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                result = WIFI
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                result = CELLULAR
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                result = VPN
            }
        }
        return result
    }

    companion object {
        const val NONE = 0
        private const val CELLULAR = 1
        private const val WIFI = 2
        private const val VPN = 3
    }
}