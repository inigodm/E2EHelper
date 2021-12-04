package http

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

interface Petition<T> {
    companion object {
        @kotlin.jvm.JvmStatic
        fun sendAGetTo(url: String): RestResponse {
            val client = OkHttpClient()
            val res = client.newCall(
                Request.Builder()
                    .get()
                    .url(url)
                    .build()).execute()
            return RestResponse.from(res)
        }
    }

}
