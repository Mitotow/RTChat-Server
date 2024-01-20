package support

import dao.DbContext
import org.junit.jupiter.api.AfterAll

abstract class DbTest: ConfigTest() {
    companion object {
        @JvmStatic
        @AfterAll
        fun refreshDb() {
            // Delete all from messages
            DbContext.getConn().createStatement().executeUpdate("DELETE FROM messages WHERE TRUE")
            // Delete all from users
            DbContext.getConn().createStatement().executeUpdate("DELETE FROM users WHERE TRUE")
        }
    }
}