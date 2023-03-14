package com.joelzhu.lib.scanner.kotlin.commands

import com.joelzhu.lib.scanner.annotation.CompileScan
import com.joelzhu.lib.scanner.kotlin.ICommand

/**
 * Command: human to sleep.
 *
 * @author JoelZhu
 * @since 2023-03-13
 */
@CompileScan(priority = 4)
class CommandSleep : ICommand {
    override fun execute(): String {
        return "Sleeping... Priority = 4"
    }
}