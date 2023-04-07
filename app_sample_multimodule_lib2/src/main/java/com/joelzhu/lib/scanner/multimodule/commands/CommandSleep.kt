package com.joelzhu.lib.scanner.multimodule.commands

import com.joelzhu.lib.scanner.annotation.CompileScan
import com.joelzhu.lib.scanner.multimodule.ICommand

/**
 * Command: human to sleep.
 *
 * @author JoelZhu
 * @since 2023-03-13
 */
@CompileScan(priority = 4)
class CommandSleep : ICommand {
    override fun execute(): String {
        return "Sleeping... From module2, priority = 4"
    }
}