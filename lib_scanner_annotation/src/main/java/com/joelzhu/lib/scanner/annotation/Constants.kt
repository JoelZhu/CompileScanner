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

        const val CLASS_NAME = "CompileScanner"

        @Deprecated(message = "To distinguish different methods, use GET_CLASS_METHOD_NAME instead.")
        const val GETTER_METHOD_NAME = "getAnnotatedClassed"

        const val GET_CLASS_METHOD_NAME = "getAnnotatedClasses"

        const val GET_INSTANCE_METHOD_NAME = "getAnnotatedInstances"
    }
}