//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package properties

import properties.PropertyLoader
import java.io.IOException
import java.util.*

class PropertyLoader {
    private var fileName = "application"
    private var env: String? = null

    fun withFileName(fileName: String): PropertyLoader {
        this.fileName = fileName
        return this
    }

    fun withEnv(env: String?): PropertyLoader {
        this.env = env
        return this
    }

    fun withEnvSystemProperty(envKey: String?): PropertyLoader {
        env = System.getProperty(envKey)
        return this
    }

    fun load(): Properties {
        val properties = Properties()
        this.load(properties, fileName + ".properties")
        if (!this.isEmpty(env)) {
            this.load(properties, fileName + "-" + env + ".properties")
        }
        properties.putAll(System.getProperties())
        return properties
    }

    private fun load(properties: Properties, resourcePath: String) {
        val resourceStream = this.javaClass.classLoader.getResourceAsStream(resourcePath)
        if (resourceStream != null) {
            try {
                properties.load(resourceStream)
            } catch (e: IOException) {
                e.printStackTrace()
                throw e
            }
        }
    }

    private fun isEmpty(string: String?): Boolean {
        return string == null || string.length == 0
    }
}
