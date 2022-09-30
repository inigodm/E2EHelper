package db

import org.assertj.core.api.Assertions.assertThat
import org.opentest4j.AssertionFailedError

class DBResponse (private val inner: List<Map<String, Any>>) {

    fun assertThatNumberOfResults(number: Int): DBResponse {
        assertThat(inner.size).isEqualTo(number)
        return this
    }

    fun assertThatExistAnEntryWithFields(toCheck: Map<String, Any>) : DBResponse{
        if (inner.none { innerMap ->
                toCheck.keys.filter { key ->
                    toCheck[key]!!.equals(innerMap[key.uppercase()])
                }.toList().size == toCheck.keys.size
            }) {
            throw AssertionFailedError("Cannot find a row with given values", toCheck, inner)
        }
        return this
    }
}
