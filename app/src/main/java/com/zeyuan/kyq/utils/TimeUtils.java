package com.zeyuan.kyq.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/1/11.
 * 所传的参数必须是时间的字符串经过showtimetoloadtime
 */
public class TimeUtils {

    public TimeUtils(List<TimeBean> mTimeData) {
        this.dataorg = mTimeData;
        refreshData();
    }

    public void refreshData() {
        int size=this.dataorg.size();
        this.data =new ArrayList<>(size);
        TimeBean temp=null;
        for (int i=0;i<size;i++){
            temp=this.dataorg.get(i);
            data.add(new TimeBean(temp.getStartTime(),temp.getEndTime()));
        }
    }

    private List<TimeBean> dataorg;
    private List<TimeBean> data;

    /**
     * 编辑阶段，修改开始时间的最小值（可以落在左边）
     * 上一个阶段的结束时间
     *
     * @return
     */
    public long getStartTimeMin(String startTime) {
        int index = getSTPosition(startTime);
        if (index == 0) { //说明这个starttime是最小时间所在的阶段 这个时候可以改无限最小值
            return -1;
        }
        String leftStartTime = data.get(index - 1).getEndTime();
        return Long.valueOf(leftStartTime);
    }

    /**
     * 编辑阶段 开始时间的最大值（可以落在右边）
     * 是本阶段的结束时间
     *
     * @return
     */
    public long getStartTimeMax(String startTime) {
        int index = getSTPosition(startTime);
//        if (index == data.size() - 1) {//这个说明 是最后面的阶段。
//            return -1;
//        }
        String rightStartTime = data.get(index).getStartTime();
        return Long.valueOf(rightStartTime);
    }


    /**
     * 这个是修改开始时间的 判断输入的时间是否合法
     *
     * @param startTime 开始时间的秒数
     * @param inputTime 输入时间的秒数
     * @return true 合法 修改有效 false 不合法 修改无效
     */
    public boolean editStartTime(String startTime, String inputTime) {
        Long start = getStartTimeMin(startTime);
        Long end = getStartTimeMax(startTime);
        long input = Long.valueOf(inputTime);
        if (start < input && end > input) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 编辑阶段 结束时间的最大值
     * 下一个阶段的开始时间
     *
     * @param endTime
     * @return 修改结束时间时，结束时间的最大值
     */
    public long getEndTimeMax(String endTime) {
        int index = getETPosition(endTime);
        if (index == data.size() - 1) {//说明这个是最后一个阶段 他的最大值为无线
            return Long.MAX_VALUE;
        }
        String time = data.get(index + 1).getStartTime();
        return Long.valueOf(time);
    }

    /**
     * 编辑阶段 结束时间的最小值
     * 本阶段的开始时间
     *
     * @param endTime 要修改的
     * @return
     */
    public long getEndTimeMin(String endTime) {
        int index = getETPosition(endTime);
        String time = data.get(index).getStartTime();
        return Long.valueOf(time);
    }

    /**
     * 这个是修改结束时间的 判断输入的时间是否合法
     * @param endTime   原本开始时间的秒数
     * @param inputTime 输入时间的秒数
     * @return true 合法 修改有效 false 不合法 修改无效
     */
    public boolean editEndTime(String endTime, String inputTime) {
        Long start = getEndTimeMin(endTime);
        Long end = getEndTimeMax(endTime);
        long input = Long.valueOf(inputTime);
        if (start < input && end > input) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 得到修改开始时间的位置
     * @param startTime
     * @return
     */
    public int getSTPosition(String startTime) {
        for (int i = 0, j = data.size(); i < j; i++) {
            String time = data.get(i).getStartTime();
            if (startTime.equals(time)) {
                return i;
            }
        }
        return -1;
    }

    public int getETPosition(String startTime) {
        for (int i = 0, j = data.size(); i < j; i++) {
            String time = data.get(i).getEndTime();
            if (startTime.equals(time)) {
                return i;
            }
        }
        return -1;
    }

    public static String getPostTime(String posttime) throws ParseException{
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");

        return sdf.parse(posttime).toString();
    }
}
