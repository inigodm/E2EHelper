package db

import org.assertj.core.api.Assertions.assertThat
import org.opentest4j.AssertionFailedError

class DBResult (val inner: List<Map<String, String>>) {

    fun assertThatNumberOfResponses(number: Int): DBResult {
        assertThat(inner.size).isEqualTo(number)
        return this
    }

    fun assertThatExistAEntryWithFields(toCheck: Map<String, String>) : DBResult{
        if (inner.filter { innerMap ->
            toCheck.keys.filter { key ->
                toCheck[key.uppercase()].equals(innerMap[key.uppercase()]) }
                 .toList().size.equals(toCheck.keys.size) }.isEmpty()) {
            throw AssertionFailedError("Cannot find a row with givwn values", toCheck, inner)
        }
        return this
    }
}
