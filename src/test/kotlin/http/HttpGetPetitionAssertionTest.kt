package http

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError

class HttpGetPetitionAssertionTest {
    @Test
    fun should_call_to_a_page_and_assert_that_is_200() {
        Petition.sendAGetTo("https://www.google.com")
            .assertThatResponseIsOk()
    }

    @Test
    fun should_throw_assert_error_if_error_is_not_ok() {
        assertThrows<AssertionFailedError> {
            Petition.sendAGetTo("https://www.google.com/tortilla")
                .assertThatResponseIsOk()
        }
    }
}
