package http

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Response
import org.assertj.core.api.Assertions.*


class E2EResponse private constructor(var returned: Response, var gson : Gson = Gson()) {
    fun assertThatResponseIsOk() : E2EResponse {
        assertThat(returned.code).isEqualTo(200)
        return this
    }

    fun assertThatResponseCodeIs(code: Int) : E2EResponse {
        assertThat(returned.code).isEqualTo(code)
        return this
    }

    fun assertThatBodyContainsExactlyInAnyOrder(expectedResponse : Map<String, Any>) : E2EResponse {
        assertThat(Gson().fromJson(extractBodyAsMap()["data"].toString(), object : TypeToken<Map<String?, Any?>?>() {}.type)
         as Map<String, Any>)
            .containsExactlyInAnyOrderEntriesOf(expectedResponse)
        return this
    }

    fun assertThatBodyContains(expectedResponse : Map<String, Any?>) : E2EResponse {
        assertThat(extractBodyAsMap()).containsAllEntriesOf(expectedResponse)
        return this
    }

    fun assertThatBodyContainsKey(vararg keys : String) : E2EResponse {
        assertThat(extractBodyAsMap()).containsKeys(*keys)
        return this
    }

    fun body() : Map<String, Any?> {
        return gson.fromJson(returned.body?.string(), object : TypeToken<Map<String?, Any?>?>() {}.type)
    }

    fun assertThatBodyContains(expectedResponse : String) : E2EResponse {
        val body = returned.body
        assertThat(body?.string()).contains(expectedResponse)
        return this
    }

    fun assertThatBodyIsEqualTo(expectedResponse : String) : E2EResponse {
        val body = returned.body
        assertThat(body?.string()).isEqualTo(expectedResponse)
        return this
    }

    fun assertThatHasHeaderThatContains(header: String, contains: String) {
        assertThat(returned.headers[header]).contains(contains)
    }

    fun assertThatHasHeader(header: String, value: String) {
        assertThat(returned.headers[header]).isEqualTo(value)
    }


    fun headers(name: String) : String? {
        return returned.header(name)
    }

    private fun extractBodyAsMap(): Map<String, Any> {
        val body = returned.body
        return gson.fromJson(body?.string(), object : TypeToken<Map<String?, Any?>?>() {}.type)
    }

    inline fun <reified T> parseBody() = Gson().fromJson(returned.body.toString(), T::class.java)

    companion object {
        @kotlin.jvm.JvmStatic
        fun  from(t: Response): E2EResponse {
            return E2EResponse(t)
        }
    }
}
