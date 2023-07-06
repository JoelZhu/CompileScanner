package com.joelzhu.lib.scanner.annotation

import kotlin.reflect.KClass

/**
 * The annotation which declare the class should been scanned at the progress of compiling.
 *
 * @param category Category of annotated classes, Scanner will classified by this TAG.
 * @param priority The priority of annotated classes, index of each class will sorted by the number which defined here.
 * The priority should defined from 0 to positive, for example, 0 will stay above 1. Default priority is the lowest.
 * @param isDefault Mark the current one is default. The difference between true and false is: if you have two classes
 * with the same tag, one is default, the other is not, you will only got the non-default one.
 * @param extension Support developer to customize annotation if [CompileScan][CompileScan] is not convenient for you to use.
 *
 * @author JoelZhu
 * @since 2023-01-17
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CompileScan(
    val category: Int = 0,

    val priority: Int = Int.MAX_VALUE,

    val isDefault: Boolean = false,

    val extension: KClass<out Annotation> = DefaultAnnotation::class,

    @Deprecated("Use category instead.")
    val tag: String = ""
)