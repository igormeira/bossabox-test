package com.programeira.vuttr.data.repository

import com.programeira.vuttr.data.datasource.ApiService
import com.programeira.vuttr.data.model.ToolResponse
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class ToolsRequest(private val successCallback: (Int, message: String?, List<ToolResponse>?) -> Unit,
                   private val errorCallback: () -> Unit) : KoinComponent {

    private val service: ApiService by inject()

    fun getTools() {
        service.getTools()
            .enqueue(object : Callback<List<ToolResponse>> {
                override fun onResponse(call: Call<List<ToolResponse>>, response: Response<List<ToolResponse>>) {
                    successCallback.invoke(response.code(), response.message(), response.body())
                }
                override fun onFailure(call: Call<List<ToolResponse>>, t: Throwable) {
                    errorCallback.invoke()
                }
            })
    }

}