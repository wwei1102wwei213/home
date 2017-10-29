package com.zeyuan.kyq.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/29.
 *
 * @author wwei
 */
public class PushMsgBean implements Serializable{

    private String iResult;
    private List<PushMsgEntity> data;

    public String IResult() {
        return iResult;
    }

    public List<PushMsgEntity> getData() {
        return data;
    }

    public void setData(List<PushMsgEntity> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PushMsgBean{" +
                "iResult='" + iResult + '\'' +
                ", data=" + data +
                '}';
    }
}
