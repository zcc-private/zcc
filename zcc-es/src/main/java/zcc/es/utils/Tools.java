package zcc.es.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Tools {
    public Tools() {
    }

    public static <T> String makeClazzPath(T object, String methodName) {
        if (object == null) {
            return "";
        } else {
            StringBuffer buffer = new StringBuffer();
            buffer.append(object.getClass().getName());
            buffer.append(".");
            buffer.append(methodName);
            return buffer.toString();
        }
    }

    public static <T> String makeClazzPath(Class<T> clazz, String methodName) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(clazz.getName());
        buffer.append(".");
        buffer.append(methodName);
        return buffer.toString();
    }

    public static boolean isEmpty(List list) {
        return null == list || list.size() == 0;
    }

    public static boolean isEmpty(Map map) {
        return null == map || map.size() == 0;
    }

    public static boolean isNotEmpty(List list) {
        return null != list && list.size() != 0;
    }

    public static String getException(Exception e) {
        String err = "";

        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            err = "\r\n" + sw.toString() + "\r\n";
            sw.close();
            pw.close();
            return err;
        } catch (Exception var4) {
            return e.toString();
        }
    }

    public static <K, V> Map.Entry<K, V> getLast(LinkedHashMap<K, V> map) throws NoSuchFieldException, IllegalAccessException {
        Field tail = map.getClass().getDeclaredField("tail");
        tail.setAccessible(true);
        return (Map.Entry)tail.get(map);
    }

    public static <K, V> Map.Entry<K, V> getFirst(LinkedHashMap<K, V> map) {
        return (Map.Entry)map.entrySet().iterator().next();
    }

    public static <K, V> Map.Entry<K, V> getTail(LinkedHashMap<K, V> map, int i) {
        Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
        Map.Entry<K, V> tail = null;
        int var4 = 1;

        while(iterator.hasNext()) {
            tail = (Map.Entry)iterator.next();
            if (var4++ == i) {
                break;
            }
        }

        return tail;
    }
}
