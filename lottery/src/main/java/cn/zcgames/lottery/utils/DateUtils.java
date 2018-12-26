package cn.zcgames.lottery.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by admin on 2017/4/10.
 * <p>
 * SimpleDateFormat函数语法：
 * G 年代标志符
 * y 年
 * M 月
 * d 日
 * h 时 在上午或下午 (1~12)
 * H 时 在一天中 (0~23)
 * m 分
 * s 秒
 * S 毫秒
 * E 星期
 * D 一年中的第几天
 * F 一月中第几个星期几
 * w 一年中第几个星期
 * W 一月中第几个星期
 * a 上午 / 下午 标记符
 * k 时 在一天中 (1~24)
 * K 时 在上午或下午 (0~11)
 * z 时区
 */

public class DateUtils {

    private static final String TAG = "DateUtils";

    private static String FORMAT_STRING_mm_ss = "mm:ss";
    private static SimpleDateFormat SDF_mm_ss;

    /**
     * 格式化long类型的时间:mm:ss
     *
     * @param timeLong
     * @return
     */
    public static String formatTime_mm_ss(long timeLong) {
        if (SDF_mm_ss == null) {
            SDF_mm_ss = new SimpleDateFormat(FORMAT_STRING_mm_ss);
        }
        return SDF_mm_ss.format(timeLong);
    }

    private static String FORMAT_STRING_yyyy_MM_dd = "yyyy-MM-dd";
    private static SimpleDateFormat SDF_yyyy_MM_dd;

    /**
     * 格式化long类型的时间:yyyy-MM-dd
     *
     * @param timeLong
     * @return
     */
    public static String formatTime_yyyy_MM_dd(long timeLong) {
        if (SDF_yyyy_MM_dd == null) {
            SDF_yyyy_MM_dd = new SimpleDateFormat(FORMAT_STRING_yyyy_MM_dd);
        }
        return SDF_yyyy_MM_dd.format(timeLong);
    }

    private static String FORMAT_STRING_HH_mm_E = "HH:mm(E)";
    private static SimpleDateFormat SDF_HH_mm_E;

    public static String formatTime_HH_mm_E(long timeLong) {
        if (SDF_HH_mm_E == null) {
            SDF_HH_mm_E = new SimpleDateFormat(FORMAT_STRING_HH_mm_E);
        }
        return SDF_HH_mm_E.format(timeLong);
    }

    private static String FORMAT_STRING_HH_mm = "HH:mm";
    private static SimpleDateFormat SDF_HH_mm;

    public static String formatTime_HH_mm(long timeLong) {
        if (SDF_HH_mm == null) {
            SDF_HH_mm = new SimpleDateFormat(FORMAT_STRING_HH_mm);
        }
        return SDF_HH_mm.format(timeLong);
    }

    private static String FORMAT_STRING_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat SDF_yyyy_MM_dd_HH_mm_ss;

    /**
     * 格式化long类型的时间
     *
     * @param timeLong
     * @return
     */
    public static String formatTime_yyyy_MM_dd_HH_mm_ss(long timeLong) {
        if (SDF_yyyy_MM_dd_HH_mm_ss == null) {
            SDF_yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat(FORMAT_STRING_yyyy_MM_dd_HH_mm_ss);
        }
        return SDF_yyyy_MM_dd_HH_mm_ss.format(timeLong);
    }

    private static String FORMAT_STRING_MM_dd_E = "MM-dd(E)";
    private static SimpleDateFormat SDF_MM_dd_E;

    /**
     * 格式化long类型的时间
     *
     * @param timeLong
     * @return
     */
    public static String formatTime_MM_dd_E(long timeLong) {
        if (SDF_MM_dd_E == null) {
            SDF_MM_dd_E = new SimpleDateFormat(FORMAT_STRING_MM_dd_E);
        }
        return SDF_MM_dd_E.format(timeLong);
    }

    private static String FORMAT_STRING_MM_dd = "MM月dd号";
    private static SimpleDateFormat SDF_MM_dd;

    /**
     * 格式化long类型的时间
     *
     * @param timeLong
     * @return
     */
    public static String formatTime_MM_dd(long timeLong) {
        if (SDF_MM_dd == null) {
            SDF_MM_dd = new SimpleDateFormat(FORMAT_STRING_MM_dd);
        }
        return SDF_MM_dd.format(timeLong);
    }

    private static String FORMAT_STRING_MM_SS = "mm分ss秒";
    private static SimpleDateFormat SDF_MM_SS;

    /**
     * 格式化long类型的时间
     *
     * @param timeLong
     * @return
     */
    public static String formatTime_MM_SS(long timeLong) {
        if (SDF_MM_SS == null) {
            SDF_MM_SS = new SimpleDateFormat(FORMAT_STRING_MM_SS);
        }
        return SDF_MM_SS.format(timeLong);
    }

    private static String FORMAT_STRING_MM_dd_HH_mm = "MM-dd HH:mm";
    private static SimpleDateFormat SDF_MM_dd_HH_mm;

    /**
     * 格式化long类型的时间
     *
     * @param timeLong
     * @return
     */
    public static String formatTime_MM_dd_HH_mm(long timeLong) {
        if (SDF_MM_dd_HH_mm == null) {
            SDF_MM_dd_HH_mm = new SimpleDateFormat(FORMAT_STRING_MM_dd_HH_mm);
        }
        return SDF_MM_dd_HH_mm.format(timeLong);
    }

