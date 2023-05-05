package com.joelzhu.lib.scanner.plugin.core

import com.joelzhu.lib.scanner.annotation.ImplConstants
import com.joelzhu.lib.scanner.annotation.ScannedClass
import com.joelzhu.lib.scanner.plugin.util.Constants
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.AASTORE
import org.objectweb.asm.Opcodes.ACC_FINAL
import org.objectweb.asm.Opcodes.ACC_PRIVATE
import org.objectweb.asm.Opcodes.ACC_PUBLIC
import org.objectweb.asm.Opcodes.ACC_STATIC
import org.objectweb.asm.Opcodes.ACC_SUPER
import org.objectweb.asm.Opcodes.ALOAD
import org.objectweb.asm.Opcodes.ANEWARRAY
import org.objectweb.asm.Opcodes.ARETURN
import org.objectweb.asm.Opcodes.BIPUSH
import org.objectweb.asm.Opcodes.DUP
import org.objectweb.asm.Opcodes.GETSTATIC
import org.objectweb.asm.Opcodes.ICONST_0
import org.objectweb.asm.Opcodes.ICONST_1
import org.objectweb.asm.Opcodes.INVOKESPECIAL
import org.objectweb.asm.Opcodes.NEW
import org.objectweb.asm.Opcodes.PUTSTATIC
import org.objectweb.asm.Opcodes.RETURN
import org.objectweb.asm.Opcodes.V1_8
import org.objectweb.asm.Type

/**
 * Generate ```ScannerImpl.class```'s util.
 *
 * @author JoelZhu
 * @since 2023-04-07
 */
object ScannerImplCodeGenerator : Opcodes {
    private const val CLASSES_FIELD_NAME = "CLASSES"

    private val SCANNED_CLASS_BEAN_NAME =
        ScannedClass::class.java.name.replace(".", Constants.JAVA_SEPARATOR)

    private val SCANNED_CLASS_BEAN_DESCRIPTOR = "L${SCANNED_CLASS_BEAN_NAME};"

    private val SCANNED_CLASS_BEAN_ARRAY_DESCRIPTOR = "[${SCANNED_CLASS_BEAN_DESCRIPTOR}"

