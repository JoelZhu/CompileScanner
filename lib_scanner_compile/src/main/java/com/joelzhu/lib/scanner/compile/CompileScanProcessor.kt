package com.joelzhu.lib.scanner.compile

import com.joelzhu.lib.scanner.annotation.CompileScan
import com.joelzhu.lib.scanner.annotation.Constants
import java.io.*
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

/**
 * The processor to process the annotation: [CompileScan][CompileScan]
 *
 * @author JoelZhu
 * @since 2023-01-20
 */
@SupportedOptions(Options.APP_MODULE, Options.WITH_ANNO, Options.IS_MULTI_MODULE,
        Options.MODULE_PACKAGE, Options.ARTIFACT_TYPE)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class CompileScanProcessor : AbstractProcessor() {
    companion object {
        // Java format placeholder.
        const val PH = "    "

        const val SPLIT_SYMBOL = ":"

        const val CACHE_FILE = "scanner.build.cache"
    }

    private var scannedClasses: MutableMap<String, MutableMap<String, Int>> = mutableMapOf()

    private var sortedClasses: MutableMap<String, MutableList<String>> = mutableMapOf()

    private lateinit var writer: Writer

    private var isAppModule: Boolean = false

    private var appModuleWithAnno: Boolean = false

    // Consider the project as multi modules project as default, if not, it will be updated in init
    private var isMultiModule: Boolean = true

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(CompileScan::class.java.canonicalName)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedOptions(): MutableSet<String> {
        return mutableSetOf(Options.APP_MODULE, Options.WITH_ANNO, Options.IS_MULTI_MODULE,
                Options.MODULE_PACKAGE, Options.ARTIFACT_TYPE)
    }

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        printProcessorLog("Initializing...")

        var isAppModule = true
        var modulePackage = ""
        processingEnv?.options?.forEach { (key, value) ->
            printProcessorLog("Option: $key - $value")
            if (Options.ARTIFACT_TYPE == key && "LIBRARY" == value) {
                isAppModule = false
            } else if (Options.MODULE_PACKAGE == key) {
                modulePackage = value
            }
        }

        isAppModule = "true" == processingEnv?.options?.get(Options.APP_MODULE)
        appModuleWithAnno = "true" == processingEnv?.options?.get(Options.WITH_ANNO)
        printProcessorLog("ModulePackage: $modulePackage, isAppModule: $isAppModule")

        // Project has more than one modules, must use file to cache annotated classes
        if (!isAppModule) {
            // Module is library, will scan annotated classes in method: process
            printProcessorLog("Module is not application, won't generate file")
            return
        }

        // Current module is application, will read parameter: isMultiModule
        isMultiModule = "true" == processingEnv?.options?.get(Options.IS_MULTI_MODULE)
        if (!isMultiModule) {
            // Project only has one module, and that is application module, will cache in memory
            printProcessorLog("Project is not multi module, won't cache using file")
            return
        }

        // Module is application
        if (!appModuleWithAnno) {
            // Application module without any annotations, must generate code here, because won't
            // running into method: process
            printProcessorLog("Module is application, to generate file")
            // To generate code
            startToGenerateCode()
        } else {
            // Application module still with annotation, should process it first, and generate code
            // after scanning classes in application module.
            printProcessorLog("Module is application, but still have annotation classes")
        }
    }

    override fun process(elements: MutableSet<out TypeElement>?, env: RoundEnvironment?): Boolean {
        if (elements == null || elements.size == 0) {
            return false
        }

        printProcessorLog("Processing... Is multi module: $isMultiModule.")

        // Scan annotated classes
        scanAnnotatedClasses(env, isMultiModule)

        if (!isMultiModule || isAppModule) {
            // To generate if app module with annotated classes in it.
            // Two cases to generate code here:
            // 1. Project only with one module;
            // 2. Project has more than one module, and current one is application module. And
            // application module still has annotated classes.
            startToGenerateCode()
        }

        printProcessorLog("Process ending...")
        return true
    }

    private fun scanAnnotatedClasses(env: RoundEnvironment?, cacheUsingFile: Boolean = false) {
        env?.getElementsAnnotatedWith(CompileScan::class.java)?.forEach { element ->
            val compileScan = element.getAnnotation(CompileScan::class.java)
            val className = "${element.enclosingElement}.${element.simpleName}"

            // Put the class into map.
            cacheClassesIntoMemory(compileScan.tag, className, compileScan.priority)
            printProcessorLog("Find annotated class: $className, it's tag: ${compileScan.tag}, priority: ${compileScan.priority}")
        }

        if (cacheUsingFile) {
            // Cache the classes into file
            toCacheInFile()
        }
    }

    private fun startToGenerateCode() {
        // Sort the classes first
        sortAnnotatedClasses()
        // Generate code
        writeGeneratingCode()
    }

    private fun cacheClassesIntoMemory(classTag: String, className: String, classPriority: Int) {
        var classesWithTag = scannedClasses[classTag]
        if (classesWithTag == null) {
            classesWithTag = mutableMapOf()
        }
        classesWithTag[className] = classPriority
        scannedClasses[classTag] = classesWithTag
    }

    private fun toCacheInFile() {
        var outputStream: FileOutputStream? = null
        var writer: OutputStreamWriter? = null

        try {
            val cacheFile = File(getCacheFilePath())
            if (!cacheFile.exists()) {
                cacheFile.createNewFile()
            }

            outputStream = FileOutputStream(cacheFile, true)
            writer = OutputStreamWriter(outputStream)
            scannedClasses.forEach { (classTag, classPriorityMap) ->
                classPriorityMap.forEach { (className, priority) ->
                    val writingContent = toCacheInFileString(classTag, className, priority)
                    printProcessorLog("To write into file: $writingContent")
                    writer.append(writingContent).append("\n")
                }
            }

            writer.flush()
        } catch (exception: IOException) {
            printProcessorError("To cache into file got IO exception")
            printException(exception)
        } catch (exception: FileNotFoundException) {
            printProcessorError("To cache into file, but didn't find the file")
            printException(exception)
        } finally {
            try {
                outputStream?.close()
            } catch (exception: IOException) {
                // Do nothing.
            }
            try {
                writer?.close()
            } catch (exception: IOException) {
                // Do nothing.
            }
        }
    }

    private fun readFromFile() {
        var reader: BufferedReader? = null
        val fileReader = FileReader(getCacheFilePath())
        try {
            reader = BufferedReader(fileReader)
            var readString: String?
            while (true) {
                readString = reader.readLine()
                if (readString == null) {
                    break
                }

                val subStrings = readString.split(SPLIT_SYMBOL)
                if (subStrings.size != 3) {
                    printProcessorError("Processor ran into a bug, please report it to owner")
                }

                var priority: Int = -1
                try {
                    priority = Integer.parseInt(subStrings[2])
                } catch (exception: NumberFormatException) {
                    printProcessorError("Processor ran into a bug, please report it to owner")
                }

                // Cache into memory from file
                cacheClassesIntoMemory(subStrings[0], subStrings[1], priority)
            }
        } catch (exception: FileNotFoundException) {
            printProcessorLog("Cache file not found, will generate nothing.")
            printException(exception)
        } finally {
            try {
                reader?.close()
            } catch (exception: IOException) {
                // Do nothing.
            }
            try {
                fileReader.close()
            } catch (exception: IOException) {
                // Do nothing.
            }
        }
    }

    private fun sortAnnotatedClasses() {
        scannedClasses.keys.forEach { tag ->
            val classPriorityMap = scannedClasses[tag]
            val sortedMap = classPriorityMap?.toSortedMap(compareBy { classPriorityMap[it] })
            val sortedList = mutableListOf<String>()
            sortedMap?.keys?.forEach { sortedList.add(it) }
            sortedClasses[tag] = sortedList
        }
    }

    private fun writeGeneratingCode() {
        openWriteStream()
        try {
            // To write down the generating-code
            writeClassDeclaration()
            writePrivateConstructor()
            writeClassesGetter()
            writeInstancesGetter()
            writeClassEnding()
        } catch (exception: IOException) {
            printProcessorError("Write file got exception: " + exception.message)
            printException(exception)
        } finally {
            closeWriteStream()
        }
    }

    private fun toDeleteCacheFile() {
        // To delete the cache file
        val cacheFile = File(getCacheFilePath())
        val deleteResult = cacheFile.delete()
        printProcessorLog("Delete cache file result: $deleteResult.")
    }

    private fun openWriteStream() {
        try {
            val fileObject = processingEnv!!.filer.createSourceFile(Constants.CLASS_NAME)
            writer = fileObject.openWriter()
        } catch (exception: IOException) {
            printProcessorError("Create file got exception: " + exception.message)
            printException(exception)
        }
    }

    private fun closeWriteStream() {
        try {
            writer.flush()
            writer.close()
        } catch (exception: IOException) {
            printProcessorError("Close stream got exception: " + exception.message)
            printException(exception)
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
        sortedClasses.keys.forEach { tag ->
            val classesList = sortedClasses[tag]
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
        sortedClasses.keys.forEach { tag ->
            val classesList = sortedClasses[tag]
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

    private fun toCacheInFileString(classTag: String, className: String, priority: Int): String {
        return "$classTag$SPLIT_SYMBOL$className$SPLIT_SYMBOL$priority"
    }

    private fun getCacheFilePath(): String {
        return CACHE_FILE
    }

    private fun printProcessorLog(logContent: String) {
        processingEnv?.messager?.printMessage(Diagnostic.Kind.NOTE, "CompileScanProcessor: $logContent")
    }

    private fun printProcessorError(logContent: String) {
        processingEnv?.messager?.printMessage(Diagnostic.Kind.ERROR, "CompileScanProcessor: $logContent")
    }

    private fun printException(exception: java.lang.Exception) {
        val stackTraces = exception.stackTrace
        printProcessorLog(exception.message!!)
        stackTraces.forEach { stackTrace ->
            printProcessorLog("${stackTrace.className}.${stackTrace.methodName}:${stackTrace.lineNumber}")
        }
    }
}