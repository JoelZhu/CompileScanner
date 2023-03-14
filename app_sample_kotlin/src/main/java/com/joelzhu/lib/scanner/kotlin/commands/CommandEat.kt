package com.joelzhu.lib.scanner.kotlin.commands

import com.joelzhu.lib.scanner.annotation.CompileScan
import com.joelzhu.lib.scanner.kotlin.ICommand

/**
 * Command: human to eat.
 *
 * @author JoelZhu
 * @since 2023-03-13
 */
@CompileScan(priority = 3)
class CommandEat : ICommand {
    override fun execute(): String {
        return "Eating... Priority = 3"
    }
}