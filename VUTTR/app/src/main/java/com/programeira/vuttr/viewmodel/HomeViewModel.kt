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
    val showRetryError = MutableLiveData<Boolean>()
    val showLoading = MutableLiveData<Boolean>()
    val toolsObjects = MutableLiveData<List<ToolResponse>>()
    val addedTool = MutableLiveData<ToolResponse>()
    val deletedTool = MutableLiveData<Int>()

    private val connection: ConnectivityService by inject()

    fun getTools(context: Context) {
        if (connection.isNetworkAvailable(context)) {
            showLoading.postValue(true)
            val toolsRequest = ToolsRequest(::onToolsSuccess, ::onFailureList)
            toolsRequest.getTools()
        } else {
            showConnectionAlert()
        }
    }

    fun getToolsBySearch(context: Context, element: String, onlyTags: Boolean) {
        if (connection.isNetworkAvailable(context)) {
            showLoading.postValue(true)
            if (onlyTags) {
                val tagRequest = ByTagRequest(::onToolsSuccess, ::onFailureList, element)
                tagRequest.getToolsByTitle()
            } else {
                val titleRequest = ByTitleRequest(::onToolsSuccess, ::onFailureList, element)
                titleRequest.getToolsByTitle()
            }
        } else {
            showConnectionAlert()
        }
    }

    fun addTool(context: Context, tool: Tool) {
        if (connection.isNetworkAvailable(context)) {
            showLoading.postValue(true)
            val addRequest = AddToolRequest(::onAddSuccess, ::onFailureChange, tool)
            addRequest.addTool()
        } else {
            showConnectionAlert()
        }
    }

    fun removeTool(context: Context, id: Int) {
        if (connection.isNetworkAvailable(context)) {
            showLoading.postValue(true)
            val deleteRequest = DeleteToolRequest(::onDeleteSuccess, ::onFailureChange, id)
            deleteRequest.deleteTool()
        } else {
            showConnectionAlert()
        }
    }

    private fun showConnectionAlert() {
        showLoading.postValue(false)
        showNoConnectionAlert.postValue(true)
    }

    fun retry(context: Context, element: String = "", onlyTags: Boolean = false) {
        if (element.isNotBlank()) getToolsBySearch(context, element, onlyTags)
        else getTools(context)
    }

    private fun onToolsSuccess(code: Int, message: String?, body: List<ToolResponse>?) {
        showLoading.postValue(false)
        showRetryError.postValue(false)
        when (code) {
            200 -> {
                logSuccessBody(code, body)
                toolsObjects.postValue(body)
            }
            else -> {
                logSuccessMessage(code, message)
            }
        }
    }

    private fun onAddSuccess(code: Int, message: String?, body: ToolResponse?) {
        showLoading.postValue(false)
        when (code) {
            200 -> {
                logSuccessBody(code, body)
                addedTool.postValue(body)
            }
            else -> {
                logSuccessMessage(code, message)
            }
        }
    }

    private fun onDeleteSuccess(code: Int, message: String?, id: Int?) {
        showLoading.postValue(false)
        when (code) {
            200 -> {
                Log.i("Success::", "$code")
                findPositionById(id)?.let { position -> deletedTool.postValue(position) }
            }
            else -> {
                logSuccessMessage(code, message)
            }
        }
    }

    private fun onFailureList() {
        showLoading.postValue(false)
        showRetryError.postValue(true)
        Log.e("Error::", "failure! :(")
    }

    private fun onFailureChange() {
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

    private fun logSuccessMessage(code: Int, message: String?) {
        Log.i("Success::", "${code}")
        Log.e("${code}::", "$message")
    }

    private fun logSuccessBody(code: Int, body: Any?) {
        Log.i("Success::", "${code}")
        Log.i("Body::", "${body}")
    }
}