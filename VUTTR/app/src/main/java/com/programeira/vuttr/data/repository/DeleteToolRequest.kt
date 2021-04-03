package com.programeira.vuttr.data.repository

import com.programeira.vuttr.data.datasource.ApiService
import com.programeira.vuttr.data.model.Tool
import com.programeira.vuttr.data.model.ToolResponse
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class DeleteToolRequest(private val successCallback: (status: Int, message: String?, id: Int?) -> Unit,
                        private val errorCallback: () -> Unit,
                        private val id: Int) : KoinComponent {

    private val service: ApiService by inject()

    fun deleteTool() {
        service.deleteToolRequest(id.toString())
            .enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    successCallback.invoke(response.code(), response.message(), id)
                }
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    errorCallback.invoke()
                }
            })
    }

}