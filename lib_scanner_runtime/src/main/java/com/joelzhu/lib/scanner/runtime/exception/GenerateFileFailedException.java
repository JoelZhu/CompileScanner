package com.joelzhu.lib.scanner.runtime.exception;

/**
 * Generate file failed.
 *
 * @author JoelZhu
 * @since 2023-01-27
 */
public final class GenerateFileFailedException extends RuntimeException {
    public GenerateFileFailedException() {
        super("Generate file failed, check annotated classes please, also should pay attention to the options " +
            "defined in build.gradle.");
    }
}