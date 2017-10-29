package com.zeyuan.kyq.biz;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;

import com.zeyuan.kyq.Entity.UserInfoEntity;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.Secret.DefaultSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2016/8/9.
 *
 *
 * @author wwei
 */
public class WriteUserInfoBiz extends AsyncTask<Void,Void,Void>{

    private Context context;
    private String InfoID;
    private String extras;

    public WriteUserInfoBiz(Context context,String infoID,String extras){
        this.context = context;
        this.InfoID = infoID;
        this.extras = extras;
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            //构建实体类对象
            UserInfoEntity entity = new UserInfoEntity();
            entity.setMills(System.currentTimeMillis()/1000+"");
            entity.setInfoID(InfoID);
            entity.setInfoName(UserinfoData.getInfoname(context));
            entity.setDeviceID(ZYApplication.deviceId);
            entity.setVer(ZYApplication.versionNum);
            entity.setHeadImgUrl(UserinfoData.getAvatarUrl(context));
            entity.setUMengToken(getUMengToken());
            entity.setExtras(extras);
            entity.setPhoneType(ZYApplication.phoneInfo);
            String type = UserinfoData.getLoginType(context);
            if(!TextUtils.isEmpty(type)){
                entity.setLoginType(type);
                entity.setOpenID(UserinfoData.getOpenID(context));
                if("1".equals(type)){
                    entity.setUnionID(UserinfoData.getUnionID(context));
                }
            }
            try {
                //得到用户信息字符串
                String msg = entity.toString();
                msg = DefaultSecretUtils.TEA.encryptByTea(msg);
                File txt = new File(Environment.getExternalStorageDirectory(),"/kaq/info/"+InfoID+".txt");
                //父目录是否存在
                if(!txt.getParentFile().exists()){
                    txt.getParentFile().mkdirs();
                }
                //文件是否存在
                if(txt.exists()){
                    txt.delete();
                }
                txt.createNewFile();
                try {
                    byte[] bytes = msg.getBytes("UTF-8");
                    FileOutputStream fos=new FileOutputStream(txt);
                    fos.write(bytes, 0, bytes.length);
                    fos.close();
                    UserinfoData.saveUserInfoMsg(context, InfoID, System.currentTimeMillis() / 1000 + "");
                }catch (Exception e){
                    ExceptionUtils.ExceptionToUM(e,context,"写入用户配置信息到SD卡失败");
                }
            } catch (Exception e){

            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,context,"写入用户配置信息失败");
        }
        return null;
    }

    private String getUMengToken(){
        return "";
//        return PushAgent.getInstance(context).getRegistrationId();
    }
}
