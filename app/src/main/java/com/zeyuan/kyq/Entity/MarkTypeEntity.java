package com.zeyuan.kyq.Entity;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/14.
 *
 * @author wwei
 */
public class MarkTypeEntity implements Serializable{

    private int status;
    private String TypeList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<String> getTypeList() {
        ArrayList<String> temp = new ArrayList<>();
        if (TextUtils.isEmpty(TypeList)){
            return temp;
        }
        String args[] = TypeList.split(",");
        for (String str:args){
            temp.add(str);
        }
        return temp;
    }

    public void setTypeList(String typeList) {
        TypeList = typeList;
    }

    @Override
    public String toString() {
        return "MarkTypeEntity{" +
                "status=" + status +
                ", TypeList='" + TypeList + '\'' +
                '}';
    }
}
