/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://blog.cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.framework.util;

import lombok.extern.slf4j.Slf4j;
import org.fusesource.jansi.Ansi;

/**
 * @author Ant丶
 * @date 2020-04-25.
 */
@Slf4j
public class AnsiUtil {

    public static String getAnsi(Ansi.Color color,String text){

        return Ansi.ansi().eraseScreen().fg(color).a(text).reset().toString();
    }

    public static String getAnsi(Ansi.Color color,String text,boolean flag){
        if (flag){
            return Ansi.ansi().eraseScreen().fg(color).a(text).reset().toString();
        }
        return text;
    }
}
