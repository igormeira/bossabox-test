package com.programeira.vuttr.data.model

import com.google.gson.annotations.SerializedName

data class Tool (
    @SerializedName("name")
    val name: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("tags")
    val tags: List<String>
)