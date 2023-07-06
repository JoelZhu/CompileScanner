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
import com.joelzhu.lib.scanner.plugin.code.cache.CacheHandler
import com.joelzhu.lib.scanner.plugin.code.generator.CodeGenerator
import com.joelzhu.lib.scanner.plugin.util.LogUtil
import org.apache.commons.codec.digest.DigestUtils

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
            LogUtil.printInfo("Not incremental compile, to delete all provider output.")
            invocation?.outputProvider?.deleteAll()
        }

        if (invocation == null) {
            LogUtil.printError("TransformInvocation is null, ignore transform.")
            return
        }

        // Start compile.
        CacheHandler.initializeCompilation()

        // Scan for finding inject class.
        ScanHelper.toFindInjectPlace(invocation)

        // To iterate each input, to find all the annotated classes.
        invocation.outputProvider?.let { provider ->
            ScanHelper.toScanAnnotatedClasses(invocation, { copyJarInput(provider, it) },
                { copyDirInput(provider, it) })

            // Preparing data.
            CodeGenerator.prepareData(CacheHandler.getCachedData())

            // To inject code in jarInput.
            ScanHelper.generateInjectingCode()?.let {
                LogUtil.printInfo("Generate finished, to copy jarInput.")
                copyJarInput(provider, it)
            }
        }
    }

    private fun copyJarInput(provider: TransformOutputProvider, jarInput: JarInput) {
        val destName = if (jarInput.name.endsWith(".jar")) {
            jarInput.name.substring(0, jarInput.name.length - 4)
        } else {
            jarInput.name
        }

        val jarPathHex = DigestUtils.md5Hex(jarInput.file.absolutePath)
        FileUtils.copyFile(jarInput.file,
            provider.getContentLocation("${destName}_${jarPathHex}", jarInput.contentTypes,
                jarInput.scopes, Format.JAR))
    }

    private fun copyDirInput(provider: TransformOutputProvider, dirInput: DirectoryInput) {
        val destDirInput =
            provider.getContentLocation(dirInput.name, dirInput.contentTypes, dirInput.scopes,
                Format.DIRECTORY)
        LogUtil.printInfo("To copy dir: ${dirInput.file.path} into file: ${destDirInput.path}.")
        FileUtils.copyDirectory(dirInput.file, destDirInput)
    }
}