package com.joelzhu.lib.scanner.multimodule.commands

import com.joelzhu.lib.scanner.annotation.CompileScan
import com.joelzhu.lib.scanner.multimodule.ICommand

/**
 * Command: human to sit.
 *
 * @author JoelZhu
 * @since 2023-03-13
 */
@CompileScan(priority = 2)
class CommandSit : ICommand {
    override fun execute(): String {
        return "Sitting... From module1, priority = 2"
    }
}