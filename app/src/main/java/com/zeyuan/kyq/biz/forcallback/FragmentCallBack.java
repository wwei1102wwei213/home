package com.zeyuan.kyq.biz.forcallback;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/4.
 *
 *
 *
 *
 * @author wwei
 */
public interface FragmentCallBack extends Serializable{

    /**
     *
     * @param str 字符串返回
     * @param flag int类型标识
     * @param tag String类型标识
     * @param obj  Object类型返回
     */
    void dataCallBack(String str,int flag,String tag,Object obj);
}
