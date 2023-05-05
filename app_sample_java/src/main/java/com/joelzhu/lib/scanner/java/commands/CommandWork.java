package com.joelzhu.lib.scanner.java.commands;

import com.joelzhu.lib.scanner.annotation.CompileScan;
import com.joelzhu.lib.scanner.java.ICommand;
import com.joelzhu.lib.scanner.java.utils.Constants;

/**
 * Command: man to work.
 *
 * @author JoelZhu
 * @since 2023-03-10
 */
@CompileScan(tag = Constants.TAG_MAN, group = Constants.GROUP_WORK, priority = 1)
public final class CommandWork implements ICommand {
    @Override
    public String execute() {
        return "Working... By man, priority = 1";
    }
}