package com.zeyuan.kyq.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/22.
 *
 * @author wwei
 */
public class PersonalBean implements Serializable{


    private String iResult;

    private PersonalEntity data;

    public String getiResult() {
        return iResult;
    }

    public void setiResult(String iResult) {
        this.iResult = iResult;
    }

    public PersonalEntity getData() {
        return data;
    }

    public void setData(PersonalEntity data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PersonalBean{" +
                "iResult='" + iResult + '\'' +
                ", data=" + data +
                '}';
    }
}
