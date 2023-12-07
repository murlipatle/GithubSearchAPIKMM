package com.example.githubapp.repository

import com.example.githubapp.db.Database
import com.example.githubapp.db.DatabaseDriverFactory
import com.example.githubapp.model.Item
import com.example.githubapp.network.ConnectionUtils
import com.example.githubapp.network.GithubRestAPI

class GithubRepository (databaseDriverFactory: DatabaseDriverFactory,val connectionUtils: ConnectionUtils) {
    private val database = Database(databaseDriverFactory)
    private val api = GithubRestAPI()

    @Throws(Exception::class) suspend fun getSearch(searchQuery: String): List<Item> {
        if (connectionUtils.isNetworkAvailable()) {
            api.searchRepo(searchQuery).also {
                if (it.items.isEmpty()){
                    throw IllegalArgumentException("No Data Found..")
                }
                database.clearSearchData(searchQuery)
                database.saveSearchResult(it.items)
                return it.items
            }
        }else{
          val itemDb= database.getItemsByName(searchQuery)
            if (itemDb.isEmpty()){
                throw IllegalArgumentException("Please Check your Connection")
            }else{
               return itemDb
            }
        }

    }
}