    private static String FORMAT_STRING_UTC = "yyyy-MM-dd'T'HH:mm:ss";
    //    private static String FORMAT_STRING_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSS'+08:00'";
    private static SimpleDateFormat SDF_UTC_MM_dd;

    /**
     * 将UTC转成MM_dd
     *
     * @param timeString
     * @return
     */
    public static String parseUTC2MM_dd(String timeString) {
        if (SDF_UTC_MM_dd == null) {
            SDF_UTC_MM_dd = new SimpleDateFormat(FORMAT_STRING_UTC);
        }

        Date date = null;
        try {
            date = SDF_UTC_MM_dd.parse(timeString);
        } catch (ParseException e) {
        }
        return formatTime_MM_dd(date.getTime());
    }

    /**
     * 将UTC转成HH_mm_E
     *
     * @param timeString
     * @return
     */
    public static String parseUTC2HH_mm_E(String timeString) {
        if (SDF_UTC_MM_dd == null) {
            SDF_UTC_MM_dd = new SimpleDateFormat(FORMAT_STRING_UTC);
        }

        Date date = null;
        try {
            date = SDF_UTC_MM_dd.parse(timeString);
        } catch (ParseException e) {
        }
        return formatTime_HH_mm_E(date.getTime());
    }

    /**
     * 将UTC转成HH_mm
     *
     * @param timeString
     * @return
     */
    public static String parseUTC2HH_mm(String timeString) {
        if (SDF_UTC_MM_dd == null) {
            SDF_UTC_MM_dd = new SimpleDateFormat(FORMAT_STRING_UTC);
        }

        Date date = null;
        try {
            date = SDF_UTC_MM_dd.parse(timeString);
        } catch (ParseException e) {
        }
        return formatTime_HH_mm(date.getTime());
    }

    /**
     * 将UTC转成yyyy_MM_dd_HH_mm_ss
     *
     * @param timeString
     * @return
     */
    public static String parseUTC2yyyy_dd_HH_mm_ss(String timeString) {
        if (SDF_UTC_MM_dd == null) {
            SDF_UTC_MM_dd = new SimpleDateFormat(FORMAT_STRING_UTC);
        }

        Date date = null;
        try {
            date = SDF_UTC_MM_dd.parse(timeString);
        } catch (ParseException e) {
        }
        return formatTime_yyyy_MM_dd_HH_mm_ss(date.getTime());
    }

    private static SimpleDateFormat SDF_UTC_yyyy_MM_dd_HH_mm_ss;

    /**
     * 将UTC转成yyyy_MM_dd_HH_mm_ss
     *
     * @param timeString
     * @return
     */
    public static String parseUTC2yyyy_MM_dd_HH_mm_ss(String timeString) {
        if (SDF_UTC_yyyy_MM_dd_HH_mm_ss == null) {
            SDF_UTC_yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat(FORMAT_STRING_UTC);
        }

        Date date = null;
        try {
            date = SDF_UTC_yyyy_MM_dd_HH_mm_ss.parse(timeString);
        } catch (ParseException e) {
        }
        return formatTime_yyyy_MM_dd_HH_mm_ss(date.getTime());
    }

    /**
     * @param timeLong
     * @return
     */
    public static String formatDate2UTC(long timeLong) {
        if (SDF_UTC_yyyy_MM_dd_HH_mm_ss == null) {
            SDF_UTC_yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat(FORMAT_STRING_UTC);
        }
        return SDF_UTC_yyyy_MM_dd_HH_mm_ss.format(timeLong);
    }

    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    public static String formatDateTime(String time) {
        SimpleDateFormat format = new java.text.SimpleDateFormat(FORMAT_STRING_UTC);
        if (time == null || "".equals(time)) {
            return "";
        }
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar current = Calendar.getInstance();

        Calendar today = Calendar.getInstance();    //今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar tomorrow = Calendar.getInstance();    //昨天

        tomorrow.set(Calendar.YEAR, current.get(Calendar.YEAR));
        tomorrow.set(Calendar.MONTH, current.get(Calendar.MONTH));
        tomorrow.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) + 1);
        tomorrow.set(Calendar.HOUR_OF_DAY, 0);
        tomorrow.set(Calendar.MINUTE, 0);
        tomorrow.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance();    //昨天

        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        current.setTime(date);

        if (current.after(today) && current.after(tomorrow)) {
            return "明天 " + formatTime_HH_mm_E(date.getTime());
        } else if (current.before(tomorrow) && current.after(yesterday)) {
            return "今天 " + formatTime_HH_mm_E(date.getTime());
        } else {
            return formatTime_MM_dd_HH_mm(date.getTime());
        }

//        if (current.getTime().getTime() > today.getTime().getTime() && current.getTime().getTime() < tomorrow.getTime().getTime()) {
//            return "明天 " + formatTime_HH_mm_E(date.getTime());
//        } else if (current.getTime().getTime() == today.getTime().getTime()) {
//            return "今天 " + formatTime_HH_mm_E(date.getTime());
//        } else {
//            return formatTime_MM_dd_HH_mm(date.getTime());
//        }
    }
}
