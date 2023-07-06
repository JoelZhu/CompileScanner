package com.joelzhu.lib.scanner.plugin.util

/**
 * Constants of each names named in ```ScannerImpl.class```, include: package, class, getters.
 *
 * @author JoelZhu
 * @since 2023-04-07
 */
object ImplConstants {
    const val IMPL_CLASS = "com${JAVA_SEPARATOR}joelzhu${JAVA_SEPARATOR}lib${JAVA_SEPARATOR}scanner${JAVA_SEPARATOR}" +
            "runtime${JAVA_SEPARATOR}core${JAVA_SEPARATOR}ScannerImpl"

    const val CLASS_GET = "getClasses"

    const val CLASS_GET_PARAM_TAG = "tags"

    const val CLASS_GET_PARAM_DEFAULT = "withDefault"
}