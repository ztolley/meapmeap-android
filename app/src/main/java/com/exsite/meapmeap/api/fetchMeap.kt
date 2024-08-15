package com.exsite.meapmeap.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class Meap(
    val id: String,
    val locationName: String,
    val address: String,
    val postcode: String,
    val defibrillatorLocation: String?,
    val defibrillatorNotes: String?,
    val stretcherLocation: String?,
    val firstaidRoomLocation: String?,
    val hospitalName: String?,
    val hospitalAddress: String?,
    val hospitalPostcode: String?,
    val hospitalJourneyTime: String?,
    val walkInCentreName: String?,
    val walkInCentreAddress: String?,
    val walkInCentrePostcode: String?,
    val accessRouteForAmbulance: String?,
    val latitude: String,
    val longitude: String,
    val what3words: String?,
    val createdAt: String,
    val updatedAt: String,
    val authorEmail: String,
    val authorName: String,
    val draft: Boolean
)

suspend fun fetchMeap(id: String): Meap {
    return withContext(Dispatchers.IO) {
        val url = URL("https://www.meapmeap.co.uk/api/meap/$id")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()

        val response = connection.inputStream.bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(response)

        Meap(
            id = jsonObject.getString("id"),
            locationName = jsonObject.getString("locationName"),
            address = jsonObject.getString("address"),
            postcode = jsonObject.getString("postcode"),
            defibrillatorLocation = jsonObject.optString("defibrillatorLocation")
                .takeIf { it != "null" },
            defibrillatorNotes = jsonObject.optString("defibrillatorNotes").takeIf { it != "null" },
            stretcherLocation = jsonObject.optString("stretcherLocation").takeIf { it != "null" },
            firstaidRoomLocation = jsonObject.optString("firstaidRoomLocation")
                .takeIf { it != "null" },
            hospitalName = jsonObject.optString("hospitalName").takeIf { it != "null" },
            hospitalAddress = jsonObject.optString("hospitalAddress").takeIf { it != "null" },
            hospitalPostcode = jsonObject.optString("hospitalPostcode").takeIf { it != "null" },
            hospitalJourneyTime = jsonObject.optString("hospitalJourneyTime")
                .takeIf { it != "null" },
            walkInCentreName = jsonObject.optString("walkInCentreName").takeIf { it != "null" },
            walkInCentreAddress = jsonObject.optString("walkInCentreAddress")
                .takeIf { it != "null" },
            walkInCentrePostcode = jsonObject.optString("walkInCentrePostcode")
                .takeIf { it != "null" },
            accessRouteForAmbulance = jsonObject.optString("accessRouteForAmbulance")
                .takeIf { it != "null" },
            latitude = jsonObject.getString("latitude"),
            longitude = jsonObject.getString("longitude"),
            what3words = jsonObject.optString("what3words").takeIf { it != "null" },
            createdAt = jsonObject.getString("createdAt"),
            updatedAt = jsonObject.getString("updatedAt"),
            authorEmail = jsonObject.getString("authorEmail"),
            authorName = jsonObject.getString("authorName"),
            draft = jsonObject.getBoolean("draft")
        )
    }
}