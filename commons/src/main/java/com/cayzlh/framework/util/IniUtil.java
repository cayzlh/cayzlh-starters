package com.cayzlh.framework.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import org.ini4j.Config;
import org.ini4j.Ini;
import org.ini4j.Profile;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-21.
 */
public class IniUtil {

    public static Map<String,String> parseIni(String string) {
        Config config = new Config();
        config.setGlobalSection(true);
        config.setGlobalSectionName("");
        Ini ini = new Ini();
        ini.setConfig(config);
        try {
            ini.load(new StringReader(string));
            return ini.get("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String,String> parseIni(String sectionName,String string) {
        Ini ini = new Ini();
        try {
            ini.load(new StringReader(string));
            Profile.Section section = ini.get(sectionName);
            return section;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
