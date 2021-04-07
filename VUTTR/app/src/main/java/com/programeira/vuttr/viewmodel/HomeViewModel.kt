package com.programeira.vuttr.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.programeira.vuttr.data.datasource.ConnectivityService
import com.programeira.vuttr.data.model.Tool
import com.programeira.vuttr.data.model.ToolResponse
import com.programeira.vuttr.data.repository.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class HomeViewModel : ViewModel(), KoinComponent {

    val showNoConnectionAlert = MutableLiveData<Boolean>()
    val showErrorAlert = MutableLiveData<Boolean>()
    val showLoading = MutableLiveData<Boolean>()
    val toolsObjects = MutableLiveData<List<ToolResponse>>()
    val addedTool = MutableLiveData<ToolResponse>()
    val deletedTool = MutableLiveData<Int>()

    private val connection: ConnectivityService by inject()

    fun getTools(context: Context) {
        if (connection.isNetworkAvailable(context)) {
            showLoading.postValue(true)
            val toolsRequest = ToolsRequest(::onToolsSuccess, ::onFailure)
            toolsRequest.getTools()
        } else {
            showLoading.postValue(false)
            showNoConnectionAlert.postValue(true)
        }
    }

    fun getToolsBySearch(context: Context, element: String, onlyTags: Boolean) {
        if (connection.isNetworkAvailable(context)) {
            showLoading.postValue(true)
            if (onlyTags) {
                val tagRequest = ByTagRequest(::onToolsSuccess, ::onFailure, element)
                tagRequest.getToolsByTitle()
            } else {
                val titleRequest = ByTitleRequest(::onToolsSuccess, ::onFailure, element)
                titleRequest.getToolsByTitle()
            }
        } else {
            showLoading.postValue(false)
            showNoConnectionAlert.postValue(true)
        }
    }

    fun addTool(context: Context, tool: Tool) {
        if (connection.isNetworkAvailable(context)) {
            showLoading.postValue(true)
            val addRequest = AddToolRequest(::onAddSuccess, ::onFailure, tool)
            addRequest.addTool()
        } else {
            showLoading.postValue(false)
            showNoConnectionAlert.postValue(true)
        }
    }

    fun removeTool(context: Context, id: Int) {
        if (connection.isNetworkAvailable(context)) {
            showLoading.postValue(true)
            val deleteRequest = DeleteToolRequest(::onDeleteSuccess, ::onFailure, id)
            deleteRequest.deleteTool()
        } else {
            showLoading.postValue(false)
            showNoConnectionAlert.postValue(true)
        }
    }

    private fun onToolsSuccess(status: Int, message: String?, body: List<ToolResponse>?) {
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

    private fun onAddSuccess(status: Int, message: String?, body: ToolResponse?) {
        val code = 200
        showLoading.postValue(false)
        when (status) {
            200 -> {
                Log.i("Success::", "${code}")
                Log.i("Body::", "${body}")
                addedTool.postValue(body)
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

    private fun onDeleteSuccess(status: Int, message: String?, id: Int?) {
        val code = 200
        showLoading.postValue(false)
        when (status) {
            200 -> {
                Log.i("Success::", "$code")
                findPositionById(id)?.let { position -> deletedTool.postValue(position) }
            }
            404 -> {
                Log.i("Success::", "$code")
                Log.e("${code}::", "$message")
            }
            else -> {
                Log.i("Success::", "$code")
                Log.e("${code}::", "$message")
            }
        }
    }

    private fun onFailure() {
        showLoading.postValue(false)
        showErrorAlert.postValue(true)
        Log.e("Error::", "failure! :(")
    }

    private fun findPositionById(id: Int?) : Int? {
        id?.let { idValue ->
            toolsObjects.value?.let { tools ->
                for ((index, tool) in tools.withIndex()) {
                    if (tool.id == idValue) return index
                }
            }
        }
        return null
    }

}