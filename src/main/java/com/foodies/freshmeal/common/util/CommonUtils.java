package com.foodies.freshmeal.common.util;

import java.util.Comparator;

import org.springframework.util.StringUtils;

public final class CommonUtils {

    public static boolean isBlank(String foodId) {
        return !StringUtils.hasText(foodId);
    }

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
