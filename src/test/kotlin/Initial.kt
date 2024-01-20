import org.junit.jupiter.api.BeforeAll

class Initial {
    companion object {
        @JvmStatic
        @BeforeAll
        fun setDbTestMod() {
            Conf.getConfig().setBool("DB_TEST_MOD", true)
        }
    }
}