package zcc.es.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil extends StringUtils {
    public StringUtil() {
    }

    public static boolean isBlank(String... params) {
        String[] var1 = params;
        int var2 = params.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            String param = var1[var3];
            if (StringUtils.isBlank(param)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isEmpty(String... params) {
        String[] var1 = params;
        int var2 = params.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            String param = var1[var3];
            if (StringUtils.isEmpty(param)) {
                return true;
            }
        }

        return false;
    }

    public static String toSBC(String input) {
        char[] c = input.toCharArray();

        for (int i = 0; i < c.length; ++i) {
            if (c[i] == ' ') {
                c[i] = 12288;
            } else if (c[i] < 127) {
                c[i] += 'ﻠ';
            }
        }

        return new String(c);
    }

    public static String toDBC(String input) {
        char[] c = input.toCharArray();

        for (int i = 0; i < c.length; ++i) {
            if (c[i] == 12288) {
                c[i] = ' ';
            } else if (c[i] > '\uff00' && c[i] < '｟') {
                c[i] -= 'ﻠ';
            }
        }

        return new String(c);
    }

    public static String leftZero(int i, int len) {
        String l = "%0" + len + "d";
        return String.format(l, i);
    }

    public static String toLowerCase(String str, String sign) {
        char[] ch = str.toCharArray();
        StringBuffer strbuf = new StringBuffer();

        for (int i = 0; i < ch.length; ++i) {
            if (i == 0) {
                strbuf.append(String.valueOf(ch[i]).toLowerCase());
            } else if (ch[i] >= 'A' && ch[i] <= 'Z') {
                strbuf.append(sign).append(String.valueOf(ch[i]).toLowerCase());
            } else {
                strbuf.append(ch[i]);
            }
        }

        return strbuf.toString();
    }

    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = Pattern.compile("_(\\w)").matcher(str);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }

        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String initcap(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }

        return new String(ch);
    }

    public static int compareDesc(String str1, String str2) {
        return compareDesc(str1, str2, true);
    }

    public static int compareDesc(String str1, String str2, boolean nullIsLess) {
        if (str1 == str2) {
            return 0;
        } else if (str1 == null) {
            return nullIsLess ? -1 : 1;
        } else if (str2 == null) {
            return nullIsLess ? 1 : -1;
        } else {
            return str1.compareTo(str2) > 0 ? -1 : 1;
        }
    }

    public static int upperToNumber(String letter) {
        int length = letter.length();
        int num = 0;
        int number = 0;

        for (int i = 0; i < length; ++i) {
            char ch = letter.charAt(length - i - 1);
            num = ch - 65 + 1;
            num = (int) ((double) num * Math.pow(26.0D, (double) i));
            number += num;
        }

        return number;
    }

    public static int lowerToNumber(String letter) {
        int length = letter.length();
        int num = 0;
        int number = 0;

        for (int i = 0; i < length; ++i) {
            char ch = letter.charAt(length - i - 1);
            num = ch - 97 + 1;
            num = (int) ((double) num * Math.pow(26.0D, (double) i));
            number += num;
        }

        return number;
    }

    public static String numberToUpper(int num) {
        if (num <= 0) {
            return null;
        } else {
            String letter = "";
            --num;

            do {
                if (letter.length() > 0) {
                    --num;
                }

                letter = (char) (num % 26 + 65) + letter;
                num = (num - num % 26) / 26;
            } while (num > 0);

            return letter;
        }
    }

    public static String numberToLower(int num) {
        if (num <= 0) {
            return null;
        } else {
            String letter = "";
            --num;

            do {
                if (letter.length() > 0) {
                    --num;
                }

                letter = (char) (num % 26 + 97) + letter;
                num = (num - num % 26) / 26;
            } while (num > 0);

            return letter;
        }
    }

    public static String trimFirstAndLastChar(String source, char element) {
        boolean beginIndexFlag = true;
        boolean endIndexFlag = true;

        do {
            int beginIndex = source.indexOf(element) == 0 ? 1 : 0;
            int endIndex = source.lastIndexOf(element) + 1 == source.length() ? source.lastIndexOf(element) : source.length();
            source = source.substring(beginIndex, endIndex);
            beginIndexFlag = source.indexOf(element) == 0;
            endIndexFlag = source.lastIndexOf(element) + 1 == source.length();
        } while (beginIndexFlag || endIndexFlag);

        return source;
    }
}