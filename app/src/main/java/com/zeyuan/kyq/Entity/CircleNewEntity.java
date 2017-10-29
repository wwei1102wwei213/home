package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/15.
 */
public class CircleNewEntity implements Serializable{

    private String CircleID;
    private String CircleName;
    private int CircleType;

    public String getCircleID() {
        return CircleID;
    }

    public void setCircleID(String circleID) {
        CircleID = circleID;
    }

    public String getCircleName() {
        return CircleName;
    }

    public void setCircleName(String circleName) {
        CircleName = circleName;
    }

    public int getCircleType() {
        return CircleType;
    }

    public void setCircleType(int circleType) {
        CircleType = circleType;
    }

    @Override
    public String toString() {
        return "CircleNewEntity{" +
                "CircleID='" + CircleID + '\'' +
                ", CircleName='" + CircleName + '\'' +
                ", CircleType='" + CircleType + '\'' +
                '}';
    }
}
