package com.programeira.vuttr.data.repository

import com.programeira.vuttr.data.datasource.ApiService
import com.programeira.vuttr.data.model.Tool
import com.programeira.vuttr.data.model.ToolResponse
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class AddToolRequest(private val successCallback: (Int, message: String?, ToolResponse?) -> Unit,
                     private val errorCallback: () -> Unit,
                     private val tool: Tool) : KoinComponent {

    private val service: ApiService by inject()

    fun addTool() {
        service.addToolRequest(tool)
            .enqueue(object : Callback<ToolResponse> {
                override fun onResponse(call: Call<ToolResponse>, response: Response<ToolResponse>) {
                    successCallback.invoke(response.code(), response.message(), response.body())
                }
                override fun onFailure(call: Call<ToolResponse>, t: Throwable) {
                    errorCallback.invoke()
                }
            })
    }

}