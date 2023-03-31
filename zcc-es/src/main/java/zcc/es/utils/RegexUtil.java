package zcc.es.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
    public RegexUtil() {
    }

    public static List<String> getSubUtil(String str, String rgex) {
        List<String> list = new ArrayList();
        Pattern pattern = Pattern.compile(rgex);
        Matcher m = pattern.matcher(str);

        while(m.find()) {
            list.add(m.group(0));
        }

        return list;
    }

    public static String getSubStr(String str, String rgex) {
        Pattern p = Pattern.compile(rgex);
        Matcher matcher = p.matcher(str);
        if (matcher.find()) {
            String result = matcher.group(0);
            return result;
        } else {
            return "";
        }
    }

    public static boolean checkLetter(String cardNum) {
        String regex = "^[A-Za-z]+$";
        return Pattern.matches(regex, cardNum);
    }

    public static boolean checkChinese(String chinese) {
        String regex = "^[一-龥]+$";
        return Pattern.matches(regex, chinese);
    }
}
