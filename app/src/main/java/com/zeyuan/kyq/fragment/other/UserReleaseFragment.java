package com.zeyuan.kyq.fragment.other;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.NewReplyEntity;
import com.zeyuan.kyq.Entity.UserReplyEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.InfoReplyAdapter;
import com.zeyuan.kyq.app.BaseZyFragment;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.OtherUtils;
import com.zeyuan.kyq.view.ArticleDetailActivity;
import com.zeyuan.kyq.view.ForumDetailActivity;
import com.zeyuan.kyq.view.InfoCenterActivity;
import com.zeyuan.kyq.widget.MyListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/31.
 *
 * 用户个人中心 回复、评论 列表
 *
 * @author wwei
 */
public class UserReleaseFragment extends BaseZyFragment implements AdapterView.OnItemClickListener
        ,HttpResponseInterface{

    private MyListView clv;
    private List<NewReplyEntity> datas;
    private InfoReplyAdapter adapter;
    private int page = 0;
    private int pagesize = 10;
    private boolean loading = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user_forum,container,false);

        initView();
        initData();

        return rootView;
    }

    private void initView(){
        clv = (MyListView)findViewById(R.id.cr_lv);
        clv.setFocusable(false);
        datas = new ArrayList<>();
        adapter = new InfoReplyAdapter(context, datas);
        clv.setAdapter(adapter);
        clv.setOnItemClickListener(this);
    }

    private void initData(){
        Factory.postPhp(this, Const.PUserCommentInfo);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NewReplyEntity entity = adapter.getItem(position);
        if (!OtherUtils.isEmpty(entity.getId()+"")){
            if ("Thread".equals(entity.getType())){
                startActivity(new Intent(context, ForumDetailActivity.class)
                        .putExtra(Const.FORUM_ID, entity.getId()+""));
            }else if ("Art".equals(entity.getType())){
                startActivity(new Intent(context, ArticleDetailActivity.class)
                        .putExtra(Const.INTENT_ARTICLE_ID,entity.getId()+""));
            }
        }

    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String,String> map = new HashMap<>();
        if (tag == Const.PUserCommentInfo){
            map.put(Contants.InfoID, InfoCenterID);
            map.put("page",page+"");
            map.put("pagesize",pagesize+"");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {

        if (flag == Const.PUserCommentInfo){
            UserReplyEntity entity = (UserReplyEntity)response;
            if (Const.RESULT.equals(entity.getiResult())){
                List<NewReplyEntity> list = entity.getData();
                if (list!=null&&list.size()>0){
                    if (page==0){
                        datas = new ArrayList<>();
                        clv.setVisibility(View.VISIBLE);
                        findViewById(R.id.v_no_result).setVisibility(View.GONE);
                    }
                    datas.addAll(list);
                    adapter.update(datas);
                    if (loading&&page>0) overLoading(0,true);
                }else {
                    showErrorView(2);
                }
            }else {
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
        if (flag == Const.PUserCommentInfo){
            showErrorView(2);
        }
    }

    /**
     * 数据请求或加载失败操作
     * flag 1：加载失败
     *      2：没有更多了
     *
     * @param flag
     */
    private void showErrorView(int flag){
        if (page==0) {
            showNoResult();
        }else {
            if(loading){
                page--;
                overLoading(flag,true);
            }
        }
    }

    @Override
    public void loadingMore() {
        //重写父类加载方法
        if (!loading){
            if (datas==null||datas.size()<pagesize-1){
                overLoading(2,true);
            }else {
                loading = true;
                page++;
                initData();
            }
        }
    }

    //用户没有帖子
    private void showNoResult(){
        clv.setVisibility(View.GONE);
        datas = null;
        findViewById(R.id.v_no_result).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.tv_no_result)).setText("用户暂无该数据");
    }

    //加载完成，通知父控件
    private void overLoading(int tag,boolean fit){
        loading = false;
        try {
            ((InfoCenterActivity)context).overLoading(tag,fit);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "转型出错");
        }
    }
}
