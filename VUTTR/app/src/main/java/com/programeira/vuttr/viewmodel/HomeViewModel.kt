package com.programeira.vuttr.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.programeira.vuttr.data.datasource.ConnectivityService
import com.programeira.vuttr.data.model.Tool
import org.koin.core.KoinComponent
import org.koin.core.inject

class HomeViewModel : ViewModel(), KoinComponent {

    val showNoConnectionAlert = MutableLiveData<Boolean>()
    val showLoading = MutableLiveData<Boolean>()

    private val connection: ConnectivityService by inject()

    fun getTools(context: Context) {
        if (connection.isNetworkAvailable(context)) {
            showLoading.postValue(true)
            //TODO: recuperar lista
        } else {
            showNoConnectionAlert.postValue(true)
        }
    }

    fun addTool(context: Context, tool: Tool) {
        if (connection.isNetworkAvailable(context)) {
            showLoading.postValue(true)
            //TODO: add na lista
        } else {
            showNoConnectionAlert.postValue(true)
        }
    }

    fun removeTool(context: Context, id: Int) {
        if (connection.isNetworkAvailable(context)) {
            showLoading.postValue(true)
            //TODO: remove da lista
        } else {
            showNoConnectionAlert.postValue(true)
        }
    }



}