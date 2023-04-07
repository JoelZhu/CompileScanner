package com.joelzhu.lib.scanner.plugin.visitor

import com.joelzhu.lib.scanner.annotation.CompileScan
import com.joelzhu.lib.scanner.plugin.util.LogUtil
import com.joelzhu.lib.scanner.plugin.util.ScannerCache
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Opcodes

/**
 * [Description here].
 *
 * @author JoelZhu
 * @since 2023-03-30
 */
class AnnotationScanVisitor(private val className: String) : AnnotationVisitor(Opcodes.ASM8) {
    private var tag: String? = null

    private var isDefault: Boolean = false

    private var priority: Int? = null

    override fun visit(name: String?, value: Any?) {
        super.visit(name, value)
        when (name) {
            CompileScan::tag.name -> tag = value as String?
            CompileScan::isDefault.name -> isDefault = value as Boolean
            CompileScan::priority.name -> priority = value as Int?
            else -> {}
        }
    }

    override fun visitEnd() {
        super.visitEnd()
        LogUtil.printLog("Annotation found, class: $className, tag: $tag, isDefault: $isDefault, priority: $priority.")
        if (isDefault) {
            ScannerCache.CACHE.addDefaultClass(tag, className, priority!!)
        } else {
            ScannerCache.CACHE.addNormalClass(tag, className, priority!!)
        }
    }
}