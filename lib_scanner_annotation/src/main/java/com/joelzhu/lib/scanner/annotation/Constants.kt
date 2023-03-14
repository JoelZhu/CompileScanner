package com.joelzhu.lib.scanner.annotation

/**
 * Constants for library.
 *
 * @author JoelZhu
 * @since 2023-01-26
 */
class Constants {
    companion object {
        const val CLASS_PACKAGE = "com.joelzhu.lib.scanner"

        const val CLASS_NAME = "JoelCompileScanner"

        const val GET_CLASS_METHOD_NAME = "getAnnotatedClasses"

        const val GET_INSTANCE_METHOD_NAME = "getAnnotatedInstances"

        const val OPTION_MODULE_IS_APP = "appModule"

        const val OPTION_APP_WITH_ANNO = "withAnnotation"

        const val OPTION_IS_MULTI_MODULE = "isMultiModule"
    }
}