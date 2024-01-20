import java.util.Properties

class Conf {
    private val properties = Properties()

    init {
        refresh()
    }

    private fun refresh() {
        try {
            properties.load(javaClass.getResourceAsStream("/config.properties"))
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }

    fun get(prop:String): String = properties.getProperty(prop, "")

    fun getBool(prop:String): Boolean = properties.getProperty(prop, "false").toBoolean()

    fun getInt(prop:String): Int = properties.getProperty(prop, "8089").toInt()

    fun set(prop:String, value:String) {
        properties.setProperty(prop, value)
        refresh()
    }

    fun setBool(prop:String, value:Boolean) = set(prop, value.toString())

    fun setInt(prop:String, value:Int) = set(prop, value.toString())

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