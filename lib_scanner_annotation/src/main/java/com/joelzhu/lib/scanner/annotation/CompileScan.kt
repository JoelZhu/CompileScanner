package com.joelzhu.lib.scanner.annotation

/**
 * The annotation which declare the class should been scanned at the progress of compiling.
 *
 * @author JoelZhu
 * @since 2023-01-17
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class CompileScan(val tag: String = "")