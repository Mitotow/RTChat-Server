import java.util.Properties

class Conf {
    private val properties = Properties()

    init {
        try {
            properties.load(javaClass.getResourceAsStream("/config.properties"))
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }

    fun get(prop:String): String = properties.getProperty(prop, "")

    fun getInt(prop:String): Int = properties.getProperty(prop, "8089").toInt()

    companion object {
        private var config : Conf? = null

        fun getConfig(): Conf {
            if (config == null) {
                config = Conf()
            }

            return config as Conf
        }
    }
}