package http.clientWrapper

import com.google.gson.Gson
import http.E2EResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor

class HttpClientWrapper(private val url: String) {

    fun get(queryParams : Map<String, String> = mapOf(), innerHeaders: Map<String, String>): E2EResponse {
        val client = buildClient()
        val res = client.newCall(
            createAPetition("${url}?${mapToQuery(queryParams)}", innerHeaders)
                .get()
                .build()).execute()
        return E2EResponse.from(res)
    }

    fun get(innerHeaders: Map<String, String> = mapOf()): E2EResponse {
        val client = buildClient()
        val res = client.newCall(
            createAPetition(url, innerHeaders)
                .get()
                .build()).execute()
        return E2EResponse.from(res)
    }

    fun put(body: String, innerHeaders: Map<String, String>): E2EResponse {
        val client = buildClient()
        val res = client.newCall(
            createAPetition(url, innerHeaders)
                .put(body.toRequestBody())
                .build()).execute()
        return E2EResponse.from(res)
    }

    fun put(body : Map<String, Any?>, innerHeaders: Map<String, String>) : E2EResponse{
        val client = buildClient()
        val res = client.newCall(
            createAPetition(url, innerHeaders)
                .put(Gson().toJson(body)
                    .toRequestBody("application/json".toMediaType())).build()).execute()
        return E2EResponse.from(res)
    }

    fun post(body : Map<String, String?>, innerHeaders: Map<String, String>): E2EResponse {
        val client = buildClient()
        val res = client.newCall(
            createAPetition(url, innerHeaders)
                .post(Gson().toJson(body)
                    .toRequestBody("application/json".toMediaType())).build()).execute()
        return E2EResponse.from(res)
    }

    fun postAFile(body : MultipartBody, innerHeaders: Map<String, String>): E2EResponse {
        val client = buildClient()
        val res = client.newCall(
            createAPetition(url, innerHeaders)
                .post(Gson().toJson(body)
                    .toRequestBody("application/json".toMediaType())).build()).execute()
        return E2EResponse.from(res)
    }

    fun post(body: String, innerHeaders: Map<String, String>): E2EResponse {
        val client = buildClient()
        val res = client.newCall(
            createAPetition(url, innerHeaders)
                .post(body.toRequestBody())
                .build()).execute()
        return E2EResponse.from(res)
    }

    fun delete(body: String = "", innerHeaders: Map<String, String>): E2EResponse {
        val client = buildClient()
        val res = client.newCall(
            createAPetition(url, innerHeaders)
                .delete(body.toRequestBody())
                .build()).execute()
        return E2EResponse.from(res)
    }

    fun buildClient() : OkHttpClient {
        val client = OkHttpClient().newBuilder()
        val logging = HttpLoggingInterceptor()
        logging.level = (HttpLoggingInterceptor.Level.HEADERS)
        client.addInterceptor(logging)
        return client.build()
    }

    private fun mapToQuery(queryParams : Map<String, String>) = queryParams.keys.map { "${it.utf8()}=${queryParams[it]!!.utf8()}"} .joinToString("&")

    private fun String.utf8(): String = java.net.URLEncoder.encode(this, "UTF-8")

}

private fun createAPetition(url: String, headers: Map<String, String>): Request.Builder {
    val request = Request.Builder()
    headers.forEach { request.header(it.key, it.value) }
    return request.url(url)
}
