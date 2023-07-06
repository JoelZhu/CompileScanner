package com.joelzhu.lib.scanner.java.commands;

import com.joelzhu.lib.scanner.annotation.CompileScan;
import com.joelzhu.lib.scanner.java.ICommand;
import com.joelzhu.lib.scanner.java.utils.Constants;

/**
 * Command: human to sleep.
 *
 * @author JoelZhu
 * @since 2023-01-27
 */
@CompileScan(priority = 10)
public final class CommandSleep implements ICommand {
    @Override
    public String execute() {
        return "Sleeping... By default, priority = 10";
    }
}