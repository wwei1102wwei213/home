package com.zeyuan.kyq.biz.forcallback;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/15.
 *
 * @author wwei
 */
public interface ChooseTimeNewInterface extends Serializable{

    void onTimeCallBack(String time,int ViewTag,int selection);

}
