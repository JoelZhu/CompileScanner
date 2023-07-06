package com.joelzhu.lib.scanner.plugin

import com.android.build.gradle.AppExtension
import com.joelzhu.lib.scanner.plugin.code.generator.CodeGenerator
import com.joelzhu.lib.scanner.plugin.core.ScannerTransform
import com.joelzhu.lib.scanner.plugin.extension.ExtensionConfig
import com.joelzhu.lib.scanner.plugin.util.LogUtil
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Scanner plugin.
 *
 * @author JoelZhu
 * @since 2023-03-30
 */
class ScannerPlugin : Plugin<Project> {
    internal companion object {
        private const val SCRIPT_CLOSURE = "Scanner"

        const val DEFAULT_ENABLE_LOG = false

        const val DEFAULT_GLOBAL_PRIORITY = false

        const val DEFAULT_USE_EXTENSION = false
    }

    override fun apply(project: Project) {
        val container = project.extensions
        container.create(SCRIPT_CLOSURE, ExtensionConfig::class.java)
        container.findByType(AppExtension::class.java)?.registerTransform(ScannerTransform())

        project.afterEvaluate {
            val extension = project.extensions.findByType(ExtensionConfig::class.java)
            handleLogPrint(extension)
            handleCompilation(extension)
        }
    }

    private fun handleLogPrint(extension: ExtensionConfig?) {
        val isEnableLogPrint = extension?.enableLog ?: run { DEFAULT_ENABLE_LOG }
        LogUtil.adjustLogPrintPrivacy(isEnableLogPrint)
        LogUtil.printImportant("Scanner's log print ${
            if (isEnableLogPrint) {
                "enabled."
            } else {
                "disabled."
            }
        }.")
    }

    private fun handleCompilation(extension: ExtensionConfig?) {
        val globalPriority = extension?.globalPriority ?: run { DEFAULT_GLOBAL_PRIORITY }
        LogUtil.printImportant("Scanner's priority ${
            if (globalPriority) {
                "works on all annotated classes"
            } else {
                "only works in same tag"
            }
        }.")
        val useExtension = extension?.useExtension ?: run { DEFAULT_USE_EXTENSION }
        LogUtil.printImportant("Scanner's extension ${
            if (useExtension) {
                "enabled"
            } else {
                "disabled"
            }
        }.")

        CodeGenerator.initializeGenerator(globalPriority, useExtension)
    }
}