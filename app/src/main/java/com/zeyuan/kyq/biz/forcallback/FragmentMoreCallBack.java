package com.zeyuan.kyq.biz.forcallback;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/15.
 */
public interface FragmentMoreCallBack extends Serializable {

    /**
     *
     * @param str 字符串返回
     * @param flag int类型标识
     * @param tag String类型标识
     * @param obj  Object类型返回
     */
    void dataCallBack(String str,int flag,String tag,List<String> listForData,Map<String,Integer> map,Object obj);
}
