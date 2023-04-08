package com.joelzhu.lib.scanner.annotation

/**
 * The annotation which declare the class should been scanned at the progress of compiling.
 *
 * @param tag TAG of annotated classes, accessor will classified by this TAG.
 * @param isDefault Mark the current one is default. The difference between true and false is:
 *                  If you have two classes with the same tag, one is default, the other is not,
 *                  you will only got the non-default one.
 * @param priority The priority of annotated classes, index of each class will sorted by the
 *                 number which defined here. The priority should defined from 0 to positive,
 *                 for example, 0 will stay above 1. Default priority is the lowest.
 *
 * @author JoelZhu
 * @since 2023-01-17
 */
@Target(AnnotationTarget.CLASS)
annotation class CompileScan(
    val tag: String = "",
    val isDefault: Boolean = false,
    val priority: Int = Int.MAX_VALUE
)