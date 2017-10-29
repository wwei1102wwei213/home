package com.zeyuan.kyq.http.bean;

import java.util.Comparator;

public class UserStepComparator implements Comparator<UserStepBean> {
    public UserStepComparator() {
    }

    public UserStepComparator(boolean isdesc) {
        this.isdesc = isdesc;
    }

    private boolean isdesc;

    @Override
    public int compare(UserStepBean lhs, UserStepBean rhs) {
        long temp = 0;
        if (this.isdesc) {
            temp = rhs.getCompareDateBeg() - lhs.getCompareDateBeg();
        } else {
            temp = lhs.getCompareDateBeg() - rhs.getCompareDateBeg();
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