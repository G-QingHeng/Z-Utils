package qh.z.utils

import java.io.File

/**
 * Created by qingheng on 2018-8-24 024.
 */
class Cpu {
    val temperature: Double

    init {
        temperature = try {
            File("/sys/class/thermal/")
                    .listFiles{ pathName -> pathName.isDirectory
                            && pathName.name.startsWith("thermal_zone")}
                    .mapNotNull { File(it, "temp").bufferedReader().use { it.readLine().toIntOrNull() } }
                    .average() / 1000.0
        } catch (e: Exception) {
            e.printStackTrace()
            0.0
        }
    }
}