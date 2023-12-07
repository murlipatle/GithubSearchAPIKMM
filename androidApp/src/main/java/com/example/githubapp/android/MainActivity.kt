package com.example.githubapp.android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubapp.db.DatabaseDriverFactory
import com.example.githubapp.network.GithubRestAPI
import com.example.githubapp.model.Item
import com.example.githubapp.network.ConnectionUtils
import com.example.githubapp.repository.GithubRepository
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_main)

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val scope = rememberCoroutineScope()
                    var text by remember { mutableStateOf("") }
                    var searchDataList by remember { mutableStateOf<List<Item>>(emptyList()) }

                    val searchQuery = remember { mutableStateOf("") }

                    val onSearchQueryChange: (String) -> Unit = { newValue ->
                        searchQuery.value = newValue
                        if (newValue=="") {
                            searchDataList = mutableListOf()
                        }
                       // filteredItems.value = items.filter { it.contains(newValue, ignoreCase = true) }
                    }

                    Column( modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedTextField(
                                value = searchQuery.value,
                                onValueChange = onSearchQueryChange,
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Done
                                ),
                               /* keyboardActions = KeyboardActions(
                                    onDone = {
                                        // Handle "Enter" action here
                                    }
                                ),*/
                                leadingIcon = {
                                    Image(
                                        painter = painterResource(R.drawable.github_svgrepo_com),
                                        contentDescription = "GitHub",
                                    )
                                },
                                trailingIcon = {
                                    if (searchQuery.value.isNotEmpty()) {
                                        IconButton(
                                            onClick = { searchQuery.value = ""
                                                searchDataList = mutableListOf()},
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Clear",
                                            )
                                        }
                                    }
                                },
                                placeholder = { Text("Search Here...") },
                                modifier = Modifier
                                    .weight(1f)

                            )
                            IconButton(
                                onClick = {
                                    if (searchQuery.value.isNotEmpty()) {
                                        scope.launch {
                                            kotlin.runCatching {
                                                text="Fetching the data..."
                                                searchDataList = mutableListOf()
                                                GithubRepository(DatabaseDriverFactory(this@MainActivity),
                                                    ConnectionUtils(this@MainActivity)
                                                ).getSearch(searchQuery.value)
                                            }.onSuccess {
                                                searchDataList = it
                                                text=""
                                                Log.d("searchData", "onCreate: " + searchDataList)
                                            }.onFailure {
                                                text = "error ${it.localizedMessage}"
                                                Log.d(
                                                    "searchData",
                                                    "onCreate: " + it.localizedMessage
                                                )
                                            }
                                        }
                                    }
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                )
                            }
                        }
                        Column {
                            GreetingView(text)
                        }
                        LazyColumn(
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 8.dp)
                                .background(Color.White, RoundedCornerShape(4.dp))
                        ) {
                            items(searchDataList) { item ->
                                ListItem(item)
                            }
                        }

                    }

                }
            }
        }
    }
}

@Composable
fun ListItem(item: Item) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)

        ) {
            Text(text = item.full_name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))

            // Add other relevant information from the repository data
            Text(text = "Description: ${item.description ?: "N/A"}")
            Text(text = "Visibility: ${item.visibility ?: "N/A"}")
            Text(text = "Stars: ${item.stargazers_count}")
            // Add more details as needed

            // You can add click listeners or other interactions as needed
        }
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}
