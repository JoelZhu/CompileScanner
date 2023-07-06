package com.joelzhu.lib.scanner.runtime;

import java.lang.reflect.Array;

import com.joelzhu.lib.scanner.annotation.CompileScan;
import com.joelzhu.lib.scanner.runtime.core.Options;
import com.joelzhu.lib.scanner.runtime.core.ScannerImpl;

import android.util.Log;

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
 * in this case, the method {@link #getAnnotatedClasses(Options)} will works,<br>
 * you will get corresponding arrays by different parameters:<br>
 * 1. <code>parameter: type1; result: String[]{ com.example.ScanWhenCompileA.class }</code><br>
 * 2. <code>parameter: type2; result: String[]{ com.example.ScanWhenCompileB.class }</code>
 * <p>
 * By the way, if creating instance of those classes is very simple (means only need to new it),<br>
 * has public and non-parameter constructor, you can call the method {@link #getAnnotatedInstances(Class)} to get
 * instances of annotated classes.
 *
 * @author JoelZhu
 * @since 2023-01-26
 */
public final class Scanner {
    private static final String TAG = "CompileScan";

    private Scanner() {}

    /**
     * Get array of the classes annotated with {@link CompileScan}. And the parameters are set as below: <br>
     * 1. {@link Options#tag} is empty string, which is: <code>""</code>;<br>
     * 2. {@link Options#withDefault} is true, which means you will got default class if there's no other empty tag's
     * class exists;<br>
     * 3. {@link Options#enableNullReturn} is true, means you will got <code>null</code> if the default one still not
     * exists at the situation above.
     * <p>
     * See {@link #getAnnotatedClasses(Options)} for more information.
     *
     * @return The classes which annotated with {@link CompileScan}.
     */
    public static Class<?>[] getAnnotatedClasses() {
        return getAnnotatedClasses(new Options.Builder().create());
    }

    /**
     * Get array of the classes annotated with {@link CompileScan}. As it should be, the classes' annotation fields are
     * declared as below:<br>
     * 1. Declared the tag as the field {@link Options#tag} in <code>options</code>;<br>
     * 2. Declared the default or not as the field {@link Options#withDefault} in <code>options</code>; And if the class
     * which filtered by above conditions do not exists, you will got a null array back. But if you don't want to see
     * the <code>null</code>, you can set the field {@link Options#enableNullReturn} as false to avoid the situation,
     * and you will got a empty array instead.
     *
     * @param options The options, indicate the scanner, what kind of classes to be returned. For more information,
     *        please refers for {@link Options}.
     * @return The classes which annotated with {@link CompileScan}.
     */
    public static Class<?>[] getAnnotatedClasses(final Options options) {
        final Class<?>[] result = ScannerImpl.getClasses(options.getCategories(), options.withDefault());
        return handleReturning(options, result);
    }

    /**
     * Get array of the instances which created by the classes, annotated with {@link CompileScan}. And the parameters
     * are set as below:<br>
     * 1. {@link Options#tag} is empty string, which is: <code>""</code>;<br>
     * 2. {@link Options#withDefault} is true, which means you will got default class if there's no other empty tag's
     * class exists;<br>
     * 3. {@link Options#enableNullReturn} is true, means you will got <code>null</code> if the default one still not
     * exists at the situation above.
     * <p>
     * See {@link #getAnnotatedInstances(Options, Class)} for more information.
     * <p>
     * <b>!! Attention: </b>, the annotated class must have non-parameter public constructor, otherwise, the creation of
     * the class will failed, and you will got a runtime exception.
     *
     * @param instanceClass The instances' class.
     * @param <T> The generic class.
     * @return The array created by the class which annotated with {@link CompileScan}.
     */
    public static <T> T[] getAnnotatedInstances(final Class<T> instanceClass) {
        return getAnnotatedInstances(new Options.Builder().create(), instanceClass);
    }

    /**
     * Get array of the instances which created by the classes, annotated with {@link CompileScan}. As it should be, the
     * classes' annotation fields are declared as below:<br>
     * 1. Declared the tag as the field {@link Options#tag} in <code>options</code>;<br>
     * 2. Declared the default or not as the field {@link Options#withDefault} in <code>options</code>; And if the class
     * which filtered by above conditions do not exists, you will got a null array back. But if you don't want to see
     * the <code>null</code>, you can set the field {@link Options#enableNullReturn} as false to avoid the situation,
     * and you will got a empty array instead.
     * <p>
     * <b>!! Attention: </b>, the annotated class must have non-parameter public constructor, otherwise, the creation of
     * the class will failed, and you will got a runtime exception.
     *
     * @param options The options, indicate the scanner, what kind of classes to be returned. For more information,
     *        please refers for {@link Options}.
     * @param instanceClass The instances' class.
     * @param <T> The generic class.
     * @return The array created by the class which annotated with {@link CompileScan}.
     */
    public static <T> T[] getAnnotatedInstances(final Options options, final Class<T> instanceClass) {
        final Class<?>[] classes = ScannerImpl.getClasses(options.getCategories(), options.withDefault());
        final T[] result = createInstanceByBeans(classes, instanceClass);
        return handleReturning(options, result, instanceClass);
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
            } catch (IllegalAccessException exception) {
                Log.e(TAG, classes[index].getName() + "'s non-parameter public constructor not found, " +
                    exception.getMessage());
                exception.printStackTrace();
            } catch (InstantiationException exception) {
                Log.e(TAG,
                    "Create instance of " + classes[index].getName() + "got exception, " + exception.getMessage());
                exception.printStackTrace();
            }
        }
        return instances;
    }

    private static Class<?>[] handleReturning(final Options options, final Class<?>[] originResult) {
        if (!options.isEnableNullReturn() && originResult == null) {
            return new Class<?>[0];
        }
        return originResult;
    }

    private static <T> T[] handleReturning(final Options options, final T[] originResult,
        final Class<T> instanceClass) {
        if (!options.isEnableNullReturn() && originResult == null) {
            return (T[]) Array.newInstance(instanceClass, 0);
        }
        return originResult;
    }
}