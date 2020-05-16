package com.example.httpclien

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var url = "http://mysafeinfo.com/api/data?list=presidents&format=json"

        AsyncTaskHandleJson().execute(url)
    }

    inner class AsyncTaskHandleJson : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg url: String?): String {

            var text: String
            var connection = URL(url[0]).openConnection() as HttpURLConnection
            try {
                connection.connect()
                text = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
            } finally {
                connection.disconnect()
            }

            return text
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            handleJson(result)
        }
    }

    private fun handleJson(jsonString: String?) {
        var jsonArray = JSONArray(jsonString)

        val list =ArrayList<President>()

        var i = 0
        while (i < jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)

            list.add(President(
                jsonObject.getInt("ID"),
                jsonObject.getString("FullName"),
                jsonObject.getString("Party"),
                jsonObject.getString("Terms")
            ))

            i++
        }

        var adapter = ListAdapter(this, list)
        presidents_list.adapter = adapter
    }
}
