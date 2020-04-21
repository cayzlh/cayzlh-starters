package com.cayzlh.framework.util;

import java.util.UUID;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-21.
 */
public class UUIDUtil {

    public static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

}
