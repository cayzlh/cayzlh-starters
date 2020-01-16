package com.cayzlh.framework.base.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * @author cayzlh
 * @link https://github.com/cayzlh
 */
public class StringFormatUtil {

    private static Pattern pattern;

    static {
        pattern = Pattern.compile("((?<=\\{)([a-zA-Z_]+)(?=}))");
    }

    public static String format(String text, Map<String, Object> params) {

        // 把文本中的所有需要替换的变量捞出来, 丢进keys
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String key = matcher.group();
            text = StringUtils.replace(text, "{" + key + "}", params.get(key) + "");
        }

        return text;
    }

    public static List<String> batchFormat(String text, List<Map<String, Object>> params) {

        List<String> keys = new ArrayList<>();

        // 把文本中的所有需要替换的变量捞出来, 丢进keys
        Matcher matcher = pattern.matcher(text);
        int tempIndex = 0;
        while (matcher.find()) {
            String key = matcher.group();
            if (keys.contains(key)) {
                continue;
            }

            text = StringUtils.replace(text, key, tempIndex + "");
            tempIndex++;
            keys.add(key);
        }

        List<String> result = new ArrayList<>(params.size());
        Object[] tempParamAry = new Object[keys.size()];
        for (Map<String, Object> param : params) {

            for (int i = 0; i < keys.size(); i++) {
                tempParamAry[i] = param.get(keys.get(i)) + "";
            }

            result.add(MessageFormat.format(text, tempParamAry));
        }

        return result;
    }

}
