package com.programeira.vuttr.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.programeira.vuttr.data.datasource.ConnectivityService
import com.programeira.vuttr.data.model.Tool
import com.programeira.vuttr.data.model.ToolResponse
import com.programeira.vuttr.data.repository.ToolsRequest
import org.koin.core.KoinComponent
import org.koin.core.inject

class HomeViewModel : ViewModel(), KoinComponent {

    val showNoConnectionAlert = MutableLiveData<Boolean>()
    val showLoading = MutableLiveData<Boolean>()
    val toolsObjects = MutableLiveData<List<ToolResponse>>()

    private val connection: ConnectivityService by inject()

    fun getTools(context: Context) {
        if (connection.isNetworkAvailable(context)) {
            showLoading.postValue(true)
            val toolsRequest = ToolsRequest(::onSuccess, ::onFailure)
            toolsRequest.getTools()
        } else {
            showLoading.postValue(false)
            showNoConnectionAlert.postValue(true)
        }
    }

    fun addTool(context: Context, tool: Tool) {
        if (connection.isNetworkAvailable(context)) {
            showLoading.postValue(true)
            //TODO: add na lista
        } else {
            showLoading.postValue(false)
            showNoConnectionAlert.postValue(true)
        }
    }

    fun removeTool(context: Context, id: Int) {
        if (connection.isNetworkAvailable(context)) {
            showLoading.postValue(true)
            //TODO: remove da lista
        } else {
            showLoading.postValue(false)
            showNoConnectionAlert.postValue(true)
        }
    }

    private fun onSuccess(status: Int, message: String?, body: List<ToolResponse>?) {
        val code = 200
        showLoading.postValue(false)
        when (status) {
            200 -> {
                Log.i("Success::", "${code}")
                Log.i("Body::", "${body}")
                toolsObjects.postValue(body)
            }
            404 -> {
                Log.i("Success::", "${code}")
                Log.e("${code}::", "$message")
            }
            else -> {
                Log.i("Success::", "${code}")
                Log.e("${code}::", "$message")
            }
        }
    }

    private fun onFailure() {
        showLoading.postValue(false)
        Log.e("Error::", "failure! :(")
    }

}