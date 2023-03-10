package com.joelzhu.lib.scanner.java.commands;

import com.joelzhu.lib.scanner.annotation.CompileScan;
import com.joelzhu.lib.scanner.java.ICommand;

/**
 * Command: human to sit.
 *
 * @author JoelZhu
 * @since 2023-01-27
 */
@CompileScan(priority = 2)
public final class CommandSit implements ICommand {
    @Override
    public String execute() {
        return "Sitting...";
    }
}