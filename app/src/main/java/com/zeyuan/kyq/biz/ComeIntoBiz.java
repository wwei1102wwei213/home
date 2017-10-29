package com.zeyuan.kyq.biz;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.zeyuan.kyq.Entity.DigitConfEntity;
import com.zeyuan.kyq.Entity.StepConfEntity;
import com.zeyuan.kyq.Entity.SyncConfEntity;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.db.DBHelper;
import com.zeyuan.kyq.service.ZYKaqService;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.Secret.DataSecretUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.view.SplashActivity;

import java.util.List;

/**
 * Created by Administrator on 2016/7/12.
 *
 *
 *
 * @author wwei
 */
public class ComeIntoBiz extends AsyncTask<Void,Void,Boolean> {

    private Context context;
    private boolean hasConf = false;
    private boolean hasDigit = false;
    private boolean next = false;

    public ComeIntoBiz(Context context){
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        initBiz();

        return next;
    }

    @Override
    protected void onPostExecute(Boolean flag) {
        ((SplashActivity)context).toNextActivity(flag);
    }

    private void initBiz(){
        try {
            //设置密钥
            int[] key = KeyBiz.getKeyOnThread();
            if (key==null){
                key = KeyBiz.getKeyOnThread();
                if (key==null){
                    key = KeyBiz.getKeyOnThread();
                }
            }
            if (key!=null){
                DataSecretUtils.TEA.setKey(key);
                HttpSecretUtils.TEA.setKey(key);
                //初始化阶段数据
                initStep();
                //初始化配置数据
                initData();
                if(hasDigit&&hasConf){
                    next = true;
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, context, "onCreate()");
        }
    }

    /***
     * 初始化阶段数据
     *
     */
    private void initStep(){
        try {

            List<StepConfEntity> list = DBHelper.getInstance().queryStepConf(ZYApplication.application);
            if(list!=null&&list.size()!=0&&ZYApplication.versionNum.equals(list.get(0).getVersionNum())){
              //  SqlConfigBiz.setStepDataMemory(StepDataUtils.TEA.decryptByTea(list.get(0).getData()));
                SqlConfigBiz.setStepDataMemory(list.get(0).getData());
            }else{
                String req = KeyBiz.getConfDataOnThread(Const.N_DataConfStep);
              //  String deReq = HttpSecretUtils.TEA.decryptByTea(req);
                SqlConfigBiz.saveSteoConfUpdata(req, ZYApplication.versionNum);
            }
            Intent intent = new Intent(context,ZYKaqService.class);
            intent.setAction(Const.SERVICE_STEP_UPDATA_START);
            intent.setPackage(context.getPackageName());
            context.startService(intent);

        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, context, "initData,ComeIntoBiz");
        }
    }

    /***
     * 初始化数据
     *
     */
    private void initData() {
        try {

            List<SyncConfEntity> syncList =
                    DBHelper.getInstance().querySyncConf(ZYApplication.application);
            List<DigitConfEntity> digitList =
                    DBHelper.getInstance().queryDigitConf(ZYApplication.application);
            /*if(syncList==null||syncList.size()==0){
                LogCustom.i(Const.TAG.ZY_DATA, "初始化阶段：数据库中没有配置数据");
                SqlConfigBiz.getDataAgainOnThread(Const.N_DataSyncConf);
                hasConf = true;
            }else{
                SyncConfEntity syncConfEntity = syncList.get(0);
                LogCustom.i(Const.TAG.ZY_DATA, "SyncConf版本：" +syncConfEntity.getVersionNum()+ "");
                LogCustom.i(Const.TAG.ZY_DATA, "SyncConf时间(天数)：" +
                        DataUtils.getDayForLong(syncConfEntity.getMaxtimestamp()) + "");
                if(ZYApplication.versionNum.equals(syncConfEntity.getVersionNum())&&
                        DataUtils.getDayForLong(syncConfEntity.getMaxtimestamp())<1){
                    hasConf = true;
                }else {
                    SqlConfigBiz.getDataAgainOnThread(Const.N_DataSyncConf);
                    hasConf = true;
                }
            }*/
            SqlConfigBiz.getDataAgainOnThread(Const.N_DataSyncConf);
            hasConf = true;
            if(digitList==null||digitList.size()==0){
                LogCustom.i(Const.TAG.ZY_DATA, "初始化阶段：数据库中没有分期数据");
                SqlConfigBiz.getDataAgainOnThread(Const.N_DataDigitConf);
                hasDigit = true;
            }else{
                DigitConfEntity digitConfEntity = digitList.get(0);
                LogCustom.i(Const.TAG.ZY_DATA, "DigitConf版本：" +digitConfEntity.getVersionNum() + "");
                LogCustom.i(Const.TAG.ZY_DATA, "DigitConf时间(天数)" +
                        DataUtils.getDayForLong(digitConfEntity.getMaxtimestamp()) + "");
                if(ZYApplication.versionNum.equals(digitConfEntity.getVersionNum())&&
                        DataUtils.getDayForLong(digitConfEntity.getMaxtimestamp())<3){
                    hasDigit = true;
                }else {
                    SqlConfigBiz.getDataAgainOnThread(Const.N_DataDigitConf);
                    hasDigit = true;
                }
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, context, "initData,ComeIntoBiz");
        }
    }
}
