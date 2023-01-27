package com.joelzhu.lib.scanner.java.commands;

import com.joelzhu.lib.scanner.annotation.CompileScan;
import com.joelzhu.lib.scanner.java.ICommand;

/**
 * Command: to sit.
 *
 * @author JoelZhu
 * @since 2023-01-27
 */
@CompileScan
public final class CommandSit implements ICommand {
    @Override
    public String execute() {
        return "Sitting...";
    }
}