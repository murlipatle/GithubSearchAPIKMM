package com.example.githubapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchData(
    val incomplete_results: Boolean,
    @SerialName("items")
    val items: List<Item>,
    val total_count: Int
)