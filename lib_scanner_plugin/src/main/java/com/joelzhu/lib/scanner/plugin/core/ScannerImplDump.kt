package com.joelzhu.lib.scanner.plugin.core

import com.joelzhu.lib.scanner.annotation.ImplConstants
import com.joelzhu.lib.scanner.plugin.util.ScannerCache
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

/**
 * [Description here].
 *
 * @author JoelZhu
 * @since 2023-04-07
 */
object ScannerImplDump : Opcodes {
    private const val NORMAL_MAP_NAME = "NORMAL_MAP"

    private const val DEFAULT_MAP_NAME = "DEFAULT_MAP"

    private val CLASS_NAME =
        "${ImplConstants.IMPL_PACKAGE.replace(".", "/")}/${ImplConstants.IMPL_CLASS}"

    private var lineNumber = 13

    @Throws(Exception::class)
    fun dump(): ByteArray {
        val classWriter = ClassWriter(0)
        var fieldVisitor: FieldVisitor
        var methodVisitor: MethodVisitor
        classWriter.visit(
            Opcodes.V1_8,
            Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL or Opcodes.ACC_SUPER,
            CLASS_NAME,
            null,
            "java/lang/Object",
            null
        )
        classWriter.visitSource("${ImplConstants.IMPL_CLASS}.java", null)
        run {
            fieldVisitor = classWriter.visitField(
                Opcodes.ACC_PRIVATE or Opcodes.ACC_FINAL or Opcodes.ACC_STATIC,
                NORMAL_MAP_NAME,
                "Ljava/util/Map;",
                "Ljava/util/Map<Ljava/lang/String;[Ljava/lang/Class<*>;>;",
                null
            )
            fieldVisitor.visitEnd()
        }
        run {
            fieldVisitor = classWriter.visitField(
                Opcodes.ACC_PRIVATE or Opcodes.ACC_FINAL or Opcodes.ACC_STATIC,
                DEFAULT_MAP_NAME,
                "Ljava/util/Map;",
                "Ljava/util/Map<Ljava/lang/String;[Ljava/lang/Class<*>;>;",
                null
            )
            fieldVisitor.visitEnd()
        }
        run {
            methodVisitor =
                classWriter.visitMethod(Opcodes.ACC_STATIC, "<clinit>", "()V", null, null)
            methodVisitor.visitCode()

            initializeClassesMaps(methodVisitor)

            methodVisitor.visitMaxs(2, 0)
            methodVisitor.visitEnd()
        }
        run {
            methodVisitor =
                classWriter.visitMethod(Opcodes.ACC_PRIVATE, "<init>", "()V", null, null)
            methodVisitor.visitCode()
            lineNumber += 3
            val label0 = Label()
            methodVisitor.visitLabel(label0)
            methodVisitor.visitLineNumber(lineNumber, label0)
            methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
            methodVisitor.visitMethodInsn(
                Opcodes.INVOKESPECIAL,
                "java/lang/Object",
                "<init>",
                "()V",
                false
            )
            lineNumber += 1
            val label1 = Label()
            methodVisitor.visitLabel(label1)
            methodVisitor.visitLineNumber(lineNumber, label1)
            methodVisitor.visitInsn(Opcodes.RETURN)
            val label2 = Label()
            methodVisitor.visitLabel(label2)
            methodVisitor.visitLocalVariable(
                "this", "L${CLASS_NAME};", null, label0,
                label2, 0
            )
            methodVisitor.visitMaxs(1, 1)
            methodVisitor.visitEnd()
        }
        run {
            methodVisitor = classWriter.visitMethod(
                Opcodes.ACC_PUBLIC or Opcodes.ACC_STATIC,
                ImplConstants.NORMAL_CLASS_GET,
                "(Ljava/lang/String;)[Ljava/lang/Class;",
                "(Ljava/lang/String;)[Ljava/lang/Class<*>;",
                null
            )
            methodVisitor.visitCode()
            lineNumber += 3
            val label0 = Label()
            methodVisitor.visitLabel(label0)
            methodVisitor.visitLineNumber(lineNumber, label0)
            methodVisitor.visitFieldInsn(
                Opcodes.GETSTATIC, CLASS_NAME, NORMAL_MAP_NAME,
                "Ljava/util/Map;"
            )
            methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
            methodVisitor.visitMethodInsn(
                Opcodes.INVOKEINTERFACE, "java/util/Map", "get",
                "(Ljava/lang/Object;)Ljava/lang/Object;", true
            )
            methodVisitor.visitTypeInsn(Opcodes.CHECKCAST, "[Ljava/lang/Class;")
            methodVisitor.visitInsn(Opcodes.ARETURN)
            val label1 = Label()
            methodVisitor.visitLabel(label1)
            methodVisitor.visitLocalVariable(
                "tag",
                "Ljava/lang/String;",
                null,
                label0,
                label1,
                0
            )
            methodVisitor.visitMaxs(2, 1)
            methodVisitor.visitEnd()
        }
        run {
            methodVisitor = classWriter.visitMethod(
                Opcodes.ACC_PUBLIC or Opcodes.ACC_STATIC,
                ImplConstants.DEFAULT_CLASS_GET,
                "(Ljava/lang/String;)[Ljava/lang/Class;",
                "(Ljava/lang/String;)[Ljava/lang/Class<*>;",
                null
            )
            methodVisitor.visitCode()
            lineNumber += 4
            val label0 = Label()
            methodVisitor.visitLabel(label0)
            methodVisitor.visitLineNumber(lineNumber, label0)
            methodVisitor.visitFieldInsn(
                Opcodes.GETSTATIC, CLASS_NAME, DEFAULT_MAP_NAME,
                "Ljava/util/Map;"
            )
            methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
            methodVisitor.visitMethodInsn(
                Opcodes.INVOKEINTERFACE, "java/util/Map", "get",
                "(Ljava/lang/Object;)Ljava/lang/Object;", true
            )
            methodVisitor.visitTypeInsn(Opcodes.CHECKCAST, "[Ljava/lang/Class;")
            methodVisitor.visitInsn(Opcodes.ARETURN)
            val label1 = Label()
            methodVisitor.visitLabel(label1)
            methodVisitor.visitLocalVariable(
                "tag",
                "Ljava/lang/String;",
                null,
                label0,
                label1,
                0
            )
            methodVisitor.visitMaxs(2, 1)
            methodVisitor.visitEnd()
        }
        classWriter.visitEnd()
        return classWriter.toByteArray()
    }

