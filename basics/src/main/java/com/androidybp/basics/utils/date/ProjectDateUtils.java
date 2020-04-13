package com.androidybp.basics.utils.date;

import android.text.TextUtils;

import com.androidybp.basics.R;
import com.androidybp.basics.entity.TempDateEntity;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.androidybp.basics.utils.verification.VerificationUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期处理工具
 */

public class ProjectDateUtils {

    /**
     * 格式化日期
     *
     * @return 格式化日期格式为：yyyy-MM-dd HH:mm:ss
     */

    public static String getDayHour() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * 格式化输入时间
     *
     * @param type 日期格式
     * @param time 传入时间
     * @return 格式化后的时间
     */
    public static String getTimeDay(String type, long time) {
        Date date = null;
        if (time == -1) {
            date = new Date();
        } else {
            date = new Date(time);
        }
        return new SimpleDateFormat(type).format(date);
    }
//    public static String  getDayBefore(){
//    	   Date date = new Date();
//           sdf = new SimpleDateFormat("yyyy-MM-dd");
//           return getDateDayBefore(sdf.format(date), "yyyy-MM-dd");
//    }

    /**
     * 获取当前时间，指定显示的格式
     *
     * @return 当前时间  格式是 “yyyy-MM-dd”
     */
    public static String getToday() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    /**
     * 获取当前时间   指定显示的格式
     *
     * @param format 要显示时间的格式
     * @return 返回当前时间
     */
    public static String getToday(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    public static Date getCurrentDate(){
        String today = getToday("yyyy-MM-dd");
        Date date = null;
        //换算成 date
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(today);
        } catch (ParseException e) {
            throw new ClassCastException(ResourcesTransformUtil.getString(R.string.data_conversion_failed));
        }
        return date;
    }



    /**
     * 给定时间的long值   获取指定显示的格式
     *
     * @param format 要显示时间的格式
     * @param date   要显示的时间
     * @return 返回当前时间
     */
    public static String getToday(String format, Date date) {
//        Date date = new Date(dataLong);
        if (date == null)
            date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * @param endTime 时间  给定的格式必须是 yyyy-MM-dd   也可以是 yyyy-MM-dd HH:mm:ss 等开头是 yyyy-MM-dd 就可以
     * @return 返回给定时间的前一个月          返回的格式：yyyy-MM-dd
     */
    public static String getLeftMonth(String endTime) {
        return getLeftMonth(endTime, "yyyy-MM-dd", "yyyy-MM-dd", -1);
    }

    /**
     * @param endTime        要转换的时间
     * @param dateType       当前转换时间的格式
     * @param returnDateType 获取到转换后的时间格式
     * @param number         当前时间的间距月数    负数是当前月份的前面月      正数是当前月份的后面的月
     * @return 给定时间的前 几个月
     */
    public static String getLeftMonth(String endTime, String dateType, String returnDateType, int number) {
        if (TextUtils.isEmpty(endTime))
            return getToday();
        Date date = new Date();
        try {
            date = new SimpleDateFormat(dateType).parse(endTime.trim().substring(0, 10));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        ca.setTime(date);//月份是从0开始的，所以11表示12月
        ca.add(Calendar.MONTH, number); //月份减1
        Date lastMonth = ca.getTime(); //结果
        SimpleDateFormat sf = new SimpleDateFormat(returnDateType);
        return sf.format(lastMonth);
    }

    public static String getTodayMD() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        String specifiedDay = sdf.format(date);
        return getSpecifiedTodayMD(specifiedDay);
    }

    /**
     * 获取给定日期的前一天
     *
     * @param time   具体的显示日期
     * @param format 日期显示的格式
     * @return 当前日期格式的前一天    不对返回“”
     */
    public static String getDateDayBefore(String time, String format) {
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(format).parse(time);
        } catch (ParseException e) {
            return "";
        }
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return new SimpleDateFormat(format).format(calendar
                .getTime());
    }

    /**
     * 获得指定日期的前一天 ，月日， 格式如：07-19
     *
     * @param specifiedDay 指定日期
     * @return 获得指定日期的前一天
     */
    public static String getSpecifiedTodayMD(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day);

