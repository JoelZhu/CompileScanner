package com.joelzhu.lib.scanner.plugin.code.generator.strategy

import com.joelzhu.lib.scanner.annotation.internal.CompileScanBean
import com.joelzhu.lib.scanner.plugin.code.generator.base.AbstractCodeGenerator
import com.joelzhu.lib.scanner.plugin.util.ImplConstants
import com.joelzhu.lib.scanner.plugin.util.JAVA_NAME_SEPARATOR
import com.joelzhu.lib.scanner.plugin.util.JAVA_SEPARATOR
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.ACONST_NULL
import org.objectweb.asm.Opcodes.ALOAD
import org.objectweb.asm.Opcodes.ANEWARRAY
import org.objectweb.asm.Opcodes.ARETURN
import org.objectweb.asm.Opcodes.ARRAYLENGTH
import org.objectweb.asm.Opcodes.ASTORE
import org.objectweb.asm.Opcodes.CHECKCAST
import org.objectweb.asm.Opcodes.DUP
import org.objectweb.asm.Opcodes.GETSTATIC
import org.objectweb.asm.Opcodes.GOTO
import org.objectweb.asm.Opcodes.IADD
import org.objectweb.asm.Opcodes.ICONST_0
import org.objectweb.asm.Opcodes.IFEQ
import org.objectweb.asm.Opcodes.IFNULL
import org.objectweb.asm.Opcodes.ILOAD
import org.objectweb.asm.Opcodes.INVOKEINTERFACE
import org.objectweb.asm.Opcodes.INVOKESPECIAL
import org.objectweb.asm.Opcodes.INVOKESTATIC
import org.objectweb.asm.Opcodes.INVOKEVIRTUAL
import org.objectweb.asm.Opcodes.ISTORE
import org.objectweb.asm.Opcodes.NEW
import org.objectweb.asm.Opcodes.POP
import org.objectweb.asm.Type

class SameTagPriorityWithoutExtensionGenerator : AbstractCodeGenerator() {
    internal companion object {
        private const val NORMAL_CLASSES_NAME = "NORMAL_CLASSES"

        private const val DEFAULT_CLASSES_NAME = "DEFAULT_CLASSES"

        private const val WILDCARD_OF_CLASS = "<*>"

        private const val START_LINE_NUMBER = 22

        private val MAP_KEY_CLASS = Integer::class.java.name.replace(JAVA_NAME_SEPARATOR, JAVA_SEPARATOR)

        private val MAP_VALUE_CLASS = Class::class.java.name.replace(JAVA_NAME_SEPARATOR, JAVA_SEPARATOR)

        private val BEAN_CLASS = HashMap::class.java.name.replace(JAVA_NAME_SEPARATOR, JAVA_SEPARATOR)

        private val BEAN_DESCRIPTOR = "L$BEAN_CLASS;"

        private val BEAN_SIGNATURE = "$BEAN_DESCRIPTOR<L$MAP_KEY_CLASS;[L$MAP_VALUE_CLASS$WILDCARD_OF_CLASS;>"
    }

    private var lineNumber: Int = START_LINE_NUMBER

    override fun visitMemberVariable(classWriter: ClassWriter) {
        run {
            val fieldVisitor = classWriter.visitField(Opcodes.ACC_PRIVATE or Opcodes.ACC_FINAL or Opcodes.ACC_STATIC,
                NORMAL_CLASSES_NAME, BEAN_DESCRIPTOR, BEAN_SIGNATURE, null)
            fieldVisitor.visitEnd()
        }

        run {
            val fieldVisitor = classWriter.visitField(Opcodes.ACC_PRIVATE or Opcodes.ACC_FINAL or Opcodes.ACC_STATIC,
                DEFAULT_CLASSES_NAME, BEAN_DESCRIPTOR, BEAN_SIGNATURE, null)
            fieldVisitor.visitEnd()
        }
    }

    override fun visitStaticMethod(methodVisitor: MethodVisitor) {
        if (data == null) {
            return
        }

        lineNumber = START_LINE_NUMBER

        data!!.defaultData.forEach { (tag, beans) ->
            insertMap(methodVisitor, DEFAULT_CLASSES_NAME, tag, beans)
        }
        data!!.normalData.forEach { (tag, beans) ->
            insertMap(methodVisitor, NORMAL_CLASSES_NAME, tag, beans)
        }

        lineNumber += 1
        val label0 = Label()
        methodVisitor.visitLabel(label0)
        methodVisitor.visitLineNumber(lineNumber, label0)
        methodVisitor.visitInsn(Opcodes.RETURN)
        methodVisitor.visitMaxs(0, 0)
    }