    private fun initializeClassesMaps(methodVisitor: MethodVisitor) {
        // Initialize static maps.
        initializeMaps(methodVisitor, NORMAL_MAP_NAME)
        initializeMaps(methodVisitor, DEFAULT_MAP_NAME)
        // Static method add one more line.
        lineNumber += 1

        // List normal classes.
        val normalLabels = mutableListOf<Label>()
        ScannerCache.CACHE.getNormalClassesMap().forEach { (tag, classMap) ->
            val label = fillMapsWithEachClasses(
                methodVisitor,
                tag,
                classMap,
                NORMAL_MAP_NAME
            )
            normalLabels.add(label)
        }
        // List default classes.
        val defaultLabels = mutableListOf<Label>()
        ScannerCache.CACHE.getDefaultClassesMap().forEach { (tag, classMap) ->
            val label = fillMapsWithEachClasses(
                methodVisitor,
                tag,
                classMap,
                DEFAULT_MAP_NAME
            )
            defaultLabels.add(label)
        }

        // To generate local variable
        val label1 = Label()
        methodVisitor.visitLabel(label1)
        methodVisitor.visitLineNumber(lineNumber + 1, label1)
        methodVisitor.visitInsn(Opcodes.RETURN)
        // Put each normal classes labels into map.
        normalLabels.forEachIndexed { index, label ->
            methodVisitor.visitLocalVariable(
                "beans$index", "[Ljava/lang/Class;", "[Ljava/lang/Class<*>;", label, label1,
                index
            )
        }
        // Put each default classes labels into map.
        defaultLabels.forEachIndexed { index, label ->
            val defaultIndex = index + normalLabels.size
            methodVisitor.visitLocalVariable(
                "beans$defaultIndex", "[Ljava/lang/Class;", "[Ljava/lang/Class<*>;", label, label1,
                defaultIndex
            )
        }
    }

    private fun initializeMaps(
        methodVisitor: MethodVisitor,
        mapFieldName: String,
    ) {
        val label0 = Label()
        methodVisitor.visitLabel(label0)
        methodVisitor.visitLineNumber(lineNumber, label0)
        methodVisitor.visitTypeInsn(Opcodes.NEW, "java/util/HashMap")
        methodVisitor.visitInsn(Opcodes.DUP)
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            "java/util/HashMap",
            "<init>",
            "()V",
            false
        )
        methodVisitor.visitFieldInsn(
            Opcodes.PUTSTATIC, CLASS_NAME, mapFieldName,
            "Ljava/util/Map;"
        )

        // Declaration of each class maps with two lines add.
        lineNumber += 2
    }

    private fun fillMapsWithEachClasses(
        methodVisitor: MethodVisitor,
        tag: String,
        classes: MutableList<String>,
        mapFieldName: String
    ): Label {
        val classSize = classes.size
        // Generate a new array of java.lang.Class.
        val label0 = Label()
        methodVisitor.visitLabel(label0)
        methodVisitor.visitLineNumber(lineNumber, label0)
        methodVisitor.visitInsn(classSize + 3)
        methodVisitor.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Class")
        // Create each class instance
        var classIndex = 0
        classes.forEach {
            // Insert each annotated classes into the array.
            insertClass(methodVisitor, classIndex, it)
            classIndex++
        }
        methodVisitor.visitVarInsn(Opcodes.ASTORE, 0)

        // Put classes into map
        lineNumber += classSize + 1
        val label1 = Label()
        methodVisitor.visitLabel(label1)
        methodVisitor.visitLineNumber(lineNumber, label1)
        methodVisitor.visitFieldInsn(
            Opcodes.GETSTATIC, CLASS_NAME, mapFieldName,
            "Ljava/util/Map;"
        )
        methodVisitor.visitLdcInsn(tag)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKEINTERFACE, "java/util/Map", "put",
            "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true
        )
        methodVisitor.visitInsn(Opcodes.POP)
        // Move line to next.
        lineNumber += 1
        return label1
    }

    private fun insertClass(methodVisitor: MethodVisitor, classIndex: Int, className: String) {
        methodVisitor.visitInsn(Opcodes.DUP)
        methodVisitor.visitInsn(classIndex + 3)
        methodVisitor.visitLdcInsn(Type.getType("L${className};"))
        methodVisitor.visitInsn(Opcodes.AASTORE)
    }
}