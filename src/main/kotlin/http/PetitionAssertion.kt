package http

import http.clientWrapper.HttpClientWrapper

class Petition(private val client : HttpClientWrapper) {
    private constructor(url: String) : this(HttpClientWrapper(url))

    private val innerHeaders = mutableMapOf<String, String>()

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

    fun sendAGet(queryParams : Map<String, String> = mapOf()): RestResponse = client.get(queryParams)
    fun sendAPut(body: String): RestResponse = client.put(body)

    fun sendAPut(body : Map<String, Any?>) : RestResponse = client.put(body)

    fun sendAPost(body : Map<String, String?>): RestResponse = client.post(body)

    fun sendAPost(body: String): RestResponse  = client.post(body)

    fun sendADelete(body: String = ""): RestResponse  = client.delete(body)
    companion object {
        @JvmStatic
        fun to(url: String) = Petition(url)

    }
}
