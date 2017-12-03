package com.zeyuan.kyq.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.RecordPhotoAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.CommentProjectItem;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.fragment.dialog.ZYDialog;
import com.zeyuan.kyq.utils.CDNHelper;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ConstUtils;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DecryptUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.PhotoUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CircleImageView;
import com.zeyuan.kyq.widget.CustomScrollView;
import com.zeyuan.kyq.widget.MyGridView;
import com.zeyuan.kyq.widget.MyLayout;
import com.zeyuan.kyq.widget.selector.CommentLevelSelector;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017-11-26.
 */

public class AddCommentAllActivity extends BaseActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener
        ,MyLayout.OnSoftKeyboardListener,View.OnClickListener,HttpResponseInterface, CommentLevelSelector.LevelSelectorListener {

    private static final int REQUEST_PICK = 0;
    private static final int REQUEST_SUMMARY = 31;
    //记录分类标识
    private int type;
    //已选择图片路径列表
    private List<String> selectedPicture;
    //已选择图片路径列表
    private List<String> selectedPictureSummary;
    //图片选择适配器
    private RecordPhotoAdapter adapter;
    //有返回的启动标识
    private boolean REQUEST_FLAG = false;
    //启动返回时是否有修改
    private boolean Request_Is_Changed = false;
    //选择的数据
//    private ArrayList<String> check = null;

    private CommentProjectItem itemData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment_all);
        initType();
        initView();
        initListener();
        initData();
    }

    //标题文本框
    private TextView tv_title;
    //设置记录类型
    private void initType(){
        type = getIntent().getIntExtra(Const.INTENT_ADD_FLAG_TYPE, 1);
        itemData = (CommentProjectItem)getIntent().getSerializableExtra("Comment_Item_Data");
        REQUEST_FLAG = getIntent().getBooleanExtra(Const.RECORD_REQUEST_FLAG, false);
        tv_title = (TextView)findViewById(R.id.tv_title);
        String titleName = "";
        if (type==1){
            titleName = "项目";
        } else if(type==2){
            titleName = "医生";
        } else {
            titleName = "医院";
        }
        tv_title.setText("评价"+titleName);
        tv_type_txt = (TextView) findViewById(R.id.tv_type_txt);
        tv_type_txt.setText("对"+titleName+"的描述");
        if (itemData!=null){
            if (type==1){
                findViewById(R.id.v_dot).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.tv_dot_name)).setText(TextUtils.isEmpty(itemData.getPname())?"":itemData.getPname());
                ((TextView) findViewById(R.id.tv_dot_type)).setText("");
                ((TextView) findViewById(R.id.tv_host_name)).setText(TextUtils.isEmpty(itemData.getJob())?"未录入医院":itemData.getJob());

                ((TextView) findViewById(R.id.tv_sub)).setText(TextUtils.isEmpty(itemData.getPsubject())?"":itemData.getPsubject());
                Glide.with(this).load(itemData.getPicUrl()).into((CircleImageView) findViewById(R.id.civ));

            } else {
                findViewById(R.id.v_other).setVisibility(View.VISIBLE);
                Glide.with(this).load(itemData.getPicUrl()).into((ImageView) findViewById(R.id.iv_other));
                ((TextView) findViewById(R.id.tv_name_other)).setText(TextUtils.isEmpty(itemData.getPname())?"":itemData.getPname());
                ((TextView) findViewById(R.id.tv_sub_other)).setText(TextUtils.isEmpty(itemData.getPsubject())?"":itemData.getPsubject());
            }
        }

    }

    //备注输入框
    private EditText et_remark;
    //保存按钮
    private TextView tv_save;
    //添加照片控件
    private MyGridView gv;
    //监听输入法控件
    private MyLayout mLayout;
    //医院区域框
    private TextView tv_type_txt;
    //滑动器
    private CustomScrollView sv;
    private CommentLevelSelector cls1,cls2,cls3,cls4;
    private TextView tv_cls_1,tv_cls_2,tv_cls_3,tv_cls_4;
    private void initView(){
        tv_save = (TextView)findViewById(R.id.tv_save);

        et_remark = (EditText)findViewById(R.id.et_remark);
        gv = (MyGridView)findViewById(R.id.gv);
        selectedPicture = new ArrayList<>();
        selectedPictureSummary = new ArrayList<>();
        adapter = new RecordPhotoAdapter(this,selectedPicture);
        gv.setAdapter(adapter);
        sv = (CustomScrollView)findViewById(R.id.sv);
        mLayout = (MyLayout)findViewById(R.id.my_layout);

        cls1 = (CommentLevelSelector) findViewById(R.id.csl1);
        cls2 = (CommentLevelSelector) findViewById(R.id.cls2);
        cls3 = (CommentLevelSelector) findViewById(R.id.cls3);
        cls4 = (CommentLevelSelector) findViewById(R.id.cls4);
        cls1.init(1,1,0,this);
        cls2.init(2,2,0,this);
        cls3.init(2,3,0,this);
        cls4.init(2,4,0,this);

        tv_cls_1 = (TextView) findViewById(R.id.tv_csl_1);
        tv_cls_2 = (TextView) findViewById(R.id.tv_cls_2);
        tv_cls_3 = (TextView) findViewById(R.id.tv_cls_3);
        tv_cls_4 = (TextView) findViewById(R.id.tv_cls_4);

    }

    String[] levels = {"不满意", "一般", "好", "很好", "非常好"};
    @Override
    public void onSelectorCallback(int type, int back, int level) {
        String temp = levels[level-1];
        switch (back){
            case 1:
                tv_cls_1.setText(temp);
                break;
            case 2:
                tv_cls_2.setText(temp);
                break;
            case 3:
                tv_cls_3.setText(temp);
                break;
            case 4:
                tv_cls_4.setText(temp);
                break;
        }
    }

    //1.项目2.医生3.医院
    //设置监听事件;
    private void initListener(){
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_save.setOnClickListener(this);
        mLayout.setOnSoftKeyboardListener(this);
        gv.setOnItemClickListener(this);
        gv.setOnItemLongClickListener(this);
    }

    private void initData(){}



    private int InfoID = Integer.valueOf(UserinfoData.getInfoID(this));

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_save:
                toSaveData();
                break;
        }
    }



    private void toSaveData(){
        toSave();
    }

    private ProgressDialog mProgressDialog;
    private void toSave(){

        /*&&(selectedPicture==null||selectedPicture.size()==0)*/
        if (cls1.getLevel()==0){
            showToast("请给出总体评价");
            return;
        }
        if (TextUtils.isEmpty(et_remark.getText().toString())){
            showToast("请输入描述");
            return;
        }
        toUpdateData();
    }

    private void toUpdateData(){
        tv_save.setClickable(false);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在保存");
        mProgressDialog.show();
        if (selectedPicture != null && selectedPicture.size() > 0) {//说明是有图片的
            uploadPhoto();
        } else {
            toPost();
        }
    }

    private void toPost(){
        Factory.postPhp(this, Const.PApi_addPcomment);
    }

    //图片url集合
    private List<String> urls;
    private void uploadPhoto() {
        try {
            urls = new ArrayList<>();
            final int index = selectedPicture.size();
            for (int i = 0; i < selectedPicture.size(); i++) {
                final CDNHelper get = new CDNHelper(this);
                try {
                    String imageName = getImgName(this, false);
                    final File big = PhotoUtils.scal(selectedPicture.get(i), PhotoUtils.SCAL_IMAGE_100);
                    get.uploadFile(big.getPath(),imageName, new SaveCallback() {
                        @Override
                        public void onProgress(String s, int i, int i1) {}
                        @Override
                        public void onFailure(String s, OSSException e) {}
                        @Override
                        public void onSuccess(String s) {
                            urls.add(get.getResourseURL());
                            try {
                                big.delete();
                            }catch (Exception e){
                                ExceptionUtils.ExceptionSend(e,"删除临时图片（大）失败");
                            }
                            if (urls.size() == index) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        toPost();
                                    }
                                });
                            }
                        }
                    });

                    final CDNHelper gets = new CDNHelper(this);
                    final File small = PhotoUtils.scal(selectedPicture.get(i), PhotoUtils.SCAL_IMAGE_30);
                    gets.uploadFile(small.getPath(),insertThumb(imageName), new SaveCallback() {
                        @Override
                        public void onSuccess(String s) {
                            try {
                                small.delete();
                            }catch (Exception e){
                                ExceptionUtils.ExceptionSend(e,"删除临时图片（小）失败");
                            }
                        }
                        @Override
                        public void onProgress(String s, int i, int i1) {}
                        @Override
                        public void onFailure(String s, OSSException e) {}
                    });

                } catch (Exception e) {
                    ExceptionUtils.ExceptionToUM(e, this, "ReleaseForumActivity");
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "ReleaseForumActivity");
        }
    }




