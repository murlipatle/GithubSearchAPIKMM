package com.example.githubapp.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build


actual class ConnectionUtils(private val context: Context) {
    actual fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

            return actNw.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo ?: return false
            return activeNetworkInfo.isConnectedOrConnecting
        }
    }
}