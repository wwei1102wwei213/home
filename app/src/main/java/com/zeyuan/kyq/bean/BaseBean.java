package com.zeyuan.kyq.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/6.
 */
public class BaseBean implements Serializable{
    public String iResult;
    public String ErrMsg;
    public String uid;
    public String infoid;
    public String NowTime;

    @Override
    public String toString() {
        return "BaseBean [iResult=" + iResult + ", ErrMsg=" + ErrMsg + ", uid="
                + uid + ", infoid=" + infoid + ", NowTime=" + NowTime + "]";
    }
}
