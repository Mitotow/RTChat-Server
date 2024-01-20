package controllers

import Server
import io.github.oshai.kotlinlogging.KotlinLogging
import models.Client
import java.io.PrintWriter
import java.net.Socket

abstract class Controller(val cmd:String, clazz: Class<out Controller>) {
    protected val logger = KotlinLogging.logger(clazz.name)
    abstract fun execute(server:Server, client: Client, args:List<String>)
}