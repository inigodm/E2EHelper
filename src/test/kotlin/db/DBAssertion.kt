package db

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
    }

    @BeforeEach
    fun firstly() {
        DBSetup("jdbc:sqlite:sample.db")
            .givenEmptyTable("users")
            .given("""
                insert into users values ('user', 'pass', 'salt');
                insert into users values ('user2', 'pass2', 'salt2')
            """)
    }

    @Test
    fun `should retrieve the appropriate number of coincidences`() {
        DBSetup("jdbc:sqlite:sample.db")
            .`when`("select * from users")
            .assertThatNumberOfCoincidences(2)
    }

    @Test
    fun `should throw an exception when number of responses unexpected`() {
        Assertions.assertThatThrownBy {  DBSetup("jdbc:sqlite:sample.db")
            .`when`("select * from users")
            .assertThatNumberOfCoincidences(42) }
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
        val sut = DBResponse(listOf(mapOf("a" to "1", "b" to "2"), mapOf("a" to "3", "b" to "4")))

        sut.assertThatExistAEntryWithFields(mapOf("a" to "3"))
    }

    @Test
    fun `should remove all data in table`() {
        DBSetup("jdbc:sqlite:sample.db")
            .givenEmptyTable("users")
            .`when`("select * from users")
            .assertThatNumberOfCoincidences(0)
    }
}
