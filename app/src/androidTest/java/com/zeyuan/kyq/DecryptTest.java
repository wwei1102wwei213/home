package com.zeyuan.kyq;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.DecryptUtils;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016/2/25.
 */
public class DecryptTest extends ApplicationTestCase<Application> {

    public DecryptTest() {
        super(Application.class);
    }



    public void testAESDecrypt() {
        //测试用例
        String content = "hello abcdefggsdfasdfasdf";
        String pStr = DecryptUtils.AES.encode(content);
    Log.i("Main", "加密前：" + content);
    Log.i("Main","加密后:" + pStr);
    //      pStr = "23A2C1C270A407BE7C2A5F9CAD84F1666E8FC791F160C561070B17A3BCC8B82D";
    String postStr = DecryptUtils.AES.decode(pStr);
    Log.i("Main", "解密后：" + postStr);
}

    public void testTeaDecrypt() {

        String info = "www.blogjava.net/orangehf";
        Log.i("Main", "原数据：" + info);
//		for (byte i : info.getBytes())
//			System.out.print(i + " ");

        byte[] encryptInfo = DecryptUtils.Tea.encryptByTea(info);
//		Log.i("Main",);
        try {
            Log.i("Main", "加密后的数据：" + new String(encryptInfo,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        for (byte i : encryptInfo)
//            Log.i("Main", (i + " "));
//		Log.i("Main",);

        String decryptInfo = DecryptUtils.Tea.decryptByTea(encryptInfo);
//        System.out.print("解密后的数据：");
        Log.i("Main", "解密后的数据："+decryptInfo);
    }


}
