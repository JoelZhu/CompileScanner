package com.joelzhu.lib.scanner.plugin.code.generator.strategy

import com.joelzhu.lib.scanner.plugin.code.generator.base.AbstractCodeGenerator
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor

class GlobalPriorityCodeGenerator : AbstractCodeGenerator() {
    override fun visitMemberVariable(classWriter: ClassWriter) {
        TODO("Not yet implemented")
    }

    override fun visitStaticMethod(methodVisitor: MethodVisitor) {
        TODO("Not yet implemented")
    }

    override fun visitGetterMethod(methodVisitor: MethodVisitor) {
        TODO("Not yet implemented")
    }
}