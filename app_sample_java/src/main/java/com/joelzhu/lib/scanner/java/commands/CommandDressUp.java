package com.joelzhu.lib.scanner.java.commands;

import com.joelzhu.lib.scanner.annotation.CompileScan;
import com.joelzhu.lib.scanner.java.ICommand;
import com.joelzhu.lib.scanner.java.utils.Constants;

/**
 * Command: a woman to dress up.
 *
 * @author JoelZhu
 * @since 2023-03-10
 */
@CompileScan(tag = Constants.TAG_WOMAN, group = Constants.GROUP_DAILY, priority = 1)
public final class CommandDressUp implements ICommand {
    @Override
    public String execute() {
        return "Dressing up... By woman, priority = 1";
    }
}