import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.EOFException
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

class Server {
    // Default value to dev only (has to be moved in a config file)
    private val port = Conf.getConfig().getInt("port")

    // Socket declaration
    private val socket = ServerSocket(port)
    init {
        println("Server connected on port : ${socket.localPort}")
    }

    /*
    * Used to accept connection from a client
    */
    fun accept() {
        val sock = socket.accept()
        println("client connected with address ${sock.remoteSocketAddress}")
        Thread { handleClient(sock) }.start()
    }

    /**
     * Used to disconnect a client
     */
    fun disconnect(sock: Socket) {
        println("client disconnected with address ${sock.remoteSocketAddress}")
        sock.close()
    }

    fun stop() {
        socket.close()
    }

    /**
     * Used to treat client's requests
     */
    private fun handleClient(sock:Socket) {
        val reader = BufferedReader(InputStreamReader(sock.getInputStream()))
        val writer = PrintWriter(sock.getOutputStream(), true)
        while(sock.isConnected) {
            val read = reader.readLine()
            if (read == "ping") {
                writer.println("pong")
            }
        }
        disconnect(sock)
    }
}