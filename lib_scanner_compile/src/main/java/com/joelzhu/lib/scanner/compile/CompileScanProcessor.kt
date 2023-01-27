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
            writePrivateConstructor()
            writeClassesGetter(classesMap)
            writeInstancesGetter(classesMap)
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
        writer.append("package ${Constants.CLASS_PACKAGE};\n")
        writer.append("\n")
        writer.append("public final class ${Constants.CLASS_NAME} {\n")
    }

    private fun writePrivateConstructor() {
        writer.append("${PH}private ${Constants.CLASS_NAME}() {}\n")
    }

    private fun writeClassesGetter(classesMap: MutableMap<String, MutableList<String>>) {
        writer.append("\n")
        writer.append("${PH}public static Class<?>[] ${Constants.GET_CLASS_METHOD_NAME}(final String tag) {\n")
        writer.append("${PH}${PH}final Class<?>[] classesArray;\n")
        writer.append("${PH}${PH}switch(tag) {\n")
        classesMap.keys.forEach { tag ->
            val classesList = classesMap[tag]
            writer.append("${PH}${PH}${PH}case \"$tag\":\n")
            writer.append("${PH}${PH}${PH}${PH}classesArray = new Class<?>[] {\n")
            classesList?.forEach { clazz ->
                writer.append("${PH}${PH}${PH}${PH}${PH}$clazz,\n")
            }
            writer.append("${PH}${PH}${PH}${PH}};\n")
            writer.append("${PH}${PH}${PH}${PH}break;\n")
        }
        writer.append("${PH}${PH}${PH}default:\n")
        writer.append("${PH}${PH}${PH}${PH}classesArray = new Class<?>[0];\n")
        writer.append("${PH}${PH}${PH}${PH}break;\n")
        writer.append("${PH}${PH}}\n")
        writer.append("${PH}${PH}return classesArray;\n")
        writer.append("${PH}}\n")
    }

    private fun writeInstancesGetter(classesMap: MutableMap<String, MutableList<String>>) {
        writer.append("\n")
        writer.append("${PH}public static <T> T[] ${Constants.GET_INSTANCE_METHOD_NAME}(final String tag, final Class<T> instanceClass) {\n")
        writer.append("${PH}${PH}final T[] instancesArray;\n")
        writer.append("${PH}${PH}switch(tag) {\n")
        classesMap.keys.forEach { tag ->
            val classesList = classesMap[tag]
            writer.append("${PH}${PH}${PH}case \"$tag\":\n")
            writer.append("${PH}${PH}${PH}${PH}instancesArray = (T[]) new Object[] {\n")
            classesList?.forEach { clazz ->
                writer.append("${PH}${PH}${PH}${PH}${PH}new ${clazz.replace(".class", "")}(),\n")
            }
            writer.append("${PH}${PH}${PH}${PH}};\n")
            writer.append("${PH}${PH}${PH}${PH}break;\n")
        }
        writer.append("${PH}${PH}${PH}default:\n")
        writer.append("${PH}${PH}${PH}${PH}instancesArray = (T[]) new Object[0];\n")
        writer.append("${PH}${PH}${PH}${PH}break;\n")
        writer.append("${PH}${PH}}\n")
        writer.append("${PH}${PH}return instancesArray;\n")
        writer.append("${PH}}\n")
    }

    private fun writeClassEnding() {
        writer.append("}")
    }

    private fun printProcessorLog(logContent: String) {
        processingEnv?.messager?.printMessage(Diagnostic.Kind.NOTE, "CompileScanProcessor: $logContent")
    }

    private fun printProcessorError(logContent: String) {
        processingEnv?.messager?.printMessage(Diagnostic.Kind.ERROR, "CompileScanProcessor: $logContent")
    }
}