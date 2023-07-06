package com.joelzhu.lib.scanner.plugin.core

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.TransformInvocation
import com.android.utils.FileUtils
import com.joelzhu.lib.scanner.plugin.code.visitor.generate.GenerateCodeClassVisitor
import com.joelzhu.lib.scanner.plugin.code.visitor.scan.InjectPlaceClassVisitor
import com.joelzhu.lib.scanner.plugin.code.visitor.scan.ScanAnnotatedClassVisitor
import com.joelzhu.lib.scanner.plugin.util.LogUtil
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.InputStream
import java.util.jar.JarFile

/**
 * Handle to scan annotated classes, and generate method to acquire those classes.
 *
 * @author JoelZhu
 * @since 2023-04-04
 */
object ScanHelper {
    private var injectClassJarInput: JarInput? = null

    private var injectClassName: String? = null

    fun toFindInjectPlace(invocation: TransformInvocation) {
        // Find out how many directory inputs with classes in it.
        LogUtil.printEmptyLine()
        LogUtil.printInfo("To scan inject place for compiling.")

        run scan@{
            invocation.inputs?.forEach { inputs ->
                // scanJarInputWithInjectPlace
                inputs.jarInputs.forEach { jarInput ->
                    if (isInjectClassExists()) {
                        LogUtil.printInfo("Inject place found, break scanning for inject place.")
                        return@scan
                    }

                    scanClassesInJarInput(jarInput) {
                        scanInjectClasses(jarInput, it)
                    }
                }
            }
        }

        // Print inject class.
        injectClassJarInput?.let {
            LogUtil.printInfo("Found annotated inject class in jarInput: ${it.name}.")
        } ?: run {
            LogUtil.printError("Didn't find annotated inject class.")
        }
    }

    fun toScanAnnotatedClasses(invocation: TransformInvocation, onJarInputScanFinished: (JarInput) -> Unit,
        onDirInputScanFinished: (DirectoryInput) -> Unit) {
        invocation.inputs?.forEach { inputs ->
            inputs.jarInputs.forEach { jarInput ->
                if (jarInput.scopes.contains(QualifiedContent.Scope.SUB_PROJECTS)) {
                    // Only scan implementation project or scanner-runtime
                    toScanJarInputInternal(jarInput)
                }
                onJarInputScanFinished(jarInput)
            }

            inputs.directoryInputs.forEach { dirInput ->
                toScanDirectoryInputInternal(dirInput)
                onDirInputScanFinished(dirInput)
            }
        }
    }

    fun updateInjectClassJarInputName(jarInput: JarInput, className: String) {
        if (isInjectClassExists()) {
            return
        }

        LogUtil.printInfo(
            "Found inject class(name: $className) in jarInput(name: ${jarInput.name}).")
        this.injectClassJarInput = jarInput
        this.injectClassName = className
    }

    fun generateInjectingCode(): JarInput? {
        LogUtil.printEmptyLine()
        LogUtil.printInfo("Ready to insert code.")

        if (injectClassJarInput == null || injectClassName == null) {
            LogUtil.printError("Illegal place class to inject.")
            return null
        }

        val originJarFile = JarFile(injectClassJarInput!!.file)
        val originJarEntries = originJarFile.entries()
        while (originJarEntries.hasMoreElements()) {
            val originJarEntry = originJarEntries.nextElement()

            val inputStream = originJarFile.getInputStream(originJarEntry)
            if (originJarEntry.name.endsWith(".class")) {
                val reader = ClassReader(inputStream)
                val writer = ClassWriter(reader, 0)
                val visitor = GenerateCodeClassVisitor(writer, injectClassName!!)
                reader.accept(visitor, ClassReader.EXPAND_FRAMES)
            }
            inputStream.close()
        }
        return injectClassJarInput
    }

    private fun toScanJarInputInternal(jarInput: JarInput) {
        LogUtil.printInfo("To scan jar input: ${jarInput.name}.")
        if (jarInput.name != injectClassJarInput?.name) {
            scanClassesInJarInput(jarInput) {
                scanAnnotatedClasses(it)
            }
        } else {
            LogUtil.printInfo(
                "There's inject class in it(name: ${jarInput.name}), will handle it later.")
        }
    }

    private fun toScanDirectoryInputInternal(dirInput: DirectoryInput) {
        LogUtil.printInfo("To scan directory input: ${dirInput.name}.")
        FileUtils.getAllFiles(dirInput.file).forEach { file ->
            if (!file.name.endsWith(".class")) {
                return
            }

            LogUtil.printDebug("To scan file: ${file.name} in dirInput.")
            if (!file.exists()) {
                LogUtil.printError("To scan file not exists, expect path: ${file.path}.")
                return
            }

            val inputStream = file.inputStream()
            scanAnnotatedClasses(inputStream)
            inputStream.close()
        }
    }

    private fun scanClassesInJarInput(jarInput: JarInput, onJarEntryGot: (InputStream) -> Unit) {
        val originJarFile = JarFile(jarInput.file)
        val originJarEntries = originJarFile.entries()
        while (originJarEntries.hasMoreElements()) {
            val originJarEntry = originJarEntries.nextElement()

            val inputStream = originJarFile.getInputStream(originJarEntry)
            if (originJarEntry.name.endsWith(".class")) {
                onJarEntryGot(inputStream)
            }
            inputStream.close()
        }
    }

    private fun scanInjectClasses(jarInput: JarInput, inputStream: InputStream) {
        val reader = ClassReader(inputStream)
        val writer = ClassWriter(reader, 0)
        val visitor = InjectPlaceClassVisitor(writer, jarInput)
        reader.accept(visitor, ClassReader.EXPAND_FRAMES)
    }

    private fun scanAnnotatedClasses(inputStream: InputStream) {
        val reader = ClassReader(inputStream)
        val writer = ClassWriter(reader, 0)
        val visitor = ScanAnnotatedClassVisitor(writer)
        reader.accept(visitor, ClassReader.EXPAND_FRAMES)
    }

    private fun isInjectClassExists(): Boolean {
        return injectClassJarInput != null
    }
}