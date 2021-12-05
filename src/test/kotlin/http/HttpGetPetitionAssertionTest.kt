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
            Petition.sendAGetTo("http://localhost/getoquehase")
                .assertThatResponseIsOk()
        }
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
}
