package com.joelzhu.lib.scanner.plugin.core

import com.joelzhu.lib.scanner.plugin.util.LogUtil

/**
 * Cache to store annotated classes which scanned by transform.
 *
 * @author JoelZhu
 * @since 2023-04-03
 */
class ScannerCache private constructor() {
    companion object {
        val CACHE = ScannerCache()
    }

    private var classesList: MutableList<CompileScanBean> = mutableListOf()

    fun initializeCompile() {
        classesList.clear()
    }

    fun addClasses(bean: CompileScanBean) {
        classesList.add(bean)
    }

    fun getClasses(): List<CompileScanBean> {
        val sortedList = classesList.sortedBy { it.priority }
        printClasses(sortedList)
        return sortedList
    }

    private fun printClasses(classes: List<CompileScanBean>) {
        LogUtil.printEmptyLine()
        LogUtil.printLog("Iterator classes.")
        classes.forEach { bean ->
            LogUtil.printLog("Class: $bean.")
        }
    }
}