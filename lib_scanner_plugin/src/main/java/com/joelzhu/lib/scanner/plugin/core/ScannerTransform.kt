package com.joelzhu.lib.scanner.plugin.core

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import com.joelzhu.lib.scanner.plugin.util.LogUtil
import org.apache.commons.codec.digest.DigestUtils
import java.io.File

/**
 * Transform of scanner.
 * To scan classes which annotated with the annotation: [CompileScan][com.joelzhu.lib.scanner.annotation.CompileScan]
 *
 * @author JoelZhu
 * @since 2023-03-30
 */
class ScannerTransform : Transform() {
    override fun getName(): String {
        return ScannerTransform::class.java.simpleName
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun isIncremental(): Boolean {
        return false
    }

    override fun transform(invocation: TransformInvocation?) {
        if (!isIncremental) {
            LogUtil.printEmptyLine()
            LogUtil.printLog("Not incremental compile, to delete all provider output.")
            invocation?.outputProvider?.deleteAll()
        }

        if (invocation == null) {
            LogUtil.printLog("TransformInvocation is null, ignore transform.")
            return
        }

        TransformHandler.onCompileStarted()

        // Find out how many directory inputs with classes in it.
        LogUtil.printEmptyLine()
        LogUtil.printLog("To find all the directoryInputs which with class(es) in it.")
        scanDirInputWhichWithClasses(invocation)
        LogUtil.printEmptyLine()

        // To iterate each input, to find all the annotated classes.
        var dirInputsWithClasses: MutableSet<String>? =
            TransformHandler.getDirectoryInputsWithClasses()
        invocation.outputProvider?.let { provider ->
            invocation.inputs?.forEach { inputs ->
                inputs.jarInputs.forEach { jarInput ->
                    if (jarInput.scopes.contains(QualifiedContent.Scope.SUB_PROJECTS)) {
                        // Only scan implementation project or scanner-runtime
                        TransformHandler.scanJarInput(jarInput)
                    }

                    copyJarInput(provider, jarInput)
                }

                inputs.directoryInputs.forEach { dirInput ->
                    val dirInputName = dirInput.file.name
                    dirInputsWithClasses?.remove(dirInputName)
                    LogUtil.printEmptyLine()
                    LogUtil.printLog("To scan the dirInput: $dirInputName.")
                    TransformHandler.scanDirectoryInput(dirInput)

                    // If the set is empty, means the code had already generated into the directory.
                    if (dirInputsWithClasses != null && dirInputsWithClasses!!.size == 0) {
                        LogUtil.printEmptyLine()
                        LogUtil.printLog("Ready to insert code.")
                        TransformHandler.generateInjectingCode()
                        dirInputsWithClasses = null
                    }

                    copyDirInput(provider, dirInput)
                }
            }
        }
    }

    private fun scanDirInputWhichWithClasses(invocation: TransformInvocation) {
        invocation.inputs?.forEach { inputs ->
            inputs.directoryInputs.forEach { dirInput ->
                TransformHandler.scanDirectoryInputWhichWithClasses(dirInput, dirInput.file.name)
            }
        }

        var logContent = "List of dirInput(s) with class(es) in it: "
        TransformHandler.getDirectoryInputsWithClasses().forEachIndexed { index, dirInputName ->
            logContent += if (index == 0) {
                dirInputName
            } else {
                ", $dirInputName"
            }
        }
        LogUtil.printLog(logContent)
    }

    private fun copyJarInput(provider: TransformOutputProvider, jarInput: JarInput) {
        val destJarInput = getJarOutputFile(provider, jarInput)
        FileUtils.copyFile(jarInput.file, destJarInput)
    }

    private fun copyDirInput(provider: TransformOutputProvider, dirInput: DirectoryInput) {
        val destDirInput = provider.getContentLocation(
            dirInput.name,
            dirInput.contentTypes,
            dirInput.scopes,
            Format.DIRECTORY
        )
        LogUtil.printLog("To copy dir into file: ${destDirInput.path}.")
        FileUtils.copyDirectory(dirInput.file, destDirInput)
    }

    private fun getJarOutputFile(provider: TransformOutputProvider, jarInput: JarInput): File {
        val destName = if (jarInput.name.endsWith(".jar")) {
            jarInput.name.substring(0, jarInput.name.length - 4)
        } else {
            jarInput.name
        }

        val jarPathHex = DigestUtils.md5Hex(jarInput.file.absolutePath)
        return provider.getContentLocation(
            "${destName}_${jarPathHex}",
            jarInput.contentTypes, jarInput.scopes, Format.JAR
        )
    }
}