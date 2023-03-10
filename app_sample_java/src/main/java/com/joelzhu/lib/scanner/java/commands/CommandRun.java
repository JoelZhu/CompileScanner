package com.joelzhu.lib.scanner.java.commands;

import com.joelzhu.lib.scanner.annotation.CompileScan;
import com.joelzhu.lib.scanner.java.ICommand;

/**
 * Command: human to run.
 *
 * @author JoelZhu
 * @since 2023-01-27
 */
@CompileScan(priority = 4)
public final class CommandRun implements ICommand {
    @Override
    public String execute() {
        return "Running...";
    }
}