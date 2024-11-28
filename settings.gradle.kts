rootProject.name = "todo-service"

pluginManagement {
    plugins {
        id("org.springframework.boot").version(settings.extra["springBoot.version"] as String)
        id("io.spring.dependency-management").version(settings.extra["springDependencyManagementPlugin.version"] as String)
        id("com.google.cloud.tools.jib").version(settings.extra["jib.version"] as String)
    }
}
