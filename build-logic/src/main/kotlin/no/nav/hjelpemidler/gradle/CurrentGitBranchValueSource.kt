package no.nav.hjelpemidler.gradle

import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import org.gradle.process.ExecOperations
import java.io.ByteArrayOutputStream
import javax.inject.Inject

abstract class CurrentGitBranchValueSource : ValueSource<String, ValueSourceParameters.None> {
    @get:Inject
    abstract val execOperations: ExecOperations

    override fun obtain(): String? {
        val output = ByteArrayOutputStream()
        val result = execOperations.exec {
            commandLine("git", "rev-parse", "--abbrev-ref", "HEAD")
            isIgnoreExitValue = true
            standardOutput = output
        }
        if (result.exitValue != 0) return null
        return output
            .toString(Charsets.UTF_8)
            .trim()
            .takeUnless { it.isEmpty() || it == "HEAD" }
    }
}
