package com.joelzhu.lib.scanner.runtime.exception;

/**
 * Library's implements failed.
 *
 * @author JoelZhu
 * @since 2023-01-27
 */
public final class MismatchLibraryException extends RuntimeException {
    public MismatchLibraryException() {
        super("Implement library failed, check your environment please.");
    }
}