package com.zeyuan.kyq.utils;

import android.content.Context;
import android.os.Environment;

import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.OSSServiceProvider;
import com.alibaba.sdk.android.oss.model.AccessControlList;
import com.alibaba.sdk.android.oss.model.AuthenticationType;
import com.alibaba.sdk.android.oss.model.ClientConfiguration;
import com.alibaba.sdk.android.oss.model.TokenGenerator;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.alibaba.sdk.android.oss.storage.OSSData;
import com.alibaba.sdk.android.oss.storage.OSSFile;
import com.alibaba.sdk.android.oss.util.OSSLog;
import com.alibaba.sdk.android.oss.util.OSSToolKit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by zhouzhuo on 9/13/15.
 */
public final class OSSServiceManager {
    public static String accessKey;
    public static String screctKey;
    public static String bucketName;
    public static String srcFileDir;
    public static OSSService ossService;
    public static OSSServiceManager instance;
    private Context context;

    private OSSServiceManager(Context context) {
        this.context = context;
        main();
    }

    public static OSSServiceManager getInstance(Context context) {
        if (instance == null) {
            synchronized (OSSServiceManager.class) {
                if (instance == null) {
                    instance = new OSSServiceManager(context);
                }
            }
        }
        return instance;
    }

    public OSSBucket getOssBucket() {

        return ossService.getOssBucket(OSSServiceManager.bucketName);

    }

    public void main() {

        // 测试代码没有考虑AK/SK的安全性，保存在本地
        accessKey = "5OfoPMetXYRtEnm6";
        screctKey = "yHWjpCh05A19QW4f5ocsuCN85lkljN";
        bucketName = "zeyuan1";
        // 在本地文件系统建立两个随机文件，用于后续的上传
        try {
            initLocalFile();
        } catch (Exception ignore) {
        }
        // 初始化ossService
        initOssService();
//        runDemo(context);
    }

    private void initOssService() {
        // 开启Log
        OSSLog.enableLog();

        ossService = OSSServiceProvider.getService();

        ossService.setApplicationContext(context);
//        ossService.setGlobalDefaultHostId("oss-cn-qingdao.aliyuncs.com"); // 设置region host 即 endpoint
        ossService.setGlobalDefaultHostId("oss-cn-shenzhen.aliyuncs.com"); // 设置region host 即 endpoint

        ossService.setGlobalDefaultACL(AccessControlList.PRIVATE); // 默认为private
        ossService.setAuthenticationType(AuthenticationType.ORIGIN_AKSK); // 设置加签类型为原始AK/SK加签
        ossService.setGlobalDefaultTokenGenerator(new TokenGenerator() { // 设置全局默认加签器
            @Override
            public String generateToken(String httpMethod, String md5, String type, String date,
                                        String ossHeaders, String resource) {

                String content = httpMethod + "\n" + md5 + "\n" + type + "\n" + date + "\n" + ossHeaders
                        + resource;

                return OSSToolKit.generateToken(accessKey, screctKey, content);
            }
        });
        ossService.setCustomStandardTimeWithEpochSec(System.currentTimeMillis() / 1000);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectTimeout(30 * 1000); // 设置全局网络连接超时时间，默认30s
        conf.setSocketTimeout(30 * 1000); // 设置全局socket超时时间，默认30s
        conf.setMaxConcurrentTaskNum(5); // 替换设置最大连接数接口，设置全局最大并发任务数，默认为6
        conf.setIsSecurityTunnelRequired(false); // 是否使用https，默认为false
        ossService.setClientConfiguration(conf);
    }

    private void initLocalFile() throws IOException {
        srcFileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/oss_demo_dir/";
        File dir = new File(srcFileDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file1m = new File(srcFileDir + "file1m");
        File file10m = new File(srcFileDir + "file10m");
        writeRandomContentToFile(file1m, 1024 * 1024);
        writeRandomContentToFile(file10m, 1024 * 1024 * 10);
    }

    private void writeRandomContentToFile(File file, long size) throws IOException {
        FileOutputStream fo = new FileOutputStream(file);
        byte[] buffer = new byte[4096];
        new Random().nextBytes(buffer);

        for (int i = 0; i < size / 4096; i++) {
            fo.write(buffer);
        }
        fo.close();
    }

//    public void runDemo(final Context context) {
//
//        // new Thread(new Runnable() {
//        //     @Override
//        //     public void run() {
//        //         new ListObjectsInBucketDemo().show();
//        //     }
//        // }).start();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
////                new GetAndUploadDataDemo(context).show();;
//            }
//        }).start();


    // new Thread(new Runnable() {
    //     @Override
    //     public void run() {
    //         new GetAndUploadFileDemo().show();
    //     }
    // }).start();

    // new Thread(new Runnable() {
    //     @Override
    //     public void run() {
    //         new MultipartUploadDemo().show();
    //     }
    // }).start();
//    }

    public OSSData getOssData(OSSBucket bucket, String s) {
        return ossService.getOssData(bucket, s);
    }

    public OSSFile getOssFile(OSSBucket bucket, String s) {
        return ossService.getOssFile(bucket, s);
    }
}
