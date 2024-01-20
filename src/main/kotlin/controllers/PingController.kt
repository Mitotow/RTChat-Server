package controllers

import Server
import models.Client

class PingController: Controller("ping", PingController::class.java) {
    override fun execute(server: Server, client: Client, args: List<String>) {
        client.write("pong")
    }
}