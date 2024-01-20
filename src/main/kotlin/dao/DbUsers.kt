package dao

import models.User
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class DbUsers {
    companion object : DBTable<User> {
        override fun all(): ArrayList<User> =
            convert(DbContext.getConn().createStatement().executeQuery("SELECT * FROM users"))

        override fun findId(id : Int): User? {
            // Create statement
            val preparedStatement = DbContext.getConn()
                .prepareStatement("SELECT * FROM users WHERE id=?")
            preparedStatement.setInt(1, id)

            // Get users found with id
            val users = convert(preparedStatement.executeQuery())

            // Return and check if a user was found
            return if (users.size == 0) null; else users[0]
        }

        override fun delete(element: User): Int {
            // Create a callable statement and pass in parameter of delete_user procedure username of the targeted user
            val cstmt = DbContext.getConn().prepareCall("{CALL delete_user(?)}")
            cstmt.setString(1, element.username)

            // Execute the statement and store returned value and close the callable statement
            val rows = try {
                cstmt.executeUpdate()
            } catch (e:SQLException) {
                e.printStackTrace()
                0
            }
            cstmt.close()

            return rows
        }

        override fun forceDelete(element: User): Int {
            if (element.id == null) return 0
            val preparedStatement = DbContext.getConn().prepareStatement("DELETE FROM users WHERE id=?")
            preparedStatement.setInt(1, element.id)
            return preparedStatement.executeUpdate()
        }

        /**
         * Finds a user by their username.
         *
         * @param username - Username of the targeted user.
         * @return The found user or null if not found.
         */
        fun findUsername(username: String): User? {
            // Create statement
            val preparedStatement = DbContext.getConn()
                .prepareStatement("SELECT * FROM users WHERE username=?")
            preparedStatement.setString(1, username)
            // Get users found with id
            val users = convert(preparedStatement.executeQuery())
            // Return and check if a user was found
            return if (users.size == 0) null; else users[0]
        }

        override fun insert(element: User): User? {
            // Create statement
            val preparedStatement = DbContext.getConn()
                .prepareStatement("INSERT INTO users (username, ip_addr, token) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)

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
            val preparedStatement = DbContext.getConn()
                .prepareStatement("UPDATE users SET username=?, ip_addr=?, token=? WHERE id=?", Statement.RETURN_GENERATED_KEYS)

            // Insert values in preparedStatement
            preparedStatement.setString(1, element.username)
            preparedStatement.setString(2, element.addr)
            preparedStatement.setString(3, element.token)
            preparedStatement.setInt(4, element.id)

            // Execute statement
            preparedStatement.executeUpdate()

            return element
        }

        override fun convert(rs : ResultSet): ArrayList<User> {
            val array = arrayListOf<User>()
            var user: User

            while (rs.next()) {
                user = User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDate(5), rs.getDate(6))
                array.add(user)
            }

            return array
        }

        /**
         * Creates a user by providing a username and optionally an IP address.
         *
         * @param username - The new user's username.
         * @param addr - The new user's IP address (optional).
         * @return The user with its generated ID or null if the username is already in use or if an error occurs.
         */
        fun create(username: String,
                   addr: String? = null): User? =
            try {
                // Insert user into database
                insert(User(null, username, addr, null, null, null))
            } catch(e : SQLException) {
                // Error when inserting user (e.g., if the ID already exists)
                e.printStackTrace()
                null
            }

        /**
         * Updates a user by providing updatable arguments.
         *
         * @param id - The ID of the user you want to update.
         * @param username - Optional parameter to update the username.
         * @param addr - Optional parameter to update the user's IP address.
         * @param token - Optional parameter to update the user's token.
         * @return The updated user or null if an error occurs.
         */
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