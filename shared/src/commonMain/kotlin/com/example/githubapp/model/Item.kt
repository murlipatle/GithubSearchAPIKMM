package com.example.githubapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val description: String?,
    val full_name: String,
    val id: Int,
    val name: String,
    val stargazers_count: Int,
    val visibility: String,
)