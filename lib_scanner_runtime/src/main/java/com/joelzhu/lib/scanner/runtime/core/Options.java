package com.joelzhu.lib.scanner.runtime.core;

import java.util.ArrayList;
import java.util.List;

import com.joelzhu.lib.scanner.annotation.CompileScan;
import com.joelzhu.lib.scanner.runtime.Scanner;

import android.util.Log;

/**
 * Options for {@link Scanner} to get classes.
 *
 * @author JoelZhu
 * @since 2023-04-06
 */
public final class Options {
    private static final String TAG = "CompileScan";

    private final List<String> tags = new ArrayList<>();

    private final List<String> groups = new ArrayList<>();

    private boolean enableNullReturn = false;

    private boolean listAllTags = false;

    private boolean listAllGroups = false;

    private boolean withDefault = false;

    private boolean enableRuntime = true;

    public static class Builder {
        private final Options options;

        public Builder() {
            options = new Options();
        }

        /**
         * To specify the tag when get annotated classes.
         *
         * @return Builder.
         */
        public Builder tag(final String tag) {
            if (options.listAllTags) {
                Log.e(TAG, "Already specified to list all tags, can't specify tag again.");
                return this;
            }
            options.tags.add(tag);
            return this;
        }

        /**
         * To specify the group when get annotated classes.
         *
         * @return Builder.
         */
        public Builder group(final String group) {
            if (options.listAllGroups) {
                Log.e(TAG, "Already specified to list all groups, can't specify group again.");
                return this;
            }
            options.groups.add(group);
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
         * To list all tags when get annotated classes. This will ignore the specified of {@link CompileScan#tag()}.
         *
         * @return Builder
         */
        public Builder listAllTags() {
            if (!options.tags.isEmpty()) {
                Log.w(TAG, "Already specified tag, this set will ignore the tag which specified before.");
            }
            options.listAllTags = true;
            return this;
        }

        /**
         * To list all groups when get annotated classes. This will ignore the specified of {@link CompileScan#group()}.
         *
         * @return Builder
         */
        public Builder listAllGroups() {
            if (!options.groups.isEmpty()) {
                Log.w(TAG, "Already specified group, this set will ignore the group which specified before.");
            }
            options.listAllGroups = true;
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
        public Builder disableRuntime() {
            options.enableRuntime = false;
            return this;
        }

        public Options create() {
            return options;
        }
    }

    public List<String> getTags() {
        return tags;
    }

    public List<String> getGroups() {
        return groups;
    }

    public boolean isWithDefault() {
        return withDefault;
    }

    public boolean isListAllTags() {
        return listAllTags;
    }

    public boolean isListAllGroups() {
        return listAllGroups;
    }

    public boolean isEnableNullReturn() {
        return enableNullReturn;
    }

    public boolean isEnableRuntime() {
        return enableRuntime;
    }
}