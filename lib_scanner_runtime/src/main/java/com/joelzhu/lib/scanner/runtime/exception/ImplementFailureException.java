package com.joelzhu.lib.scanner.runtime.exception;

/**
 * The two libraries' version mismatch.
 *
 * @author JoelZhu
 * @since 2023-01-27
 */
public final class ImplementFailureException extends RuntimeException {
    public ImplementFailureException() {
        super("Library mismatch, check scanner's annotation and runtime library please, " +
            "must make the two library's version equal.");
    }
}