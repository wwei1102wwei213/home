package com.zeyuan.kyq.biz;

import android.os.AsyncTask;
import android.util.Log;

import com.zeyuan.kyq.Entity.CircleNewEntity;
import com.zeyuan.kyq.Entity.ConfStepEntity;
import com.zeyuan.kyq.Entity.PerformEntity;
import com.zeyuan.kyq.Entity.QuotaItemChildEntity;
import com.zeyuan.kyq.bean.ArticleListBean;
import com.zeyuan.kyq.bean.CityCancerForumBean;
import com.zeyuan.kyq.utils.PingYinUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/23.
 *
 * 搜索帮助类
 *
 * @author wwei
 */
public class SearchStringBiz extends AsyncTask<Void, Void, List<List<String>>>{

    public static final int CIRCLE = 1; //圈子
    public static final int SYMPTOM = 2; //症状
    public static final int ARTICLE = 3; //文章
    public static final int DRUG = 4; //阶段药物
    public static final int QUOTA = 5;//指标
    public static final int CIRCLE_NEW = 6;//新圈子

    private int flag;
    private SearchStringInterface callback;
    private Object obj;

    public SearchStringBiz(int flag,SearchStringInterface callback,Object obj){
        this.flag = flag;
        this.callback = callback;
        this.obj = obj;
    }

    @Override
    protected List<List<String>> doInBackground(Void... params) {
        List<List<String>> o = new ArrayList<>();
        List<String> pylist;
        List<String> firstlist;
        String temptitle;
        switch (flag){
            case CIRCLE:
                List<CityCancerForumBean.NumEntity> list0 = (List<CityCancerForumBean.NumEntity>)obj;
                pylist = new ArrayList<>();
                firstlist = new ArrayList<>();
//                temptitle = "";
                for(CityCancerForumBean.NumEntity entity:list0){
                    temptitle = entity.getCancerName();
                    pylist.add(PingYinUtil.getPingYin(temptitle));
                    firstlist.add(PingYinUtil.getFirstSpell(temptitle));
                }
                o.add(pylist);
                o.add(firstlist);
                break;
            case SYMPTOM:
                List<PerformEntity> list1 = (List<PerformEntity>)obj;
                Log.i("ZYS",list1.toString());
                pylist = new ArrayList<>();
                firstlist = new ArrayList<>();
//                temptitle = "";
                for(PerformEntity entity:list1){
                    temptitle = entity.getPerformname();
                    pylist.add(PingYinUtil.getPingYin(temptitle.charAt(0)+""));
                    firstlist.add(PingYinUtil.getFirstSpell(temptitle));
                }
                o.add(pylist);
                o.add(firstlist);
                break;
            case ARTICLE:
                List<ArticleListBean.ArticlenumEntity> list2 = (List<ArticleListBean.ArticlenumEntity>)obj;
                pylist = new ArrayList<>();
                firstlist = new ArrayList<>();
//                temptitle = "";
                for(ArticleListBean.ArticlenumEntity entity:list2){
                    temptitle = entity.getTitle();
                    pylist.add(PingYinUtil.getPingYin(temptitle));
                    firstlist.add(PingYinUtil.getFirstSpell(temptitle));
                }
                o.add(pylist);
                o.add(firstlist);
                break;
            case DRUG:
                List<ConfStepEntity> drugList = (List<ConfStepEntity>)obj;
                pylist = new ArrayList<>();
                firstlist = new ArrayList<>();
//                temptitle = "";
                for(ConfStepEntity entity:drugList){
                    temptitle = entity.getStepName();
                    pylist.add(PingYinUtil.getPingYin(temptitle));
                    firstlist.add(PingYinUtil.getFirstSpell(temptitle));
                }
                o.add(pylist);
                o.add(firstlist);
                break;
            case QUOTA:
                List<QuotaItemChildEntity> quotaList = (List<QuotaItemChildEntity>)obj;
                pylist = new ArrayList<>();
                firstlist = new ArrayList<>();
//                temptitle = "";
                for(QuotaItemChildEntity entity:quotaList){
                    temptitle = entity.getSpName();
                    pylist.add(PingYinUtil.getPingYin(temptitle));
                    firstlist.add(PingYinUtil.getFirstSpell(temptitle));
                }
                o.add(pylist);
                o.add(firstlist);
                break;
            case CIRCLE_NEW:
                List<CircleNewEntity> mCircleNewList = (List<CircleNewEntity>)obj;
                pylist = new ArrayList<>();
                firstlist = new ArrayList<>();
                for(CircleNewEntity entity:mCircleNewList){
                    temptitle = entity.getCircleName();
                    pylist.add(PingYinUtil.getPingYin(temptitle));
                    firstlist.add(PingYinUtil.getFirstSpell(temptitle));
                }
                o.add(pylist);
                o.add(firstlist);
                break;
        }

        return o;
    }

    @Override
    protected void onPostExecute(List<List<String>> o) {
        callback.setData(o);
    }

    public interface SearchStringInterface{
        void setData(List<List<String>> o);
    }
}
