package server.conn

import support.ClientServerTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals

class ConnectionTest: ClientServerTest() {
    @Test
    fun clientConnectToServerTest() {
        assertDoesNotThrow {
            client!!.connect()
            server!!.accept()
        }
        client!!.disconnect()
    }

    @Test
    fun clientDisconnectFromServerTest() {
        assertDoesNotThrow {
            client!!.connect()
            server!!.accept()
            client!!.disconnect()
        }
    }

    @Test
    fun clientSendPingToServerTest() {
        assertDoesNotThrow {
            client!!.connect()
            server!!.accept()
        }

        var read = ""

        assertDoesNotThrow {
            client!!.write("ping")
            read = client!!.read()
        }

        assertEquals("pong", read.trim(), "Server does not well respond to ping request")

        assertDoesNotThrow {
            client!!.disconnect()
        }
    }
}