package com.programeira.vuttr.data.repository

import com.programeira.vuttr.data.datasource.ApiService
import com.programeira.vuttr.data.model.ToolResponse
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class ByTitleRequest(private val successCallback: (Int, message: String?, List<ToolResponse>?) -> Unit,
                     private val errorCallback: () -> Unit,
                     private val title: String) : KoinComponent {

    private val service: ApiService by inject()

    fun getToolsByTitle() {
        service.getToolsByTitle(title)
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