/*参数：
{
    AT = 1;
CommentTxt = MzQxMjM0; // 评价内容 base64后urlEncode
CommnetNum = 5;   // 综合评价
DeviceID = 36522EC4E18F4D7EBF7B31B4AFD91354;
FWNum = 5; // 服务
InfoID = 280180;
    LT = 2;
    MD = "Simulator x64";
PicTxt = "";  // 图片描述，base64（不需要urlEncode）后用逗号隔开
PicUrl = "";
RelationID = 1; //项目、医生、医院的ID
TGNum = 4;  // 体感
TypeID = 1;  // 1.项目2.医生3.医院
Ver = "3.0";
YXNum = 5; // 影像
}
*/

    @Override
    public Map getParamInfo(int tag) {
        Map<String,String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        if (tag == Const.PApi_addPcomment){
            if (urls!=null&&urls.size()>0){
                map.put("PicUrl", ConstUtils.getParamsForPic(urls));
                if (selectedPictureSummary==null) selectedPictureSummary = new ArrayList<>();
                if (selectedPictureSummary.size()<selectedPicture.size()){
                    int temp = selectedPicture.size() - selectedPictureSummary.size();
                    for (int i=0;i<temp;i++){
                        selectedPictureSummary.add("");
                    }
                }
                map.put("PicTxt", ConstUtils.getParamsForPicSummary(selectedPictureSummary));
            }

            String Ptext = et_remark.getText().toString();
            if (!TextUtils.isEmpty(Ptext)){
                map.put("CommentTxt", DecryptUtils.encodeAndURL(Ptext));
            }
            map.put("RelationID",itemData.getId()+"");
            map.put("TypeID",type+"");
            map.put("CommnetNum",cls1.getLevel()+"");
            map.put("FWNum",cls2.getLevel()+"");
            map.put("TGNum",cls3.getLevel()+"");
            map.put("YXNum",cls4.getLevel()+"");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PApi_addPcomment ){
            PhpUserInfoBean bean = (PhpUserInfoBean)response;
            if (Const.RESULT.equals(bean.getiResult())){
//                showToast("新增成功");
                if (REQUEST_FLAG){
                    Request_Is_Changed = true;
                }
                exit = 1;
                startActivity(new Intent(this, AddCommentSuccessActivity.class).putExtra("add_success_type",1));
                finish();
            }else {
                showToast("评价失败");
            }
        }
    }

    @Override
    public void showLoading(int flag) {

    }

    @Override
    public void hideLoading(int flag) {
        if (flag == Const.PApi_addPcomment ){
            if (mProgressDialog!=null) mProgressDialog.dismiss();
            tv_save.setClickable(true);
        }
    }

    @Override
    public void showError(int flag) {
        if (flag == Const.PApi_addPcomment ){
            if (mProgressDialog!=null) mProgressDialog.dismiss();
            tv_save.setClickable(true);
        }
        showToast("网络请求失败");
    }

    private static final int STORAGE_AND_CAMERA_PERMISSIONS = 12;
    private int selectedIndex = 0;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId()==R.id.gv){
            try {
                if (position == selectedPicture.size()) {
                    int index = adapter.getCount();
                    if (index >= 10) {
                        showToast("最多上传9张图片");
                        return;
                    }
                    selectedIndex = index;
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                                    PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                , STORAGE_AND_CAMERA_PERMISSIONS);
                    } else {
                        Intent intent = new Intent(this, SelectPictureActivity.class);
                        index = 10 - index;
                        intent.putExtra(SelectPictureActivity.INTENT_MAX_NUM, index);
                        startActivityForResult(
                                intent, REQUEST_PICK);
                    }
                } else {
                    /*startActivity(new Intent(this, ShowPhotoActivity.class).putExtra("list",
                            (Serializable) selectedPicture).putExtra("position", position));*/
                    if (selectedPictureSummary==null) selectedPictureSummary = new ArrayList<>();
                    if (selectedPictureSummary.size()<selectedPicture.size()){
                        int temp = selectedPicture.size() - selectedPictureSummary.size();
                        for (int i=0;i<temp;i++){
                            selectedPictureSummary.add("");
                        }
                    }
                    startActivityForResult(new Intent(this, AddPhotoSummaryActivity.class)
                            .putExtra("list",(Serializable) selectedPicture)
                            .putExtra("list_summary", (Serializable) selectedPictureSummary)
                            .putExtra("position", position),REQUEST_SUMMARY);
                }
            }catch (Exception e){
                ExceptionUtils.ExceptionToUM(e, this, "ReleaseForumActivity");
            }
        }
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        if (position == adapter.getCount() - 1) {
            return true;
        }
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectedPicture.remove(position);
                    adapter.updateDate(selectedPicture);
                }
            });
            builder.create().show();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "ReleaseForumActivity");
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == STORAGE_AND_CAMERA_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, SelectPictureActivity.class);
                selectedIndex = 10 - selectedIndex;
                intent.putExtra(SelectPictureActivity.INTENT_MAX_NUM, selectedIndex);
                startActivityForResult(intent, REQUEST_PICK);
            } else {
                Toast.makeText(this, "没有拍照或相册权限", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean hasSymptomRemark = false;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == RESULT_OK) {
                List list = (ArrayList<String>) data.getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);
                LogCustom.i("ZYS", "选择的图片uri是：" + list.toString());
                selectedPicture.addAll(list);
                adapter.updateDate(selectedPicture);
            } else if (requestCode == REQUEST_SUMMARY){
                selectedPictureSummary = (List<String>) data.getSerializableExtra("list_summary_back");
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "RecordActivity onActivityResult");
        }
    }

    private boolean flag = false;// 这个控制隐藏键盘的时候 回调只被调用一次

    @Override
    public void onShown() {
        flag = true;
    }

    @Override
    public void onHidden() {
        if (flag) {
            flag = false;
        }
    }

    private int exit = 0;

    @Override
    public void finish() {
        if (exit==0){
            if (isChanged()){
                ZYDialog.Builder builder = new ZYDialog.Builder(this);
                builder.setTitle("提示")
                        .setMessage("不保存就退出吗？")
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                toSaveData();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                exit = 2;
                                dialog.dismiss();
                                finish();
                            }
                        }).create().show();
            } else {
                super.finish();
            }
//            super.finish();
        }else if (exit == 1){
            if (REQUEST_FLAG){
                this.setResult(1, getIntent().putExtra("isChanged", Request_Is_Changed));
            }
            super.finish();
        }else {
            super.finish();
        }

    }

    //是否有更改
    private boolean isChanged(){
        if (!TextUtils.isEmpty(et_remark.getText().toString())
                ||(selectedPicture!=null&&selectedPicture.size()>0)){
            return true;
        }
        return false;
    }

}
