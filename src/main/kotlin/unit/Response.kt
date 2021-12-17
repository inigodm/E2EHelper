package unit

import com.google.gson.Gson
import org.opentest4j.AssertionFailedError

class Response {
    fun assertThatExistInMap(toCheck: Map<String, String>, existIn: Map<String, String>) {
        if (toCheck.keys.filter {
                    key -> toCheck[key.uppercase()].equals(existIn[key.uppercase()])
                    }
                    .toList().size.equals(toCheck.keys.size)) {
            throw AssertionFailedError("Cannot find a row with given values", toCheck, existIn)
        }
    }

    inline fun <reified T> parseTo(string: String) = Gson().fromJson(string, T::class.java)
    inline fun <reified T> parseTo(map: Map<String, String>) = Gson().fromJson(Gson().toJson(map)!!, T::class.java)
}
