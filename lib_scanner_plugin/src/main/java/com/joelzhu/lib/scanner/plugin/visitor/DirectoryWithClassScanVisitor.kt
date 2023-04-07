package com.joelzhu.lib.scanner.plugin.visitor

import com.joelzhu.lib.scanner.plugin.core.TransformHandler
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

/**
 * [Description here].
 *
 * @author JoelZhu
 * @since 2023-04-07
 */
class DirectoryWithClassScanVisitor(classVisitor: ClassVisitor, private val dirInputName: String) :
    ClassVisitor(Opcodes.ASM8, classVisitor) {
    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        TransformHandler.updateDirectoryWithClasses(dirInputName)
    }
}