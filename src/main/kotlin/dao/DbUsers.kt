package dao

import models.User
import java.sql.ResultSet
import java.sql.Statement

class DbUsers {
    companion object : DBTable<User> {
        override fun all(): ArrayList<User> = convertToUser(DbContext.getConn().createStatement().executeQuery("SELECT * FROM users"))

        override fun findId(id : Int): User? {
            // Create statement
            val preparedStatement = DbContext.getConn().prepareStatement("SELECT * FROM users WHERE id=?")
            preparedStatement.setInt(1, id)
            // Get users found with id
            val users = convertToUser(preparedStatement.executeQuery())
            // Return and check if a user was found
            return if (users.size == 0) null; else users[0]
        }

        override fun insert(element: User): User? {
            // Create statement
            val preparedStatement = DbContext.getConn().prepareStatement("INSERT INTO users (username, ip_addr, token) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)
            // Insert values in preparedStatement
            preparedStatement.setString(1, element.username)
            preparedStatement.setString(2, element.addr.toString())
            preparedStatement.setString(3, element.token)
            // Execute statement
            preparedStatement.executeUpdate()

            // Get the generated ID
            val generatedKeys = preparedStatement.generatedKeys
            var user : User? = null
            if (generatedKeys.next()) {
                val generatedId = generatedKeys.getInt(1)
                // Update user object
                user = element.copy(id = generatedId)
            }

            return user
        }

        private fun convertToUser(rs : ResultSet): ArrayList<User> {
            val array = arrayListOf<User>()
            var user: User
            while (rs.next()) {
                user = User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4))
                array.add(user)
            }

            return array
        }
    }
}