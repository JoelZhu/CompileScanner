package com.joelzhu.lib.scanner.plugin.code.cache

import com.joelzhu.lib.scanner.annotation.internal.CompileScanBean
import com.joelzhu.lib.scanner.plugin.code.cache.bean.CodeGenerateBean
import com.joelzhu.lib.scanner.plugin.util.LogUtil

object CacheHandler {
    private var normalClasses: HashMap<Int, ArrayList<CompileScanBean>>? = null

    private var defaultClasses: HashMap<Int, ArrayList<CompileScanBean>>? = null

    fun initializeCompilation() {
        LogUtil.printInfo("Scanner compile started.")

        normalClasses = hashMapOf()
        defaultClasses = hashMapOf()
    }

    fun cacheScannedClass(scannedBean: CompileScanBean) {
        if (scannedBean.isDefault) {
            storeClassInternal(defaultClasses, scannedBean)
        } else {
            storeClassInternal(normalClasses, scannedBean)
        }
    }

    fun getCachedData(): CodeGenerateBean {
        return CodeGenerateBean(sortMap(normalClasses), sortMap(defaultClasses))
    }

    private fun storeClassInternal(classes: HashMap<Int, ArrayList<CompileScanBean>>?, scannedBean: CompileScanBean) {
        classes?.get(scannedBean.category)?.let {
            LogUtil.printDebug("Category already exists, to add class into it.")
            it.add(scannedBean)
        } ?: run {
            LogUtil.printDebug("Category not exists before, to new it.")
            val list = arrayListOf<CompileScanBean>()
            list.add(scannedBean)
            classes?.put(scannedBean.category, list)
        }
        LogUtil.printDebug("Found ${classes?.size} more class(es) in category: ${scannedBean.category}.")
    }

    private fun sortMap(classes: HashMap<Int, ArrayList<CompileScanBean>>?): HashMap<Int, ArrayList<CompileScanBean>> {
        val sortedMap = hashMapOf<Int, ArrayList<CompileScanBean>>()
        classes?.keys?.forEach { category ->
            val sortedList = classes[category]!!.sortedBy { bean -> bean.priority }
            val sortedMutableList = arrayListOf<CompileScanBean>()
            sortedList.forEach { sortedMutableList.add(it) }
            sortedMap[category] = sortedMutableList
        }
        return sortedMap
    }
}