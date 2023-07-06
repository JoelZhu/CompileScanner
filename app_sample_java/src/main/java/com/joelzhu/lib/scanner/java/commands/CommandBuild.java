package com.joelzhu.lib.scanner.java.commands;

import com.joelzhu.lib.scanner.annotation.CompileScan;
import com.joelzhu.lib.scanner.java.ICommand;
import com.joelzhu.lib.scanner.java.utils.Constants;

/**
 * Command: man to build.
 *
 * @author JoelZhu
 * @since 2023-03-10
 */
@CompileScan(category = Constants.TAG_MAN, priority = 2)
public final class CommandBuild implements ICommand {
    @Override
    public String execute() {
        return "Building... By man, priority = 2";
    }
}