package com.arsvechkarev.vault.features.common.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface NetworkConnectivityProvider {
  fun networkConnectivityFlow(): Flow<Boolean>
}

class AndroidNetworkConnectivityProvider(context: Context) : NetworkConnectivityProvider {
  
  private val connectivityManager =
      context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  
  private val _flow = MutableSharedFlow<Boolean>(replay = 1)
  
  private val callback = object : ConnectivityManager.NetworkCallback() {
    
    override fun onAvailable(network: Network) {
      _flow.tryEmit(true)
    }
    
    override fun onUnavailable() {
      _flow.tryEmit(false)
    }
  }
  
  init {
    connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), callback)
  }
  
  override fun networkConnectivityFlow(): Flow<Boolean> {
    return _flow
  }
}
