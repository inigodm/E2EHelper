package http

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Response
import org.assertj.core.api.Assertions.*


class RestResponse private constructor(var returned: Response, var gson : Gson = Gson()) {
    fun assertThatResponseIsOk() : RestResponse {
        assertThat(returned.code).isEqualTo(200)
        return this
    }

    fun assertThatResponseCodeIs(code: Int) : RestResponse {
        assertThat(returned.code).isEqualTo(code)
        return this
    }

    fun assertThatBodyEqualsTo(expectedResponse : Map<String, Any>) : RestResponse {
        val body = returned.body
        val responseMap : Map<String, Any> =
            gson.fromJson(body?.string(), object : TypeToken<Map<String?, Any?>?>() {}.getType())
        assertThat(responseMap).containsExactlyInAnyOrderEntriesOf(expectedResponse)
        return this
    }

    fun assertThatBodyContains(expectedResponse : Map<String, Any?>) : RestResponse {
        val body = returned.body
        val responseMap : Map<String, Any> =
            gson.fromJson(body?.string(), object : TypeToken<Map<String?, Any?>?>() {}.getType())
        assertThat(responseMap).containsAllEntriesOf(expectedResponse)
        return this
    }


    fun assertThatBodyContainsKey(vararg keys : String) : RestResponse {
        val body = returned.body
        val responseMap : Map<String, Any> =
            gson.fromJson(body?.string(), object : TypeToken<Map<String?, Any?>?>() {}.getType())
        assertThat(responseMap).containsKeys(*keys)
        return this
    }

    fun body() : Map<String, Any?> {
        return gson.fromJson(returned.body?.string(), object : TypeToken<Map<String?, Any?>?>() {}.getType())
    }

    fun assertThatBodyContains(expectedResponse : String) : RestResponse {
        val body = returned.body
        assertThat(body?.string()).contains(expectedResponse)
        return this
    }

    fun assertThatBodyIsEqualTo(expectedResponse : String) : RestResponse {
        val body = returned.body
        assertThat(body?.string()).isEqualTo(expectedResponse)
        return this
    }

    fun assertThatHasHeaderThatContains(header: String, contains: String) {
        assertThat(returned.headers[header]).contains(contains)
    }

    fun headers(name: String) : String? {
        return returned.header(name)
    }

    inline fun <reified T> parseBody() = Gson().fromJson(returned.body.toString(), T::class.java)

    companion object {
        @kotlin.jvm.JvmStatic
        fun  from(t: Response): RestResponse {
            return RestResponse(t)
        }
    }
}
