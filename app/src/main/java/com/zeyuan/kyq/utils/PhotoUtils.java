package com.zeyuan.kyq.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Administrator on 2016/3/9.
 * 图片压缩帮助类
 *
 * @user wwei
 */
public class PhotoUtils {

    /***
     * SCAL_IMAGE_100 压缩至100kb以内
     * SCAL_IMAGE_80 压缩至80kb以内
     * SCAL_IMAGE_30 压缩至30kb以内
     *
     */
    public static final int SCAL_IMAGE_100 = 200;
    public static final int SCAL_IMAGE_80 = 200;
    public static final int SCAL_IMAGE_30 = 30;
    private static String TAG = "PhotoUtils";

    public static File scal(String path,int type){
        File outputFile = new File(path);
        LogCustom.i(TAG, "文件路径：" + path);
        long fileSize = outputFile.length();
        LogCustom.i(TAG,"文件大小："+fileSize/1024);
        final long fileMaxSize = type * 1024;
        if (fileSize >= fileMaxSize) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(path, options);
            int height = options.outHeight;
            int width = options.outWidth;
            double scale = Math.sqrt((float) fileSize / fileMaxSize);
            options.outHeight = (int) (height / scale);
            options.outWidth = (int) (width / scale);
            options.inSampleSize = (int) (scale + 0.5);
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            try {
                bitmap = reviewPicRotate(bitmap,path);
            }catch (Exception e){
                ExceptionUtils.ExceptionSend(e,"旋转出错");
            }
            outputFile = new File(PhotoUtils.createImageFile().getPath());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                ExceptionUtils.ExceptionSend(e,TAG + ".scal()");
            }
            Log.d("info", "ok"  + outputFile.length());
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }else{
                File tempFile = outputFile;
                outputFile = new File(PhotoUtils.createImageFile().getPath());
                PhotoUtils.copyFileUsingFileChannels(tempFile, outputFile);
            }
        }else {
            File tempFile = outputFile;
            outputFile = new File(PhotoUtils.createImageFile().getPath());
            PhotoUtils.copyFileUsingFileChannels(tempFile, outputFile);
        }
        return outputFile;
    }

    /** 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
//        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 获取图片文件的信息，是否旋转了90度，如果是则反转
     * @param bitmap 需要旋转的图片
     * @param path   图片的路径
     */
    public static Bitmap reviewPicRotate(Bitmap bitmap,String path){
        int degree = getPicRotate(path);
        if(degree!=0){
            LogCustom.i(TAG,"旋转角度："+degree);
            Matrix m = new Matrix();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            m.setRotate(degree); // 旋转angle度
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,m, true);// 从新生成图片
        }
        return bitmap;
    }

    /**
     * 读取图片文件旋转的角度
     * @param path 图片绝对路径
     * @return 图片旋转的角度
     */
    public static int getPicRotate(String path) {
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static File forSmall(String path,int type){
//        String path = fileUri.getPath();
        File outputFile = new File(path);
        LogCustom.i(TAG,"文件路径："+path);
        long fileSize = outputFile.length();
        LogCustom.i(TAG,"文件大小："+fileSize/1024);
        final long fileMaxSize = type * 1024;
        if (fileSize >= fileMaxSize) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(path, options);
            int height = options.outHeight;
            int width = options.outWidth;

            double scale = Math.sqrt((float) fileSize / fileMaxSize);
//            options.outHeight = (int) (height / scale);
            options.outHeight = 100;
            options.outWidth = 100;
//            options.outWidth = (int) (width / scale);
            options.inSampleSize = (int) (scale + 0.5);
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            outputFile = new File(PhotoUtils.createImageFile().getPath());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                ExceptionUtils.ExceptionSend(e,TAG + ".scal()");
            }
            LogCustom.d(TAG, "ok"  + outputFile.length());
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }else{
                File tempFile = outputFile;
                outputFile = new File(PhotoUtils.createImageFile().getPath());
                PhotoUtils.copyFileUsingFileChannels(tempFile, outputFile);
            }
        }else {
            File tempFile = outputFile;
            outputFile = new File(PhotoUtils.createImageFile().getPath());
            PhotoUtils.copyFileUsingFileChannels(tempFile, outputFile);
        }
        return outputFile;
    }

    /***
     * 设置图片的存放目录
     *
     * @return
     */
    public static Uri createImageFile(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = Environment.getExternalStorageDirectory();
        File storageDir = new File(Contants.KAQ_DISK_IMG_TEMP);
        if(!storageDir.exists()){
            storageDir.mkdirs();
        }
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            ExceptionUtils.ExceptionSend(e, TAG + ".createImageFile()");
        }

        // Save a file: path for use with ACTION_VIEW intents
        return Uri.fromFile(image);
    }
    public static void copyFileUsingFileChannels(File source, File dest){
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            try {
                inputChannel = new FileInputStream(source).getChannel();
                outputChannel = new FileOutputStream(dest).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                ExceptionUtils.ExceptionSend(e, TAG + ".copyFileUsingFileChannels()");
            }
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                ExceptionUtils.ExceptionSend(e, TAG + ".copyFileUsingFileChannels()");
            }
        }
    }

    /***
     * 图片压缩方法封装
     *
     * @param bs  1为大图片，0为小图片
     * @param index 上传图片在List中的下标
     * @param ishead 是否为头像
     * @param path 图片的位置
     * @return
     */
    public static String getPhotoPath(Context context,int bs,int index,boolean ishead,String path){
        Log.i(TAG,index +":"+path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        /**开启图片外框模式*/
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path,options);
        options.inJustDecodeBounds = false;
        final int ow = options.outWidth;
        final int oh = options.outHeight;
        int w = 0;int h = 0;int be = 1;
        /**如果bs=0,则小图片w = 100,h = 100*/
        if(bs == 0){//小图像按小比例缩放
            w = 100; h = 100;
            if(ow<=oh){
                be = ow/w;
            }else{
                be = oh/h;
            }
        }else{//大图像按大比例缩放
            w = 480; h = 800;
            if(ow>=oh){
                be = ow/w;
            }else{
                be = oh/h;
            }
        }
        if(be<=0) be =1;
        Log.i(TAG, "图片缩放比例：" + be);

        options.inSampleSize = be;

        /**生成bitmap图片*/
        bitmap = BitmapFactory.decodeFile(path,options);

        File tempFile;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int compress = 0;
        if(bs == 0){
            compress = 30;
        }else{
            compress = 60;
        }
        int ops = 100;
        while ( baos.toByteArray().length / 1024 > compress) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, ops, baos);//这里压缩options%，把压缩后的数据存放到baos中
            //每次都减少10
            ops -= 10;
            if(ops < 60) break;//防止出现死循环
            Log.i(TAG,"文件ops："+ops);
            Log.i(TAG,"文件长度："+baos.toByteArray().length);
        }
        FileOutputStream fos = null;
        String fpath = "";
        if(bs == 0 ){
            fpath = Contants.KAQ_DISK_IMG_SMALL;
        }else {
            fpath = Contants.KAQ_DISK_IMG_BIG;
        }
        tempFile = new File(fpath,getImgName(context,ishead,index));
        if (!tempFile.exists()) {
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LogUtil.i(TAG, "file path : " + tempFile.getPath());
        try {
            fos = new FileOutputStream(tempFile);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return tempFile.getPath();
    }



    /***
     *  图片按比例大小压缩方法（根据Bitmap图片压缩）
     *
     * @param image
     * @return
     */
    private Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return null;//压缩好比例大小后再进行质量压缩
    }

    public static String getPhoto(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空
        Log.i("photo","bitmap="+bitmap);
        newOpts.inJustDecodeBounds = false;
        newOpts.inSampleSize = 1;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compass(bitmap);
    }

    public static String compass(Bitmap image) {

        ByteArrayOutputStream baoss = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baoss);
        /*if( baoss.toByteArray().length / 1024>100) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baoss.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baoss);//这里压缩50%，把压缩后的数据存放到baos中
        }*/
        ByteArrayInputStream isBm = new ByteArrayInputStream(baoss.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 100f;//这里设置高度为800f
        float ww = 100f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }else if(w>ww||h>hh){
            be = ((int) (newOpts.outWidth / ww))>((int) (newOpts.outHeight / hh))?((int) (newOpts.outWidth / ww)):((int) (newOpts.outHeight / hh));
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baoss.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        File tempFile;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中

//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        FileOutputStream fos = null;
        tempFile = new File(Environment.getExternalStorageDirectory(),
                "11.jpg");
        if (!tempFile.exists()) {
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LogUtil.i("photo", "file path : " + tempFile.getPath());
        try {
            fos = new FileOutputStream(tempFile);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tempFile.getPath();
    }







    /***
     * 按质量压缩
     *
     * @param image 该图片信息只是图片的大小和边框高度
     * @return
     */
    public static String compressImage(Bitmap image) {
        File tempFile;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        FileOutputStream fos = null;
        tempFile = new File(Environment.getExternalStorageDirectory(),
                "11.jpg");
        if (!tempFile.exists()) {
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LogUtil.i("photo", "file path : " + tempFile.getPath());
        try {
            fos = new FileOutputStream(tempFile);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tempFile.getPath();
    }

    /***
     * 按质量压缩
     *
     * @param image 该图片信息只是图片的大小和边框高度
     * @return
     */
    public static Bitmap compressMinImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /***
     * 将图片流生成本地图片
     * @param json 图片流
     * @param target 文件地址
     * @throws IOException
     */
    public static void copyImage(byte[] json,File target) throws IOException{
        if(!target.getParentFile().exists()){
            target.getParentFile().mkdirs();
        }
        if(!target.exists()){
            ByteArrayInputStream bis = new ByteArrayInputStream(json);
            BufferedInputStream is = new BufferedInputStream(bis);

            FileOutputStream fos = new FileOutputStream(target);
            BufferedOutputStream os = new BufferedOutputStream(fos);

            byte[] buff = new byte[2*1024];
            int i;
            while((i=is.read(buff))!=-1){
                os.write(buff, 0, i);
            }
            os.close();
            fos.close();
            is.close();
            bis.close();
        }
    }

    /**
     * 读取InputStream数据，转为byte[]数据类型
     * @param is
     *            InputStream数据
     * @return 返回byte[]数据
     */
    public static byte[] readInputStream(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = -1;
        try {
            while ((length = is.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = baos.toByteArray();
        try {
            is.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


    /**
     * 根据图片网络地址获取图片的byte[]类型数据
     *
     * @param urlPath
     *            图片网络地址
     * @return 图片数据
     */
    public static byte[] getImageFromURL(String urlPath) {
        byte[] data = null;
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            // conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(6000);
            is = conn.getInputStream();
            if (conn.getResponseCode() == 200) {
                data = readInputStream(is);
            } else{
                data=null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(is != null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                conn.disconnect();
            }catch (Exception e){
                ExceptionUtils.ExceptionSend(e,"PhotoUtils,conn");
            }

        }
        return data;
    }

    /**
     * 生成100 * 100像素的缩率图
     *
     * @param fileName
     * @param width
     * @param heigth
     * @return
     */
    public static File thumbnailBitmap(String fileName, int width, int heigth) {
        Bitmap bitmap = BitmapFactory.decodeFile(fileName);//可能内存溢出
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, heigth);
        return compressBitmap(bitmap);
    }

    /**
     * 小图的url
     */
    private static final String THUMB = "thumb";
    public static String insertThumb(String imageName) {
        StringBuilder sb = new StringBuilder(imageName);
        int index = sb.indexOf(".");
        return sb.insert(index, THUMB).toString();
    }

    /***
     * 图像压缩
     *
     * @param bitmap
     * @return file文件
     */
    public static File compressBitmap(Bitmap bitmap) {
        File tempFile;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int options = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        FileOutputStream fos = null;
        tempFile = new File(Contants.KAQ_DISK_IMG_TEMP,
                getPhotoFileName());
        try {
            fos = new FileOutputStream(tempFile);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tempFile;
    }

    /***
     * 获取文件名
     *
     * @return
     */
    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }


    /***
     * 上传图片的命名规则
     *
     * @param context
     * @param isAvatar true 为头像上传 false 为其他类型图片上传
     * @return
     */
    public  static String getImgName(Context context, boolean isAvatar,int index) {
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
        builder.append(index);
        builder.append("2");//标记码  android 为2 ios为1
        if (isAvatar) {
            return builder.toString() + ".png";
        } else {
            return builder.toString() + ".png";
        }
    }

    /***
     * 生成一个1到10^6的随机数
     *
     * @return
     */
    public static String getRandom() {
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

    public static Bitmap fastblur(Context context, Bitmap sentBitmap, int radius) {

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int temp = 256 * divsum;
        int dv[] = new int[temp];
        for (i = 0; i < temp; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }



}
