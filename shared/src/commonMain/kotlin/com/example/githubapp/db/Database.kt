package com.example.githubapp.db

import com.example.githubapp.model.Item


internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = GitDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.gitDatabaseQueries

    internal fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllItem()
        }
    }
    internal fun clearSearchData(name:String) {
        dbQuery.transaction {
            dbQuery.deleteItemByName(name)
        }
    }

    internal fun getAllItems(): List<Item> {
        return dbQuery.selectAllSearch().executeAsList()
            .map {
                Item(
                    id = it.id.toInt(),
                    name = it.name,
                    full_name = it.full_name,
                    stargazers_count = it.stargazers_count.toInt(),
                    visibility = it.visibility,
                    description = it.description.toString(),
                )
            }
    }
    internal fun getItemsByName(name: String): List<Item> {
        return dbQuery.getItemByName("%$name%").executeAsList()
            .map {
                Item(
                    id = it.id.toInt(),
                    name = it.name,
                    full_name = it.full_name,
                    stargazers_count = it.stargazers_count.toInt(),
                    visibility = it.visibility,
                    description = it.description.toString(),
                )
            }
    }


    internal fun saveSearchResult(items: List<Item>) {
        dbQuery.transaction {
            items.forEach { item ->
                insertResult(item)
            }
        }
    }

    private fun insertResult(item: Item) {
        dbQuery.insertItem(
            id = item.id.toLong(),
            name = item.name,
            full_name = item.full_name,
            visibility = item.visibility,
            stargazers_count = item.stargazers_count.toLong(),
            description = item.description
        )
    }
}