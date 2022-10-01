package http

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError

class HttpGetPetitionAssertionTest {
    @Test
    fun `should call to a page and assert that is 200`() {
        E2ERequest.to("http://localhost/get").sendAGet()
            .assertThatResponseIsOk()
    }

    @Test
    fun `should throw assert error if error is not ok`() {
        assertThrows<AssertionFailedError> {
            E2ERequest.to("http://localhost/status/400").sendAGet()
                .assertThatResponseIsOk()
        }
    }

    @Test
    fun `should assert that a 400 is returned`() {
        E2ERequest.to("http://localhost/status/400").sendAGet()
            .assertThatResponseCodeIs(400)
    }

    @Test
    fun `should respond with given values in the body`() {
        val response : Map<String, Any> = mapOf("url" to "http://localhost/get")
        E2ERequest.to("http://localhost/get").sendAGet()
            .assertThatBodyContains(response)
    }

    @Test
    fun `should respond with given string in the body`() {
        E2ERequest.to("http://localhost/get").sendAGet()
            .assertThatBodyContains("url")
    }

    @Test
    fun `should send a put petition with body and receive a 200`() {
        E2ERequest.to("http://localhost/put").sendAPut("innerbody")
            .assertThatResponseIsOk()
            .assertThatBodyContains("innerbody")
    }

    @Test
    fun `should send a post petition with body and receive a 200`() {
        E2ERequest.to("http://localhost/post").sendAPost(mapOf("innerbody" to "value"))
            .assertThatResponseIsOk()
            .assertThatBodyContainsExactlyInAnyOrder(mapOf("innerbody" to "value"))
    }

    @Test
    fun `should send a delete petition with body and receive a 200`() {
        E2ERequest.to("http://localhost/delete").sendADelete("innerbody")
            .assertThatResponseIsOk()
            .assertThatBodyContains("innerbody")
    }
}
