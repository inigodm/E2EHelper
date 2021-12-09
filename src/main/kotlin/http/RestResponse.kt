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

    fun assertThatBodyContains(expectedResponse : Map<String, Any>) : RestResponse {
        val body = returned.body
        val responseMap : Map<String, Any> =
            gson.fromJson(body?.string(), object : TypeToken<Map<String?, Any?>?>() {}.getType())
        assertThat(responseMap).containsAllEntriesOf(expectedResponse)
        return this
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

    companion object {
        @kotlin.jvm.JvmStatic
        fun  from(t: Response): RestResponse {
            return RestResponse(t)
        }
    }
}
