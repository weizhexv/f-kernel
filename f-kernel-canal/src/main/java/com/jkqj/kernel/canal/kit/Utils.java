package com.jkqj.kernel.canal.kit;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public final class Utils {

    public static String concat(Object... os) {
        if (ArrayUtils.isEmpty(os)) {
            return StringUtils.EMPTY;
        }

        StringBuilder sb = new StringBuilder(os.length * 5);
        Arrays.stream(os).forEach(sb::append);

        return sb.toString();
    }
}
