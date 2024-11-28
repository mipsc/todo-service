import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra

class Versions(private val project: Project) {

    operator fun get(name: String) = project.extra.get("$name.version") as String

}