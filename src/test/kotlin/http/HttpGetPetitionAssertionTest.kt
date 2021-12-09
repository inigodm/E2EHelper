package http

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError

class HttpGetPetitionAssertionTest {
    @Test
    fun `should call to a page and assert that is 200`() {
        Petition.sendAGetTo("http://localhost/get")
            .assertThatResponseIsOk()
    }

    @Test
    fun `should throw assert error if error is not ok`() {
        assertThrows<AssertionFailedError> {
            Petition.sendAGetTo("http://localhost/status/400")
                .assertThatResponseIsOk()
        }
    }

    @Test
    fun `should assert that a 400 is returned`() {
        Petition.sendAGetTo("http://localhost/status/400")
            .assertThatResponseCodeIs(400)
    }

    @Test
    fun `should respond with given values in the body`() {
        val response : Map<String, Any> = mapOf("url" to "http://localhost/get")
        Petition.sendAGetTo("http://localhost/get")
            .assertThatBodyContains(response)
    }

    @Test
    fun `should respond with given string in the body`() {
        Petition.sendAGetTo("http://localhost/get")
            .assertThatBodyContains("url")
    }

    @Test
    fun `should send a put petition with body and receive a 200`() {
        Petition.sendAPutTo("http://localhost/put", "innerbody")
            .assertThatResponseIsOk()
            .assertThatBodyContains("innerbody")
    }

    @Test
    fun `should send a post petition with body and receive a 200`() {
        Petition.sendAPostTo("http://localhost/post", "innerbody")
            .assertThatResponseIsOk()
            .assertThatBodyContains("innerbody")
    }

    @Test
    fun `should send a delete petition with body and receive a 200`() {
        Petition.sendADeleteTo("http://localhost/delete", "innerbody")
            .assertThatResponseIsOk()
            .assertThatBodyContains("innerbody")
    }
}
