package com.joelzhu.lib.scanner.plugin

import com.android.build.gradle.AppExtension
import com.joelzhu.lib.scanner.plugin.core.ScannerTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * [Description here].
 *
 * @author JoelZhu
 * @since 2023-03-30
 */
class ScannerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.findByType(AppExtension::class.java)
        extension?.registerTransform(ScannerTransform())
    }
}