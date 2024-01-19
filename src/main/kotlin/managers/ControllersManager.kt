package managers

import controllers.Controller
import java.io.File
import java.lang.reflect.Modifier

class ControllersManager {
    companion object {
        val controllers: Map<String, Controller> = loadControllers()

        private fun loadControllers(): Map<String, Controller> {
            val controllersMap = mutableMapOf<String, Controller>()
            val controllersPackage = "controllers"  // Remplacez par le nom de votre package

            val controllersFolder = File("src/main/kotlin/$controllersPackage")
            val controllerFiles = controllersFolder.listFiles { file ->
                file.isFile && file.name.endsWith("Controller.kt")
            }

            controllerFiles?.forEach { file ->
                val className = "${controllersPackage}.${file.nameWithoutExtension}"
                try {
                    val controllerClass = Class.forName(className)
                    if (Controller::class.java.isAssignableFrom(controllerClass) &&
                        !Modifier.isAbstract(controllerClass.modifiers)
                    ) {
                        val constructor = controllerClass.getDeclaredConstructor()
                        constructor.isAccessible = true
                        val controllerInstance = constructor.newInstance() as Controller
                        controllersMap[controllerInstance.cmd] = controllerInstance
                    }
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                }
            }

            return controllersMap
        }

    }
}