package com.foodies.freshmeal.common.util;

import java.util.Comparator;

public final class CommonUtils {

    private CommonUtils() {
    }

    public static Comparator<String> alphabeticalWithOtherLast() {
        return (first, second) -> {
            if ("Other".equalsIgnoreCase(first)) {
                return 1;
            }
            if ("Other".equalsIgnoreCase(second)) {
                return -1;
            }
            return first.compareToIgnoreCase(second);
        };
    }
}
