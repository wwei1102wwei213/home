package com.zeyuan.kyq.biz;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.ta.common.AsyncTask;
import com.zeyuan.kyq.utils.CDNHelper;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.PhotoUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;


/***
 *
 *
 */
public class TestImgBiz extends AsyncTask<Void,Void,Void> {

    private static final String TAG = "ZYS";

    private Context context;
    private UpdataPhotoCallBack callback;
    private boolean isMore;
    private String url;
    private List<String> urls;
    private static final int UPDATA_PHOTO_FINISH = 1;
    private static final int UPDATA_PHOTO_FAIL = 2;
    private static final int SMALL_PIC_SUCCSED =3;
    private static final int SMALL_PIC_FAIL = 4;
    private static final int BIG_PIC_SUCCSED = 5;
    private static final int BIG_PIC_FAIL = 6;
    private static final int TRY_TIME_MAX = 3;
    private boolean small_pic_flag = false;
    private boolean big_pic_flag = false;
    private boolean SUCCSED = false;
    private boolean FIAL = false;
    private int times = 0;
    private MyHandler mHandler;
    private String Headimgurl;
    private Bitmap btp;

    public TestImgBiz(Context context,boolean isMore,String url,List<String> urls,Bitmap btp){
        this.context = context;
        this.isMore = isMore;
        this.url = url;
        this.urls = urls;
        this.mHandler = new MyHandler(this);
        this.callback = (UpdataPhotoCallBack)context;
        this.btp = btp;
    }

    private File tempFile;
    @Override
    protected Void doInBackground(Void... voids){
        LogCustom.i(TAG, "工作线程id:" + Thread.currentThread().getId());

        final CDNHelper getDemo = new CDNHelper(context);
        final CDNHelper littleDemo = new CDNHelper(context);

        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/kaq_2.1_19.apk");
        if (file.exists()){
            Log.i("ZYS", "文件存在");


            try {
                littleDemo.uploadFile(file.getPath(),"kaq_2.1_19.apk", new SaveCallback() {
                    @Override
                    public void onSuccess(String s) {
                        LogCustom.i(TAG, "上传成功.URL是：" + littleDemo.getResourseURL());

                    }

                    @Override
                    public void onProgress(String s, int i, int i1) {
                        LogCustom.i(TAG, "上传进度："+ i + "上传进度1:"+i1);
                    }

                    @Override
                    public void onFailure(String s, OSSException e) {
                        LogCustom.i(TAG, "上传Failure");

                    }
                });
            }catch (Exception e){
                LogCustom.i(TAG, "上传Error");
            }
        }else {
            Log.i("ZYS", "文件不存在");
        }





        /*if(!isMore){
            String path = Environment.getExternalStorageDirectory().getPath() + "/kaq/photo/" + getPhotoFileName();
            tempFile = new File(path);
            try {
                if(!tempFile.getParentFile().exists()){
                    tempFile.getParentFile().mkdirs();
                }
                if (tempFile.exists()){
                    tempFile.delete();
                }
            }catch (Exception e){

            }

            try {
//                Bitmap map = null;

                byte[] json = Bitmap2Bytes(btp);
//                        PhotoUtils.getImageFromURL(url);
                if(json!=null){
                    PhotoUtils.copyImage(json, tempFile);
                    updatePhoto(tempFile,0);
                }else {
                    mHandler.sendEmptyMessage(UPDATA_PHOTO_FAIL);
                }
            } catch (Exception e) {
                mHandler.sendEmptyMessage(UPDATA_PHOTO_FAIL);
            }

        }*/
        return null;
    }

