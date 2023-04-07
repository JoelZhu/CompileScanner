package com.joelzhu.lib.scanner.plugin.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Only used to generate ASM code generator to write down the impl class.
 *
 * @author JoelZhu
 * @since 2023-04-07
 */
public final class ScannerImpl {
    private static final Map<String, Class<?>[]> NORMAL_MAP = new HashMap<>();

    private static final Map<String, Class<?>[]> DEFAULT_MAP = new HashMap<>();

    static {
    }

    private ScannerImpl() {
    }

    public static Class<?>[] getNormalClasses(final String tag) {
        return NORMAL_MAP.get(tag);
    }

    public static Class<?>[] getDefaultClasses(final String tag) {
        return DEFAULT_MAP.get(tag);
    }
}