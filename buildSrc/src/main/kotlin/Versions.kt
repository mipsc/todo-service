//object Versions {
//    const val springBoot = "3.4.0"
//    const val springDependencyManagementPlugin = "1.1.6"
//    const val jib = "3.4.4"
//    const val mapstruct = "1.6.3"
//    const val mapstructLombokBinding = "0.2.0"
//    const val lombok = "1.18.36"
//    const val testcontainers = "1.20.4"
//    const val mockito = "5.14.2"
//    const val junit = "5.11.3"
//}

import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra

class Versions(private val project: Project) {

    operator fun get(name: String) = project.extra.get("$name.version") as String

}