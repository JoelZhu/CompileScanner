package com.joelzhu.lib.scanner.plugin.visitor

import com.joelzhu.lib.scanner.plugin.util.LogUtil
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * [Description here].
 *
 * @author JoelZhu
 * @since 2023-03-30
 */
class CodeInjectClassVisitor(classVisitor: ClassVisitor) :
    ClassVisitor(Opcodes.ASM8, classVisitor) {
    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        LogUtil.printLog("Visit method, name: $name")
        val methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        if ("<clinit>" == name) {
            return CodeInjectMethodVisitor(methodVisitor)
        }
        return methodVisitor
    }
}