    private val CLASS_NAME = "${
        ImplConstants.IMPL_PACKAGE.replace(
            ".",
            Constants.JAVA_SEPARATOR
        )
    }${Constants.JAVA_SEPARATOR}${ImplConstants.IMPL_CLASS}"

    private var lineNumber = 14

    @Throws(Exception::class)
    fun generate(): ByteArray {
        val classWriter = ClassWriter(0)
        classWriter.visit(
            V1_8,
            ACC_PUBLIC or ACC_FINAL or ACC_SUPER,
            CLASS_NAME,
            null,
            "java/lang/Object",
            null
        )
        classWriter.visitSource("${ImplConstants.IMPL_CLASS}.java", null)

        // Static fields declaration.
        run {
            declareClassesFields(classWriter)
        }

        // Static method.
        run {
            generateStaticMethod(classWriter)
        }

        // Private constructor.
        run {
            generatePrivateConstructor(classWriter)
        }

        // Getters.
        run {
            generateGetter(classWriter)
        }

        classWriter.visitEnd()
        return classWriter.toByteArray()
    }

    private fun declareClassesFields(classWriter: ClassWriter) {
        val fieldVisitor = classWriter.visitField(
            ACC_PRIVATE or ACC_FINAL or ACC_STATIC,
            CLASSES_FIELD_NAME,
            SCANNED_CLASS_BEAN_ARRAY_DESCRIPTOR,
            null,
            null
        )
        fieldVisitor.visitEnd()
    }

    private fun generateStaticMethod(classWriter: ClassWriter) {
        val classes = ScannerCache.CACHE.getClasses()

        val methodVisitor = classWriter.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null)
        methodVisitor.visitCode()

        val label0 = Label()
        methodVisitor.visitLabel(label0)
        methodVisitor.visitLineNumber(lineNumber, label0)
        methodVisitor.visitIntInsn(BIPUSH, classes.size)
        methodVisitor.visitTypeInsn(ANEWARRAY, SCANNED_CLASS_BEAN_NAME)

        classes.forEachIndexed { index, bean ->
            fillClasses(methodVisitor, index, bean)
        }

        methodVisitor.visitFieldInsn(
            PUTSTATIC,
            CLASS_NAME,
            CLASSES_FIELD_NAME,
            SCANNED_CLASS_BEAN_ARRAY_DESCRIPTOR
        )
        methodVisitor.visitInsn(RETURN)
        methodVisitor.visitMaxs(0, 0)
        methodVisitor.visitEnd()
    }

    private fun fillClasses(
        methodVisitor: MethodVisitor,
        classIndex: Int,
        bean: CompileScanBean
    ) {
        val adjustedTag = if (bean.tag == null) {
            ""
        } else {
            bean.tag
        }
        val adjustedGroup = if (bean.group == null) {
            ""
        } else {
            bean.group
        }
        val isDefaultInsn = if (bean.isDefault) {
            ICONST_1
        } else {
            ICONST_0
        }

        methodVisitor.visitInsn(DUP)
        methodVisitor.visitIntInsn(BIPUSH, classIndex)
        methodVisitor.visitTypeInsn(NEW, SCANNED_CLASS_BEAN_NAME)
        methodVisitor.visitInsn(DUP)
        methodVisitor.visitLdcInsn(Type.getType("L${bean.belongingClass.replace(".", "/")};"))
        methodVisitor.visitLdcInsn(adjustedTag)
        methodVisitor.visitLdcInsn(adjustedGroup)
        methodVisitor.visitInsn(isDefaultInsn)
        methodVisitor.visitMethodInsn(
            INVOKESPECIAL, SCANNED_CLASS_BEAN_NAME, "<init>",
            "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;Z)V", false
        )
        methodVisitor.visitInsn(AASTORE)

        lineNumber += 1
    }

    private fun generatePrivateConstructor(classWriter: ClassWriter) {
        val methodVisitor = classWriter.visitMethod(ACC_PRIVATE, "<init>", "()V", null, null)
        methodVisitor.visitCode()
        lineNumber += 3
        val label0 = Label()
        methodVisitor.visitLabel(label0)
        methodVisitor.visitLineNumber(lineNumber, label0)
        methodVisitor.visitVarInsn(ALOAD, 0)
        methodVisitor.visitMethodInsn(
            INVOKESPECIAL,
            "java/lang/Object",
            "<init>",
            "()V",
            false
        )
        lineNumber += 1
        val label1 = Label()
        methodVisitor.visitLabel(label1)
        methodVisitor.visitLineNumber(lineNumber, label1)
        methodVisitor.visitInsn(RETURN)
        val label2 = Label()
        methodVisitor.visitLabel(label2)
        methodVisitor.visitLocalVariable("this", "L${CLASS_NAME};", null, label0, label2, 0)
        methodVisitor.visitMaxs(0, 0)
        methodVisitor.visitEnd()
    }

    private fun generateGetter(classWriter: ClassWriter) {
        val methodVisitor = classWriter.visitMethod(
            ACC_PUBLIC or ACC_STATIC,
            ImplConstants.CLASS_GET,
            "()${SCANNED_CLASS_BEAN_ARRAY_DESCRIPTOR}",
            null,
            null
        )
        methodVisitor.visitCode()

        val label0 = Label()
        methodVisitor.visitLabel(label0)
        methodVisitor.visitLineNumber(30, label0)
        methodVisitor.visitFieldInsn(
            GETSTATIC,
            CLASS_NAME,
            CLASSES_FIELD_NAME,
            SCANNED_CLASS_BEAN_ARRAY_DESCRIPTOR
        )
        methodVisitor.visitInsn(ARETURN)
        methodVisitor.visitMaxs(0, 0)
        methodVisitor.visitEnd()
    }
}