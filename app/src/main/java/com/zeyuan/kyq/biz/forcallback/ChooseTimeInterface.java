package com.zeyuan.kyq.biz.forcallback;

import android.os.Bundle;
import android.os.IBinder;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/30.
 *
 * 时间控件回调接口
 *
 * @author wwei
 */
public interface ChooseTimeInterface extends Serializable{

    void timeCallBack(String time);

}
