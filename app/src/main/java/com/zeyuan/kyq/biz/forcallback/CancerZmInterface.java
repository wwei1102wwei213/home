package com.zeyuan.kyq.biz.forcallback;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/22.
 */
public interface CancerZmInterface extends Serializable{

    void chooseCallBack(String title,String typeID,int tag);

}
