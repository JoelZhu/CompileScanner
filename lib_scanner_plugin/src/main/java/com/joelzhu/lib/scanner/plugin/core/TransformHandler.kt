package com.joelzhu.lib.scanner.plugin.core

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.JarInput
import com.android.utils.FileUtils
import com.joelzhu.lib.scanner.annotation.ImplConstants
import com.joelzhu.lib.scanner.plugin.util.LogUtil
import com.joelzhu.lib.scanner.plugin.util.ScannerCache
import com.joelzhu.lib.scanner.plugin.visitor.ClassScanVisitor
import com.joelzhu.lib.scanner.plugin.visitor.DirectoryWithClassScanVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.FileOutputStream
import java.io.InputStream
import java.util.jar.JarFile

/**
 * [Description here].
 *
 * @author JoelZhu
 * @since 2023-04-04
 */
object TransformHandler {
    private var buildPath: String? = null

    private val dirWithClass = mutableSetOf<String>()

    fun onCompileStarted() {
        LogUtil.printLog("Scanner compile started.")
        ScannerCache.CACHE.initializeCompile()
    }

    fun scanJarInput(jarInput: JarInput) {
        val originJarFile = JarFile(jarInput.file)
        val originJarEntries = originJarFile.entries()
        while (originJarEntries.hasMoreElements()) {
            val originJarEntry = originJarEntries.nextElement()

            val inputStream = originJarFile.getInputStream(originJarEntry)
            if (originJarEntry.name.endsWith(".class")) {
                scanAnnotatedClasses(inputStream, null)
            }
            inputStream.close()
        }
    }

    fun scanDirectoryInput(dirInput: DirectoryInput) {
        FileUtils.getAllFiles(dirInput.file).forEach { file ->
            if (!file.name.endsWith(".class")) {
                return
            }

            if (!file.exists()) {
                LogUtil.printLog("To scan file not exists, file path: ${file.absolutePath}")
                return
            }

            LogUtil.printLog("To scan file: ${file.path}.")
            val inputStream = file.inputStream()
            scanAnnotatedClasses(inputStream, file.path)
            inputStream.close()
        }
    }

    fun scanDirectoryInputWhichWithClasses(dirInput: DirectoryInput, dirInputName: String) {
        FileUtils.getAllFiles(dirInput.file).forEach { file ->
            if (!file.name.endsWith(".class")) {
                return
            }

            if (!file.exists()) {
                return
            }

            val inputStream = file.inputStream()
            val reader = ClassReader(inputStream)
            val writer = ClassWriter(reader, 0)
            val visitor = DirectoryWithClassScanVisitor(writer, dirInputName)
            reader.accept(visitor, ClassReader.EXPAND_FRAMES)
            inputStream.close()
        }
    }

    fun getDirectoryInputsWithClasses(): MutableSet<String> {
        return dirWithClass
    }

    fun updateBuildPath(buildPath: String) {
        LogUtil.printLog("Update build path: $buildPath.")
        this.buildPath = buildPath
    }

    fun updateDirectoryWithClasses(dirInputName: String) {
        if (dirWithClass.contains(dirInputName)) {
            return
        }
        
        LogUtil.printLog("Found class(es) in dirInput: $dirInputName.")
        dirWithClass.add(dirInputName)
    }

    fun generateInjectingCode() {
        buildPath?.let {
            val outputStream = FileOutputStream("$it${ImplConstants.IMPL_CLASS}.class")
            outputStream.write(ScannerImplDump.dump())
            outputStream.flush()
            outputStream.close()
        }
    }

    private fun scanAnnotatedClasses(inputStream: InputStream, fileAbsPath: String?) {
        val reader = ClassReader(inputStream)
        val writer = ClassWriter(reader, 0)
        val visitor = ClassScanVisitor(writer, fileAbsPath)
        reader.accept(visitor, ClassReader.EXPAND_FRAMES)
    }
}