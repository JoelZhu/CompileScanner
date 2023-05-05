package com.joelzhu.lib.scanner.java.commands;

import com.joelzhu.lib.scanner.annotation.CompileScan;
import com.joelzhu.lib.scanner.java.ICommand;
import com.joelzhu.lib.scanner.java.utils.Constants;

/**
 * Command: human to sit.
 *
 * @author JoelZhu
 * @since 2023-01-27
 */
@CompileScan(group = Constants.GROUP_REST, priority = 2)
public final class CommandSit implements ICommand {
    @Override
    public String execute() {
        return "Sitting... By default, priority = 2";
    }
}