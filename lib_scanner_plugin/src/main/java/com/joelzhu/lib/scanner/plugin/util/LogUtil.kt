package com.joelzhu.lib.scanner.plugin.util

/**
 * [Description here].
 *
 * @author JoelZhu
 * @since 2023-04-03
 */
object LogUtil {
    const val LOG_TAG = "JoelCompile"

    private var isPrintLog = true

    fun adjustLogPrintPrivacy(isPrintLog: Boolean) {
        LogUtil.isPrintLog = isPrintLog
    }

    fun printEmptyLine() {
        if (isPrintLog) {
            println()
        }
    }

    fun printLog(logContent: String) {
        if (isPrintLog) {
            println("$LOG_TAG: $logContent")
        }
    }
}