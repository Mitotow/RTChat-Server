package models

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class Client(val sock: Socket) {
    val reader = BufferedReader(InputStreamReader(sock.getInputStream()))
    val writer = PrintWriter(sock.getOutputStream(), true)

    fun write(vararg args: String) {
        writer.println(args.joinToString(";"))
    }
}