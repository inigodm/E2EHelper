package http

import db.DBResult
import db.DBSetup
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

object DBAssertion {
    val url = "jdbc:sqlite:sample.db"
    @BeforeAll
    @JvmStatic fun setup() {
        try {
            DBSetup("jdbc:sqlite:sample.db")
                .given("drop table users")
        } catch (e: Exception) {}
        DBSetup("jdbc:sqlite:sample.db")
            .given("""Create table users 
                 (USER           TEXT    NOT NULL, 
                  PASS           TEXT     NOT NULL,
                  SALT			TEXT  	   NOT NULL)""")
            .given("""
                insert into users values ('user', 'pass', 'salt')
            """)
    }

    @Test
    fun `should check number of responses`() {
        DBSetup("jdbc:sqlite:sample.db")
            .`when`("select * from users")
            .assertThatNumberOfResponses(1)
    }

    @Test
    fun `should throw an exception when number of responses unexpected`() {
        Assertions.assertThatThrownBy {  DBSetup("jdbc:sqlite:sample.db")
            .`when`("select * from users")
            .assertThatNumberOfResponses(2) }
            .isInstanceOf(org.opentest4j.AssertionFailedError::class.java)
    }

    @Test
    fun `should check responses`() {
        DBSetup("jdbc:sqlite:sample.db")
            .`when`("select * from users")
            .assertThatExistAEntryWithFields(mutableMapOf("USER" to "user", "PASS" to "pass"))
    }

    @Test
    fun `should find a response inside the resultset`() {
        val sut = DBResult(listOf(mapOf("a" to "1", "b" to "2"), mapOf("a" to "3", "b" to "4")))

        sut.assertThatExistAEntryWithFields(mapOf("a" to "3"))
    }
}
