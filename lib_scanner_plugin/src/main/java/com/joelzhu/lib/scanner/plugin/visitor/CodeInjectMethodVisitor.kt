package com.joelzhu.lib.scanner.plugin.visitor

import com.joelzhu.lib.scanner.plugin.util.Constants
import com.joelzhu.lib.scanner.plugin.util.ScannerCache
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type


/**
 * Method visitor for Scanner to inject code, which put annotated classes into map.
 * The map will be read in lib-runtime called from developer at application runtime.
 *
 * @author JoelZhu
 * @since 2023-04-04
 */
class CodeInjectMethodVisitor(private val methodVisitor: MethodVisitor) :
    MethodVisitor(Opcodes.ASM8, methodVisitor) {
    // This number linked to the line, which is the line below the declaration of Scanner.class's
    // static method, if the Scanner.class changed, we must modify this number.
    private var lineNumber = Constants.CONSTRUCTOR_FIRST_LINE

    override fun visitCode() {
        methodVisitor.visitCode()

        // To generate initializing code.
        initializeClassesMaps()

        methodVisitor.visitMaxs(0, 0)
        methodVisitor.visitEnd()
    }

    private fun initializeClassesMaps() {
        // Initialize static maps.
        initializeMaps(Constants.SCANNER_NORMAL_MAP_NAME, Constants.NORMAL_MAP_DECLARATION_LINE)
        initializeMaps(Constants.SCANNER_DEFAULT_MAP_NAME, Constants.DEFAULT_MAP_DECLARATION_LINE)

        // List normal classes.
        val normalLabels = mutableListOf<Label>()
        ScannerCache.CACHE.getNormalClassesMap().forEach { (tag, classMap) ->
            val label = fillMapsWithEachClasses(
                tag,
                classMap,
                Constants.SCANNER_NORMAL_MAP_NAME
            )
            normalLabels.add(label)
        }
        // List default classes.
        val defaultLabels = mutableListOf<Label>()
        ScannerCache.CACHE.getDefaultClassesMap().forEach { (tag, classMap) ->
            val label = fillMapsWithEachClasses(
                tag,
                classMap,
                Constants.SCANNER_DEFAULT_MAP_NAME
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

    private fun initializeMaps(mapFieldName: String, mapFieldDeclarationLine: Int) {
        val label0 = Label()
        methodVisitor.visitLabel(label0)
        methodVisitor.visitLineNumber(mapFieldDeclarationLine, label0)
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
            Opcodes.PUTSTATIC, Constants.SCANNER_CLASS_NAME, mapFieldName,
            "Ljava/util/Map;"
        )
    }

    private fun fillMapsWithEachClasses(
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
            insertClass(classIndex, it)
            classIndex++
        }
        methodVisitor.visitVarInsn(Opcodes.ASTORE, 0)

        // Put classes into map
        lineNumber += classSize + 1
        val label1 = Label()
        methodVisitor.visitLabel(label1)
        methodVisitor.visitLineNumber(lineNumber, label1)
        methodVisitor.visitFieldInsn(
            Opcodes.GETSTATIC, Constants.SCANNER_CLASS_NAME, mapFieldName,
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
        lineNumber++
        return label1
    }

    private fun insertClass(classIndex: Int, className: String) {
        methodVisitor.visitInsn(Opcodes.DUP)
        methodVisitor.visitInsn(classIndex + 3)
        methodVisitor.visitLdcInsn(Type.getType("L${className};"))
        methodVisitor.visitInsn(Opcodes.AASTORE)
    }
}