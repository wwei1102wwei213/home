package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/16.
 *
 *
 * @author wwei
 */
public class ChartEntity implements Serializable{

    private int width;
    private int height;
    private String date;
    private int year;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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
        return "ChartEntity{" +
                "width=" + width +
                ", height=" + height +
                ", date='" + date + '\'' +
                ", year=" + year +
                '}';
    }
}
