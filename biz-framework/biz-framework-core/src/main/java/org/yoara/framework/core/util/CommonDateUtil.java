package org.yoara.framework.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

/**
 * 时间控制相关的工具类
 * Created by yoara on 2016/3/3.
 */
public class CommonDateUtil {
    /**
     * 获取指定日期零点 00:00:00
     *
     * @return Date
     * @date 2014-9-4 上午09:30:12
     */
    public static Date getZeroOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获得当天剩下的秒数
     **/
    public static long getTodayLeftSeconds() {
        Date now = new Date();
        return secondBetween(new Date(),getZeroOfDay(addDay(now,1)));
    }

    public static Date getFirstDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取前后N个年时间Date
     * @param date
     * @param year
     * @return Date
     */
    public static Date addYear(Date date, int year) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, year);
        date = c.getTime();
        return date;
    }

    /**
     * 获取前后N个月时间Date
     * @param date
     * @param month
     * @return Date
     */
    public static Date addMonth(Date date, int month) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, month);
        date = c.getTime();
        return date;
    }

    /**
     * 获取前后N天时间Date
     *
     * @param date
     * @param day
     * @return Date
     */
    public static Date addDay(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, day);
        date = c.getTime();
        return date;
    }

    /** 获取前后N小时时间Date */
    public static Date addHour(Date date, int hours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY,hours);
        return cal.getTime();
    }

    /** 获取前后N小时时间Date */
    public static Date addMinute(Date date, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE,minute);
        return cal.getTime();
    }
    /**
     * 计算两个日期相差天数
     *
     * @param startDate 开始日期
     * @param endDate  结束日期
     * @return
     * @throws ParseException
     */
    public static int daysBetween(Date startDate, Date endDate) throws ParseException {
        LocalDate sDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate eDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period period = Period.between(eDate,sDate);
        return period.getDays();
    }

    /**
     * 功能说明：计算两个日期相差的秒
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     * @date 2014-12-5
     */
    public static long secondBetween(Date startTime,Date endTime) {
        LocalTime sTime = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        LocalTime eTime = endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        Duration duration = Duration.between(eTime,sTime);
        return duration.getSeconds();
    }

    /**取得一个日期是星期几*/
    public static String getDayOfWeekNum(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case 1:
                return "7";
            case 2:
                return "1";
            case 3:
                return "2";
            case 4:
                return "3";
            case 5:
                return "4";
            case 6:
                return "5";
            case 7:
                return "6";
            default:
                return "7";
        }
    }
    /**
     * 从身份证中提取出生日期
     *
     * @return yyyy-MM-dd,不满足15或18位,返回空
     */
    public static String getBirthdayFromIdNo(String idNo) {
        if (idNo.length() == 15) {
            return new StringBuffer(
                    Integer.parseInt(idNo.substring(6, 8)) + 1900).append("-")
                    .append(idNo.substring(8, 10)).append("-").append(
                            idNo.substring(10, 12)).toString();
        } else if (idNo.length() == 18) {
            return new StringBuffer(idNo.substring(6, 10)).append("-").append(
                    idNo.substring(10, 12)).append("-").append(
                    idNo.substring(12, 14)).toString();
        } else {
            return "";
        }
    }

    /**
     * 将月份的英文缩写转成相应的int型，供Calendar、Date等日期类型使用
     */
    public static int parseMonth(String monthStr) {
        if ("JAN".equalsIgnoreCase(monthStr)) {
            return 0;
        } else if ("FEB".equalsIgnoreCase(monthStr)) {
            return 1;
        } else if ("MAR".equalsIgnoreCase(monthStr)) {
            return 2;
        } else if ("APR".equalsIgnoreCase(monthStr)) {
            return 3;
        } else if ("MAY".equalsIgnoreCase(monthStr)) {
            return 4;
        } else if ("JUN".equalsIgnoreCase(monthStr)) {
            return 5;
        } else if ("JUL".equalsIgnoreCase(monthStr)) {
            return 6;
        } else if ("AUG".equalsIgnoreCase(monthStr)) {
            return 7;
        } else if ("SEP".equalsIgnoreCase(monthStr)) {
            return 8;
        } else if ("OCT".equalsIgnoreCase(monthStr)) {
            return 9;
        } else if ("NOV".equalsIgnoreCase(monthStr)) {
            return 10;
        } else if ("DEC".equalsIgnoreCase(monthStr)) {
            return 11;
        } else {
            return -1;
        }
    }


    private final static String format_yyyy_MM_ddHH_mm_ss 	= "yyyy-MM-dd HH:mm:ss";
    private final static String format_yyyyMMddHHmmss 		= "yyyyMMddHHmmss";
    private final static String format_yyyyMMdd 			= "yyyyMMdd";
    private final static String format_yyyy_MM_dd 			= "yyyy-MM-dd";
    private final static String format_ddMMMyy 				= "ddMMMyy";
    private final static String format_freedom 				= "freedom";

    private static ThreadLocal<Map<String,SimpleDateFormat>> threadLocal =
            new ThreadLocal<Map<String,SimpleDateFormat>>(){
                @Override
                protected Map<String,SimpleDateFormat> initialValue() {
                    Map<String,SimpleDateFormat> dateFormatMap 	= new HashMap<String,SimpleDateFormat>();
                    SimpleDateFormat sdf_yyyy_MM_ddHH_mm_ss 	= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat sdf_yyyyMMddHHmmss 		= new SimpleDateFormat("yyyyMMddHHmmss");
                    SimpleDateFormat sdfyyyyMMdd 				= new SimpleDateFormat("yyyyMMdd");
                    SimpleDateFormat sdf_yyyy_MM_dd 			= new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdf_ddMMMyy 				= new SimpleDateFormat("ddMMMyy", Locale.US);
                    SimpleDateFormat sdf_freedom				= new SimpleDateFormat();

                    dateFormatMap.put(format_yyyy_MM_ddHH_mm_ss, sdf_yyyy_MM_ddHH_mm_ss);
                    dateFormatMap.put(format_yyyyMMddHHmmss, sdf_yyyyMMddHHmmss);
                    dateFormatMap.put(format_yyyyMMdd, sdfyyyyMMdd);
                    dateFormatMap.put(format_yyyy_MM_dd, sdf_yyyy_MM_dd);
                    dateFormatMap.put(format_ddMMMyy, sdf_ddMMMyy);
                    dateFormatMap.put(format_freedom, sdf_freedom);

                    return dateFormatMap;
                }
            };
    /**自由转换方法
     * @param date 时间
     * @param format 时间格式
     * **/
    public static String parseDateToString(Date date, String format) {
        SimpleDateFormat simpleDateFormat = threadLocal.get().get(format_freedom);
        simpleDateFormat.applyPattern(format);
        if (date == null) {
            return null;
        }
        return simpleDateFormat.format(date);
    }
    /**自由转换方法
     * @param date 时间
     * @param format 时间格式
     * **/
    public static Date parseStringToDate(String date, String format) {
        SimpleDateFormat simpleDateFormat = threadLocal.get().get(format_freedom);
        simpleDateFormat.applyPattern(format);
        if (date == null) {
            return null;
        }
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
    /**DDMMMYY转成yyyyMMdd**/
    public static String formatDDMMMYYToyyyyMMdd(String date) throws ParseException {
        return threadLocal.get().get(format_yyyyMMdd).format(
                threadLocal.get().get(format_ddMMMyy).parse(date));
    }
    /**DDMMMYY转成yyyy-MM-dd**/
    public static String formatDDMMMYYToyyyy_MM_dd(String date) throws ParseException {
        return threadLocal.get().get(format_yyyy_MM_dd).format(
                threadLocal.get().get(format_ddMMMyy).parse(date));
    }
    /**DDMMMYY转成Date**/
    public static Date formatDDMMMYYToDate(String date) throws ParseException {
        return threadLocal.get().get(format_ddMMMyy).parse(date);
    }
    /**Date转成DDMMMYY**/
    public static String formatDateToDDMMMYY(Date date){
        return threadLocal.get().get(format_ddMMMyy).format(date).toUpperCase();
    }

    /**yyyyMMdd转成DDMMMYY**/
    public static String formatyyyyMMddToDDMMMYY(String date) throws ParseException {
        return threadLocal.get().get(format_ddMMMyy).format(
                threadLocal.get().get(format_yyyyMMdd).parse(date));
    }
    /**yyyyMMdd转成yyyy-MM-dd**/
    public static String formatyyyyMMddToyyyy_MM_dd(String date) throws ParseException {
        return threadLocal.get().get(format_yyyy_MM_dd).format(
                threadLocal.get().get(format_yyyyMMdd).parse(date));
    }
    /**yyyyMMdd转成Date**/
    public static Date formatyyyyMMddToDate(String date) throws ParseException {
        return threadLocal.get().get(format_yyyyMMdd).parse(date);
    }
    /**Date转成yyyyMMdd**/
    public static String formatDateToyyyyMMdd(Date date){
        return threadLocal.get().get(format_yyyyMMdd).format(date);
    }

    /**yyyy-MM-dd转成DDMMMYY**/
    public static String formatyyyy_MM_ddToDDMMMYY(String date) throws ParseException {
        return threadLocal.get().get(format_ddMMMyy).format(
                threadLocal.get().get(format_yyyy_MM_dd).parse(date));
    }
    /**yyyy-MM-dd转成yyyyMMdd**/
    public static String formatyyyy_MM_ddToyyyyMMdd(String date) throws ParseException {
        return threadLocal.get().get(format_yyyyMMdd).format(
                threadLocal.get().get(format_yyyy_MM_dd).parse(date));
    }
    /**yyyy-MM-dd转成Date**/
    public static Date formatyyyy_MM_ddToDate(String date) throws ParseException {
        return threadLocal.get().get(format_yyyy_MM_dd).parse(date);
    }
    /**Date转成yyyy-MM-dd**/
    public static String formatDateToyyyy_MM_dd(Date date){
        return threadLocal.get().get(format_yyyy_MM_dd).format(date);
    }

    /**Date转成yyyyMMddHHmmss**/
    public static String formatDateToyyyyMMddHHmmss(Date date){
        return threadLocal.get().get(format_yyyyMMddHHmmss).format(date);
    }
    /**Date转成yyyy-MM-dd HH:mm:ss**/
    public static String formatDateToyyyy_MM_dd_HH_mm_ss(Date date){
        return threadLocal.get().get(format_yyyy_MM_ddHH_mm_ss).format(new Date());
    }

    /**格式化当前时间 yyyyMMdd**/
    public static String formatNowToyyyyMMdd(Date date){
        return threadLocal.get().get(format_yyyyMMdd).format(date);
    }
    /**格式化当前时间 yyyy-MM-dd**/
    public static String formatNowToyyyy_MM_dd(Date date){
        return threadLocal.get().get(format_yyyy_MM_dd).format(date);
    }
    /**格式化当前时间 DDMMMYY**/
    public static String formatNowToddMMMyy(Date date){
        return threadLocal.get().get(format_ddMMMyy).format(date);
    }
    /**格式化当前时间 yyyy-MM-dd HH:mm:ss**/
    public static String formatNowToyyyy_MM_dd_HH_mm_ss(Date date){
        return threadLocal.get().get(format_yyyy_MM_ddHH_mm_ss).format(date);
    }
    /**格式化当前时间 yyyyMMddHHmmss**/
    public static String formatNowToyyyyMMddHHmmss(Date date){
        return threadLocal.get().get(format_yyyyMMddHHmmss).format(date);
    }

    /** 判断日期变量是否为空 */
    public static boolean isNull(Date date) {
        return date == null;
    }
}
