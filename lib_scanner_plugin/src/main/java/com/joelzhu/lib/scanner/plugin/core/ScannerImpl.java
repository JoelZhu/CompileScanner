package com.joelzhu.lib.scanner.plugin.core;

import com.joelzhu.lib.scanner.annotation.ScannedClass;

import java.util.ArrayList;

/**
 * Only used to generate ASM code generator to write down the impl class.
 *
 * @author JoelZhu
 * @since 2023-04-07
 */
public final class ScannerImpl {
    private static final ScannedClass[] CLASSES = new ScannedClass[] {
        new ScannedClass(ArrayList.class, "tag1", "group1", true),
        new ScannedClass(String.class, "tag2", "group2", true),
        new ScannedClass(String.class, "tag3", "", false),
        new ScannedClass(String.class, "tag4", "", false),
        new ScannedClass(String.class, "tag5", "", false),
        new ScannedClass(String.class, "tag6", "", false),
        new ScannedClass(String.class, "tag7", "", false),
        new ScannedClass(String.class, "tag8", "", false),
    };

    private ScannerImpl() {
    }

    public static ScannedClass[] getClasses() {
        return CLASSES;
    }
}