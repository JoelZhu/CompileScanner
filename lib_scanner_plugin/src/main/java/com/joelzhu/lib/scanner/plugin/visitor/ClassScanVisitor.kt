package com.joelzhu.lib.scanner.plugin.visitor

import com.joelzhu.lib.scanner.annotation.CompileScan
import com.joelzhu.lib.scanner.plugin.util.Constants
import com.joelzhu.lib.scanner.plugin.util.LogUtil
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

/**
 * [Description here].
 *
 * @author JoelZhu
 * @since 2023-03-30
 */
class ClassScanVisitor(classVisitor: ClassVisitor) : ClassVisitor(Opcodes.ASM8, classVisitor) {
    private companion object {
        val ANNOTATION_DESCRIPTOR =
            "L${CompileScan::class.java.name.replace(".", Constants.JAVA_SEPARATOR)};"
    }

    private var className: String? = null

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        LogUtil.printLog("Scanning class: $name.")
        name?.let { className ->
            this.className = className
        }
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        LogUtil.printLog("Find annotation, descriptor: $descriptor, visible: $visible.")
        if (ANNOTATION_DESCRIPTOR == descriptor) {
            return AnnotationScanVisitor(className!!)
        }
        return super.visitAnnotation(descriptor, visible)
    }
}