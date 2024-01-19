package dao

import models.User
import java.sql.ResultSet
import java.sql.SQLException
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

        fun findName(name: String): User? {
            // Create statement
            val preparedStatement = DbContext.getConn().prepareStatement("SELECT * FROM users WHERE username=?")
            preparedStatement.setString(1, name)
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
            preparedStatement.setString(2, element.addr)
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

        override fun update(element: User): User? {
            if(element.id == null) return null
            // Create statement
            val preparedStatement = DbContext.getConn().prepareStatement("UPDATE users SET username=?, ip_addr=?, token=? WHERE id=?", Statement.RETURN_GENERATED_KEYS)
            // Insert values in preparedStatement
            preparedStatement.setString(1, element.username)
            preparedStatement.setString(2, element.addr)
            preparedStatement.setString(3, element.token)
            preparedStatement.setInt(4, element.id)
            // Execute statement
            preparedStatement.executeUpdate()
            return element
        }

        private fun convertToUser(rs : ResultSet): ArrayList<User> {
            val array = arrayListOf<User>()
            var user: User
            while (rs.next()) {
                user = User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDate(5), rs.getDate(6))
                array.add(user)
            }

            return array
        }

        fun create(username: String,
                   addr: String? = null): User? {
            var user:User? = null
            try {
                // Insert user into database
                user = insert(User(null, username, addr, null, null, null))
            } catch(e : SQLException) {
                // Error when inserting user (it can be : id already exists)
                e.printStackTrace()
            }
            return user
        }

        fun update(id: Int,
            username: String? = null,
            addr: String? = null,
            token: String? = null): User? {
            val user = findId(id)

            return if(user == null) {
                null
            } else {
                user.username = username ?: user.username
                user.addr = addr ?: user.addr
                user.token = token ?: user.token

                update(user)
            }
        }
    }
}