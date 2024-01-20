package server.commands

import ClientServerTest
import DbTest
import dao.DbMessages
import dao.DbUsers
import models.Message
import models.User
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SendCommandTest: ClientServerTest(true) {
    @Test
    fun sendSimpleMessageTest() {
        // Set message content, create user and send the message to server
        val messageContent = "HelloWorld!"
        val user = DbUsers.create("TestUser")
        client!!.write("send;${user?.id!!};$messageContent")

        // Wait for thread to insert message in db
        Thread.sleep(1000)

        // Check if the message has been stored in the database
        val message = assertDoesNotThrow("") { DbMessages.findAuthor(user.id!!)[0] }
        assertNotNull(message, "")

        // Check the content of the message
        assertEquals(messageContent, message.content, "")

        // Delete the test message
        message.forceDelete()
        // Delete the test user
        user.forceDelete()
    }

    @Test
    fun sendMessageWithSemiColonTest() {
        // Set message content, create user and send the message to server
        val messageContent = "Hello;World!"
        val user = DbUsers.create("TestUser2")
        client!!.write("send;${user?.id!!};$messageContent")

        // Wait for thread to insert message in db
        Thread.sleep(1000)

        // Check if the message has been stored in the database
        val message = assertDoesNotThrow("") { DbMessages.findAuthor(user.id!!)[0] }
        assertNotNull(message, "")

        // Check the content of the message
        assertEquals(messageContent, message.content, "")

        // Delete the test message
        message.forceDelete()
        // Delete the test user
        user.forceDelete()
    }

    @Test
    fun sendEmptyMessageTest() {
        // Set message content, create user and send the message to server
        val messageContent = ""
        val user = DbUsers.create("TestUser3")
        client!!.write("send;${user?.id!!};$messageContent")

        // Wait for thread to insert message in db
        Thread.sleep(1000)

        // Check if the message has been stored in the database
        assertEquals(0, DbMessages.findAuthor(user.id!!).size, "")

        // Delete the test user
        user.forceDelete()
    }

    @Test
    fun sendOnlyOneSemiColonTest() {
        // Set message content, create user and send the message to server
        val messageContent = ";"
        val user = DbUsers.create("TestUser4")
        client!!.write("send;${user?.id!!};$messageContent")

        // Wait for thread to insert message in db
        Thread.sleep(1000)

        // Check if the message has been stored in the database
        val message = assertDoesNotThrow("") { DbMessages.findAuthor(user.id!!)[0] }
        assertEquals(messageContent, message.content)

        // Delete the test message
        message.forceDelete()
        // Delete the test user
        user.forceDelete()
    }
}