package com.zeyuan.kyq.utils;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.zeyuan.kyq.Entity.ConfStepEntity;
import com.zeyuan.kyq.biz.Factory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2015/8/28.
 * 数据工具类 把后台数据转为显示的数据和把显示数据转为上传给后台需要的数据
 */
public final class DataUtils {

    private static final String TAG = "ChooseMedicaDialog";

    public static String inputStreamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int ch = -1;
        byte[] buffer = new byte[1024 * 4];
        while ((ch = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, ch);
        }
        baos.flush();
        baos.close();
        inputStream.close();
        String responseXml = baos.toString();
        return responseXml;
    }

    public String getPostString(String path, Context context) throws IOException {
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(500);
        connection.setReadTimeout(500);
        connection.connect();
        if (connection.getResponseCode() != 200) {
            Toast.makeText(context, "没网", Toast.LENGTH_SHORT).show();
            return null;
        }
        InputStream inputStream = connection.getInputStream();
        String s = inputStreamToString(inputStream);
        return s;
    }


    public String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    /**
     * 由stepUID组成的list转化为显示的string
     * 如：易瑞沙，特罗凯，紫杉醇
     *
     * @param list 这个list里面装的是 药物id组成的list
     * @return 需要显示的string字符串，使用时请判断string是否为空
     */

    /*public static String listToString(List<String> list) {
        String result = "";
        LogUtil.i(TAG, "选中药物的个数是：" + list.size());
        if (list == null && list.size() == 0) {
            return result;
        }
        for (int i = 0; i < list.size(); i++) {
            String str = GlobalData.cureValues.get(list.get(i));
            if (i == 0) {
                result = str;
            } else {
                result = result + ",".concat(str);
            }
        }
        return result;

    }*/

    /**
     * 由stepUID组成的list转化为要提交给后台的string
     * 如5001,5002
     *
     * @param list 由stepUID组成的list
     * @return 后台需要的string
     */
    public static String listToLoadString(List<String> list) {
        String result = "";
        if (list == null && list.size() == 0) {
            return result;
        }
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            if (i == 0) {
                result = str;
            } else {
                result = result + ",".concat(str);
            }
        }
        return result;
    }


    /**
     * 把后台传的String转化为显示的String
     * 如5001,5002 转化为 易瑞沙，特罗凯
     *
     * @param loadString 需要转化的数据
     * @return 显示在屏幕的数据
     */
    public static String loadStringToShowString(String loadString) {
        if (TextUtils.isEmpty(loadString)) return "";
        if ("0".equals(loadString)) return "空窗期";
        String result = "";
        try {
            String[] strings = loadString.split(",");
            SparseArray<ConfStepEntity> array = (SparseArray<ConfStepEntity>) Factory.getData(Const.N_DataDrugNames);
            for (int i = 0; i < strings.length; i++) {
                String str = array.get(Integer.valueOf(strings[i])).getStepName();
                if(!TextUtils.isEmpty(str)){
                    if (i == 0) {
                        result = str;
                    } else {
                        result = result + ",".concat(str);
                    }
                }
            }
        }catch (Exception e){

        }
        if(TextUtils.isEmpty(result)) result = "";
        return result;
    }

    /**
     * 把在桌面是显示的时间 如2015-07-08转化为需要上传给服务器的时间（秒数）
     *
     * @return
     */
    public static String showTimeToLoadTime(String showtime) {
        if ("至今".equals(showtime)||"更早".equals(showtime)){
            return "0";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String loadingTime = "";
        try {
            Date time = format.parse(showtime);
            long timenum = time.getTime();
            loadingTime = String.valueOf(timenum / 1000);
//            loadingTime = temp.substring(0, temp.length() - 3);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("输入的时间格式不对");
        }
        return loadingTime;
    }

    /**
     * 把在桌面是显示的时间 如2015/07/08转化为需要上传给服务器的时间（秒数）
     *
     * @return
     */
    public static String showTimeToLoadTime2(String showtime) {
        if ("至今".equals(showtime)||"更早".equals(showtime)){
            return "0";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String loadingTime = "";
        try {
            Date time = format.parse(showtime);
            long timenum = time.getTime();
            loadingTime = String.valueOf(timenum / 1000);
//            loadingTime = temp.substring(0, temp.length() - 3);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("输入的时间格式不对");
        }
        return loadingTime;
    }

    public static long showTimeMillsForTimeStr(String showtime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date time = format.parse(showtime);
            return time.getTime()/1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }



    /**
     * 这样处理是后台给的数据是秒
     * @param date 后台给的数据加上“000”
     * @return 返回一个需要的日期字符串
     */
    public static String getDate(String date) {
        if (date==null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        long dates = Long.parseLong(date.concat("000"));
        return format.format(new Date(dates));
    }


    public static String getDateDetail(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long dates = Long.parseLong(date.concat("000"));
//        long dates = Long.parseLong(date);
        return format.format(new Date(dates));
    }


    public static String getDateForTimeMills() {
        String time = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            time = format.format(new Date(System.currentTimeMillis()));
        }catch (Exception e){
            e.printStackTrace();
        }
        return time;
    }

    /***
     *
     * 获得日期串
     *
     * @param input
     * @return
     */
    public static String getDateString(String input) {
        if(TextUtils.isEmpty(input)){
            return "";
        }
        String temp = "";
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(input));
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String Month = month<10?"0"+month:""+month;
            String Day = day<10?"0"+day:""+day;
            temp = year +"-"+ Month +"-"+Day;
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "时间串格式化错误");
        }
        return temp;
    }

    /**
     * 得到当前的日期
     *
     * @return 日期
     */
    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 得到后台返回数据的日期 几号
     *
     * @param inputtime 秒数
     * @return 几号
     */
    public static int getInputDay(String inputtime) {
        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(Integer.valueOf(inputtime + "000"));
        calendar.setTimeInMillis(Integer.valueOf(inputtime));
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 得到当前的小时 如2015-11-19 17:10 得到17
     *
     * @return 小时
     */
    public static int getCurrentHour() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 得到后台返回数据的日期 几号 如2015-11-19 17:10 得到17
     *
     * @param inputtime 秒数
     * @return 几号
     */
    public static int getInputHour(String inputtime) {
        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(Integer.valueOf(inputtime + "000"));
        calendar.setTimeInMillis(Integer.valueOf(inputtime));
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 得到当前的分钟 如2015-11-19 17:10 得到10
     *
     * @return 分钟
     */
    public static int getCurrentMin() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 得到后台的分钟
     *
     * @return 分钟
     */
    public static int getInputMin(String inputTime) {
        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(Integer.valueOf(inputTime + "000"));
        calendar.setTimeInMillis(Integer.valueOf(inputTime));
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 判断是否显示日期
     *
     * @param inputTime
     * @return true表示超过10天 false 没有超过10天
     */
    public static boolean isShowTime(String inputTime) {
        long showTime = Integer.valueOf(inputTime);
        long second = System.currentTimeMillis() / 1000 - showTime;//相差的秒数
        long day = second / (60 * 60 * 24);
        if (day > 10) {
            return true;
        }
        return false;
    }

    /**
     * > 10天显示日期
     * < 10天 显示几天前 其中以前显示昨天
     * < 1天显示 多少小时前
     * < 1小时 显示多少分钟前
     * < 5分钟 显示刚刚
     *
     * @param "后台传的秒数"
     * @return 所需要显示的字符串
     */
    public static String showDay(String inputtime) {
        if (isShowTime(inputtime)) {//是否超过10天
            return getDate(inputtime);
        }
        //没有超过10天
        int day = getCurrentDay() - getInputDay(inputtime);//判断后台输入的时间和现在的时间相隔的天数
        if (day > 2) { //大于2天 小于10天
            return day + "天前";
        }
        if (day >= 1 && day <= 2) { //大于1 小鱼2
            return "昨天";
        }
        //剩下的都是在1天之类
        int hour = getCurrentHour() - getInputHour(inputtime);//相差的小时
        if (hour >= 1) {//超过一个小时
            return hour + "小时前";
        }
        //剩下的都是在1小时之类
        int min = getCurrentMin() - getInputMin(inputtime);//相差的分钟
        if (min < 5) {
            return "刚刚";
        }
        if (min >= 5) {
            return min + "分钟前";
        }
        return "";
    }


    public static String getVersionName(Context context) {
        String version = "1.0";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    /***
     *
     * 获得天数，判断配置数据时调用
     *
     * @param inputTime
     * @return
     */
    public static long getDayForLong(String inputTime){
        if(inputTime==null){
            return 30;
        }
        long showTime = Integer.valueOf(inputTime);
        if(showTime<0){
            return 30;
        }
        long second = System.currentTimeMillis() / 1000 - showTime;//相差的秒数
        long day = second / (60 * 60 * 24);
        return day;
    }

    /***
     *
     * 获得天数，判断配置数据时调用
     *
     * @param inputTime
     * @return
     */
    public static String getDayForStepDetail(String inputTime){
        if(inputTime==null){
            return null;
        }
        if("1".equals(inputTime)){
            return null;
        }
        long showTime = Integer.valueOf(inputTime);
        if(showTime<0){
            return null;
        }
        long second = System.currentTimeMillis() / 1000 - showTime;//相差的秒数
        long day = second / (60 * 60 * 24);
        if(day == 0) day = 1;
        return day+"天";
    }

    /***
     *
     * 获得天数，判断配置数据时调用
     *
     * @param inputTime
     * @return
     */
    public static String getDayForStepDetail(String inputTime,int flag){
        if(inputTime==null){
            return null;
        }
        if("1".equals(inputTime)){
            return null;
        }
        long showTime = Integer.valueOf(inputTime);
        if(showTime<0){
            return null;
        }
        long second = System.currentTimeMillis() / 1000 - showTime;//相差的秒数
        long day = second / (60 * 60 * 24);
        if(day == 0) day = 1;
        return day+"";
    }

    /***
     *
     * 获得天数，判断配置数据时调用
     *
     * @param inputTime
     * @return
     */
    public static String getDayForStepDetail(String inputTime,String endTime){
        long second = 0;
        if(inputTime==null){
            return null;
        }
        if("1".equals(inputTime)){
            return null;
        }
        long showTime = Integer.valueOf(inputTime);
        if(showTime<0){
            return null;
        }
        if(TextUtils.isEmpty(endTime)||endTime.equals("0")){
            second = System.currentTimeMillis() / 1000 - showTime;
        }else {
            second = Integer.valueOf(endTime) - showTime;//相差的秒数
        }
        long day = second / (60 * 60 * 24);
        if(day>0) day++;
        if(day == 0) day = 1;
        return day+"天";
    }

    public static String getDayForEditStep(int inputTime,int endTime){

        if (inputTime<0||endTime<0) return "";
        long second = 0;
        if(endTime==0){
            second = System.currentTimeMillis() / 1000 - inputTime;
        }else {
            second = endTime - inputTime;//相差的秒数
        }
        long day = second / (60 * 60 * 24);
        if(day>0) day++;
        if(day == 0) day = 1;
        return day+"天";
    }

    public static String showFormatTime(String inputTime){
        if(inputTime==null){
            return "";
        }
        long showTime = Integer.valueOf(inputTime);
        if(showTime<0){
            return "";
        }
        long second = System.currentTimeMillis() / 1000 - showTime;//相差的秒数
        long day = second / (60 * 60 * 24);
//        System.out.println("time:"+inputTime+"Day:"+day+"%:"+);
        if (day > 10) {
            return getDate(inputTime);
        }else if (day > 1 && day <= 10) { //大于2天 小于10天
            return day + "天前";
        }else if (day == 1) { //大于1 小鱼2
            return "昨天";
        }else {
            long hour = second / (60 * 60);
            if (hour >= 1) {//超过一个小时
                return hour + "小时前";
            }else {
                long min = second / 60;
                if (min >= 5) {
                    return min + "分钟前";
                }else {
                    return "刚刚";
                }
            }
        }
    }

    public static String getRandomMath(){
        String temp = (int)(Math.random()*89999 + 10000)+"";
        return temp;
    }

    public static String showFormatTimeSecond(String inputTime){
        if(inputTime==null){
            return "";
        }
        long showTime = Integer.valueOf(inputTime);
        if(showTime<0){
            return "";
        }
        long second = System.currentTimeMillis() / 1000 - showTime;//相差的秒数
//        Log.i("ZYS", "showTime:" + showTime);
//        Log.i("ZYS", "currentTimeMillis:" + System.currentTimeMillis() / 1000 );
        long day = second / (60 * 60 * 24);

        if (day > 10) {
            return getDate(inputTime);
        }else if (day > 1 && day <= 10) { //大于1天 小于10天
            return day + "天前";
        }else if (day == 1) { //大于1 小鱼2
            return "昨天";
        }else {
            long hour = second / (60 * 60);
            if (hour >= 1) {//超过一个小时
                return hour + "小时前";
            }else {
                long min = second / 60;
                if (min >= 1) {
                    return min + "分钟前";
                }else {
                    return second +"秒前";
                }
            }
        }
    }

    public static String getCurrentTime(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }

    public static String getCurrentTime() {
        return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
    }

    public static String getStartTime(int tills){
        if (tills==0) return  "更早";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            return sdf.format(new Date(Long.parseLong((tills+"").concat("000"))));
        }catch (Exception e){

        }
        return "";
    }

    public static String getEndTime(int tills){
        if (tills==0) return  "至今";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            return sdf.format(new Date(Long.parseLong((tills+"").concat("000"))));
        }catch (Exception e){

        }
        return "";
    }

    /**
     * 判断给定字符串时间是否为今日
     * @param longTime
     * @return boolean
     */
    public static boolean isToday(String longTime){
        boolean b = false;
        /*long lTime = Long.parseLong(sdate);
        Log.i("ZYS","TIME:"+lTime);
        String ssdate = dateFormater2.get().format(lTime);
        Date time = toDate(ssdate);
        Log.i("ZYS","TIME:"+time);
        Date today = new Date();
        if(time != null){
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if(nowDate.equals(timeDate)){
                b = true;
            }
        }*/
        Date date = new Date(Long.parseLong(longTime));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String old = format.format(date);
        Log.i("ZYS", "TIME:" + old);
        System.out.println(old);
        String now = format.format(new Date());
        Log.i("ZYS","TIME:"+now);
        System.out.println(now);
        if(now.equals(old)){
            b = true;
        }
        return b;
    }

    /*public static String getTime(long time) {
        SimpleDateFormat format=new SimpleDateFormat(yyyy-MM-dd HH:mm:ss);
        Date d1=new Date(time);
        return format.format(d1);*/

    /**
     * 将字符串转位日期类型
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 病理报告日期
     * @param date
     * @return
     */
    public static String getPathologyDate(String date) {
        if (OtherUtils.isEmpty(date)) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        long dates = Long.parseLong(date.concat("000"));
        return format.format(new Date(dates));
    }

    /**
     * 患者病历日期
     * @param date
     * @return
     */
    public static String getPatientDate(String date) {
        if (OtherUtils.isEmpty(date)) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        long dates = Long.parseLong(date.concat("000"));
        return format.format(new Date(dates));
    }

    /**
     * 患者病历日期
     * @param date
     * @return
     */
    public static String getPatientDateNoYear(String date) {
        if (OtherUtils.isEmpty(date)) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
        long dates = Long.parseLong(date.concat("000"));
        return format.format(new Date(dates));
    }

    /**
     * 病理报告日期
     * @param date
     * @return
     */
    public static String getPathologyWeek(String date) {
        if (OtherUtils.isEmpty(date)) {
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(date.concat("000")));
        return getStringForWeek(calendar.get(Calendar.DAY_OF_WEEK));

    }

    /**
     * 病理报告日期
     * @param date
     * @return
     */
    public static int getPathologyYear(Long date) {
        if (date == 0) return 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date*1000);
        return calendar.get(Calendar.YEAR);
    }

    public static String getStringForWeek(int week){
        String w = "星期";
        switch (week){
            case 0:
                w += "一";
                break;
            case 1:
                w += "二";
                break;
            case 2:
                w += "三";
                break;
            case 3:
                w += "四";
                break;
            case 4:
                w += "五";
                break;
            case 5:
                w += "六";
                break;
            case 6:
                w += "日";
                break;
            default:
                w = "";
                break;
        }
        return w;
    }

    /**
     * 这样处理是后台给的数据是秒
     * @param date 后台给的数据加上“000”
     * @return 返回一个需要的日期字符串
     */
    public static String getRecordDate(long date) {
        if (date==0) return "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        return format.format(new Date(date*1000));
    }

    /**
     * 这样处理是后台给的数据是秒
     * @param date 后台给的数据加上“000”
     * @return 返回一个需要的日期字符串
     */
    public static String getRecordDateForStep(long date) {
        if (date==0) return "至今";
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd",Locale.getDefault());
        return format.format(new Date(date*1000));
    }


}
