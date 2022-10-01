package db

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError

object E2EDBAssertion {
    val url = "jdbc:sqlite:sample.db"
    @BeforeAll
    @JvmStatic fun setup() {
        try {
            E2EDB("jdbc:sqlite:sample.db")
                .given("drop table users")
        } catch (e: Exception) {}
        E2EDB("jdbc:sqlite:sample.db")
            .given("""Create table users 
                 (USER           TEXT    NOT NULL, 
                  PASS           TEXT     NOT NULL,
                  SALT			TEXT  	   NOT NULL,
                  ID INTEGER)""")
    }

    @BeforeEach
    fun firstly() {
        E2EDB("jdbc:sqlite:sample.db")
            .givenEmptyTable("users")
            .given("""
                insert into users values ('user', 'pass', 'salt', 1);
                insert into users values ('user2', 'pass2', 'salt2', 2)
            """)
    }

    @Test
    fun `should retrieve the appropriate number of coincidences`() {
        E2EDB("jdbc:sqlite:sample.db")
            .doQuery("select * from users")
            .assertThatNumberOfResults(2)
    }

    @Test
    fun `should throw an exception when number of responses unexpected`() {
        Assertions.assertThatThrownBy {  E2EDB("jdbc:sqlite:sample.db")
            .doQuery("select * from users")
            .assertThatNumberOfResults(42) }
            .isInstanceOf(org.opentest4j.AssertionFailedError::class.java)
    }

    @Test
    fun `should check responses`() {
        E2EDB("jdbc:sqlite:sample.db")
            .doQuery("select * from users where user='user'")
            .assertThatExistAnEntryWithFields(mutableMapOf("USER" to "user", "PASS" to "pass"))
            .assertThatNumberOfResults(1)
    }

    @Test
    fun `should throw an exception if no  entry exists with given values`() {
        Assertions.assertThatThrownBy {  E2EDB("jdbc:sqlite:sample.db")
            .doQuery("select * from users where user='user'")
            .assertThatExistAnEntryWithFields(mutableMapOf("USER" to "user", "PASS" to "nopass")) }
            .isInstanceOf(AssertionFailedError::class.java)
            .hasMessage("Cannot find a row with given values")
    }

    @Test
    fun `should find a response inside the resultset`() {
        val sut = E2EDBResponse(listOf(mapOf("A" to 11, "B" to 2), mapOf("A" to 3, "B" to 4)))

        sut.assertThatExistAnEntryWithFields(mapOf("a" to 3))
    }

    @Test
    fun `should remove all data in table`() {
        E2EDB("jdbc:sqlite:sample.db")
            .givenEmptyTable("users")
            .doQuery("select * from users")
            .assertThatNumberOfResults(0)
    }
}
