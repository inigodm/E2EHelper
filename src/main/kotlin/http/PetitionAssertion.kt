package http

import com.google.gson.Gson
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

class Petition(val url: String) {
    val innerHeaders = mutableMapOf<String, String>()

    fun withHeaders(headers: Map<String, String>) : Petition {
        innerHeaders.putAll(headers)
        return this
    }

    fun withHeader(name: String, value: String) : Petition {
        innerHeaders[name] = value
        return this
    }

    fun withContentType(contentType: String) : Petition {
        innerHeaders["Content-Type"] = contentType
        return this
    }

    fun sendAGet(queryParams : Map<String, String> = mapOf()): RestResponse {
        val client = OkHttpClient()
        val res = client.newCall(
            createAPetition("${url}?${mapToQuery(queryParams)}", innerHeaders)
                .get()
                .build()).execute()
        return RestResponse.from(res)
    }

    fun mapToQuery(queryParams : Map<String, String>) = queryParams.keys.map { "${it.utf8()}=${queryParams[it]!!.utf8()}"} .joinToString("&")

    fun String.utf8(): String = java.net.URLEncoder.encode(this, "UTF-8")


    fun sendAPut(body: String): RestResponse {
        val client = OkHttpClient()
        val res = client.newCall(
            createAPetition(url, innerHeaders)
                .put(body.toRequestBody())
                .build()).execute()
        return RestResponse.from(res)
    }

    fun sendAPut(body : Map<String, Any?>) : RestResponse{
        return sendAPut(Gson().toJson(body))
    }

    fun sendAPost(body: String): RestResponse {
        val client = OkHttpClient()
        val res = client.newCall(
            createAPetition(url, innerHeaders)
                .post(body.toRequestBody())
                .build()).execute()
        return RestResponse.from(res)
    }

    fun sendADelete(body: String = ""): RestResponse {
        val client = OkHttpClient()
        val res = client.newCall(
            createAPetition(url, innerHeaders)
                .delete(body.toRequestBody())
                .build()).execute()
        return RestResponse.from(res)
    }

    companion object {

        @JvmStatic
        fun to(url: String) = Petition(url)

        @JvmStatic
        private fun createAPetition(url: String, headers: Map<String, String>): Request.Builder {
            val request = Request.Builder()
            headers.forEach { request.header(it.key, it.value) }
            return request.url(url)
        }
    }
}
