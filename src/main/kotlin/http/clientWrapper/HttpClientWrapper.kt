package http.clientWrapper

import com.google.gson.Gson
import http.RestResponse
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class HttpClientWrapper(private val url: String) {
    private val innerHeaders = mutableMapOf<String, String>()

    fun get(queryParams : Map<String, String> = mapOf()): RestResponse {
        val client = OkHttpClient()
        val res = client.newCall(
            createAPetition("${url}?${mapToQuery(queryParams)}", innerHeaders)
                .get()
                .build()).execute()
        return RestResponse.from(res)
    }

    fun put(body: String): RestResponse {
        val client = OkHttpClient()
        val res = client.newCall(
            createAPetition(url, innerHeaders)
                .put(body.toRequestBody())
                .build()).execute()
        return RestResponse.from(res)
    }

    fun put(body : Map<String, Any?>) : RestResponse{
        return put(Gson().toJson(body))
    }

    fun post(body : Map<String, String?>): RestResponse {
        return post(Gson().toJson(body))
    }

    fun post(body: String): RestResponse {
        val client = OkHttpClient()
        val res = client.newCall(
            createAPetition(url, innerHeaders)
                .post(body.toRequestBody())
                .build()).execute()
        return RestResponse.from(res)
    }

    fun delete(body: String = ""): RestResponse {
        val client = OkHttpClient()
        val res = client.newCall(
            createAPetition(url, innerHeaders)
                .delete(body.toRequestBody())
                .build()).execute()
        return RestResponse.from(res)
    }

    private fun mapToQuery(queryParams : Map<String, String>) = queryParams.keys.map { "${it.utf8()}=${queryParams[it]!!.utf8()}"} .joinToString("&")

    private fun String.utf8(): String = java.net.URLEncoder.encode(this, "UTF-8")

}

private fun createAPetition(url: String, headers: Map<String, String>): Request.Builder {
    val request = Request.Builder()
    headers.forEach { request.header(it.key, it.value) }
    return request.url(url)
}
