package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/7.
 *
 * @author wwei
 */
public class ChartFloatEntity implements Serializable{

    private float size;
    private String date;
    private int year;

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "ChartFloatEntity{" +
                "size=" + size +
                ", date='" + date + '\'' +
                ", year=" + year +
                '}';
    }
}
