package com.zeyuan.kyq.utils;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;

/**
 * Created by Administrator on 2016/7/15.
 */
public class UpdataPhotoUtils {
    public static final String TAG= "UpdataPhotoUtils";

    private void updatePhoto(File photo, final Context context){


        String Headimgurl = "";
        CDNHelper getDemo = new CDNHelper(context);
        CDNHelper littleDemo = new CDNHelper(context);
        String imageName = getImgName(context, true);
        try {

            Log.i(TAG, "图片地址：" + photo.getPath());
            getDemo.uploadFile(PhotoUtils.scal(photo.getPath(),PhotoUtils.SCAL_IMAGE_80).getPath(), imageName, new SaveCallback() {
                @Override
                public void onSuccess(String s) {
                    /*Log.i(TAG, "上传头像成功.名字是：" + s);
                    Headimgurl = getDemo.getResourseURL();
                    Log.i(TAG, "上传头像成功.URL是：" + Headimgurl);
                    UserinfoData.saveAvatarUrl(context,Headimgurl);*/
                }
                @Override
                public void onProgress(String s, int i, int i1) {
                }
                @Override
                public void onFailure(String s, OSSException e) {
                    LogUtil.i(TAG, "上传头像Failure");
                }
            });
        } catch (FileNotFoundException e) {

        }
        try {
            littleDemo.uploadFile(PhotoUtils.scal(photo.getPath(),PhotoUtils.SCAL_IMAGE_30).getPath(), insertThumb(imageName), new SaveCallback() {
                @Override
                public void onSuccess(String s) {
                    LogUtil.i(TAG, "头像小图的url是：" + s);
                }
                @Override
                public void onProgress(String s, int i, int i1) {
                }
                @Override
                public void onFailure(String s, OSSException e) {
                }
            });
        } catch (FileNotFoundException e) {

        }
    }

    private static String Headimgurl = "";
    private static void updataBigImage(File photo, final Context context,String imageName){
//        String Headimgurl = "";
        final CDNHelper getDemo = new CDNHelper(context);
        try {

            Log.i(TAG, "图片地址：" + photo.getPath());
            getDemo.uploadFile(PhotoUtils.scal(photo.getPath(),PhotoUtils.SCAL_IMAGE_80).getPath(), imageName, new SaveCallback() {
                @Override
                public void onSuccess(String s) {
                    Log.i(TAG, "上传头像成功.名字是：" + s);
                    Headimgurl = getDemo.getResourseURL();
                    Log.i(TAG, "上传头像成功.URL是：" + Headimgurl);
                    UserinfoData.saveAvatarUrl(context,Headimgurl);
                }
                @Override
                public void onProgress(String s, int i, int i1) {
                }
                @Override
                public void onFailure(String s, OSSException e) {
                    LogUtil.i(TAG, "上传头像Failure");
                }
            });
        } catch (FileNotFoundException e) {

        }
    }

    /***
     * 上传图片的命名规则
     *
     * @param context
     * @param isAvatar true 为头像上传 false 为其他类型图片上传
     * @return
     */
    protected String getImgName(Context context, boolean isAvatar) {
        StringBuilder builder = new StringBuilder();
        builder.append(UserinfoData.getInfoID(context));//infoid
        if (isAvatar) {//模块名称 头像是HeadImg 其余都是ForumImg
            builder.append("HeadImg");
        } else {
            builder.append("ForumImg");
        }
        builder.append(System.currentTimeMillis());//当前时间戳 微秒
        String random = getRandom();
        builder.append(random);//随机码（0~999）
        builder.append(index++);
        builder.append("2");//标记码  android 为2 ios为1
        if (isAvatar) {
            return builder.toString() + ".png";
        } else {
            return builder.toString() + ".png";
        }
    }

    private int index = 0;


    /***
     * 生成一个1到10^6的随机数
     *
     * @return
     */
    protected String getRandom() {
        Random random = new Random();
        int i = random.nextInt(1000000);
        if (i / 100 > 0) {
            return i + "";
        } else if (i / 10 > 0) {
            return "0" + i;
        } else {
            return "00" + i;
        }
    }

    /**
     *
     * 小图的url
     */
    private static final String THUMB = "thumb";
    protected String insertThumb(String imageName) {
        StringBuilder sb = new StringBuilder(imageName);
        int index = sb.indexOf(".");
        return sb.insert(index, THUMB).toString();
    }

}
