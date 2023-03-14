package com.joelzhu.lib.scanner.multimodule.commands

import com.joelzhu.lib.scanner.annotation.CompileScan
import com.joelzhu.lib.scanner.multimodule.ICommand

/**
 * Command: human to eat.
 *
 * @author JoelZhu
 * @since 2023-03-13
 */
@CompileScan(priority = 1)
class CommandEat : ICommand {
    override fun execute(): String {
        return "Eating... From module2, priority = 1"
    }
}