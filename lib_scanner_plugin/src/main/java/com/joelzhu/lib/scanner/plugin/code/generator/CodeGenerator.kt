package com.joelzhu.lib.scanner.plugin.code.generator

import com.joelzhu.lib.scanner.plugin.code.cache.bean.CodeGenerateBean
import com.joelzhu.lib.scanner.plugin.code.generator.base.AbstractCodeGenerator
import com.joelzhu.lib.scanner.plugin.code.generator.strategy.GlobalPriorityCodeGenerator
import com.joelzhu.lib.scanner.plugin.code.generator.strategy.SameTagPriorityWithoutExtensionGenerator
import com.joelzhu.lib.scanner.plugin.util.LogUtil
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor

object CodeGenerator {
    private lateinit var codeGenerator: AbstractCodeGenerator

    fun initializeGenerator(globalPriority: Boolean, useExtension: Boolean) {
        codeGenerator = if (globalPriority) {
            if (useExtension) {
                GlobalPriorityCodeGenerator()
            } else {
                GlobalPriorityCodeGenerator()
            }
        } else {
            if (useExtension) {
                SameTagPriorityWithoutExtensionGenerator()
            } else {
                SameTagPriorityWithoutExtensionGenerator()
            }
        }
        LogUtil.printInfo("Selected generator: ${codeGenerator::class.java.simpleName}.")
    }

    fun prepareData(bean: CodeGenerateBean) {
        LogUtil.printEmptyLine()
        LogUtil.printInfo("Preparing data.")
        LogUtil.printDebug("Found default classes: ${bean.defaultData.size}.")
        LogUtil.printDebug("Found normal classes: ${bean.normalData.size}.")
        codeGenerator.prepareData(bean)
    }

    fun visitMemberVariable(classWriter: ClassWriter) {
        LogUtil.printInfo("To visit member variable.")
        codeGenerator.visitMemberVariable(classWriter)
    }

    fun visitStaticMethod(methodVisitor: MethodVisitor) {
        LogUtil.printInfo("To visit static method.")
        methodVisitor.visitCode()
        codeGenerator.visitStaticMethod(methodVisitor)
        methodVisitor.visitEnd()
    }

    fun visitGetterMethod(methodVisitor: MethodVisitor) {
        LogUtil.printInfo("To visit getter.")
        methodVisitor.visitCode()
        codeGenerator.visitGetterMethod(methodVisitor)
        methodVisitor.visitEnd()
    }
}