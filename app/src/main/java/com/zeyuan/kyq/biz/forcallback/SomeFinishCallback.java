package com.zeyuan.kyq.biz.forcallback;

/**
 * Created by Administrator on 2016/11/16.
 *
 * 某些事件完成回调
 *
 * @author wwei
 */
public interface SomeFinishCallback {

    void doSomeFinish(String tag,int flag,int type,boolean fit,String str,Object obj);
    void doEmptyPage(String tag,int flag,int type,boolean fit,String str,Object obj);
}
