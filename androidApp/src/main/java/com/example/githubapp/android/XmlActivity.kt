package com.example.githubapp.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.githubapp.db.DatabaseDriverFactory
import com.example.githubapp.model.Item
import com.example.githubapp.network.ConnectionUtils
import com.example.githubapp.repository.GithubRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class XmlActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var githubIcon: ImageView
    private lateinit var clearButton: ImageButton
    private lateinit var searchButton: Button
    private lateinit var loadingText: TextView
    private lateinit var listContainer: LinearLayout

    private var searchDataList: List<Item> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xml)

        // Initialize views
        searchView = findViewById(R.id.searchView)
        githubIcon = findViewById(R.id.githubIcon)
        clearButton = findViewById(R.id.clearButton)
        searchButton = findViewById(R.id.searchButton)
        loadingText = findViewById(R.id.loadingText)
        listContainer = findViewById(R.id.listContainer)

        // Set initial visibility
        clearButton.visibility = View.INVISIBLE

        // Set search button click listener
        searchButton.setOnClickListener {
            val searchQuery = searchView.query.toString()
            if (searchQuery.isNotEmpty()) {
                fetchData(searchQuery)
            }
        }

        // Set clear button click listener
        clearButton.setOnClickListener {
            searchView.setQuery("", false)
            clearButton.visibility = View.INVISIBLE
            searchDataList = emptyList()
            updateListUI()
        }
    }

    private fun fetchData(searchQuery: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                loadingText.text = "Fetching the data..."
                searchDataList = GithubRepository(
                    DatabaseDriverFactory(this@XmlActivity),
                    ConnectionUtils(this@XmlActivity)
                ).getSearch(searchQuery)

                loadingText.text = ""
                updateListUI()

            } catch (e: Exception) {
                loadingText.text = "Error: ${e.localizedMessage}"
                Log.d("searchData", "onCreate: ${e.localizedMessage}")
            }
        }
    }

    private fun updateListUI() {
        listContainer.removeAllViews()

        for (item in searchDataList) {
            val cardView = layoutInflater.inflate(R.layout.list_item, null) as CardView
            val itemName = cardView.findViewById<TextView>(R.id.itemName)
            val itemDescription = cardView.findViewById<TextView>(R.id.itemDescription)
            val itemVisibility = cardView.findViewById<TextView>(R.id.itemVisibility)
            val itemStars = cardView.findViewById<TextView>(R.id.itemStars)

            itemName.text = item.full_name
            itemDescription.text = "Description: ${item.description ?: "N/A"}"
            itemVisibility.text = "Visibility: ${item.visibility ?: "N/A"}"
            itemStars.text = "Stars: ${item.stargazers_count}"

            listContainer.addView(cardView)
        }
    }
}
