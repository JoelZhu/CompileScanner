package com.joelzhu.lib.scanner.plugin.code.visitor.scan

import com.android.build.api.transform.JarInput
import com.joelzhu.lib.scanner.annotation.internal.InjectClass
import com.joelzhu.lib.scanner.plugin.core.ScanHelper
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
class InjectPlaceClassVisitor(classVisitor: ClassVisitor, private val jarInput: JarInput) :
    ClassVisitor(Opcodes.ASM8, classVisitor) {
    private companion object {
        val INJECT_CLASS_DESCRIPTOR =
            "L${InjectClass::class.java.name.replace(JAVA_NAME_SEPARATOR, JAVA_SEPARATOR)};"
    }

    private var className: String? = null

    override fun visit(version: Int, access: Int, name: String?, signature: String?,
        superName: String?, interfaces: Array<out String>?) {
        super.visit(version, access, name, signature, superName, interfaces)
        className = name
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        if (INJECT_CLASS_DESCRIPTOR == descriptor) {
            LogUtil.printInfo("Find annotated inject class, descriptor: $descriptor.")
            className?.let {
                ScanHelper.updateInjectClassJarInputName(jarInput, it)
            }
        }
        return super.visitAnnotation(descriptor, visible)
    }
}