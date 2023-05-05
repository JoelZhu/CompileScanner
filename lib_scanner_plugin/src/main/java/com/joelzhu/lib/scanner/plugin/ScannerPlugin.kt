package com.joelzhu.lib.scanner.plugin

import com.android.build.gradle.AppExtension
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
    override fun apply(project: Project) {
        val container = project.extensions
        container.create("Scanner", ExtensionConfig::class.java)
        container.findByType(AppExtension::class.java)?.registerTransform(ScannerTransform())

        project.afterEvaluate {
            val extension = project.extensions.findByType(ExtensionConfig::class.java)
            val isEnableLogPrint = extension != null && extension.enableLog
            val logPrintResultContent = if (isEnableLogPrint) {
                "enabled."
            } else {
                "disabled."
            }
            LogUtil.adjustLogPrintPrivacy(isEnableLogPrint)
            println("Scanner's log print $logPrintResultContent.")
        }
    }
}