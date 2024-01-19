package dao

import Conf
import java.sql.Connection
import java.sql.DriverManager

class DbContext {
    companion object {
        var connection : Connection? = null

        fun getConn(): Connection {
            if(connection == null) {
                val config = Conf.getConfig()
                connection = DriverManager.getConnection("jdbc:mysql://${config.get("DB_HOST")}:${config.get("DB_PORT")}/${config.get("DB_NAME")}", config.get("DB_USER"), config.get("DB_PASSWORD"))
            }
            return connection!!
        }
    }
}