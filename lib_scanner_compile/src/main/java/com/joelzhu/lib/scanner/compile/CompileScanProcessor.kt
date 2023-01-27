package com.joelzhu.lib.scanner.compile

import com.joelzhu.lib.scanner.annotation.CompileScan
import com.joelzhu.lib.scanner.annotation.Constants
import java.io.IOException
import java.io.Writer
import java.util.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

/**
 * The processor to process the annotation: [CompileScan][CompileScan]
 *
 * @author JoelZhu
 * @since 2023-01-20
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class CompileScanProcessor : AbstractProcessor() {
    companion object {
        // Java format placeholder.
        const val PH = "    "
    }

    private lateinit var writer: Writer

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        printProcessorLog("Initializing...")
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(CompileScan::class.java.canonicalName)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun process(elements: MutableSet<out TypeElement>?, env: RoundEnvironment?): Boolean {
        if (elements == null || elements.size == 0) {
            return true
        }

        printProcessorLog("Processing...")
        val classesMap = mutableMapOf<String, MutableList<String>>()
        scanAnnotatedClasses(env, classesMap)

        try {
            openWriteStream()
            writeClassDeclaration()
            writeConstants(classesMap)
            writePrivateConstructor()
            writeGetter(classesMap)
            writeClassEnding()
        } catch (exception: IOException) {
            printProcessorError("Write file got exception: " + exception.message)
        } finally {
            closeWriteStream()
        }

        printProcessorLog("Process ending...")
        return true
    }

    private fun openWriteStream() {
        try {
            val fileObject = processingEnv!!.filer.createSourceFile(Constants.CLASS_NAME)
            writer = fileObject.openWriter()
        } catch (exception: IOException) {
            printProcessorError("Create file got exception: " + exception.message)
        }
    }

    private fun closeWriteStream() {
        try {
            writer.flush()
            writer.close()
        } catch (exception: IOException) {
            printProcessorError("Close stream got exception: " + exception.message)
        }
    }

    private fun scanAnnotatedClasses(env: RoundEnvironment?, classesMap: MutableMap<String, MutableList<String>>) {
        env?.getElementsAnnotatedWith(CompileScan::class.java)?.forEach { element ->
            val compileScan = element.getAnnotation(CompileScan::class.java)
            val className = "${element.enclosingElement}.${element.simpleName}.class"
            var classesList = classesMap[compileScan.tag]
            if (classesList == null) {
                classesList = mutableListOf()
            }
            classesList.add(className)
            classesMap[compileScan.tag] = classesList
            printProcessorLog("Find annotated class: $className, it's tag: $compileScan.tag")
        }
    }

    private fun writeClassDeclaration() {
        writer.append("package ${Constants.CLASS_PACKAGE};").append("\n")
        writer.append("\n")
        writer.append("public final class ${Constants.CLASS_NAME} {").append("\n")
    }

    private fun writeConstants(classesMap: MutableMap<String, MutableList<String>>) {
        classesMap.keys.forEach { tag ->
            val classesList = classesMap[tag]
            writer.append("${PH}private static final Class<?>[] ${getConstantsName(tag)} = new Class[] {").append("\n")
            classesList?.forEach { clazz ->
                writer.append("${PH}${PH}$clazz").append(",").append("\n")
            }
            writer.append("${PH}};").append("\n")
            writer.append("\n")
        }
    }

    private fun writePrivateConstructor() {
        writer.append("${PH}private ${Constants.CLASS_NAME}() {}").append("\n")
    }

    private fun writeGetter(classesMap: MutableMap<String, MutableList<String>>) {
        writer.append("\n")
        writer.append("${PH}public static Class<?>[] ${Constants.GETTER_METHOD_NAME}(final String tag) {").append("\n")
        writer.append("${PH}${PH}final Class<?>[] classesArray;").append("\n")
        writer.append("${PH}${PH}switch(tag) {").append("\n")
        classesMap.keys.forEach { tag ->
            writer.append("${PH}${PH}${PH}case \"$tag\":").append("\n")
            writer.append("${PH}${PH}${PH}${PH}classesArray = ${getConstantsName(tag)};").append("\n")
            writer.append("${PH}${PH}${PH}${PH}break;").append("\n")
        }
        writer.append("${PH}${PH}${PH}default:").append("\n")
        writer.append("${PH}${PH}${PH}${PH}classesArray = new Class<?>[0];").append("\n")
        writer.append("${PH}${PH}${PH}${PH}break;").append("\n")
        writer.append("${PH}${PH}}").append("\n")
        writer.append("${PH}${PH}return classesArray;").append("\n")
        writer.append("${PH}}").append("\n")
    }

    private fun writeClassEnding() {
        writer.append("}")
    }

    private fun getConstantsName(tag: String): String {
        return "CLASSES_${tag.uppercase()}"
    }

    private fun printProcessorLog(logContent: String) {
        processingEnv?.messager?.printMessage(Diagnostic.Kind.NOTE, "CompileScanProcessor: $logContent")
    }

    private fun printProcessorError(logContent: String) {
        processingEnv?.messager?.printMessage(Diagnostic.Kind.ERROR, "CompileScanProcessor: $logContent")
    }
}