package com.zeyuan.kyq.http.bean;

import java.util.Comparator;

public class UserStepChildComparator implements Comparator<UserStepChildBean> {
    public UserStepChildComparator() {
    }

    public UserStepChildComparator(boolean isdesc) {
        this.isdesc = isdesc;
    }

    private boolean isdesc;

    @Override
    public int compare(UserStepChildBean lhs, UserStepChildBean rhs) {
        long temp = 0;
        if (this.isdesc) {
            temp = rhs.getRecordTime() - lhs.getRecordTime();
        } else {
            temp = lhs.getRecordTime() - rhs.getRecordTime();
        }
//        long temp = rhs.$test - rhs.$test;
        if (temp > 0) {
            return 1;
        } else if (temp < 0) {
            return -1;
        } else {
            return 0;
        }
    }
}