    @Override
    protected void onPostExecute(Void o) {

    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private void finish(){
        callback.updataFinish(Headimgurl);
    }

    private void fial(){
        callback.updataFial();
    }

    private void toNext(){
        if(!isMore){
            if(!TextUtils.isEmpty(Headimgurl)){
                isMain();
                mHandler.sendEmptyMessage(UPDATA_PHOTO_FINISH);
            }else {
                mHandler.sendEmptyMessage(UPDATA_PHOTO_FAIL);
            }
        }
    }

    private void updatePhoto(File photo,int pos){
        LogCustom.i(TAG, "上传头像操作");
        final CDNHelper getDemo = new CDNHelper(context);
        final CDNHelper littleDemo = new CDNHelper(context);
        try {
            String imageName =
                    getImgName(context, true);
            LogCustom.i(TAG, "图片地址：" + photo.getPath());
            if(pos!=2){
                littleDemo.uploadFile(PhotoUtils.scal(photo.getPath(), PhotoUtils.SCAL_IMAGE_30).getPath(),
                        insertThumb(imageName), new SaveCallback() {
                    @Override
                    public void onSuccess(String s) {
                        LogCustom.i(TAG, "上传小图成功.URL是：" + littleDemo.getResourseURL());
                        toWait(SMALL_PIC_SUCCSED);
                    }

                    @Override
                    public void onProgress(String s, int i, int i1) {
                    }

                    @Override
                    public void onFailure(String s, OSSException e) {
                        LogCustom.i(TAG, "上传小头像Failure");
                        toTryAgain(SMALL_PIC_FAIL);
                    }
                });
            }

            if(pos!=1){
                getDemo.uploadFile(PhotoUtils.scal(photo.getPath(),PhotoUtils.SCAL_IMAGE_80).getPath(),
                        imageName, new SaveCallback() {
                    @Override
                    public void onSuccess(String s) {
                        Headimgurl = getDemo.getResourseURL();
                        LogCustom.i(TAG, "上传大图成功.URL是：" + getDemo.getResourseURL());
                        UserinfoData.saveAvatarUrl(context, Headimgurl);
                        toWait(BIG_PIC_SUCCSED);
                    }
                    @Override
                    public void onProgress(String s, int i, int i1) {
                    }
                    @Override
                    public void onFailure(String s, OSSException e) {
                        LogCustom.i(TAG, "上传大头像Failure");
                        toTryAgain(BIG_PIC_FAIL);
                    }
                });
            }

        } catch (FileNotFoundException e) {
            mHandler.sendEmptyMessage(UPDATA_PHOTO_FAIL);
        }
    }

    //等待进入下一步
    private void toWait(int flag){
        if(flag == BIG_PIC_SUCCSED){
            big_pic_flag = true;
            if(small_pic_flag){
                toNext();
            }
        }else if (flag == SMALL_PIC_SUCCSED){
            small_pic_flag = true;
            if(big_pic_flag){
                toNext();
            }
        }
    }

    //上传失败重试
    private void toTryAgain(int flag){
        if(flag == BIG_PIC_FAIL){
            Log.i(TAG, "SMALL_PIC_FAIL");
            times++;
            if(times>TRY_TIME_MAX){
                big_pic_flag = true;
                if(small_pic_flag){
                    toNext();
                }
            }else {
                updatePhoto(tempFile,1);
            }
        }else if(flag == SMALL_PIC_FAIL){
            Log.i(TAG, "SMALL_PIC_FAIL");
            times++;
            if(times>TRY_TIME_MAX){
                small_pic_flag = true;
                if(big_pic_flag){
                    toNext();
                }
            }else {
                updatePhoto(tempFile,1);
            }
        }
    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".png";

    }

    private int index = 0;

    /**
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

    /**
     * 小图的url
     */
    private static final String THUMB = "thumb";
    protected String insertThumb(String imageName) {
        StringBuilder sb = new StringBuilder(imageName);
        int index = sb.indexOf(".");
        return sb.insert(index, THUMB).toString();
    }

    /**
     * 生成一个1到10^6的随机数
     *
     * @return
     */
    protected String getRandom() {
        Random random = new Random();
        int i = random.nextInt(1000000);
//        int i = 1 + (int) (Math.random() * 1000000);
        if (i / 100 > 0) {
            return i + "";
        } else if (i / 10 > 0) {
            return "0" + i;
        } else {
            return "00" + i;
        }
    }

    private boolean isMain(){
        if(Thread.currentThread().getName().equals("main")){
            Log.i(TAG,"当前线程为主线程");
            return true;
        }
        Log.i(TAG,"当前线程为工作线程");
        return false;
    }

    /**
     *
     * Handler静态内部类
     *
     */
    private static class MyHandler extends Handler {
        private final WeakReference<TestImgBiz> mBiz;
        public MyHandler(TestImgBiz biz){
            mBiz = new WeakReference<>(biz);
        }
        @Override
        public void handleMessage(Message msg) {
            TestImgBiz biz = mBiz.get();
            if(msg.what == UPDATA_PHOTO_FINISH){
                LogCustom.i(TAG, "UPDATE_PHOTO_FINISH");
                biz.finish();
            }else if(msg.what == UPDATA_PHOTO_FAIL){
                LogCustom.i(TAG, "UPDATE_PHOTO_FAIL");
                biz.fial();
            }
        }
    }

    public interface UpdataPhotoCallBack{

        void updataFinish(Object o);

        void updataFial();

    }
}
