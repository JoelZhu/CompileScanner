package com.joelzhu.lib.scanner.java.commands;

import com.joelzhu.lib.scanner.annotation.CompileScan;
import com.joelzhu.lib.scanner.java.ICommand;
import com.joelzhu.lib.scanner.java.utils.Constants;

/**
 * Command: human to eat.
 *
 * @author JoelZhu
 * @since 2023-01-27
 */
@CompileScan(group = Constants.GROUP_DAILY, priority = 1)
public final class CommandEat implements ICommand {
    @Override
    public String execute() {
        return "Eating... By default, priority = 1";
    }
}