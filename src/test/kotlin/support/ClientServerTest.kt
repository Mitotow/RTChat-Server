package support

import Server
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow

open class ClientServerTest(private val autoConn : Boolean = false): DbTest() {
    protected var server: Server?  = null
    protected var client: FakeClient? = null

    private val port = config.getInt("PORT")

    @BeforeEach
    fun setUp() {
        server = Server()
        client = FakeClient("127.0.0.1", port)
        if (autoConn) {
            assertDoesNotThrow {
                client!!.connect()
                server!!.accept()
            }
        }
    }

    @AfterEach
    fun setDown() {
        if (autoConn) {
            client!!.disconnect()
        }
        server!!.stop()
    }
}