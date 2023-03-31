package zcc.es.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {
    private static final Logger log = LoggerFactory.getLogger(DateUtils.class);
    public static final String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE_TIME_SIMPLY = "yyyyMMddHHmmss";
    public static final String PATTERN_DATE_TIME_MS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String PATTERN_DATE_TIME_MS_Z = "yyyy-MM-dd HH:mm:ss.SSSZ";
    public static final String PATTERN_DATE_TIME_MS_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String PATTERN_DATE_MINUTE = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_DATE_F = "yyyyMMdd";
    public static final String PATTERN_M_F = "yyyyMM";
    public static final String PATTERN_TIME_F = "HHmmssSSS";
    public static final String PATTERN_TIME_HH_MM = "HH:mm";
    public static final String PATTERN_DATE_YYYY_MM = "yyyy-MM";
    public static final String PATTERN_DATE_YYMM = "yyMM";
    public static final String PATTERN_DATE_YYYY = "yyyy";
    public static final String PATTERN_DATE_CALENDAR = "yyyy年MM月dd日";
    public static final String PATTERN_DATE_YYYY_MM_DD = "yyyy/MM/dd";
    public static final String PATTERN_MM = "MM";
    public static final String PATTERN_dd = "dd";

    public DateUtils() {
    }

    public static String getPreMonth(String pattern, int preNumber) {
        Calendar preMonth = Calendar.getInstance();
        preMonth.setTime(new Date());
        int firstDate = preMonth.getMinimum(5);
        preMonth.set(5, firstDate);
        preMonth.set(2, preMonth.get(2) - preNumber);
        return dateToStr(preMonth.getTime(), pattern);
    }

    public static String dayForWeek(Date date) {
        String[] weekDays = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(7) - 1;
        if (week < 0) {
            week = 0;
        }

        return weekDays[week];
    }

    public static String dayForWeek(String dateStr) {
        return dayForWeek(strToDate(dateStr, "yyyy-MM-dd"));
    }

    public static String getMondayLastDay(String date, String pattern) {
        Date d = strToDate(date, pattern);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(5, c.getActualMaximum(5));
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String last = format.format(c.getTime());
        return last;
    }

    public static int getMonday(String date, String pattern) {
        Date d = strToDate(date, pattern);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(5, c.getActualMaximum(5));
        return c.get(2) + 1;
    }

    public static String getMondayFirstDay(String date, String pattern) {
        Date d = strToDate(date, pattern);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(5, 1);
        c.add(2, 0);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String last = format.format(c.getTime());
        return last;
    }

    public static String getMondayFirstDay(String date, String pattern, int month, int day) {
        Date d = strToDate(date, pattern);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(5, day);
        c.add(2, month);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String last = format.format(c.getTime());
        return last;
    }

    public static Date getMondayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int week = c.get(7) - 1;
        if (week == 7) {
            week = 0;
        }

        c.add(5, -week + 1);
        return c.getTime();
    }

    public static String prevDate(int i, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(5, i);
        Date result = c.getTime();
        return dateToStr(result, "yyyy-MM-dd");
    }

    public static String prevDate(int i, Date date, String pattern) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(5, i);
        Date result = c.getTime();
        return dateToStr(result, pattern);
    }

    public static String getMondayOfThisWeekForStr() {
        return dateToStr(getMondayOfThisWeek(), "yyyy-MM-dd");
    }

    public static String dateToStr(Date date, String pattern) {
        return format(date, pattern);
    }

    public static String dateToStr(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date strToDate(String str) {
        return parse(str, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date strToDate(String str, String pattern) {
        return parse(str, pattern);
    }

    public static String format(long datetime, String pattern) {
        Date date = new Date(datetime);
        return format(date, pattern);
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static Date parse(String dateStr, String pattern) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            date = sdf.parse(dateStr);
        } catch (ParseException var5) {
            log.error("日期格式化出错", var5);
        }

        return date;
    }

    public static Date parse(long datetime) {
        return new Date(datetime);
    }

    public static String format(String dateStr, String pattern) {
        Date date = parse(dateStr, pattern);
        return format(date, pattern);
    }

    public static String format(String dateStr, String orgPattern, String pattern) {
        Date date = parse(dateStr, orgPattern);
        return format(date, pattern);
    }

    public static String getCurDate() {
        return format(new Date(), "yyyy-MM-dd");
    }

    public static String getCurDate(String pattern) {
        return format(new Date(), pattern);
    }

    public static String getCurTime() {
        return format(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    public static String getIntervalDay(int interval) {
        new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.add(5, interval);
        Date date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String dateString = formatter.format(date);
        return dateString;
    }

    public static String getFormatDayByUnix(Long unixTime) {
        Date date = new Date(unixTime * 1000L);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }

    public static Long getUnixTimeByTimestamp(Timestamp timestampTime) {
        Date date = new Date(timestampTime.getTime());
        Calendar todayStart = Calendar.getInstance();
        todayStart.setTime(date);
        todayStart.set(11, 0);
        todayStart.set(12, 0);
        todayStart.set(13, 0);
        todayStart.set(14, 0);
        return todayStart.getTime().getTime() / 1000L;
    }

    public static Long getResidueUnixTime() {
        Long time = System.currentTimeMillis();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

        try {
            date = sdf.parse(sdf.format(date));
        } catch (ParseException var4) {
            var4.printStackTrace();
        }

        return (date.getTime() + 86400000L - time) / 1000L;
    }

    public static Integer getDayNum(Long dateMin, Long dateMax) {
        if (null != dateMin && null != dateMax) {
            Long days = (dateMax - dateMin) / 86400000L;
            return Integer.parseInt(String.valueOf(days));
        } else {
            return 0;
        }
    }

    public static Map<String, Long> getTimesByTime(Long time, Integer type) {
        Map<String, Long> map = new HashMap();
        Calendar calendar = Calendar.getInstance();
        if (null != time) {
            calendar.setTime(new Date(time));
        }

        if (null == type) {
            return map;
        } else {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat dateFormatStart = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                SimpleDateFormat dateFormatend = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
                System.out.println("----传入时间----" + dateFormat.format(calendar.getTime()) + "----");
                String startTime;
                String endTime;
                Long start;
                Long end;
                switch(type) {
                    case 0:
                        startTime = dateFormatStart.format(calendar.getTime());
                        endTime = dateFormatend.format(calendar.getTime());
                        start = dateFormat.parse(startTime).getTime();
                        end = dateFormat.parse(endTime).getTime();
                        map.put("start", start);
                        map.put("end", end);
                        System.out.println("----当日返回时间----" + startTime + "----" + endTime + "----");
                        break;
                    case 1:
                        if (calendar.get(7) == 1) {
                            calendar.add(5, -1);
                        }

                        calendar.set(7, calendar.getFirstDayOfWeek());
                        calendar.add(5, 1);
                        startTime = dateFormatStart.format(calendar.getTime());
                        start = calendar.getTime().getTime();
                        map.put("start", start);
                        calendar.set(7, calendar.getFirstDayOfWeek() + 6);
                        calendar.add(5, 1);
                        endTime = dateFormatend.format(calendar.getTime());
                        end = calendar.getTime().getTime();
                        map.put("end", end);
                        System.out.println("----当周返回时间----" + startTime + "----" + endTime + "----");
                        break;
                    case 2:
                        calendar.set(5, 1);
                        startTime = dateFormatStart.format(calendar.getTime());
                        start = calendar.getTime().getTime();
                        map.put("start", start);
                        calendar.add(2, 1);
                        calendar.add(5, -1);
                        endTime = dateFormatend.format(calendar.getTime());
                        end = calendar.getTime().getTime();
                        map.put("end", end);
                        System.out.println("----当月返回时间----" + startTime + "----" + endTime + "----");
                        break;
                    case 3:
                        dateFormatStart = new SimpleDateFormat("yyyy-01-01 00:00:00");
                        dateFormatend = new SimpleDateFormat("yyyy-12-31 23:59:59");
                        startTime = dateFormatStart.format(calendar.getTime());
                        endTime = dateFormatend.format(calendar.getTime());
                        start = dateFormat.parse(startTime).getTime();
                        end = dateFormat.parse(endTime).getTime();
                        map.put("start", start);
                        map.put("end", end);
                        System.out.println("----当年返回时间----" + startTime + "----" + endTime + "----");
                }
            } catch (ParseException var11) {
                var11.printStackTrace();
            }

            return map;
        }
    }

    public static Integer getHourNum(Long dateMin, Long dateMax) {
        Long days = (dateMax - dateMin) / 3600000L;
        return Integer.parseInt(String.valueOf(days));
    }
}
