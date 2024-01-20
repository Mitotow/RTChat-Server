package support

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class FakeClient(private val addr : String, private val port : Int) {
    private var sock : Socket? = null
    private var writer: PrintWriter? = null
    private var reader: BufferedReader? = null

    fun connect() {
        sock = Socket(addr, port)
        writer = PrintWriter(sock!!.getOutputStream(), true)
        reader = BufferedReader(InputStreamReader(sock!!.getInputStream()))
    }

    fun disconnect() {
        sock!!.close()
        sock = null
    }

    fun write(vararg str:String) {
        val msg = str.joinToString(" ")
        writer!!.println(msg)
    }

    fun read(): String = reader!!.readLine()
}