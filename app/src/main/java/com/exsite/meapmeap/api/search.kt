package com.exsite.meapmeap.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

data class SearchResult(
    val id: String,
    val locationName: String,
    val address: String,
    val postcode: String,
    val latitude: String,
    val longitude: String,
    val distance: Double
)

suspend fun search(query: String): List<SearchResult> {
    return withContext(Dispatchers.IO) {
        val url = URL("https://www.meapmeap.co.uk/api/search?query=$query")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()

        val response = connection.inputStream.bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(response)

        val results = mutableListOf<SearchResult>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val result = SearchResult(
                id = jsonObject.getString("id"),
                locationName = jsonObject.getString("locationName"),
                address = jsonObject.getString("address"),
                postcode = jsonObject.getString("postcode"),
                latitude = jsonObject.getString("latitude"),
                longitude = jsonObject.getString("longitude"),
                distance = jsonObject.getDouble("distance")
            )
            results.add(result)
        }
        results
    }
}