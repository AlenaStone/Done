package com.done.app.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class TipsRepository {

    suspend fun loadTip(): String {
        return withContext(Dispatchers.IO) {
            val connection =
                URL(TIPS_URL).openConnection() as HttpURLConnection

            try {
                connection.requestMethod = "GET"
                connection.connectTimeout = 8000
                connection.readTimeout = 8000

                if (connection.responseCode !in 200..299) {
                    throw IOException("Unexpected response: ${connection.responseCode}")
                }

                val response =
                    connection.inputStream.bufferedReader().use { reader ->
                        reader.readText()
                    }

                JSONObject(response)
                    .getJSONObject("slip")
                    .getString("advice")
            } finally {
                connection.disconnect()
            }
        }
    }

    private companion object {
        const val TIPS_URL = "https://api.adviceslip.com/advice"
    }
}
