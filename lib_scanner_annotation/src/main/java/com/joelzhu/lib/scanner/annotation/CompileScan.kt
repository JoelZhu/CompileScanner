package com.joelzhu.lib.scanner.annotation

/**
 * The annotation which declare the class should been scanned at the progress of compiling.
 *
 * @param tag      TAG of annotated classes, accessor will classified by this TAG.
 * @param priority The priority of annotated classes, index of each class will sorted by the number
 *                 which defined here. The priority should defined from 0 to positive, for example,
 *                 0 will stay above 1.
 *                 Default priority is the lowest.
 *
 * @author JoelZhu
 * @since 2023-01-17
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class CompileScan(val tag: String = "", val priority: Int = Int.MAX_VALUE)