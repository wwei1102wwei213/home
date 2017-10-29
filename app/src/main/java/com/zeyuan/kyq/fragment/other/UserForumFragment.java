package com.zeyuan.kyq.fragment.other;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.ForumMyAdapter;
import com.zeyuan.kyq.app.BaseZyFragment;
import com.zeyuan.kyq.bean.ForumListBean;
import com.zeyuan.kyq.bean.MyForumReleaseBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.ForumDetailActivity;
import com.zeyuan.kyq.view.InfoCenterActivity;
import com.zeyuan.kyq.widget.CustomView.OnCustomRefreshListener;
import com.zeyuan.kyq.widget.MyListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/31.
 * <p>
 * 个人中心 用户帖子 列表
 *
 * @author wwei
 */
public class UserForumFragment extends BaseZyFragment implements HttpResponseInterface
        , AdapterView.OnItemClickListener, OnCustomRefreshListener {

    private int page = 0;
    private int pagesize = 10;
    private boolean loading = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_user_forum, container, false);

        initView();
        initData();

        return rootView;
    }

    private MyListView clv;
    private List<ForumListBean.ForumnumEntity> datas;
    private ForumMyAdapter adapter;

    private void initView() {

        clv = (MyListView) findViewById(R.id.cr_lv);
        clv.setFocusable(false);
        datas = new ArrayList<>();
        adapter = new ForumMyAdapter(getActivity(), datas, InfoCenterID, InfoCenterName);
        adapter.setTop(false);
        clv.setAdapter(adapter);
        clv.setOnItemClickListener(this);

    }

    private void initData() {
        Factory.post(this, Const.EGetMyForum);

    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        /*if (tag == Const.PGetPorCommentList){
            map.put(Contants.InfoID,UserinfoData.getInfoID(this));
        }*/
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if (flag == Const.EGetMyForum) {
            args = new String[]{
                    Contants.InfoID, InfoCenterID,
                    "page", page + "",
                    "pagesize", pagesize + ""
            };
        } else if (flag == Const.EGetForumList) {
            args = new String[]{
                    Contants.InfoID, UserinfoData.getInfoID(context),
                    Contants.CircleID, "30",
                    "page", page + "",
                    "pagesize", pagesize + ""
            };
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object response, int flag) {

        if (flag == Const.EGetMyForum) {
            MyForumReleaseBean bean = (MyForumReleaseBean) response;
            if (Contants.OK_DATA.equals(bean.getIResult())) {
                try {
                    List list = bean.getForumNum();

                    if (list != null && list.size() > 0) {
                        if (page == 0) {
                            datas = new ArrayList();
                            clv.setVisibility(View.VISIBLE);
                            findViewById(R.id.v_no_result).setVisibility(View.GONE);
                        }
                        datas.addAll(list);
                        adapter.update(datas);
                        if (loading && page > 0) {
                            overLoading(0, true);
                        }
                    } else {
                        showErrorView(2);
                    }

                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "NewCircleActivity");
                }
            } else {
                showErrorView(1);
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
        if (flag == Const.EGetMyForum) {
            showErrorView(2);
        }
    }

    /**
     * 数据请求或加载失败操作
     * flag 1：加载失败
     * 2：没有更多了
     *
     * @param flag
     */
    private void showErrorView(int flag) {
        if (page == 0) {
            showNoResult();
        } else {
            if (loading) {
                page--;
                overLoading(flag, true);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Intent intent = new Intent(context, ForumDetailActivity.class);
            intent.putExtra(Const.FORUM_ID, adapter.getItem(position).getIndex());//帖子id
            intent.putExtra(Const.AUTHORID, adapter.getItem(position).getOwnerID());
            startActivity(intent);
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, context, "onItemClick UserForumFragment");
        }
    }

    @Override
    public void loadingMore() {
        if (!loading) {
            if (datas == null || datas.size() < pagesize - 1) {
                overLoading(2, true);
            } else {
                loading = true;
                page++;
                initData();
            }
        }
    }

    @Override
    public void onDownPullRefresh() {

    }

    @Override
    public void onLoadingMore() {

    }

    //用户没有帖子
    private void showNoResult() {
        clv.setVisibility(View.GONE);
        datas = null;
        findViewById(R.id.v_no_result).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.tv_no_result)).setText("用户暂无该数据");
    }

    //加载完成，通知父控件
    private void overLoading(int tag, boolean fit) {
        loading = false;
        try {
            ((InfoCenterActivity) context).overLoading(tag, fit);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "转型出错");
        }
    }

}