    override fun visitGetterMethod(methodVisitor: MethodVisitor) {
        val label0 = Label()
        methodVisitor.visitLabel(label0)
        methodVisitor.visitLineNumber(40, label0)
        methodVisitor.visitTypeInsn(NEW, "java/util/ArrayList")
        methodVisitor.visitInsn(DUP)
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false)
        methodVisitor.visitVarInsn(ASTORE, 2)
        val label1 = Label()
        methodVisitor.visitLabel(label1)
        methodVisitor.visitLineNumber(41, label1)
        methodVisitor.visitInsn(ICONST_0)
        methodVisitor.visitVarInsn(ISTORE, 3)
        val label2 = Label()
        methodVisitor.visitLabel(label2)
        methodVisitor.visitLineNumber(42, label2)
        methodVisitor.visitVarInsn(ALOAD, 0)
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "iterator", "()Ljava/util/Iterator;", true)
        methodVisitor.visitVarInsn(ASTORE, 4)
        val label3 = Label()
        methodVisitor.visitLabel(label3)
        methodVisitor.visitFrame(Opcodes.F_APPEND, 3,
            arrayOf<Any>("java/util/List", Opcodes.INTEGER, "java/util/Iterator"), 0, null)
        methodVisitor.visitVarInsn(ALOAD, 4)
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true)
        val label4 = Label()
        methodVisitor.visitJumpInsn(IFEQ, label4)
        methodVisitor.visitVarInsn(ALOAD, 4)
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true)
        methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Integer")
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false)
        methodVisitor.visitVarInsn(ISTORE, 5)
        val label5 = Label()
        methodVisitor.visitLabel(label5)
        methodVisitor.visitLineNumber(44, label5)
        methodVisitor.visitFieldInsn(GETSTATIC, ImplConstants.IMPL_CLASS, NORMAL_CLASSES_NAME, BEAN_DESCRIPTOR)
        methodVisitor.visitVarInsn(ILOAD, 5)
        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false)
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "containsKey", "(Ljava/lang/Object;)Z", false)
        val label6 = Label()
        methodVisitor.visitJumpInsn(IFEQ, label6)
        val label7 = Label()
        methodVisitor.visitLabel(label7)
        methodVisitor.visitLineNumber(45, label7)
        methodVisitor.visitFieldInsn(GETSTATIC, ImplConstants.IMPL_CLASS, NORMAL_CLASSES_NAME, BEAN_DESCRIPTOR)
        methodVisitor.visitVarInsn(ILOAD, 5)
        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false)
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "get",
            "(Ljava/lang/Object;)Ljava/lang/Object;", false)
        methodVisitor.visitTypeInsn(CHECKCAST, "[Ljava/lang/Class;")
        methodVisitor.visitVarInsn(ASTORE, 6)
        val label8 = Label()
        methodVisitor.visitLabel(label8)
        val label9 = Label()
        methodVisitor.visitJumpInsn(GOTO, label9)
        methodVisitor.visitLabel(label6)
        methodVisitor.visitLineNumber(46, label6)
        methodVisitor.visitFrame(Opcodes.F_APPEND, 1, arrayOf<Any>(Opcodes.INTEGER), 0, null)
        methodVisitor.visitVarInsn(ILOAD, 1)
        val label10 = Label()
        methodVisitor.visitJumpInsn(IFEQ, label10)
        val label11 = Label()
        methodVisitor.visitLabel(label11)
        methodVisitor.visitLineNumber(47, label11)
        methodVisitor.visitFieldInsn(GETSTATIC, ImplConstants.IMPL_CLASS, DEFAULT_CLASSES_NAME, BEAN_DESCRIPTOR)
        methodVisitor.visitVarInsn(ILOAD, 5)
        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false)
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "get",
            "(Ljava/lang/Object;)Ljava/lang/Object;", false)
        methodVisitor.visitTypeInsn(CHECKCAST, "[Ljava/lang/Class;")
        methodVisitor.visitVarInsn(ASTORE, 6)
        val label12 = Label()
        methodVisitor.visitLabel(label12)
        methodVisitor.visitJumpInsn(GOTO, label9)
        methodVisitor.visitLabel(label10)
        methodVisitor.visitLineNumber(49, label10)
        methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null)
        methodVisitor.visitInsn(ACONST_NULL)
        methodVisitor.visitVarInsn(ASTORE, 6)
        methodVisitor.visitLabel(label9)
        methodVisitor.visitLineNumber(52, label9)
        methodVisitor.visitFrame(Opcodes.F_APPEND, 1, arrayOf<Any>("[Ljava/lang/Class;"), 0, null)
        methodVisitor.visitVarInsn(ALOAD, 6)
        val label13 = Label()
        methodVisitor.visitJumpInsn(IFNULL, label13)
        val label14 = Label()
        methodVisitor.visitLabel(label14)
        methodVisitor.visitLineNumber(53, label14)
        methodVisitor.visitVarInsn(ILOAD, 3)
        methodVisitor.visitVarInsn(ALOAD, 6)
        methodVisitor.visitInsn(ARRAYLENGTH)
        methodVisitor.visitInsn(IADD)
        methodVisitor.visitVarInsn(ISTORE, 3)
        val label15 = Label()
        methodVisitor.visitLabel(label15)
        methodVisitor.visitLineNumber(54, label15)
        methodVisitor.visitVarInsn(ALOAD, 2)
        methodVisitor.visitVarInsn(ALOAD, 6)
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true)
        methodVisitor.visitInsn(POP)
        methodVisitor.visitLabel(label13)
        methodVisitor.visitLineNumber(56, label13)
        methodVisitor.visitFrame(Opcodes.F_CHOP, 2, null, 0, null)
        methodVisitor.visitJumpInsn(GOTO, label3)
        methodVisitor.visitLabel(label4)
        methodVisitor.visitLineNumber(58, label4)
        methodVisitor.visitFrame(Opcodes.F_CHOP, 1, null, 0, null)
        methodVisitor.visitVarInsn(ILOAD, 3)
        methodVisitor.visitTypeInsn(ANEWARRAY, "java/lang/Class")
        methodVisitor.visitVarInsn(ASTORE, 4)
        val label16 = Label()
        methodVisitor.visitLabel(label16)
        methodVisitor.visitLineNumber(59, label16)
        methodVisitor.visitInsn(ICONST_0)
        methodVisitor.visitVarInsn(ISTORE, 5)
        val label17 = Label()
        methodVisitor.visitLabel(label17)
        methodVisitor.visitLineNumber(60, label17)
        methodVisitor.visitVarInsn(ALOAD, 2)
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "iterator", "()Ljava/util/Iterator;", true)
        methodVisitor.visitVarInsn(ASTORE, 6)
        val label18 = Label()
        methodVisitor.visitLabel(label18)
        methodVisitor.visitFrame(Opcodes.F_APPEND, 3,
            arrayOf<Any>("[Ljava/lang/Class;", Opcodes.INTEGER, "java/util/Iterator"), 0, null)
        methodVisitor.visitVarInsn(ALOAD, 6)
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true)
        val label19 = Label()
        methodVisitor.visitJumpInsn(IFEQ, label19)
        methodVisitor.visitVarInsn(ALOAD, 6)
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true)
        methodVisitor.visitTypeInsn(CHECKCAST, "[Ljava/lang/Class;")
        methodVisitor.visitVarInsn(ASTORE, 7)
        val label20 = Label()
        methodVisitor.visitLabel(label20)
        methodVisitor.visitLineNumber(61, label20)
        methodVisitor.visitVarInsn(ALOAD, 7)
        methodVisitor.visitInsn(ARRAYLENGTH)
        methodVisitor.visitVarInsn(ISTORE, 8)
        val label21 = Label()
        methodVisitor.visitLabel(label21)
        methodVisitor.visitLineNumber(62, label21)
        methodVisitor.visitVarInsn(ALOAD, 7)
        methodVisitor.visitInsn(ICONST_0)
        methodVisitor.visitVarInsn(ALOAD, 4)
        methodVisitor.visitVarInsn(ILOAD, 5)
        methodVisitor.visitVarInsn(ILOAD, 8)
        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/System", "arraycopy",
            "(Ljava/lang/Object;ILjava/lang/Object;II)V", false)
        val label22 = Label()
        methodVisitor.visitLabel(label22)
        methodVisitor.visitLineNumber(63, label22)
        methodVisitor.visitVarInsn(ILOAD, 5)
        methodVisitor.visitVarInsn(ILOAD, 8)
        methodVisitor.visitInsn(IADD)
        methodVisitor.visitVarInsn(ISTORE, 5)
        val label23 = Label()
        methodVisitor.visitLabel(label23)
        methodVisitor.visitLineNumber(64, label23)
        methodVisitor.visitJumpInsn(GOTO, label18)
        methodVisitor.visitLabel(label19)
        methodVisitor.visitLineNumber(65, label19)
        methodVisitor.visitFrame(Opcodes.F_CHOP, 1, null, 0, null)
        methodVisitor.visitVarInsn(ALOAD, 4)
        methodVisitor.visitInsn(ARETURN)
        val label24 = Label()
        methodVisitor.visitLabel(label24)
        methodVisitor.visitLocalVariable("classes", "[Ljava/lang/Class;", "[Ljava/lang/Class<*>;", label8, label6, 6)
        methodVisitor.visitLocalVariable("classes", "[Ljava/lang/Class;", "[Ljava/lang/Class<*>;", label12, label10, 6)
        methodVisitor.visitLocalVariable("classes", "[Ljava/lang/Class;", "[Ljava/lang/Class<*>;", label9, label13, 6)
        methodVisitor.visitLocalVariable("tag", "I", null, label5, label13, 5)
        methodVisitor.visitLocalVariable("toCopySize", "I", null, label21, label23, 8)
        methodVisitor.visitLocalVariable("classes", "[Ljava/lang/Class;", "[Ljava/lang/Class<*>;", label20, label23, 7)
        methodVisitor.visitLocalVariable("tags", "Ljava/util/List;", "Ljava/util/List<Ljava/lang/Integer;>;", label0,
            label24, 0)
        methodVisitor.visitLocalVariable("withDefault", "Z", null, label0, label24, 1)
        methodVisitor.visitLocalVariable("classesList", "Ljava/util/List;", "Ljava/util/List<[Ljava/lang/Class<*>;>;",
            label1, label24, 2)
        methodVisitor.visitLocalVariable("listSize", "I", null, label2, label24, 3)
        methodVisitor.visitLocalVariable("result", "[Ljava/lang/Class;", "[Ljava/lang/Class<*>;", label16, label24, 4)
        methodVisitor.visitLocalVariable("copiedIndex", "I", null, label17, label24, 5)
        methodVisitor.visitMaxs(5, 9)
    }

    private fun insertMap(methodVisitor: MethodVisitor, memberVariableName: String, tag: Int,
        beans: List<CompileScanBean>) {
        val label0 = Label()
        methodVisitor.visitLabel(label0)
        methodVisitor.visitLineNumber(lineNumber, label0)
        methodVisitor.visitTypeInsn(Opcodes.NEW, BEAN_CLASS)
        methodVisitor.visitInsn(Opcodes.DUP)
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, BEAN_CLASS, "<init>", "()V", false)
        methodVisitor.visitFieldInsn(Opcodes.PUTSTATIC, ImplConstants.IMPL_CLASS, memberVariableName, BEAN_DESCRIPTOR)

        lineNumber += 1
        val label1 = Label()
        methodVisitor.visitLabel(label1)
        methodVisitor.visitLineNumber(lineNumber, label0)
        methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, ImplConstants.IMPL_CLASS, memberVariableName, BEAN_DESCRIPTOR)
        methodVisitor.visitIntInsn(Opcodes.BIPUSH, tag)
        methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, MAP_KEY_CLASS, "valueOf", "(I)L$MAP_KEY_CLASS;",
            false)
        methodVisitor.visitInsn(Opcodes.ICONST_2)
        methodVisitor.visitTypeInsn(Opcodes.ANEWARRAY, MAP_VALUE_CLASS)

        beans.forEachIndexed { index, bean ->
            insertScannedClass(methodVisitor, index, bean)
        }

        methodVisitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, BEAN_CLASS, "put",
            "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true)
        methodVisitor.visitInsn(Opcodes.POP)
    }

    private fun insertScannedClass(methodVisitor: MethodVisitor, classIndex: Int, bean: CompileScanBean) {
        methodVisitor.visitInsn(Opcodes.DUP)
        methodVisitor.visitInsn(classIndex + 3)
        methodVisitor.visitLdcInsn(
            Type.getType("L${bean.className::class.java.name.replace(JAVA_NAME_SEPARATOR, JAVA_SEPARATOR)};"))
        methodVisitor.visitInsn(Opcodes.AASTORE)
    }
}