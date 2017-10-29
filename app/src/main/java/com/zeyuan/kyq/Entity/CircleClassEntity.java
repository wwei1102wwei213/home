package com.zeyuan.kyq.Entity;

/**
 * Created by Administrator on 2016/8/12.
 */
public class CircleClassEntity {

    private String CircleID;
    private String ParentID;
    private boolean IsFrist;

    public String getCircleID() {
        return CircleID;
    }

    public void setCircleID(String circleID) {
        CircleID = circleID;
    }

    public String getParentID() {
        return ParentID;
    }

    public void setParentID(String parentID) {
        ParentID = parentID;
    }

    public boolean isFrist() {
        return IsFrist;
    }

    public void setIsFrist(boolean isFrist) {
        IsFrist = isFrist;
    }


    @Override
    public String toString() {
        return "CircleClassEntity{" +
                "CircleID='" + CircleID + '\'' +
                ", ParentID='" + ParentID + '\'' +
                ", IsFrist=" + IsFrist +
                '}';
    }
}
