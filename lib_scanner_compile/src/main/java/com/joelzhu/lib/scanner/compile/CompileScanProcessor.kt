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

    private lateinit var scannedClasses: MutableMap<String, MutableList<String>>

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
        scannedClasses = mutableMapOf()
        scanAnnotatedClasses(env)

        // To write down the code
        openWriteStream()
        try {
            writeClassDeclaration()
            writePrivateConstructor()
            writeClassesGetter()
            writeInstancesGetter()
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

    private fun scanAnnotatedClasses(env: RoundEnvironment?) {
        env?.getElementsAnnotatedWith(CompileScan::class.java)?.forEach { element ->
            val compileScan = element.getAnnotation(CompileScan::class.java)
            val className = "${element.enclosingElement}.${element.simpleName}"

            // Put the class into map.
            var classesWithTag = scannedClasses[compileScan.tag]
            if (classesWithTag == null) {
                classesWithTag = mutableListOf()
            }
            classesWithTag.add(className)
            scannedClasses[compileScan.tag] = classesWithTag
            printProcessorLog("Find annotated class: $className, it's tag: ${compileScan.tag}")
        }
    }

    @kotlin.jvm.Throws(IOException::class)
    private fun writeClassDeclaration() {
        writer.append("package ${Constants.CLASS_PACKAGE};\n")
        writer.append("\n")
        writer.append("public final class ${Constants.CLASS_NAME} {\n")
    }

    @kotlin.jvm.Throws(IOException::class)
    private fun writePrivateConstructor() {
        writer.append("${PH}private ${Constants.CLASS_NAME}() {}\n")
    }

    @kotlin.jvm.Throws(IOException::class)
    private fun writeClassesGetter() {
        writer.append("\n")
        writer.append("${PH}public static Class<?>[] ${Constants.GET_CLASS_METHOD_NAME}(final String tag) {\n")
        writer.append("${PH}${PH}final Class<?>[] classesArray;\n")
        writer.append("${PH}${PH}switch(tag) {\n")
        scannedClasses.keys.forEach { tag ->
            val classesList = scannedClasses[tag]
            writer.append("${PH}${PH}${PH}case \"$tag\":\n")
            writer.append("${PH}${PH}${PH}${PH}classesArray = new Class<?>[] {\n")
            classesList?.forEach { className ->
                writer.append("${PH}${PH}${PH}${PH}${PH}$className.class,\n")
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

    @kotlin.jvm.Throws(IOException::class)
    private fun writeInstancesGetter() {
        writer.append("\n")
        writer.append("${PH}public static Object[] ${Constants.GET_INSTANCE_METHOD_NAME}(final String tag) {\n")
        writer.append("${PH}${PH}final Object[] instancesArray;\n")
        writer.append("${PH}${PH}switch(tag) {\n")
        scannedClasses.keys.forEach { tag ->
            val classesList = scannedClasses[tag]
            writer.append("${PH}${PH}${PH}case \"$tag\":\n")
            writer.append("${PH}${PH}${PH}${PH}instancesArray = new Object[] {\n")
            classesList?.forEach { className ->
                writer.append("${PH}${PH}${PH}${PH}${PH}new $className(),\n")
            }
            writer.append("${PH}${PH}${PH}${PH}};\n")
            writer.append("${PH}${PH}${PH}${PH}break;\n")
        }
        writer.append("${PH}${PH}${PH}default:\n")
        writer.append("${PH}${PH}${PH}${PH}instancesArray = new Object[0];\n")
        writer.append("${PH}${PH}${PH}${PH}break;\n")
        writer.append("${PH}${PH}}\n")
        writer.append("${PH}${PH}return instancesArray;\n")
        writer.append("${PH}}\n")
    }

    @kotlin.jvm.Throws(IOException::class)
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