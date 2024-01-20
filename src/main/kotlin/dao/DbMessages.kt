package dao

import models.Message
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class DbMessages {
    companion object : DBTable<Message> {
        override fun all(): ArrayList<Message> =
            convert(DbContext.getConn().createStatement().executeQuery("SELECT * FROM messages"))

        override fun findId(id : Int): Message? {
            // Create statement
            val preparedStatement = DbContext.getConn()
                .prepareStatement("SELECT * FROM messages WHERE id=?")
            preparedStatement.setInt(1, id)

            // Get messages found with id
            val message = convert(preparedStatement.executeQuery())

            // Return and check if a message was found
            return if (message.size == 0) null; else message[0]
        }

        override fun delete(element: Message): Int {
            if (element.id == null) return 0
            val preparedStatement = DbContext.getConn()
                .prepareStatement("UPDATE messages SET deleted_at=CURRENT_TIMESTAMP WHERE id=?")
            preparedStatement.setInt(1, element.id)
            return preparedStatement.executeUpdate()
        }

        override fun forceDelete(element: Message): Int {
            if(element.id == null) return 0
            val preparedStatement = DbContext.getConn()
                .prepareStatement("DELETE FROM messages WHERE id=?")
            preparedStatement.setInt(1, element.id)
            return preparedStatement.executeUpdate()
        }

        override fun insert(element: Message): Message? {
            // Create statement
            val preparedStatement = DbContext.getConn()
                .prepareStatement("INSERT INTO messages (content, user_id) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)

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
                // Update message object
                message = element.copy(id = generatedId)
            }

            return message
        }

        override fun update(element: Message): Message? {
            if(element.id == null) return null

            try {
                update(element.id, element.content)
                return findId(element.id)
            } catch(e : SQLException) {
                e.printStackTrace()
                return null
            }
        }

        override fun convert(rs : ResultSet): ArrayList<Message> {
            val array = arrayListOf<Message>()
            var message: Message

            while (rs.next()) {
                message = Message(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDate(4), rs.getDate(5))
                array.add(message)
            }

            return array
        }


        /**
         * Creates a message by providing its content and its author.
         *
         * @param content - The content of the new message.
         * @param userId - The ID of the author of the new message.
         * @return The message with its generated ID or null if an error occurs.
         */
        fun create(content: String, userId: Int): Message? =
            try {
                // Insert message into the database
                insert(Message(null, content, userId, null, null))
            } catch (e: SQLException) {
                // An error occurs, print the stack trace and return null
                e.printStackTrace()
                null
            }

        /**
         * Updates a message by providing updatable arguments.
         *
         * @param id - The ID of the message you want to update.
         * @param content - The new content for the message.
         * @return The updated message or null if an error occurs.
         */
        fun update(id:Int, content:String): Message? {
            // Create statement
            val preparedStatement = DbContext.getConn()
                .prepareStatement("UPDATE messages SET content=? WHERE id=?", Statement.RETURN_GENERATED_KEYS)

            // Set values in the preparedStatement
            preparedStatement.setString(1, content)
            preparedStatement.setInt(2, id)

            // Execute the update statement and retrieve the updated message
            try {
                preparedStatement.executeUpdate()
                return findId(id)
            } catch (e: SQLException) {
                e.printStackTrace()
                return null
            }
        }

        /**
         * Retrieve messages by a provided author ID.
         *
         * @param userId - The ID of the author of the messages.
         * @return An ArrayList of messages.
         */
        fun findAuthor(userId: Int): ArrayList<Message> {
            val preparedStatement = DbContext.getConn().prepareStatement("SELECT * FROM messages WHERE user_id=?")
            preparedStatement.setInt(1, userId)
            return convert(preparedStatement.executeQuery())
        }
    }
}