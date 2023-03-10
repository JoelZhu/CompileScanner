package com.joelzhu.lib.scanner.java.commands;

import com.joelzhu.lib.scanner.annotation.CompileScan;
import com.joelzhu.lib.scanner.java.ICommand;
import com.joelzhu.lib.scanner.java.utils.Constants;

/**
 * Command: a woman to watch movie.
 *
 * @author JoelZhu
 * @since 2023-03-10
 */
@CompileScan(tag = Constants.TAG_WOMAN, priority = 3)
public final class CommandMovie implements ICommand {
    @Override
    public String execute() {
        return "Watching movie...";
    }
}