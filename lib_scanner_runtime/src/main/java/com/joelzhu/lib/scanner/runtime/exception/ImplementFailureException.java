package com.joelzhu.lib.scanner.runtime.exception;

/**
 * Library's implements failed.
 *
 * @author JoelZhu
 * @since 2023-01-27
 */
public final class ImplementFailureException extends RuntimeException {
    public ImplementFailureException() {
        super("Implement library failed, check your environment please.");
    }
}