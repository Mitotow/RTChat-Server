package database

import support.DbTest
import dao.DbUsers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertDoesNotThrow

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class DbUserTest: DbTest() {
    @Test
    fun testValidUserCreation() {
        assertDoesNotThrow {
            val user = DbUsers.create("TestUser")
            user!!.forceDelete()
        }
    }
}