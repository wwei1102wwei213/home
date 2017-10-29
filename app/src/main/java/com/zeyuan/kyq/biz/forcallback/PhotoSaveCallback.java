package com.zeyuan.kyq.biz.forcallback;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;

/**
 * Created by Administrator on 2016/7/15.
 *
 *
 * @author wwei
 */
public class PhotoSaveCallback extends SaveCallback{

    private String Tag;
    private int flag;
    private PhotoSaveCallback.CallBack callback;


    public PhotoSaveCallback(String Tag,PhotoSaveCallback.CallBack callback,int flag){
        this.Tag = Tag;
        this.flag = flag;
        this.callback = callback;
    }

    @Override
    public void onSuccess(String s) {
        callback.onSuccess(s,Tag,flag);
    }

    @Override
    public void onProgress(String s, int i, int i1) {
        callback.onProgress(s,i,i1,Tag,flag);
    }

    @Override
    public void onFailure(String s, OSSException e) {
        callback.onFailure(s,e,Tag,flag);
    }

    public interface CallBack{

        void  onSuccess(String s,String Tag,int flag);

        void  onProgress(String s,int i,int i1,String Tag,int flag);

        void onFailure(String s,OSSException e,String Tag,int flag);

    }


}
