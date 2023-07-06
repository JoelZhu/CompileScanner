package com.joelzhu.lib.scanner.runtime.core;

import java.util.ArrayList;
import java.util.List;

import com.joelzhu.lib.scanner.annotation.CompileScan;
import com.joelzhu.lib.scanner.runtime.Scanner;

/**
 * Options for {@link Scanner} to get classes.
 *
 * @author JoelZhu
 * @since 2023-04-06
 */
public final class Options {
    private final List<Integer> categories = new ArrayList<>();

    private boolean enableNullReturn = false;

    private boolean listAllCategories = true;

    private boolean withDefault = false;

    public static class Builder {
        private final Options options;

        public Builder() {
            options = new Options();
        }

        /**
         * {@link Scanner} will use {@link int} to represent tag id. This method won't support anymore.
         * <p>
         * Use {@link #category(int)} instead.
         */
        @Deprecated
        public Builder tag(final String tag) {
            return this;
        }

        /**
         * To specify the tag when get annotated classes.
         *
         * @return Builder.
         */
        public Builder category(final int category) {
            options.listAllCategories = false;
            options.categories.add(category);
            return this;
        }

        /**
         * {@link Scanner} will never support group any more.
         */
        @Deprecated
        public Builder group(final String group) {
            return this;
        }

        /**
         * The annotated class which set the parameter: {@link CompileScan#isDefault()} as true will be returned to
         * caller. If not calling this method, you won't get those classes as a member of array which returned to you.
         *
         * @return Builder.
         */
        public Builder enableDefault() {
            options.withDefault = true;
            return this;
        }

        /**
         * {@link Scanner} no need to set all tags options for getting all tags, don't specify tag will works instead.
         */
        @Deprecated
        public Builder listAllTags() {
            return this;
        }

        /**
         * {@link Scanner} no need to set all groups options for getting all groups, don't specify group will works
         * instead.
         */
        @Deprecated
        public Builder listAllGroups() {
            return this;
        }

        /**
         * The result which returned to you can be nullable or not. If calling this method, when you are requesting
         * classes with not exists parameter of {@link CompileScan#tag()}, you will got a <code>null</code>.
         *
         * @return Builder.
         */
        public Builder enableNullReturn() {
            options.enableNullReturn = true;
            return this;
        }

        /**
         * To disable RuntimeException thrown if something wrong.
         *
         * @return Builder.
         */
        @Deprecated
        public Builder disableRuntime() {
            return this;
        }

        public Options create() {
            return options;
        }
    }

    private Options() {}

    public List<Integer> getCategories() {
        return categories;
    }

    public boolean withDefault() {
        return withDefault;
    }

    public boolean isListAllCategories() {
        return listAllCategories;
    }

    public boolean isEnableNullReturn() {
        return enableNullReturn;
    }
}