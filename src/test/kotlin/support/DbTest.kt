package support

import dao.DbContext
import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.BeforeAll
import org.opentest4j.TestAbortedException

abstract class DbTest: ConfigTest() {
    companion object {
        @JvmStatic
        @BeforeAll
        fun setDbTestMod() {
            val dbName = Conf.getConfig().get("DB_NAME")
            val dbTestName = Conf.getConfig().get("DB_TEST_NAME")
            val logger = KotlinLogging.logger { }

            // Use try catch to print the message, assumeTrue does not print the message given for a reason (?)
            try {
                assumeTrue(dbName != dbTestName)
            } catch(e:TestAbortedException) {
                logger.warn { "You are trying to execute tests with the same DB as your test DB!" }
                throw e
            }

            Conf.getConfig().setBool("DB_TEST_MOD", true)
        }

        @JvmStatic
        @AfterAll
        fun refreshDb() {
            // Delete all from messages
            DbContext.getConn().createStatement().executeUpdate("DELETE FROM messages")
            // Delete all from users
            DbContext.getConn().createStatement().executeUpdate("DELETE FROM users")
        }
    }
}