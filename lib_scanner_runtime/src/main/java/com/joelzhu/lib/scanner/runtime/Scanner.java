package com.joelzhu.lib.scanner.runtime;

import android.util.Log;

import com.joelzhu.lib.scanner.annotation.CompileScan;
import com.joelzhu.lib.scanner.annotation.Constants;
import com.joelzhu.lib.scanner.runtime.exception.ImplementFailureException;
import com.joelzhu.lib.scanner.runtime.exception.MismatchLibraryException;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The interface of getting the classes annotated with {@link CompileScan}
 * <p>
 * The simplest way to use, is annotate class without any tag, for example:<br>
 * <code>
 * package com.example;
 * <p>
 * {@literal @}CompileScan<br>
 * public class ScanWhenCompile {<br>
 * }
 * </code>
 * then, call the method {@link #getAnnotatedClasses()}, and you will get the following array in java:<br>
 * <code>String[]{ com.example.ScanWhenCompile.class }</code>
 * <p>
 * There's another way to annotate when two or more different kinds of class need to scan, for example:<br>
 * first class as below:<br>
 * <code>
 * package com.example;
 * <p>
 * {@literal @}CompileScan(tag = "type1")<br>
 * public class ScanWhenCompileA {<br>
 * }
 * </code>
 * <p>
 * and, the second is:<br>
 * <code>
 * package com.example;
 * <p>
 * {@literal @}CompileScan(tag = "type2")<br>
 * public class ScanWhenCompileB {<br>
 * }
 * </code>
 * in this case, the method {@link #getAnnotatedClasses(String)} will works,<br>
 * you will get corresponding arrays by different parameters:<br>
 * 1. <code>parameter: type1; result: String[]{ com.example.ScanWhenCompileA.class }</code><br>
 * 2. <code>parameter: type2; result: String[]{ com.example.ScanWhenCompileB.class }</code>
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
        return getAnnotatedClasses("");
    }

    /**
     * Get array of the classes annotated with {@link CompileScan}, which modified by specified tag.
     *
     * @param tag The tag which specified when annotating the class.
     * @return the classes
     */
    public static Class<?>[] getAnnotatedClasses(final String tag) {
        final Class<?>[] classes;
        try {
            final Class<?> clazz = Class.forName(Constants.CLASS_PACKAGE + "." + Constants.CLASS_NAME);
            final Method method = clazz.getDeclaredMethod(Constants.GET_CLASS_METHOD_NAME, String.class);
            classes = (Class<?>[]) method.invoke(null, tag);
        } catch (ClassNotFoundException | NoSuchMethodException exception) {
            Log.w(TAG, "Invoke method got exception: " + exception.getMessage());
            throw new ImplementFailureException();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
            Log.w(TAG, "Invoke method got exception: " + exception.getMessage());
            throw new MismatchLibraryException();
        }
        return classes == null ? new Class[0] : classes;
    }

    public static <T> T[] getAnnotatedInstances(final Class<T> instanceClass) {
        return getAnnotatedInstances("", instanceClass);
    }

    public static <T> T[] getAnnotatedInstances(final String tag, final Class<T> instanceClass) {
        final T[] instances;
        try {
            final Class<?> clazz = Class.forName(Constants.CLASS_PACKAGE + "." + Constants.CLASS_NAME);
            final Method method =
                clazz.getDeclaredMethod(Constants.GET_INSTANCE_METHOD_NAME, String.class, Class.class);
            final Object[] invokedArray = (Object[]) method.invoke(null, tag, instanceClass);
            if (invokedArray == null) {
                throw new ImplementFailureException();
            }
            final int arrayLength = invokedArray.length;
            instances = (T[]) Array.newInstance(instanceClass, arrayLength);
            for (int index = 0; index < arrayLength; index++) {
                instances[index] = (T) invokedArray[index];
            }
        } catch (ClassNotFoundException | NoSuchMethodException exception) {
            Log.w(TAG, "Invoke method got exception: " + exception.getMessage());
            throw new ImplementFailureException();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
            Log.w(TAG, "Invoke method got exception: " + exception.getMessage());
            throw new MismatchLibraryException();
        }
        return instances == null ? (T[]) Array.newInstance(instanceClass, 0) : instances;
    }
}