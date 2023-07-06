package com.joelzhu.lib.scanner.plugin.extension

import com.joelzhu.lib.scanner.plugin.ScannerPlugin

/**
 * Configuration allow developers to have custom settings in ``build.gradle``.
 *
 * @author JoelZhu
 * @since 2023-04-08
 */
open class ExtensionConfig {
    // Is enable log print.
    open var enableLog: Boolean = ScannerPlugin.DEFAULT_ENABLE_LOG

    // Is priority works in all annotated classes.
    open var globalPriority: Boolean = ScannerPlugin.DEFAULT_GLOBAL_PRIORITY

    // Is extension of annotation will be used.
    open var useExtension: Boolean = ScannerPlugin.DEFAULT_USE_EXTENSION
}