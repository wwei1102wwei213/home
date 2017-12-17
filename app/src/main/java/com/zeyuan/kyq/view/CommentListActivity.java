package com.zeyuan.kyq.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.nww.CommentListRvAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.CommentListBean;
import com.zeyuan.kyq.bean.CommentListItem;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DensityUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-12-03.
 */

public class CommentListActivity extends BaseActivity implements HttpResponseInterface{

    private String id;
    private int type;
    private int CommentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list_ww);
        id = getIntent().getStringExtra(Const.INTENT_SHOW_COMMENT_ID);
        type = getIntent().getIntExtra(Const.INTENT_SHOW_COMMENT_TYPE, 0);
        CommentType = getIntent().getIntExtra(Const.INTENT_SHOW_COMMENT_TYPE_RANGE,0);
        initView();
        initData();
    }

    //滑动器
    private XRefreshView xv;
    //文章列表控件
    private RecyclerView rv;
    private View headerView;
    private List<CommentListItem> list;
    private CommentListRvAdapter adapter;
    private void initView(){
        try {
            findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            xv = (XRefreshView) findViewById(R.id.xrv);
            rv = (RecyclerView) findViewById(R.id.rv);
            LinearLayoutManager manager1 = new LinearLayoutManager(this);
            manager1.setOrientation(LinearLayoutManager.VERTICAL);
            rv.setLayoutManager(manager1);
            rv.setHasFixedSize(true);
            list = new ArrayList<>();
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            int width = metric.widthPixels;     // 屏幕宽度（像素）
            int result = (width - DensityUtils.dpToPx(this, 36 + 16)) / 3;//左右padding各18，space 8*2
            adapter = new CommentListRvAdapter(this, list, result ,null);
            headerView = adapter.setHeaderView(R.layout.head_comment_list, rv);
            initHeaderView();
            rv.setAdapter(adapter);
            //滑动控件设置
            xv.setPinnedTime(1000);
            xv.setPullLoadEnable(true);
            xv.setMoveForHorizontal(true);
            adapter.setCustomLoadMoreView(new XRefreshViewFooter(this));
            xv.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
                @Override
                public void onRefresh() {
                    refresh = true;
                    page = 0;
                    initData();
                }
                @Override
                public void onLoadMore(boolean isSilence) {
                    loading = true;
                    page++;
                    initData();
                }
            });
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, this, "initView");
        }

    }

    private void initData(){
        Factory.postPhp(this, Const.PApi_viewPcomment);
    }

    private TextView tv_feel_num,tv_feel_text;
    private TextView tv_all_num,tv_h_num,tv_m_num,tv_l_num;
    private TextView tv_comment_num;
    private void initHeaderView(){
        if (headerView == null) return;
        tv_feel_num = (TextView) headerView.findViewById(R.id.tv_feel_num);
        tv_feel_text = (TextView) headerView.findViewById(R.id.tv_feel_text);
        tv_all_num = (TextView) headerView.findViewById(R.id.tv_all_num);
        tv_h_num = (TextView) headerView.findViewById(R.id.tv_h_num);
        tv_m_num = (TextView) headerView.findViewById(R.id.tv_m_num);
        tv_l_num = (TextView) headerView.findViewById(R.id.tv_l_num);
        tv_all_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentType = 0;
                page = 0;
                initData();
            }
        });
        tv_h_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentType = 1;
                page = 0;
                initData();
            }
        });
        tv_m_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentType = 2;
                page = 0;
                initData();
            }
        });
        tv_l_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentType = 3;
                page = 0;
                initData();
            }
        });
        tv_comment_num = (TextView) headerView.findViewById(R.id.tv_comment_num);
    }

    private final String[] levels = {"不满意", "一般", "好", "很好", "非常好"};
    private void setUpdateHeaderView(CommentListBean bean){
        if (headerView == null || bean==null) return;
        if (TextUtils.isEmpty(bean.getAvecommentNum())){
            tv_feel_num.setText("0");
            tv_feel_text.setText("");
        } else {
            float level = Float.parseFloat(bean.getAvecommentNum());
            int temp = (int) level;
            if (level>=(temp+0.5f)){
                temp++;
            }
            if (temp>0) temp--;
            tv_feel_num.setText(bean.getAvecommentNum());
            tv_feel_text.setText(levels[temp]);
        }

        tv_all_num.setText(bean.getAcommentNum()>999?"全部 999+":"全部 "+bean.getAcommentNum());
        tv_h_num.setText(bean.getHcommentNum()>999?"好评 999+":"好评 "+bean.getHcommentNum());
        tv_m_num.setText(bean.getMcommentNum()>999?"中评 999+":"中评 "+bean.getMcommentNum());
        tv_l_num.setText(bean.getLcommentNum()>999?"差评 999+":"差评 "+bean.getLcommentNum());
        tv_comment_num.setText(bean.getAcommentNum()>999?"(999+)":"("+bean.getAcommentNum()+")");
    }

    private int page = 0;

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        if (tag == Const.PApi_viewPcomment ) {
            map.put("TypeID", type+"");
            map.put("RelationID",id);
            map.put("page", page + "");
            map.put("pageSize", "10");
            map.put("commentType", CommentType+"");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PApi_viewPcomment){
            CommentListBean bean = (CommentListBean) response;
            if ("0".equals(bean.getiResult())){
                if (page == 0){
                    list = new ArrayList<>();
                    setUpdateHeaderView(bean);
                }
                List<CommentListItem> temp = bean.getData();
                if (temp != null && temp.size() != 0) {
                    list.addAll(temp);
                    adapter.update(list);
                    overLoading(0);
                } else {
                    if (!refresh&&!loading){
                        list = new ArrayList<>();
                        adapter.update(list);
                    } else {
                        overLoading(2);
                    }
                }
            } else {
                overLoading(1);
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
        if (flag==Const.PApi_viewPcomment){
            overLoading(2);
            showToast("网络请求错误");
        }
    }

    private boolean refresh = false;
    private boolean loading = false;
    private void overLoading(int tag) {
        if (tag == 0) {
            if (refresh) {
                xv.stopRefresh();
                xv.setLoadComplete(false);
            }
            if (loading) {
                xv.stopLoadMore();
            }
        } else if (tag == 1) {
            if (refresh) {
                xv.stopRefresh();
            }
            if (loading) {
                page--;
                xv.stopLoadMore();
            }
        } else if (tag == 2) {
            if (refresh) {
                xv.stopRefresh();
            }
            if (loading) {
                page--;
                xv.setLoadComplete(true);
            }
        }
        if (refresh) refresh = false;
        if (loading) loading = false;
    }

}
