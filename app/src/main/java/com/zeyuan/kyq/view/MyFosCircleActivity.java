package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.FindCircleDetailAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.CityCancerForumBean;
import com.zeyuan.kyq.bean.FollowBean;
import com.zeyuan.kyq.bean.MyCircleBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我关注的圈子
 *
 */
public class MyFosCircleActivity extends BaseActivity implements FindCircleDetailAdapter.FollowCircleListener, ViewDataListener,
        AdapterView.OnItemClickListener ,HttpResponseInterface{
    private static final String TAG = "MyFosCircleActivity";
    private ListView mListView;
    private FindCircleDetailAdapter adapter;
    private List data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fos_circle);
        try {
            initOtherTitle(getString(R.string.foucs_circle));
            initData();
            initView();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "MyFosCircleActivity");
        }
    }

    /***
     * 初始化视图控件
     *
     */
    private void initView() {
        try {
            ImageView iv = (ImageView)findViewById(R.id.iv_other_title_share);
            iv.setImageResource(R.mipmap.more_circles);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MyFosCircleActivity.this, MoreCircleNewActivity.class));
                }
            });
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "MyFosCircleActivity");
        }
    }

    private void initListView(){
        mListView = (ListView) findViewById(R.id.listview);
        adapter = new FindCircleDetailAdapter(this, data);
        adapter.setListener(this);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }

    /***
     * 初始化数据
     *
     */
    private void initData() {
        try {
            data = new ArrayList();
            initList();
            if(data.size()==0){
                Factory.post(this,Const.EGetMycircle);
            }else {
                initListView();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "MyFosCircleActivity");
        }
    }

    /***
     * 设置圈子列表
     *
     */
    private void initList() {
        try {
            List<String> focusCircles = UserinfoData.getFocusCircle(this);
            if (focusCircles == null) {
                return;
            }
            CityCancerForumBean.NumEntity num = null;
            LogUtil.i(TAG, focusCircles.toString());
            for (String item : focusCircles) {
                num = new CityCancerForumBean.NumEntity();
                num.setCircleID(item);
                num.setIsFollow(true);
                data.add(num);
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "MyFosCircleActivity");
        }
    }

    private String circleID;
    private String followOrcancel;////1关注 2.取消

    @Override
    public void followCircle(BaseAdapter adapter, boolean isFollow, int position) {
        try {
            circleID = (String) adapter.getItem(position);
            Log.i(TAG,"CircleID:"+circleID);
            if (isFollow) {
                followOrcancel = "1";
            } else {
                followOrcancel = "2";
            }
            /**发送请求，提交圈子关注状态*/
            folowCircle();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "MyFosCircleActivity");
        }
    }

    /***
     * 发送请求，提交圈子关注状态
     *
     */
    private void folowCircle() {
        try {

            Factory.post(this, Const.EFollowCircle);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "MyFosCircleActivity");
        }
    }

    @Override
    public Map getParamInfo(int tag) {

        Map<String, String> map = new HashMap<>();

        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if(flag == Const.EFollowCircle){
            String type;
            if (Integer.valueOf(circleID) < 10000) {
                type = "1";
            } else {
                type = "2";
            }
            args = new String[]{
                    Contants.InfoID,Const.InfoID,
                    Contants.CircleID, circleID,
                    Contants.followOrcancel, followOrcancel,
                    "type", type
            };
        }else if(flag == Const.EGetMycircle){
            String temp = "";
            String StepID = UserinfoData.getStepID(this);
            String CancerID = UserinfoData.getCancerID(this);
            String CureConfID = MapDataUtils.getAllCureconfID(StepID);
            String ProvinceID;
            String CityID = UserinfoData.getCityID(this);
            temp = Contants.InfoID +",,,"+ UserinfoData.getInfoID(this);
            if(Const.NO_STEP.equals(UserinfoData.getIsHaveStep(this))){
                temp += ",,,"+Const.ISHAVESTEP+",,,"+"0";
            }else {
                if (!TextUtils.isEmpty(StepID)) {
                    temp += ",,,"+Contants.StepID+",,,"+StepID;
                }
                if (!TextUtils.isEmpty(CureConfID)) {
                    temp += ",,,"+Contants.CureConfID+",,,"+CureConfID;
                }else{
                    temp += ",,,"+Contants.CureConfID+",,,"+"0";
                }
                temp += ",,,"+Const.ISHAVESTEP+",,,"+"1";
            }

            if (!TextUtils.isEmpty(CancerID)) {
                temp += ",,,"+Contants.CancerID+",,,"+CancerID;
            }
            if("0".equals(CityID)) {
                ProvinceID = "0";
                temp += ",,,"+Contants.CityID+",,,"+CityID;
                temp += ",,,"+Contants.ProvinceID+",,,"+ProvinceID;
            }else {
                ProvinceID = CityID.substring(0,2)+"0000";
                temp += ",,,"+Contants.CityID+",,,"+CityID;
                temp += ",,,"+Contants.ProvinceID+",,,"+ProvinceID;
            }
            UserinfoData.saveProvinceID(this,ProvinceID);


            temp += ",,,page,,,"+0;
            temp += ",,,pagesize,,,"+5;
            args = new String[]{};
            args = temp.split(",,,");
        }
        return HttpSecretUtils.getParamString(args);
    }

    private List<String> lists;
    @Override
    public void toActivity(Object t, int tag) {
        try {
            if (tag == Const.EFollowCircle) {
                FollowBean bean = (FollowBean) t;
                if (Contants.OK_DATA.equals(bean.getIResult())) {
                    if ("1".equals(followOrcancel)) {
                        UserinfoData.addFocusCircle(this, circleID);
                        Toast.makeText(this, "关注成功", Toast.LENGTH_SHORT).show();
                    } else {
                        UserinfoData.removeFocusCircle(this, circleID);
                        Toast.makeText(this, "取消关注成功", Toast.LENGTH_SHORT).show();
                    }
                }
            }else if(tag == Const.EGetMycircle){
                MyCircleBean bean = (MyCircleBean)t;
                if(Const.RESULT.equals(bean.getIResult())){
                    lists = bean.getFollowCircleNum();
                    if (lists != null && lists.size() >0) {
                        UserinfoData.saveFocusCirlce(this, lists);
                        initList();
                    }
                    initListView();
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "MyFosCircleActivity");
        }
    }

    @Override
    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {

    }

    @Override
    public void showError(int tag) {

    }

    /***
     * 监听ListView点击事件所回调的方法
     *
     * @param parent
     * @param view
     * @param position 所点击item的下标
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            String circleId = (String) mListView.getAdapter().getItem(position);
            toCircle(circleId);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "MyFosCircleActivity");
        }
    }

    /***
     * 跳转到所点击圈子
     *
     * @param circleID 圈子ID
     */
    private void toCircle(String circleID) {
        try {
            Intent intent = new Intent(this, NewCircleActivity.class);
            intent.putExtra(Contants.CircleID, circleID);
            startActivity(intent);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "MyFosCircleActivity");
        }
    }
}
