import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.provideDelegate

val Project.versions: Versions
    get() {
        var versions: Versions? by rootProject.extra
        return versions ?: Versions(rootProject).also { versions = it }
    }

