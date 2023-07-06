package com.joelzhu.lib.scanner.plugin.code.generator.base

import com.joelzhu.lib.scanner.plugin.code.cache.bean.CodeGenerateBean
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor

abstract class AbstractCodeGenerator {
    protected var data: CodeGenerateBean? = null

    fun prepareData(data: CodeGenerateBean?) {
        this.data = data
    }

    abstract fun visitMemberVariable(classWriter: ClassWriter)

    abstract fun visitStaticMethod(methodVisitor: MethodVisitor)

    abstract fun visitGetterMethod(methodVisitor: MethodVisitor)
}