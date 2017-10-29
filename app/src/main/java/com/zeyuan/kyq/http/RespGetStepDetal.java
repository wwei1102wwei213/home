package com.zeyuan.kyq.http;

import com.zeyuan.kyq.http.bean.UserStepChildBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guogzhao on 16/1/18.
 */
public class RespGetStepDetal extends  RespBase implements Serializable{

    private List<UserStepChildBean> RecordNum;

    public List<UserStepChildBean> getRecordNum() {
        return RecordNum;
    }
}
