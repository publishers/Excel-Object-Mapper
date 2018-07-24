package com.excel.utils;

public final class StringUtils {
    public static final boolean isEmpty(Object value) {
        return value == null || "".equals(value);
    }

    public static final boolean hasText(CharSequence str) {
        return str != null && str.length() > 0 && containsText(str);
    }

    private static final boolean containsText(CharSequence str) {
        int strLen = str.length();

        for(int i = 0; i < strLen; ++i) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }
}
