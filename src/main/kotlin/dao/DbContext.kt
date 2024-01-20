package dao

import Conf
import java.sql.Connection
import java.sql.DriverManager

class DbContext() {
    companion object {
        val isTestMod : Boolean = Conf.getConfig().getBool("DB_TEST_MOD")
        var connection : Connection? = null

        fun getConn(): Connection {
            if(connection == null) {
                val config = Conf.getConfig()
                connection = if(!isTestMod) {
                    DriverManager.getConnection("jdbc:mysql://${config.get("DB_HOST")}:${config.get("DB_PORT")}/${config.get("DB_NAME")}", config.get("DB_USER"), config.get("DB_PASSWORD"))
                } else {
                    DriverManager.getConnection("jdbc:mysql://${config.get("DB_TEST_HOST")}:${config.get("DB_TEST_PORT")}/${config.get("DB_TEST_NAME")}", config.get("DB_TEST_USER"), config.get("DB_TEST_PASSWORD"))
                }
            }
            return connection!!
        }
    }
}