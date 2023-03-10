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
        return getAnnotatedClasses("");
    }

    /**
     * The same with the method {@link #getAnnotatedClasses()}, but the annotation is modified by specified tag.
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
        return getAnnotatedInstances("", instanceClass);
    }

    /**
     * The same with the method {@link #getAnnotatedInstances(Class)}, but the annotation is modified by specified tag.
     *
     * @param tag           The tag which specified when annotating the class.
     * @param instanceClass The instances' class.
     * @param <T>           The generic class.
     * @return Array of instances.
     */
    public static <T> T[] getAnnotatedInstances(final String tag, final Class<T> instanceClass) {
        final T[] instances;
        try {
            final Class<?> clazz = Class.forName(Constants.CLASS_PACKAGE + "." + Constants.CLASS_NAME);
            final Method method = clazz.getDeclaredMethod(Constants.GET_INSTANCE_METHOD_NAME, String.class);
            final Object[] invokedArray = (Object[]) method.invoke(null, tag);
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
        } catch (IllegalAccessException | IllegalArgumentException exception) {
            Log.w(TAG, "Invoke method got exception: " + exception.getMessage());
            throw new MismatchLibraryException();
        } catch (InvocationTargetException exception) {
            Log.e(TAG, "Invoke method got exception: " + exception.getMessage());
            throw new RuntimeException(exception.getTargetException());
        }
        return instances;
    }
}