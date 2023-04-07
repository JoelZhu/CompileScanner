package com.joelzhu.lib.scanner.plugin.util

/**
 * [Description here].
 *
 * @author JoelZhu
 * @since 2023-04-03
 */
class ScannerCache private constructor() {
    companion object {
        val CACHE = ScannerCache()
    }

    private var normalClassesMap: MutableMap<String, MutableList<Pair<String, Int>>> =
        mutableMapOf()

    private var defaultClassesMap: MutableMap<String, MutableList<Pair<String, Int>>> =
        mutableMapOf()

    fun initializeCompile() {
        normalClassesMap.clear()
        defaultClassesMap.clear()
    }

    fun addNormalClass(tag: String?, clazz: String, priority: Int) {
        val modifiedTag = tag ?: ""
        normalClassesMap[modifiedTag]?.add(Pair(clazz, priority)) ?: let {
            normalClassesMap[modifiedTag] = mutableListOf(Pair(clazz, priority))
        }
    }

    fun addDefaultClass(tag: String?, clazz: String, priority: Int) {
        val modifiedTag = tag ?: ""
        defaultClassesMap[modifiedTag]?.add(Pair(clazz, priority)) ?: let {
            defaultClassesMap[modifiedTag] = mutableListOf(Pair(clazz, priority))
        }
    }

    fun getNormalClassesMap(): MutableMap<String, MutableList<String>> {
        LogUtil.printEmptyLine()
        val sortedMap = sortClasses(normalClassesMap)
        printClasses(sortedMap)
        return sortedMap
    }

    fun getDefaultClassesMap(): MutableMap<String, MutableList<String>> {
        LogUtil.printEmptyLine()
        val sortedMap = sortClasses(defaultClassesMap)
        printClasses(sortedMap)
        return sortedMap
    }

    private fun sortClasses(classesMap: MutableMap<String, MutableList<Pair<String, Int>>>)
            : MutableMap<String, MutableList<String>> {
        val sortedMap = mutableMapOf<String, MutableList<String>>()
        classesMap.forEach { (tag, pairs) ->
            val sortedList = pairs.sortedBy {
                it.second
            }

            val sortedMutableList = mutableListOf<String>()
            sortedList.forEach {
                sortedMutableList.add(it.first)
            }
            sortedMap[tag] = sortedMutableList
            LogUtil.printLog("Found ${sortedMutableList.size} more class(es).")
        }
        return sortedMap
    }

    private fun printClasses(classesMap: MutableMap<String, MutableList<String>>) {
        LogUtil.printLog("Iterator classes.")
        classesMap.forEach { (tag, classes) ->
            LogUtil.printEmptyLine()
            LogUtil.printLog("To iterator class with tag: $tag.")
            classes.forEach {
                LogUtil.printLog("Class: $it.")
            }
        }
    }
}