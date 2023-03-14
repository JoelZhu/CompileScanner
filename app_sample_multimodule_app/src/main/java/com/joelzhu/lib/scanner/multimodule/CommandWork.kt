package com.joelzhu.lib.scanner.multimodule

import com.joelzhu.lib.scanner.annotation.CompileScan

/**
 * Command: man to work.
 *
 * @author JoelZhu
 * @since 2023-03-13
 */
@CompileScan(priority = 7)
class CommandWork : ICommand {
    override fun execute(): String {
        return "Working... From app, priority = 7"
    }
}