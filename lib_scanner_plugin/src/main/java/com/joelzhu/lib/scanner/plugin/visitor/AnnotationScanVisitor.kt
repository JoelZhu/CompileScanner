package com.joelzhu.lib.scanner.plugin.visitor

import com.joelzhu.lib.scanner.annotation.CompileScan
import com.joelzhu.lib.scanner.plugin.core.CompileScanBean
import com.joelzhu.lib.scanner.plugin.core.ScannerCache
import com.joelzhu.lib.scanner.plugin.util.LogUtil
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Opcodes

/**
 * AnnotationVisitor for scanning information specified by developer in annotation [CompileScan][com.joelzhu.lib.scanner.annotation.CompileScan]
 *
 * @author JoelZhu
 * @since 2023-03-30
 */
class AnnotationScanVisitor(className: String) : AnnotationVisitor(Opcodes.ASM8) {
    private val bean = CompileScanBean(className)

    override fun visit(name: String?, value: Any?) {
        super.visit(name, value)
        when (name) {
            CompileScan::tag.name -> bean.tag = value as String?
            CompileScan::group.name -> bean.group = value as String?
            CompileScan::isDefault.name -> bean.isDefault = value as Boolean
            CompileScan::priority.name -> bean.priority = value as Int
            else -> {}
        }
    }

    override fun visitEnd() {
        super.visitEnd()
        LogUtil.printLog("Annotation found, $bean.")
        ScannerCache.CACHE.addClasses(bean)
    }
}