        String dayBefore = new SimpleDateFormat("MM-dd").format(c
                .getTime());
        return dayBefore;
    }

    /**
     * 获得指定日期的前 天，年月日，
     */
    public static String getSpecifiedDayBeforeS(String endTime, String dateType, String returnDateType, int number) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(dateType).parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - number);

        String dayBefore = new SimpleDateFormat(returnDateType).format(c
                .getTime());
        return dayBefore;
    }

    /**
     * 获得指定日期的前一天，年月日， 格式如：2013-07-19
     *
     * @param specifiedDay 指定日期
     * @return 获得指定日期的前一天
     */
    public static String getSpecifiedDayBefore(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
                .getTime());
        return dayBefore;
    }

    /**
     * 获得指定日期的后一天，年月日，  格式如：2013-07-19
     *
     * @param specifiedDay 指定的日期
     * @param format       日期格式
     * @return 获得指定日期的后一天
     */
    public static String getSpecifiedDayAfter(String specifiedDay, String format) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(format).parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        String dayAfter = new SimpleDateFormat(format)
                .format(c.getTime());
        return dayAfter;
    }

    /**
     * 返回跟定日期 Date 的前一天的 时间  的long值
     *
     * @param date     要设置时间的毫秒单位值
     * @param interval 要获取的当前时间的时间间距
     * @return 返回获取到的时间  单位  毫秒单位
     */
    public static long getDateDayAfter(long date, int interval) {
        if (date <= 0 || interval == 0) {
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(date));
        calendar.add(Calendar.DAY_OF_MONTH, interval);
        return calendar.getTime().getTime();
    }

    /**
     * 返回给定时间的   往前 或者之后 多少年的 Date对象
     *
     * @param currentTime       当前的时间
     * @param currentTimeFormat 给定时间的时间格式
     * @param number            要推迟的数量    负数为当前时间之前    正整是当前时间之后的时间
     * @return
     */
    public static Date getDateYear(String currentTime, String currentTimeFormat, int number) {
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        Date date = null;
        try {
            date = new SimpleDateFormat(currentTimeFormat).parse(currentTime);
        } catch (ParseException e) {
            throw new ClassCastException(ResourcesTransformUtil.getString(R.string.data_conversion_failed));
        }
        ca.setTime(date); //设置时间为当前时间
        ca.add(Calendar.YEAR, number); //年份减1
        return ca.getTime(); //结果
    }

    /**
     * 返回给定时间的   往前 或者之后 多少年的 String 对象
     *
     * @param currentTime       当前的时间
     * @param currentTimeFormat 给定时间的时间格式
     * @param number            要推迟的数量    负数为当前时间之前    正整是当前时间之后的时间
     * @param returnTimeFormat  返回时间的格式
     * @return 需要显示的时间
     */
    public static String getDateYearStr(String currentTime, String currentTimeFormat, String returnTimeFormat, int number) {
        Date dateYear = getDateYear(currentTime, currentTimeFormat, number);
        return new SimpleDateFormat(returnTimeFormat).format(dateYear.getTime());
    }

    /**
     * 时间格式为：当天日期显示   今日 时:分            样例： 今日13:20
     * 今年以内的日期显示为   月 日 时:分   样例：6-25 13:20
     * 非今年的日期显示为   年 月 日        样例：2017-6-25
     *
     * @param str 时间格式只认    yyyy-MM-dd HH:mm:ss  格式的   否则直接return出去
     * @return
     */
    public static String formatDataInformation(String str) {

        if (TextUtils.isEmpty(str) || !VerificationUtil.verificationText(str, VerificationUtil.TIME_DATA_SECOND_NEW)) {
            return str;
        } else {
            String showStr = null;
            String today = getToday("yyyy-MM-dd");
            if (str.contains(today)) {//当前发布的信息
                showStr = ResourcesTransformUtil.getString(R.string.today) + str.substring(11, 16);
            } else if (str.contains(today.substring(0, 4))) {//今年发布的
                showStr = str.substring(5, 16);
            } else {
                showStr = str.substring(0, 10);
            }
            return showStr;
        }
    }

    /**
     * 资讯时间格式为：当天日期显示   今日 时:分            样例： 今日13:20
     * 非当天的日期显示为   年 月 日        样例：2017-6-25
     *
     * @param str 时间格式只认    yyyy-MM-dd HH:mm:ss  格式的   否则直接return出去
     * @return
     */
    public static String formatDataMes(String str) {

        if (TextUtils.isEmpty(str) || !VerificationUtil.verificationText(str, VerificationUtil.TIME_DATA_SECOND_NEW)) {
            return str;
        } else {
            String showStr = null;
            String today = getToday("yyyy-MM-dd");
            if (str.contains(today)) {//当前发布的信息
                showStr = ResourcesTransformUtil.getString(R.string.today) + str.substring(11, 16);
            } else {
                showStr = str.substring(0, 10);
            }
            return showStr;
        }
    }

    /**
     * 将英文单位转换为汉字
     *
     * @param English 英文单位
     * @auther sll @date 2017/7/19 9:38
     */
    public static String translationEnglishUnit(String English) {
        String chinese = ResourcesTransformUtil.getString(R.string.year);
        if (English.equals("year")) {
            chinese = ResourcesTransformUtil.getString(R.string.year);
        } else if (English.equals("month")) {
            chinese = ResourcesTransformUtil.getString(R.string.month);
        } else if (English.equals("day")) {
            chinese = ResourcesTransformUtil.getString(R.string.day);
        }
        return chinese;
    }

    /**
     * 获取两个日期之间的间隔天数
     *
     * @return
     */
    private int getGapCount(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * 比较两个日期的大小，日期格式为yyyy/MM/dd HH:mm:ss
     *
     * @param str1 the first date
     * @param str2 the second date
     * @return true 前边日期早 false 后边日期早
     */
    public static boolean timeCompare(String str1, String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = false;
        } else if (dt1.getTime() <= dt2.getTime()) {
            isBigger = true;
        }
        return isBigger;
    }


    /**
     * 根据时间戳 返回 对应 的时间格式
     *
     * @param format    参考格式 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String transformationDate(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        String dateStr = dateFormat.format(date);
        return dateStr;
    }


    /**
     * 根据时间戳 返回 对应 的时间格式
     *
     * @return
     */
    public static String transformationDate(String time, String toFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = formatter.parse(time);
            return new SimpleDateFormat(toFormat).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

     /**
     * 根据时间戳 返回 对应 的时间格式
     *
     * @param format    参考格式 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String transformationDate(String time, String format, String toFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            Date date = formatter.parse(time);
            return new SimpleDateFormat(toFormat).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }



    /**
     * 根据时间戳 返回 对应 的时间格式
     *
     * @param timestamp 当前 时间戳  一定要精确到 毫秒
     * @param format    参考格式 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String timestampToDateStr(Long timestamp, String format) {
        Date date = new Date(timestamp);
        DateFormat dateFormat = new SimpleDateFormat(format);
        String dateStr = dateFormat.format(date);
        return dateStr;
    }

    /**
     * 根据日期获取当天是周几
     *
     * @param datetime 日期  必须是 yyyy-MM-dd 格式的
     * @return 周几
     */
    public static String dateToWeek(String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        Date date;
        try {
            date = sdf.parse(datetime);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDays[w];
    }

    /**
     * 获取当前日期的 前一周的数据 或 后一周的日期
     *
     * @param type 0:当前日期 前6天的数据  1：当前日期后6天的数据
     * @return 六天的数据
     */
    public static List<TempDateEntity> getDates(int type) {
        List<TempDateEntity> arr = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        if (type == 0) {
            arr = new LinkedList<>();
            for (int i = 1; i < 7; i++) {
                TempDateEntity entity = new TempDateEntity();
                entity.dateStr = sdf.format(getDateBefore(date, i));
                entity.dateW = dateToWeek(entity.dateStr, "yyyy-MM-dd");
                entity.dateShow = entity.dateStr.substring(5) + "\n" + entity.dateW.replace("星期", "周");
                arr.add(0, entity);
            }
        } else if (type == 1) {
            arr = new ArrayList<>();
            for (int i = 1; i < 7; i++) {
                TempDateEntity entity = new TempDateEntity();
                entity.dateStr = sdf.format(getSpecifiedDayAfter(date, i));
                entity.dateW = dateToWeek(entity.dateStr, "yyyy-MM-dd");
                entity.dateShow = entity.dateStr.substring(5) + "\n" + entity.dateW.replace("星期", "周");
                arr.add(entity);
            }
        }


        return arr;
    }

    /**
     * 得到几天前的时间
     *  
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar no = Calendar.getInstance();
        no.setTime(d);
        no.set(Calendar.DATE, no.get(Calendar.DATE) - day);
        return no.getTime();
    }

    /**
     * 得到 几天后的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getSpecifiedDayAfter(Date d, int day) {
        Calendar no = Calendar.getInstance();
        no.setTime(d);
        int getDate = no.get(Calendar.DATE);
        no.set(Calendar.DATE, getDate + day);
        return no.getTime();
    }

    /**
     * 日期转星期
     *
     * @param datetime 日期
     * @param format   格式
     * @return
     */
    public static String dateToWeek(String datetime, String format) {
        SimpleDateFormat f = new SimpleDateFormat(format);
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }


}