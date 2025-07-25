package http

import http.clientWrapper.HttpClientWrapper
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import okhttp3.*

class E2ERequest(private val client : HttpClientWrapper) {
    private constructor(url: String) : this(HttpClientWrapper(url))

    private val innerHeaders = mutableMapOf<String, String>()

    fun withHeaders(headers: Map<String, String>) : E2ERequest {
        innerHeaders.putAll(headers)
        return this
    }

    fun withHeader(name: String, value: String) : E2ERequest {
        innerHeaders[name] = value
        return this
    }

    fun withContentType(contentType: String) : E2ERequest {
        innerHeaders["Content-Type"] = contentType
        return this
    }

    fun sendAGet(queryParams : Map<String, String> = mapOf()): E2EResponse = client.get(queryParams, innerHeaders)
    fun sendAPut(body: String): E2EResponse = client.put(body, innerHeaders)

    fun sendAPut(body : Map<String, Any?>) : E2EResponse = client.put(body, innerHeaders)

    fun sendAPost(body : Map<String, String?>): E2EResponse = client.post(body, innerHeaders)

    fun sendAFilePost(filePath: String, fileParamName: String, params: Map<String, Any>): E2EResponse {
        val file = File(filePath)

        require(file.exists() && file.canRead()) { "File is not reachable: $filePath" }


        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .apply {
                params.forEach { (key, value) ->
                    addFormDataPart(key, value.toString())
                }
            }
            .addFormDataPart(fileParamName, file.name, file.asRequestBody())
            .build()

        return  client.postAFile(
            requestBody,
            innerHeaders)
    }
    fun sendAPost(body: String): E2EResponse  = client.post(body, innerHeaders)

    fun sendADelete(body: String = ""): E2EResponse  = client.delete(body, innerHeaders)

    fun withABearer(bearerProducer : () -> String): E2ERequest {
        innerHeaders["Authorization"] = bearerProducer.invoke()
        return this
    }
    companion object {
        @JvmStatic
        fun to(url: String) = E2ERequest(url)

        @JvmStatic
        private fun createAPetition(headers: Map<String, String>): Request.Builder {
            val request = Request.Builder()
            headers.forEach { request.header(it.key, it.value) }
            return request
        }
    }
}
