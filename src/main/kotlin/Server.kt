import io.github.oshai.kotlinlogging.KotlinLogging
import managers.ControllersManager
import models.Client
import java.net.ServerSocket

class Server {
    // Default value to dev only (has to be moved in a config file)
    private val port = Conf.getConfig().getInt("PORT")

    // Socket declaration
    private val socket = ServerSocket(port)

    // Logger
    private val logger = KotlinLogging.logger {}

    // Connected clients
    private val clients = hashSetOf<Client>()

    init {
        logger.info { "Server listening on port $port." }
    }

    /*
    * Used to accept connection from a client
    */
    fun accept() {
        // Create a client object that will be added to the connected clients HashSet
        val sock = socket.accept()
        val client = Client(sock)
        clients.add(client)
        logger.debug { "Client connected, address ${sock.remoteSocketAddress}." }

        // Start a new thread to handle the client connection asynchronously.
        Thread { handleClient(client) }.start()
    }

    fun stop() {
        socket.close()
    }

    fun broadcast(vararg args : String) {
        clients.forEach {
            it.write(args.joinToString(";"))
        }
    }

    /**
     * Used to disconnect a client
     */
    private fun disconnect(client: Client) {
        // Remove client from the connected clients HashSet and close the connection
        clients.remove(client)
        logger.debug { "Client disconnected, address ${client.sock.remoteSocketAddress}." }
        client.sock.close()
    }

    /**
     * Used to treat client's requests
     */
    private fun handleClient(client: Client) {
        while(client.sock.isConnected) {
            val read = client.reader.readLine()
            if(read != null) {
                // Avoid NullPointerException when the end of stream has been reached
                val args = read.split(";")

                // Find the controller corresponding to the command and execute the associated function.
                ControllersManager.controllers[args[0]]?.execute(this, client, args)
            }
        }
        disconnect(client)
    }
}