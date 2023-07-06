package com.joelzhu.lib.scanner.plugin.code.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.joelzhu.lib.scanner.annotation.internal.InjectClass;

/**
 * Do not modify this class, it will be modified when compiling.
 */
@InjectClass
public final class SameTagPriorityWithoutExtensionSample {
    private static final HashMap<Integer, Class<?>[]> DEFAULT_CLASSES;

    private static final HashMap<Integer, Class<?>[]> NORMAL_CLASSES;

    private SameTagPriorityWithoutExtensionSample() {
    }

    static {
        DEFAULT_CLASSES = new HashMap<>();
        DEFAULT_CLASSES.put(1, new Class[] {String.class, ArrayList.class});
        DEFAULT_CLASSES.put(2, new Class[] {String.class, ArrayList.class});
        DEFAULT_CLASSES.put(3, new Class[] {String.class, ArrayList.class});
        DEFAULT_CLASSES.put(4, new Class[] {String.class, ArrayList.class});
        DEFAULT_CLASSES.put(5, new Class[] {String.class, ArrayList.class});
        DEFAULT_CLASSES.put(6, new Class[] {String.class, ArrayList.class});
        DEFAULT_CLASSES.put(7, new Class[] {String.class, ArrayList.class});
        DEFAULT_CLASSES.put(8, new Class[] {String.class, ArrayList.class});
        DEFAULT_CLASSES.put(9, new Class[] {String.class, ArrayList.class});
        DEFAULT_CLASSES.put(10, new Class[] {String.class, ArrayList.class});

        NORMAL_CLASSES = new HashMap<>();
        NORMAL_CLASSES.put(1, new Class[] {String.class, ArrayList.class});
        NORMAL_CLASSES.put(2, new Class[] {String.class, ArrayList.class});
    }

    public static Class<?>[] getClasses(final List<Integer> tags, final boolean withDefault) {
        final List<Class<?>[]> classesList = new ArrayList<>();
        int listSize = 0;
        for (final int tag: tags) {
            final Class<?>[] classes;
            if (NORMAL_CLASSES.containsKey(tag)) {
                classes = NORMAL_CLASSES.get(tag);
            } else if (withDefault) {
                classes = DEFAULT_CLASSES.get(tag);
            } else {
                classes = null;
            }

            if (classes != null) {
                listSize += classes.length;
                classesList.add(classes);
            }
        }

        final Class<?>[] result = new Class[listSize];
        int copiedIndex = 0;
        for (final Class<?>[] classes : classesList) {
            final int toCopySize = classes.length;
            System.arraycopy(classes, 0, result, copiedIndex, toCopySize);
            copiedIndex += toCopySize;
        }
        return result;
    }
}