package com.joelzhu.lib.scanner.java.commands;

import com.joelzhu.lib.scanner.annotation.CompileScan;
import com.joelzhu.lib.scanner.java.ICommand;

/**
 * Command: to eat.
 *
 * @author JoelZhu
 * @since 2023-01-27
 */
@CompileScan
public final class CommandEat implements ICommand {
    @Override
    public String execute() {
        return "Eating...";
    }
}