package com.joelzhu.lib.scanner.plugin.util

/**
 * Log util.
 *
 * @author JoelZhu
 * @since 2023-04-03
 */
object LogUtil {
    private const val LOG_TAG = "JoelCompile"

    private var isPrintLog = false

    private var isPrintDebug = true

    fun adjustLogPrintPrivacy(isPrintLog: Boolean) {
        LogUtil.isPrintLog = isPrintLog
    }

    fun printEmptyLine() {
        if (isPrintLog) {
            println()
        }
    }

    fun printImportant(logContent: String) {
        println("> $LOG_TAG: $logContent")
    }

    fun printError(logContent: String) {
        if (isPrintLog) {
            System.err.println("> $LOG_TAG/Error: $logContent")
        }
    }

    fun printInfo(logContent: String) {
        if (isPrintLog) {
            println("> $LOG_TAG/Info: $logContent")
        }
    }

    fun printDebug(logContent: String) {
        if (isPrintDebug) {
            println("> $LOG_TAG/Debug: $logContent")
        }
    }
}