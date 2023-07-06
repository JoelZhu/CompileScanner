package com.joelzhu.lib.scanner.plugin.code.visitor.scan

import com.joelzhu.lib.scanner.annotation.CompileScan
import com.joelzhu.lib.scanner.plugin.util.JAVA_NAME_SEPARATOR
import com.joelzhu.lib.scanner.plugin.util.JAVA_SEPARATOR
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
class ScanAnnotatedClassVisitor(classVisitor: ClassVisitor) : ClassVisitor(Opcodes.ASM8, classVisitor) {
    private companion object {
        val ANNOTATION_DESCRIPTOR = "L${CompileScan::class.java.name.replace(JAVA_NAME_SEPARATOR, JAVA_SEPARATOR)};"
    }

    private var className: String? = null

    override fun visit(version: Int, access: Int, name: String?, signature: String?,
        superName: String?, interfaces: Array<out String>?) {
        super.visit(version, access, name, signature, superName, interfaces)
        name?.let { className ->
            this.className = className
        }
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        LogUtil.printDebug("Find annotation, class: $className, descriptor: $descriptor.")
        if (ANNOTATION_DESCRIPTOR == descriptor) {
            LogUtil.printInfo("Find annotation, class: $className.")
            return ScannedAnnotationVisitor(className!!)
        }
        return super.visitAnnotation(descriptor, visible)
    }
}