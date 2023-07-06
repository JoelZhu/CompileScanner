package com.joelzhu.lib.scanner.plugin.code.visitor.generate

import com.joelzhu.lib.scanner.plugin.code.generator.CodeGenerator
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class StaticMethodVisitor(private val methodVisitor: MethodVisitor) : MethodVisitor(Opcodes.ASM8, methodVisitor) {
    override fun visitCode() {
        CodeGenerator.visitStaticMethod(methodVisitor)
    }
}