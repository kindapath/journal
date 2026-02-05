import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import java.util.Properties

abstract class GeneratePropertiesConfigTask : DefaultTask() {

    @get:InputFile
    @get:PathSensitive(PathSensitivity.NONE)
    abstract val propertiesFile: RegularFileProperty

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @get:Input
    abstract val packageName: Property<String>

    @get:Input
    abstract val objectName: Property<String>

    @get:Input
    abstract val fieldKeys: MapProperty<String, String>

    @get:Input
    abstract val fieldDefaults: MapProperty<String, String>

    @get:Input
    abstract val fieldBooleans: SetProperty<String>

    fun stringField(name: String, key: String, default: String? = null) {
        fieldKeys.put(name, key)
        if (default != null) fieldDefaults.put(name, default)
    }

    fun booleanField(name: String, key: String, default: String = "false") {
        fieldKeys.put(name, key)
        fieldDefaults.put(name, default)
        fieldBooleans.add(name)
    }

    @TaskAction
    fun generate() {
        val file = propertiesFile.asFile.get()
        val props = Properties().apply {
            file.inputStream().use { stream -> load(stream) }
        }

        val fileName = file.name
        val defaults = fieldDefaults.get()
        val booleans = fieldBooleans.get()

        val body = fieldKeys.get().entries.joinToString("\n") { (constName, propKey) ->
            val value = props.getProperty(propKey)
                ?: defaults[constName]
                ?: error("Missing required '$propKey' in $fileName")

            val literal = if (constName in booleans) value else "\"$value\""
            "const val $constName = $literal"
        }

        val pkg = packageName.get()
        val obj = objectName.get()

        val packageDir = outputDirectory.get().asFile.resolve(pkg.replace('.', '/'))
        packageDir.mkdirs()
        packageDir.resolve("$obj.kt").writeText(buildString {
            appendLine("package $pkg")
            appendLine()
            appendLine("object $obj {")
            appendLine(body)
            appendLine("}")
            appendLine()
        })
    }
}
