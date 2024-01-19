import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow

open class ClientServerTest(private val autoConn : Boolean = false) {
    protected var server: Server?  = null
    protected var client: FakeClient? = null

    @BeforeEach
    fun setUp() {
        server = Server()
        client = FakeClient()
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