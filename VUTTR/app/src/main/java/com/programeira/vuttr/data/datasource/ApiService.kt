package com.programeira.vuttr.data.datasource

import com.programeira.vuttr.data.model.Tool
import com.programeira.vuttr.data.model.ToolResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {
    // TOOLS - GET - /tools //
    @GET("/tools")
    fun getTools(): Call<List<ToolResponse>>

    // SEARCH - GET - /tools?q=notion //
    @GET("/tools?")
    fun getToolsByTitle(@Query("q") title: String): Call<List<ToolResponse>>

    // SEARCH TAGS - GET - /tools?tags_like=node //
    @GET("/tools?")
    fun getToolsByTag(@Query("tags_like") tags: String): Call<List<ToolResponse>>

    // ADD - POST - /tools //
    @POST("/tools")
    fun addToolRequest(@Body tool: Tool): Call<ToolResponse>

    // REMOVE - DELETE - /tools/:id //
    @DELETE("/tools/{id}")
    fun deleteToolRequest(@Path("id") id: String) : Call<Any>

    companion object {
        fun getBaseService(): ApiService {
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000") //local host for emulator
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}