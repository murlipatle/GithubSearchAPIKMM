package com.example.githubapp.network

import com.example.githubapp.model.SearchData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class GithubRestAPI {
    private val client = HttpClient{
        install(ContentNegotiation) {
            json(Json{
                ignoreUnknownKeys=true
                coerceInputValues = true
            })
        }
    }

    suspend fun searchRepo( searchName:String): SearchData {
        val response = client.get("https://api.github.com/search/repositories?q=$searchName&order=desc&per_page=5&page=1")
        return response.body()
    }
}