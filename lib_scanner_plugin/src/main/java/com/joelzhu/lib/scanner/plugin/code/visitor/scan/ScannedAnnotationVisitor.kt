package com.joelzhu.lib.scanner.plugin.code.visitor.scan

import com.joelzhu.lib.scanner.annotation.CompileScan
import com.joelzhu.lib.scanner.annotation.internal.BeanConstants
import com.joelzhu.lib.scanner.annotation.internal.CompileScanBean
import com.joelzhu.lib.scanner.plugin.code.cache.CacheHandler
import com.joelzhu.lib.scanner.plugin.util.LogUtil
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Opcodes
import kotlin.reflect.KClass

/**
 * AnnotationVisitor for scanning information specified by developer in annotation [CompileScan][com.joelzhu.lib.scanner.annotation.CompileScan]
 *
 * @author JoelZhu
 * @since 2023-03-30
 */
class ScannedAnnotationVisitor(className: String) : AnnotationVisitor(Opcodes.ASM8) {
    private var className: String

    private var category: Int = BeanConstants.DEFAULT_CATEGORY

    private var isDefault: Boolean = BeanConstants.DEFAULT_IS_DEFAULT

    private var priority: Int = BeanConstants.DEFAULT_PRIORITY

    private var extensionClass: KClass<*>? = null

    init {
        this.className = className
    }

    override fun visit(name: String?, value: Any?) {
        super.visit(name, value)
        LogUtil.printDebug("Visiting annotation's parameter, name: $name, value: $value.")
        when (name) {
            CompileScan::category.name -> category = value as Int
            CompileScan::isDefault.name -> isDefault = value as Boolean
            CompileScan::priority.name -> priority = value as Int
            CompileScan::extension.name -> extensionClass = value as KClass<*>?
            else -> {}
        }
    }

    override fun visitEnd() {
        super.visitEnd()

        val bean = extensionClass?.let {
            CompileScanBean(className, category, isDefault, priority, it::class.java.name)
        } ?: run {
            CompileScanBean(className, category, isDefault, priority)
        }
        LogUtil.printInfo("Annotation information: $bean.")
        CacheHandler.cacheScannedClass(bean)
    }
}