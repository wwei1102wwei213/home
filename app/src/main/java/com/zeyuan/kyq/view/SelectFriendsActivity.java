package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.CareFollowBaseEntity;
import com.zeyuan.kyq.Entity.CareFollowEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.EaseContactAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.AdapterCallback;
import com.zeyuan.kyq.utils.Cn2Spell;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.EaseSidebar;
import com.zeyuan.kyq.widget.SelectedFriendsHeader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SelectFriendsActivity extends BaseActivity implements View.OnClickListener, HttpResponseInterface {
    private TextView tv_sure;
    private EditText et_search_friends;
    private XRefreshView xRefreshView;
    private ProgressBar pd;
    private ListView listView;
    private EaseSidebar sidebar;
    private SelectedFriendsHeader selectedFriendsHeader;
    private EaseContactAdapter easeContactAdapter;
    private ArrayList<CareFollowEntity> selected_UserInfos = new ArrayList<>();//已选中的好友
    private List<CareFollowEntity> CareFollowEntitys = new ArrayList<>();
    private List<CareFollowEntity> allCareFollowEntitys = new ArrayList<>();
    private int maxNum;//最多可@的好友数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_friends);
        initView();
        initData();
        getData();
    }

    private void initView() {
        maxNum = ZYApplication.RemindNum == 0 ? 10 : ZYApplication.RemindNum;
        ImageView back = (ImageView) findViewById(R.id.btn_back);
        tv_sure = (TextView) findViewById(R.id.tv_sure);
        et_search_friends = (EditText) findViewById(R.id.et_search_friends);
        pd = (ProgressBar) findViewById(R.id.pd);
        xRefreshView = (XRefreshView) findViewById(R.id.xrv);
        listView = (ListView) findViewById(R.id.list);
        listView.setMinimumHeight(1);
        sidebar = (EaseSidebar) findViewById(R.id.sidebar);
        selectedFriendsHeader = new SelectedFriendsHeader(this, selected_UserInfos, listView);
        selectedFriendsHeader.setAdapterCallback(new AdapterCallback() {
            @Override
            public void forAdapterCallback(int pos, int tag, String id, boolean flag, Object obj) {
                if (easeContactAdapter != null)
                    easeContactAdapter.notifyDataSetChanged();
                tv_sure.setText("" + selected_UserInfos.size() + "/" + maxNum + "确定");
            }
        });
        easeContactAdapter = new EaseContactAdapter(this, 0, CareFollowEntitys, selected_UserInfos, maxNum);
        easeContactAdapter.setAdapterCallback(new AdapterCallback() {
            @Override
            public void forAdapterCallback(int pos, int tag, String id, boolean flag, Object obj) {
                selectedFriendsHeader.refreshView();
                tv_sure.setText("" + selected_UserInfos.size() + "/" + maxNum + "确定");
            }
        });
        listView.addHeaderView(selectedFriendsHeader.getView());
        listView.setAdapter(easeContactAdapter);

        xRefreshView.setPinnedTime(1000);
        xRefreshView.setPullRefreshEnable(false);
        xRefreshView.setPullLoadEnable(true);
        xRefreshView.setMoveForHorizontal(true);

        sidebar.setListView(listView);
        back.setOnClickListener(this);
        tv_sure.setOnClickListener(this);

        et_search_friends.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                easeContactAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        getData();
                    }
                }, 1000);
            }
        });
        xRefreshView.setStateCallback(new XRefreshView.XRefreshViewScrollStateChangedCallback() {
            @Override
            public void XRVScrollStateChangedCallback(int scrollState) {
                if (scrollState == 2) {
                    Glide.with(SelectFriendsActivity.this).pauseRequests();
                } else {
                    Glide.with(SelectFriendsActivity.this).resumeRequests();
                }
            }
        });

    }

    private void initData() {
        Intent intent = getIntent();
        ArrayList<CareFollowEntity> temp = intent.getParcelableArrayListExtra("selected_UserInfos");
        if (temp != null && temp.size() > 0) {
            selected_UserInfos.addAll(temp);
            selectedFriendsHeader.refreshView();
        }
        tv_sure.setText("" + selected_UserInfos.size() + "/" + maxNum + "确定");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_sure:
                confirmSelect();
                finish();
                break;
        }
    }

    private void getData() {

        Factory.postPhp(this, Const.PCareList);

    }

    //确认选择
    private void confirmSelect() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("selected_UserInfos", selected_UserInfos);
        setResult(RESULT_OK, intent);
    }

    int page;//页数

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == Const.PCareList || tag == Const.PFollowList) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
            map.put("UID", UserinfoData.getInfoID(this));
            map.put("page", page + "");
            map.put("pagesize", "150");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PFollowList || flag == Const.PCareList) {
            try {
                CareFollowBaseEntity entity = (CareFollowBaseEntity) response;
                if (Const.RESULT.equals(entity.getiResult())) {
                    asynchronousDealWithData(entity.getData());
                } else {
                    showToast("请求错误\n错误码:" + entity.getiResult());
                    overLoading(2);
                }
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "handleData");
                overLoading(2);
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
        if (pd != null)
            pd.setVisibility(View.GONE);
        if (flag == Const.PFollowList || flag == Const.PCareList) {
            showToast("网络请求失败");
        }
    }

    private void overLoading(int tag) {
        if (tag == 1) {//加载完成
            xRefreshView.setLoadComplete(true);
        } else if (tag == 2) {//加载失败
            if (page != 0)
                page--;
            xRefreshView.setLoadComplete(false);
            xRefreshView.stopLoadMore();
        } else {
            xRefreshView.setLoadComplete(false);
            xRefreshView.stopLoadMore();
        }
        if (pd != null)
            pd.setVisibility(View.GONE);
    }

    //异步处理数据
    private void asynchronousDealWithData(final List<CareFollowEntity> data) {
        if (data == null || data.size() == 0) {//加载完成了
            overLoading(1);
            return;
        }
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                dealWithData(data);
                e.onNext(0);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        overLoading(0);
                        easeContactAdapter.updata(allCareFollowEntitys);
                    }

                    @Override
                    public void onError(Throwable e) {
                        overLoading(2);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //处理服务器传回的好友列表数据
    private void dealWithData(List<CareFollowEntity> data) {

        for (CareFollowEntity cfe : data) {//设置首字母
            String i = (Cn2Spell.getPinYinFirstLetter(cfe.getInfoName())).toUpperCase();
            if (TextUtils.isEmpty(i) || i.charAt(0) < 'A' || i.charAt(0) > 'Z') {
                i = "#";
            }
            cfe.setInitialLetter(i);
        }
        allCareFollowEntitys.addAll(data);
        Collections.sort(allCareFollowEntitys, new Comparator<CareFollowEntity>() {

            @Override
            public int compare(CareFollowEntity lhs, CareFollowEntity rhs) {//按首字母排序
                if (lhs.getInitialLetter().equals(rhs.getInitialLetter())) {//首字母相同
                    return lhs.getInfoName().compareTo(rhs.getInfoName());
                } else {
                    if ("#".equals(lhs.getInitialLetter())) {
                        return -1;
                    } else if ("#".equals(rhs.getInitialLetter())) {
                        return 1;
                    }
                    return lhs.getInitialLetter().compareTo(rhs.getInitialLetter());


                }
            }
        });
        if (selected_UserInfos != null && selected_UserInfos.size() > 0) {//把之前选择的对象加进list
            for (int i = 0; i < selected_UserInfos.size(); i++) {
                CareFollowEntity careFollowEntity_s = selected_UserInfos.get(i);
                int t = -1;
                for (int j = 0; j < allCareFollowEntitys.size(); j++) {
                    if (careFollowEntity_s.getInfoID() == allCareFollowEntitys.get(j).getInfoID()) {
                        t = j;
                        break;
                    }

                }
                if (t != -1) {
                    allCareFollowEntitys.set(t, careFollowEntity_s);
                }

            }

        }


    }
}
