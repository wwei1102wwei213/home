package com.zeyuan.kyq.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/25.
 *
 * 数据交接页面
 *
 * @author wwei
 */
public class WorkActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = WorkActivity.class.getSimpleName();
    private Button bt1,bt2,bt3,bt4,bt5,bt6,bt7,bt8,bt9,bt10,bt11,bt12,bt13,bt14,bt15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        initOtherTitle("数据交接");
        initView();

    }

    private void initView(){

        bt1 = (Button)findViewById(R.id.bt1);
        bt2 = (Button)findViewById(R.id.bt2);
        bt3 = (Button)findViewById(R.id.bt3);
        bt4 = (Button)findViewById(R.id.bt4);
        bt5 = (Button)findViewById(R.id.bt5);
        bt6 = (Button)findViewById(R.id.bt6);
        bt7 = (Button)findViewById(R.id.bt7);
        bt8 = (Button)findViewById(R.id.bt8);
        bt9 = (Button)findViewById(R.id.bt9);
        bt10 = (Button)findViewById(R.id.bt10);
        bt11 = (Button)findViewById(R.id.bt11);

        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        bt4.setOnClickListener(this);
        bt5.setOnClickListener(this);
        bt6.setOnClickListener(this);
        bt7.setOnClickListener(this);
        bt8.setOnClickListener(this);
        bt9.setOnClickListener(this);
        bt10.setOnClickListener(this);
        bt11.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt1:
                showUserData();
                break;
            case R.id.bt2:
                showNameForID(2);
                break;
            case R.id.bt3:
                showNameForID(3);
                break;
            case R.id.bt4:
                showNameForID(4);
                break;
            case R.id.bt5:
                showNameForID(5);
                break;
            case R.id.bt6:
                showNameForID(6);
                break;
            case R.id.bt7:
                showNameForID(7);
                break;
            case R.id.bt8:
                showNameForID(8);
                break;
            case R.id.bt9:
                showNameForID(9);
                break;
            case R.id.bt10:
                showNameForID(10);
                break;
            case R.id.bt11:
                showNameForID(11);
                break;
        }
    }

    //用户信息数据
    private void showUserData(){
        //InfoID,用户唯一标识
        String InfoID = UserinfoData.getInfoID(this);
        //用户昵称
        String InfoName = UserinfoData.getInfoname(this);
        //是否拥有当前阶段标识,0为没有,1为有
        String IsHaveStep = UserinfoData.getIsHaveStep(this);
            //有当前阶段时
            //用户当前阶段ID
            String StepID = UserinfoData.getStepID(this);
            //用阶段ID获取阶段类型ID
            String CureConfID = MapDataUtils.getAllCureconfID(StepID);
        //用户癌种ID
        String CancerID = UserinfoData.getCancerID(this);
        //用户确诊时间
        String DiscoverTime = UserinfoData.getDiscoverTime(this);
        //用户头像
        String HeadImgUrl = UserinfoData.getAvatarUrl(this);
        //用户城市ID
        String CityID = UserinfoData.getCityID(this);
        //用户分期ID
        String PeriodID = UserinfoData.getPeriodID(this);

        LogCustom.i(TAG,"用户唯一标识:"+InfoID + "\n" +
                "用户昵称:"+InfoName + "\n" +
                "是否拥有当前阶段标识:"+IsHaveStep + "\n" +
                "用户当前阶段ID:"+StepID + "\n" +
                "阶段类型ID:"+CureConfID + "\n" +
                "用户癌种ID:"+CancerID + "\n" +
                "用户确诊时间:"+DiscoverTime + "\n" +
                "用户头像:"+HeadImgUrl + "\n" +
                "用户城市ID:"+CityID + "\n" +
                "用户分期ID:"+PeriodID );


        UiUtils.getCancerParentID(CancerID);//获取父类癌种ID
    }

    private void showNameForID(int flag){
        switch (flag){
            case 2://用阶段ID取阶段Name
                String StepID = UserinfoData.getStepID(this);
                String StepName = MapDataUtils.getAllStepName(StepID);
                LogCustom.i(TAG,"StepName:"+StepName);
                break;
            case 3://用阶段类型ID取阶段类型Name
                String StepID1 = UserinfoData.getStepID(this);
                String CureConfID = MapDataUtils.getAllCureconfID(StepID1);
                String CureConfName = MapDataUtils.getCureValues(CureConfID);
                LogCustom.i(TAG,"CureConfName:"+CureConfName);
                break;
            case 4://用癌种ID取癌种Name
                String CancerID = UserinfoData.getCancerID(this);
                String CancerName = MapDataUtils.getCancerValues(CancerID);
                LogCustom.i(TAG,"CancerName:"+CancerName);
                break;
            case 5://用圈子ID取圈子Name
                String CircleName = MapDataUtils.getCircleValues("7007");
                LogCustom.i(TAG,"CircleName:"+CircleName);
                break;
            case 6://用症状ID取症状Name,可以是单个id,也可以是多个id串,如："123,55,66"
                String PerformName = MapDataUtils.getPerfromValues("123");
                LogCustom.i(TAG,"CircleName:"+PerformName);
                break;
            case 7://用身体部位ID取身体部位Name,该取值未封装便捷方法
                //获取所有的身体部位数据
                Map<String,String> map = (LinkedHashMap<String,String>) Factory.getData(Const.N_DataBodyPosValues);
                String BodyPosName = map.get("41");
                LogCustom.i(TAG,"BodyPosName:"+BodyPosName);
                break;
            case 8://用基因ID取基因Name,可以是单个id,也可以是多个id串,如："123,55,66"
                String GeneName = MapDataUtils.getGeneForString("6,12,10");
                LogCustom.i(TAG,"GeneName:"+GeneName);
                break;
            case 9://用分期ID取分期Name
                String PeriodID = UserinfoData.getPeriodID(this);
                String PeriodName = MapDataUtils.getDigitValues(PeriodID);
                LogCustom.i(TAG,"PeriodName:"+PeriodName);
                break;
            case 10://用转移部位ID取转移部位Name,可以是单个id,也可以是多个id串,如："123,55,66"
                String TransferBodyName = MapDataUtils.getTranslateForString("9005,9002,9007");
                LogCustom.i(TAG,"TransferBodyName:"+TransferBodyName);
                break;
            case 11://用城市ID取城市Name
                String CityID = UserinfoData.getCityID(this);
                String CityName = MapDataUtils.getCityName(CityID);
                LogCustom.i(TAG,"CityName:"+CityName);
                break;
        }
    }

    private void showStepData(){
        Factory.getData(Const.N_DataConfStep);
    }

}
