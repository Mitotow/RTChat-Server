package dao

import models.Message
import models.User
import java.sql.SQLException
import java.sql.Statement

class DbMessages {
    companion object : DBTable<Message> {
        override fun all(): ArrayList<Message> {
            TODO("Not yet implemented")
        }

        override fun findId(id: Int): Message? {
            TODO("Not yet implemented")
        }

        override fun insert(element: Message): Message? {
            // Create statement
            val preparedStatement = DbContext.getConn().prepareStatement("INSERT INTO messages (content, user_id) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)
            // Insert values in preparedStatement
            preparedStatement.setString(1, element.content)
            preparedStatement.setInt(2, element.user_id)
            // Execute statement
            preparedStatement.executeUpdate()

            // Get the generated ID
            val generatedKeys = preparedStatement.generatedKeys
            var message : Message? = null
            if (generatedKeys.next()) {
                val generatedId = generatedKeys.getInt(1)
                // Update user object
                message = element.copy(id = generatedId)
            }

            return message
        }

        override fun update(element: Message): Message? {
            TODO("Not yet implemented")
        }

        fun create(content:String,
                   userid:Int): Message? {
            var message: Message? = null
            try {
                // Insert user into database
                message = insert(Message(null, content, userid, null, null))
            } catch(e : SQLException) {
                // Error when inserting user (it can be : id already exists)
                e.printStackTrace()
            }
            return message
        }
    }
}