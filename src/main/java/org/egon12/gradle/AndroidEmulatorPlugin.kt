package org.egon12.gradle_plugin

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import java.io.File

class AndroidEmulatorPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        if (!target.hasProperty("emulatorName")) {
            throw GradleException("You must provide name of the emulator. try to add -PnameOfEmulator")
        }

        val android = try {
            target.extensions.getByName("android")
        } catch (e: Exception) {
            throw GradleException("Cannot found android, please store EmulatorPlugin below android plugin", e)
        }

        if (android.javaClass.name != "com.android.build.gradle.internal.dsl.BaseAppModuleExtension_Decorated") {
            throw GradleException("Wrong type of android plugin. Need BaseAppModuleExtension_Decorated but got ${android.javaClass.name})")
        }

        val getDirectoryMethod = try {
            android.javaClass.getMethod("getSdkDirectory")
        } catch (e: NoSuchMethodException) {
            throw GradleException("Wrong type of android plugin, the last know android plugin works is 3.5.0")
        }

        val sdkDirectory: File = getDirectoryMethod(android) as File

        val emulator = sdkDirectory.path + "/emulator/emulator"

        val detach = "> /dev/null 2> /dev/null < /dev/null &"

        val emulatorName = target.property("emulatorName") as String

        target.tasks.create("emulator", Exec::class.java) {
            commandLine(
                "sh",
                "-c",
                "$emulator $emulatorName $detach"
            )
            doLast {
                println(target.properties)
            }
        }
    }

}
