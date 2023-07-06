package com.joelzhu.lib.scanner.plugin.code.visitor.generate

import com.joelzhu.lib.scanner.plugin.code.generator.CodeGenerator
import com.joelzhu.lib.scanner.plugin.util.ImplConstants
import com.joelzhu.lib.scanner.plugin.util.LogUtil
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * [Description here].
 *
 * @author JoelZhu
 * @since 2023-03-30
 */
class GenerateCodeClassVisitor(private val classWriter: ClassWriter, private val injectClassName: String) :
    ClassVisitor(Opcodes.ASM8, classWriter) {
    internal companion object {
        private const val STATIC_METHOD_NAME = "<clinit>"
    }

    private var isAnnotatedInjectClass = false

    override fun visit(version: Int, access: Int, name: String?, signature: String?,
        superName: String?, interfaces: Array<out String>?) {
        super.visit(version, access, name, signature, superName, interfaces)
        if (injectClassName == name) {
            isAnnotatedInjectClass = true
            LogUtil.printInfo("Generating code's class found, name: $name.")

            // Declare member variable.
            CodeGenerator.visitMemberVariable(classWriter)
        }
    }

    override fun visitMethod(access: Int, name: String?, descriptor: String?, signature: String?,
        exceptions: Array<out String>?): MethodVisitor {
        val methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        if (!isAnnotatedInjectClass) {
            return methodVisitor
        }

        LogUtil.printDebug("Found destiny file to inject, method: $name.")
        return when (name) {
            STATIC_METHOD_NAME -> StaticMethodVisitor(methodVisitor)
            ImplConstants.CLASS_GET -> GetterMethodVisitor(methodVisitor)
            else -> methodVisitor
        }
    }
}