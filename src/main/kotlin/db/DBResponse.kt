package db

import org.assertj.core.api.Assertions.assertThat
import org.opentest4j.AssertionFailedError

class DBResponse (val inner: List<Map<String, String>>) {

    fun assertThatNumberOfResults(number: Int): DBResponse {
        assertThat(inner.size).isEqualTo(number)
        return this
    }

    fun assertThatExistAEntryWithFields(toCheck: Map<String, String>) : DBResponse{
        if (inner.filter { innerMap ->
            toCheck.keys.filter { key ->
                toCheck[key.uppercase()].equals(innerMap[key.uppercase()]) }
                 .toList().size.equals(toCheck.keys.size) }.isEmpty()) {
            throw AssertionFailedError("Cannot find a row with givwn values", toCheck, inner)
        }
        return this
    }
}
