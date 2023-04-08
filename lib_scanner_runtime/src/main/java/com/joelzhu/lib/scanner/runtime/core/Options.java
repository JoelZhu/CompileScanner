package com.joelzhu.lib.scanner.runtime.core;

import com.joelzhu.lib.scanner.annotation.CompileScan;
import com.joelzhu.lib.scanner.runtime.Scanner;

/**
 * Options for {@link Scanner} to get classes.
 *
 * @author JoelZhu
 * @since 2023-04-06
 */
public final class Options {
    private String tag = "";

    private boolean enableNullReturn = true;

    private boolean withDefault = false;

    public static class Builder {
        private final Options options;

        public Builder(final String tag) {
            options = new Options();
            options.tag = tag;
        }

        /**
         * The annotated class which set the parameter: {@link CompileScan#isDefault()} as true
         * will be returned to caller. If not calling this method, you won't get those classes as
         * a member of array which returned to you.
         *
         * @return Builder.
         */
        public Builder enableDefault() {
            options.withDefault = true;
            return this;
        }

        /**
         * The result which returned to you can be nullable or not. If not calling this method,
         * when you are requesting classes with not exists parameter of {@link CompileScan#tag()},
         * you will got a <code>null</code>.
         *
         * @return Builder.
         */
        public Builder disableNullReturn() {
            options.enableNullReturn = false;
            return this;
        }

        public Options create() {
            return options;
        }
    }

    public String getTag() {
        return tag;
    }

    public boolean isWithDefault() {
        return withDefault;
    }

    public boolean isEnableNullReturn() {
        return enableNullReturn;
    }
}