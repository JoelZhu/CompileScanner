package com.joelzhu.lib.scanner.multimodule.commands

import com.joelzhu.lib.scanner.annotation.CompileScan
import com.joelzhu.lib.scanner.multimodule.ICommand

/**
 * Command: human to run.
 *
 * @author JoelZhu
 * @since 2023-03-13
 */
@CompileScan(priority = 4)
class CommandRun : ICommand {
    override fun execute(): String {
        return "Running... From module1, priority = 4"
    }
}