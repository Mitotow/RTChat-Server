package managers

import controllers.Controller
import java.io.File
import java.lang.reflect.Modifier

class ControllersManager {
    companion object {
        val controllers: Map<String, Controller> = loadControllers()

        private fun loadControllers(): Map<String, Controller> {
            val controllersMap = mutableMapOf<String, Controller>()
            val controllersPackage = "controllers"


            // Retrieve controllers and register each controller with its command name
            File("src/main/kotlin/$controllersPackage").listFiles { file ->
                file.isFile && file.name.endsWith("Controller.kt") && file.name != "Controller.kt"
            }?.forEach { file ->
                try {
                    val controllerClass = Class.forName("${controllersPackage}.${file.nameWithoutExtension}")
                    if (Controller::class.java.isAssignableFrom(controllerClass) &&
                        !Modifier.isAbstract(controllerClass.modifiers)) {

                        // Retrieve controller constructor and create an instance of the controller
                        val constructor = controllerClass.getDeclaredConstructor()
                        constructor.isAccessible = true
                        val controllerInstance = constructor.newInstance() as Controller

                        // Register as key the command name and as value the controller instance
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