package http

import okhttp3.Response
import org.assertj.core.api.Assertions.*

class RestResponse private constructor(var returned: Response) {
    fun assertThatResponseIsOk() {
        assertThat(returned.code).isEqualTo(200)
    }

    companion object {
        @kotlin.jvm.JvmStatic
        fun  from(t: Response): RestResponse {
            return RestResponse(t)
        }
    }


}
