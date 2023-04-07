package com.joelzhu.lib.scanner.runtime;

import android.util.Log;

import com.joelzhu.lib.scanner.annotation.CompileScan;
import com.joelzhu.lib.scanner.annotation.ImplConstants;
import com.joelzhu.lib.scanner.runtime.core.Options;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The interface of getting the classes annotated with {@link CompileScan}
 * <p>
 * The simplest way to use, is annotate class without any tag, for example:<br>
 * <code>
 * package com.example;<br>
 * {@literal @}CompileScan<br>
 * public class ScanWhenCompile {}
 * </code><br>
 * then, call the method {@link #getAnnotatedClasses()}, and you will get the following array in java:<br>
 * <code>String[]{ com.example.ScanWhenCompile.class }</code>
 * <p>
 * There's another way to annotate when two or more different kinds of class need to scan, for example:<br>
 * first class as below:<br>
 * <code>
 * package com.example;<br>
 * {@literal @}CompileScan(tag = "type1")<br>
 * public class ScanWhenCompileA {}
 * </code>
 * <p>
 * and, the second is:<br>
 * <code>
 * package com.example;<br>
 * {@literal @}CompileScan(tag = "type2")<br>
 * public class ScanWhenCompileB {}
 * </code><br>
 * in this case, the method {@link #getAnnotatedClasses(String)} will works,<br>
 * you will get corresponding arrays by different parameters:<br>
 * 1. <code>parameter: type1; result: String[]{ com.example.ScanWhenCompileA.class }</code><br>
 * 2. <code>parameter: type2; result: String[]{ com.example.ScanWhenCompileB.class }</code>
 * <p>
 * By the way, if creating instance of those classes is very simple (means only need to new it),<br>
 * has public and non-parameter constructor, you can call the method {@link #getAnnotatedInstances(Class)} to get instances of annotated classes.
 *
 * @author JoelZhu
 * @since 2023-01-26
 */
public final class Scanner {
    private static final String TAG = "JoelCompileScan";

    private Scanner() {
    }

    /**
     * Get array of the classes annotated with {@link CompileScan}
     *
     * @return the classes
     */
    public static Class<?>[] getAnnotatedClasses() {
        return getAnnotatedClasses(new Options.Builder("").create());
    }

    /**
     * The same with the method {@link #getAnnotatedClasses()}, but the annotation is modified by specified tag.
     *
     * @param options
     * @return the classes
     */
    public static Class<?>[] getAnnotatedClasses(final Options options) {
        final boolean withDefault = options.isWithDefault();
        final String tag = options.getTag();
        final Class<?>[] normalClasses = getNormalClasses(tag);
        if (normalClasses != null) {
            return normalClasses;
        }

        if (!withDefault) {
            // Current tag got empty classes from non-default list, and ignored default list by user.
            return null;
        } else {
            return getDefaultClasses(tag);
        }
    }

    /**
     * Get array of the instances which created use classes, annotated with {@link CompileScan}.
     * <p>
     * But, the annotated class must have default constructor, which is public access level,<br>
     * and should not have any parameters. Otherwise, the compiling will failed because of creating instance.
     *
     * @param instanceClass The instances' class.
     * @param <T>           The generic class.
     * @return Array of instances.
     */
    public static <T> T[] getAnnotatedInstances(final Class<T> instanceClass) {
        return getAnnotatedInstances(new Options.Builder("").create(), instanceClass);
    }

    /**
     * The same with the method {@link #getAnnotatedInstances(Class)}, but the annotation is modified by specified tag.
     *
     * @param options
     * @param instanceClass The instances' class.
     * @param <T>           The generic class.
     * @return Array of instances.
     */
    public static <T> T[] getAnnotatedInstances(final Options options, final Class<T> instanceClass) {
        final boolean withDefault = options.isWithDefault();
        final String tag = options.getTag();
        final Class<?>[] normalClasses = getNormalClasses(tag);
        if (normalClasses != null) {
            return createInstanceByBeans(normalClasses, instanceClass);
        }

        if (!withDefault) {
            // Current tag got empty classes from non-default list, and ignored default list by user.
            return null;
        } else {
            return createInstanceByBeans(getDefaultClasses(tag), instanceClass);
        }
    }

    private static Class<?>[] convertFromBeanToClass(final Class<?>[] beans) {
        if (beans == null) {
            return null;
        }

        final int arraySize = beans.length;
        final Class<?>[] classes = new Class[arraySize];
        System.arraycopy(beans, 0, classes, 0, arraySize);
        return classes;
    }

    private static <T> T[] createInstanceByBeans(final Class<?>[] classes, final Class<T> instanceClass) {
        if (classes == null) {
            return null;
        }

        final int arraySize = classes.length;
        final T[] instances = (T[]) Array.newInstance(instanceClass, arraySize);
        for (int index = 0; index < arraySize; index++) {
            try {
                instances[index] = (T) classes[index].newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return instances;
    }

    private static Class<?>[] getNormalClasses(final String tag) {
        return invokeGetter(ImplConstants.NORMAL_CLASS_GET, tag);
    }

    private static Class<?>[] getDefaultClasses(final String tag) {
        return invokeGetter(ImplConstants.DEFAULT_CLASS_GET, tag);
    }

    private static Class<?>[] invokeGetter(final String getterMethodName, final String tag) {
        Class<?>[] classes = null;
        try {
            final Class<?> scannerImpl = Class.forName(ImplConstants.IMPL_PACKAGE + "." + ImplConstants.IMPL_CLASS);
            final Method getterMethod = scannerImpl.getDeclaredMethod(getterMethodName, String.class);
            classes = (Class<?>[]) getterMethod.invoke(null, tag);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException exception) {
            Log.e(TAG, "Invoke method got unexpected exception, " + exception.getMessage());
            exception.printStackTrace();
        } catch (InvocationTargetException exception) {
            Log.e(TAG, "Invoke method failed, " + exception.getMessage());
            exception.printStackTrace();
        }
        return classes;
    }
}