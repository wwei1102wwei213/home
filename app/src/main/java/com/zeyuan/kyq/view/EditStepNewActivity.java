package com.zeyuan.kyq.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.zeyuan.kyq.Entity.EditStepBean;
import com.zeyuan.kyq.Entity.EditStepItemEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.EditStepNewAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.manager.FunctionGuideManager;
import com.zeyuan.kyq.fragment.dialog.ZYDialog;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新编辑阶段页面
 */
public class EditStepNewActivity extends BaseActivity implements HttpResponseInterface {

    private ListView lv;
    private EditStepNewAdapter adapter;
    private int SaveFlag = 0;
    private static final int SAVE_FINISH = 1;//退出时保存
    private static final int SAVE_ADD = 2;//新增时保存
    private boolean isChanged = false;
    private boolean Result_Flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent();
        setContentView(R.layout.activity_edit_step_new);
        initStatusBar();
        Result_Flag = getIntent().getBooleanExtra(Const.RECORD_REQUEST_FLAG, false);
        initView();
        initData();
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv = (ListView) findViewById(R.id.lv);
        List<EditStepItemEntity> data = new ArrayList<>();
        adapter = new EditStepNewAdapter(this, data);
        lv.setAdapter(adapter);
        findViewById(R.id.v_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogCustom.i("ZYS", "data:" + adapter.getDataList());
                if (adapter.isChanged()) {
                    SaveFlag = SAVE_FINISH;
                    Factory.postPhp(EditStepNewActivity.this, Const.PEditAppStepUser);
                } else {
                    showToast("没有任何改变");
                }
            }
        });
        findViewById(R.id.v_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAddStep();
            }
        });
    }

    private void initData() {
        Factory.postPhp(this, Const.PGetAppStepUser);
    }

    private void ToResultActivity() {
        Intent intent = new Intent(this, AddStepNewActivity.class);
        this.startActivityForResult(intent, 200);
    }

    private void toAddStep() {
        if (adapter.isChanged()) {
            ZYDialog.Builder builder = new ZYDialog.Builder(this);
            builder.setTitle("提示")
                    .setMessage("信息已经修改，是否保存？")
                    .setCancelAble(true)
                    .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SaveFlag = SAVE_ADD;
                            Factory.postPhp(EditStepNewActivity.this, Const.PEditAppStepUser);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("不保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ToResultActivity();
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ToResultActivity();
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == Const.PGetAppStepUser) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        } else if (tag == Const.PEditAppStepUser) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
            map.put("data", adapter.getDataList());
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PGetAppStepUser) {
            EditStepBean bean = (EditStepBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                List<EditStepItemEntity> data = bean.getData();
                if (data != null && data.size() > 0) {
                    adapter.update(data);
                    if (data.size() > 0)
                        FunctionGuideManager.getInstance().showSelectCurrentStepGuide(EditStepNewActivity.this);
                }
            } else {
                showToast("数据返回异常");
            }
        } else if (flag == Const.PEditAppStepUser) {
            PhpUserInfoBean bean = (PhpUserInfoBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                isChanged = true;
                //更新当前阶段数据
                if (!TextUtils.isEmpty(adapter.getNowStepID()))
                    UserinfoData.saveStepID(this, adapter.getNowStepID());
                adapter.setChanged(false);
                showToast("数据保存成功");
            } else {
                showToast("数据保存失败");
            }
            if (SaveFlag == SAVE_ADD) {
                SaveFlag = 0;
                ToResultActivity();
            }
            if (SaveFlag == SAVE_FINISH) {
                adapter.setChanged(false);
                finish();
            }
        }
    }

    @Override
    public void showLoading(int flag) {

    }

    @Override
    public void hideLoading(int flag) {

    }

    @Override
    public void showError(int flag) {
        showToast("网络请求异常");
        if (flag == Const.PEditAppStepUser) {
            if (SaveFlag == SAVE_FINISH) {
                adapter.setChanged(false);
                finish();
            }
            if (SaveFlag == SAVE_ADD) {
                SaveFlag = 0;
                ToResultActivity();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 100:
                boolean change = data.getBooleanExtra("isChanged", false);
                if (change) {
                    isChanged = true;
                    initData();
                }
                break;
        }
    }

    @Override
    public void finish() {
        if (adapter.isChanged()) {
            ZYDialog.Builder builder = new ZYDialog.Builder(this);
            builder.setTitle("提示")
                    .setMessage("信息已经修改，是否保存？")
                    .setCancelAble(true)
                    .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SaveFlag = SAVE_FINISH;
                            Factory.postPhp(EditStepNewActivity.this, Const.PEditAppStepUser);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("不保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            adapter.setChanged(false);
                            finish();
                        }
                    })
                    .create().show();
        } else {
            if (Result_Flag && isChanged) {
                setResult(Const.RESULT_CODE_EDIT_STEP_TO_MEDICAL, getIntent()
                        .putExtra(Const.RESULT_FLAG_FOR_DATA_CHANGED, true));
            }
            super.finish();
        }
    }

}
