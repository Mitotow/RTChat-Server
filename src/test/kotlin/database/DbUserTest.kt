package database

import support.DbTest
import dao.DbUsers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class DbUserTest: DbTest() {
    @Test
    fun testValidUserCreation() {
        assertDoesNotThrow {
            val user = DbUsers.create("TestUser")
            user!!.forceDelete()
        }
    }
}