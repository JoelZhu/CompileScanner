package com.joelzhu.lib.scanner.runtime.core;

/**
 * [Description here].
 *
 * @author zhuqian
 * @since 2023-04-06
 */
public final class Options {
    private String tag = "";

    private boolean withDefault = false;

    public static class Builder {
        private final Options options;

        public Builder(final String tag) {
            options = new Options();
            options.tag = tag;
        }

        public Builder enableDefault() {
            options.withDefault = true;
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
}