package controllers

import Server
import dao.DbMessages
import models.Client
import java.io.PrintWriter
import java.net.Socket

class SendController: Controller("send", SendController::class.java) {
    override fun execute(server: Server, client: Client, args: List<String>) {
        // Check number of arguments, must be at least 3
        if(args.size < 3) return

        // Retrieve message content
        val messageContent = if (args.size < 4) args[2] else args.subList(2, args.size).joinToString(";")
        if (messageContent == "") return

        // Insert message into the database
        DbMessages.create(messageContent, args[1].toInt())

        logger.info { "New message register from user with id ${args[1]}. Content: $messageContent" }

        // Broadcast the message to every client
        server.broadcast("from", args[1], messageContent)
    }
}