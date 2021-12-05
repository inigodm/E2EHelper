package http

import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

interface Petition<T> {
    companion object {
        @kotlin.jvm.JvmStatic
        fun sendAGetTo(url: String, headers: Map<String, String> = mapOf()): RestResponse {
            val client = OkHttpClient()
            val res = client.newCall(
                createAPetition(url, headers)
                    .get()
                    .build()).execute()
            return RestResponse.from(res)
        }

        fun sendAPutTo(url: String, headers: Map<String, String>, body: String): RestResponse {
            val client = OkHttpClient()
            val res = client.newCall(
                createAPetition(url, headers)
                    .put(body.toRequestBody())
                    .build()).execute()
            return RestResponse.from(res)
        }


        fun createAPetition(url: String, headers: Map<String, String>): Request.Builder {
            val request = Request.Builder()
            headers.forEach { request.header(it.key, it.value) }
            return request.url(url)
        }
    }

}
