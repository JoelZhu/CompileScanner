package com.joelzhu.lib.scanner.kotlin.commands

import com.joelzhu.lib.scanner.annotation.CompileScan
import com.joelzhu.lib.scanner.kotlin.ICommand

/**
 * Command: human to run.
 *
 * @author JoelZhu
 * @since 2023-03-13
 */
@CompileScan(priority = 1)
class CommandRun : ICommand {
    override fun execute(): String {
        return "Running... Priority = 1"
    }
}