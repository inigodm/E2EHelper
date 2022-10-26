package http

import http.clientWrapper.HttpClientWrapper

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

    fun sendAGet(queryParams : Map<String, String> = mapOf()): E2EResponse = client.get(queryParams)
    fun sendAPut(body: String): E2EResponse = client.put(body)

    fun sendAPut(body : Map<String, Any?>) : E2EResponse = client.put(body)

    fun sendAPost(body : Map<String, String?>): E2EResponse = client.post(body)

    fun sendAPost(body: String): E2EResponse  = client.post(body)

    fun sendADelete(body: String = ""): E2EResponse  = client.delete(body)

    fun withABearer(bearerProducer : () -> String): E2ERequest {
        innerHeaders["Authorization"] = bearerProducer.invoke()
        return this
    }
    companion object {
        @JvmStatic
        fun to(url: String) = E2ERequest(url)

    }
}
