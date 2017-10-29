package com.zeyuan.kyq.fragment.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.ForumBaseBean;
import com.zeyuan.kyq.Entity.ForumBaseEntity;
import com.zeyuan.kyq.Entity.Shortcut;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.CircleFindRecyclerAdapter;
import com.zeyuan.kyq.app.BaseZyFragment;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.bean.MyCircleBean;
import com.zeyuan.kyq.bean.ShortcutBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.manager.MyCircleManager;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/15.
 *
 * @author wwei
 */
public class CircleFindFragment extends BaseZyFragment implements HttpResponseInterface, View.OnClickListener {
    public static final int GETED_CIRCLEDATA = 99;//获取圈子数据成功的标志
    private int page = 0;
    // private int pagesize = 15;
    private XRefreshView xRefreshView;
    // private RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.item_fragment_find, container, false);

        initView();
        initData();

        return rootView;
    }

    //private LinearLayout ll;

    private CircleFindRecyclerAdapter adapter;
    private List<ForumBaseEntity> forumListDatas;//装帖子列表的数据
    private List<Shortcut> defShortcut;//默认快捷方式
    //private MyGridView gv;
    //    private CareCircleGvAdapter gv_adapter;
    private View body;


    private void initView() {

        try {
            body = findViewById(R.id.body);
            xRefreshView = (XRefreshView) findViewById(R.id.xrv);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);

            recyclerView.setHasFixedSize(true);
            forumListDatas = new ArrayList<>();
            adapter = new CircleFindRecyclerAdapter(context, new ArrayList<Shortcut>(), forumListDatas);
            adapter.setCallback(callback);
            defShortcut = new ArrayList<>();
            // 设置静默加载模式
//        xRefreshView1.setSilenceLoadMore();
            layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
            // 静默加载模式不能设置footerview
            recyclerView.setAdapter(adapter);
            //设置刷新完成以后，headerview固定的时间
            xRefreshView.setPinnedTime(1000);
            xRefreshView.setPullLoadEnable(true);
            xRefreshView.setMoveForHorizontal(true);
//        xRefreshView1.setAutoLoadMore(true);
            adapter.setCustomLoadMoreView(new XRefreshViewFooter(context));

            xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refresh = true;
                            page = 0;
                            initData();

                        }
                    }, 500);
                }

                @Override
                public void onLoadMore(boolean isSilence) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {

                            loading = true;
                            page++;
                            Factory.postPhp(CircleFindFragment.this, Const.PGetForumList_bank);

                        }
                    }, 1000);
                }
            });

            xRefreshView.setStateCallback(new XRefreshView.XRefreshViewScrollStateChangedCallback() {
                                              @Override
                                              public void XRVScrollStateChangedCallback(int scrollState) {
                                                  try {
                                                      if (scrollState == 2) {
                                                          Glide.with(context).pauseRequests();
                                                      } else {
                                                          Glide.with(context).resumeRequests();
                                                      }
                                                  } catch (Exception e) {

                                                  }

                                              }
                                          }

            );

        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "initView");
        }

    }

    private void initData() {
        if (MyCircleManager.getInstance().hadCircles()) {
            if (callback != null) {
                //圈子数据获取成功，通知父fragment进行下一步操作
                callback.doSomeFinish("", GETED_CIRCLEDATA, 0, true, "", null);
            }
            //创建默认快速入口
            if (defShortcut.size() == 0) {
                createDefShortcut();
            }
            //获取快速入口
            Factory.postPhp(this, Const.PApi_getCircleShortcut);
        } else {
            Factory.post(this, Const.EGetMycircle);
            LogCustom.d("zyh","在CircleFindFragment里调用EGetMycircle");
        }

        Factory.postPhp(this, Const.PGetForumList_bank);
    }


    @Override
    public void onClick(View v) {

    }


    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == Const.PGetForumList_bank) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(context));
            map.put("page", page + "");
            map.put("pagesize", "15");
        } else if (tag == Const.PApi_getCircleShortcut) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(context));
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if (flag == Const.EGetMycircle) {
            String temp;
            String StepID = UserinfoData.getStepID(context);
            String CancerID = UserinfoData.getCancerID(context);
            String CureConfID = MapDataUtils.getAllCureconfID(StepID);
            String ProvinceID;
            String CityID = UserinfoData.getCityID(context);
            temp = Contants.InfoID + ",,," + UserinfoData.getInfoID(getActivity());
            if (Const.NO_STEP.equals(UserinfoData.getIsHaveStep(getActivity()))) {
                temp += ",,," + Const.ISHAVESTEP + ",,," + "0";
            } else {
                if (!TextUtils.isEmpty(StepID)) {
                    temp += ",,," + Contants.StepID + ",,," + StepID;
                }
                if (!TextUtils.isEmpty(CureConfID)) {
                    temp += ",,," + Contants.CureConfID + ",,," + CureConfID;
                } else {
                    temp += ",,," + Contants.CureConfID + ",,," + "0";
                }
                temp += ",,," + Const.ISHAVESTEP + ",,," + "1";
            }
            if (!TextUtils.isEmpty(CancerID)) {
                temp += ",,," + Contants.CancerID + ",,," + CancerID;
            }
            if ("0".equals(CityID)) {
                ProvinceID = "0";
                temp += ",,," + Contants.CityID + ",,," + CityID;
                temp += ",,," + Contants.ProvinceID + ",,," + ProvinceID;
            } else {
                ProvinceID = CityID.substring(0, 2) + "0000";
                temp += ",,," + Contants.CityID + ",,," + CityID;
                temp += ",,," + Contants.ProvinceID + ",,," + ProvinceID;
            }
            UserinfoData.saveProvinceID(getActivity(), ProvinceID);
            temp += ",,,page,,," + "0";
            temp += ",,,pagesize,,," + "2";
            //args = new String[]{};
            args = temp.split(",,,");
        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.EGetMycircle) {
            try {
                showUI();
                MyCircleBean bean = (MyCircleBean) response;
                if (Const.RESULT.equals(bean.getIResult())) {
                    List<String> lists = bean.getFollowCircleNum();
                    ZYApplication.DefaultCircles = bean.getDefaultCircle();
                    ZYApplication.threadCircle = bean.getThreadCircle();
                    ZYApplication.RemindNum = bean.getRemindNum();
                    bindFollowCircle(lists);
                    if (callback != null) {
                        //圈子数据获取成功，通知父fragment进行下一步操作
                        callback.doSomeFinish("", GETED_CIRCLEDATA, 0, true, "", null);
                    }
                    //创建默认快速入口
                    if (defShortcut.size() == 0) {
                        createDefShortcut();
                    }
                    //获取快速入口
                    Factory.postPhp(this, Const.PApi_getCircleShortcut);
                }
            } catch (Exception e) {
                ExceptionUtils.ExceptionSend(e, "CircleFindFragment");
            }
        } else if (flag == Const.PGetForumList_bank) {
            ForumBaseBean bean = (ForumBaseBean) response;
            if (page == 0 && !refresh) {
                showUI();
            }
            if (Const.RESULT.equals(bean.getiResult())) {
                bindListView(bean);
            } else {
                overLoading(1);
            }
        } else if (flag == Const.PApi_getCircleShortcut) {
            ShortcutBean bean = (ShortcutBean) response;
            if (Const.RESULT.equals(bean.getiResult())) {
                List<Shortcut> temp = new ArrayList<>();
                temp.addAll(defShortcut);
                Collections.sort(bean.getData1(), comparator);
                temp.addAll(bean.getData1());
                adapter.updateShortcut(temp);
            }
        }
    }

    //快速入口排序比较器
    Comparator<Shortcut> comparator = new Comparator<Shortcut>() {
        public int compare(Shortcut s1, Shortcut s2) {
            // 按权重排序(从大到小)
            if (s1.getPowerNum() != s2.getPowerNum()) {
                return -(s1.getPowerNum() - s2.getPowerNum());
            }
            return 0;

        }
    };

    private void bindFollowCircle(List<String> lists) {
        if (lists != null && lists.size() >= 0) {
            if (lists.size() > 0) {//存圈子
                List<String> list1 = new ArrayList<>();
                for (String entity : lists) {
                    list1.add(entity);
                }
                UserinfoData.saveFocusCirlce(context, list1);
            }
//            List<String> temp = new ArrayList<>();
//            if (lists.size() < 7) {
//                for (String str : lists) {
//                    temp.add(str);
//                }
//            } else {
//                for (int i = 0; i < 7; i++) {
//                    temp.add(lists.get(i));
//                }
//            }
//            temp.add("9999");
//            adapter.updateCircle(temp);
        }
    }

    //创建三个默认快捷方式
    private void createDefShortcut() {
        Shortcut shortcut = new Shortcut();
        shortcut.setName("好友");
        shortcut.setId(Const.Circle_Friend);
        defShortcut.add(shortcut);
        Shortcut shortcut1 = new Shortcut();
        shortcut1.setName("项目");
        shortcut1.setId(Const.Circle_Project);
        defShortcut.add(shortcut1);
        Shortcut shortcut2 = new Shortcut();
        if (ZYApplication.DefaultCircles == null) {
            shortcut2.setName("地域圈");
        } else {
            shortcut2.setName(MapDataUtils.getCircleValues(ZYApplication.DefaultCircles.get(0).get(0)));
        }
        shortcut2.setId(Const.Circle_Local);
        defShortcut.add(shortcut2);
    }

    private void bindListView(ForumBaseBean bean) {
        try {
            List<ForumBaseEntity> list = bean.getForumAllNum();
            if (list != null && list.size() > 0) {
                if (page == 0) {
                    forumListDatas = new ArrayList<>();
                }
                forumListDatas.addAll(list);
                adapter.update(forumListDatas);//这儿是列表
                overLoading(0);
            } else {
                overLoading(2);
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, getActivity(), "CircleFindFragment");
        }
    }

    private void overLoading(int tag) {
        if (tag == 0) {
            if (refresh) {
                xRefreshView.stopRefresh();
                xRefreshView.setLoadComplete(false);
            }
            if (loading) {
                xRefreshView.stopLoadMore();
            }
        } else if (tag == 1) {
            if (refresh) {
                xRefreshView.stopRefresh();
            }
            if (loading) {
                page--;
                xRefreshView.stopLoadMore();
            }
        } else if (tag == 2) {
            if (refresh) {
                xRefreshView.stopRefresh();
            }
            if (loading) {
                page--;
                xRefreshView.setLoadComplete(true);
            }
        }
        if (refresh) refresh = false;
        if (loading) loading = false;
    }


    private void showUI() {
        if (body.getVisibility() != View.VISIBLE) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    SystemClock.sleep(300);
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    try {
                        findViewById(R.id.pd).setVisibility(View.GONE);
                        body.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        ExceptionUtils.ExceptionSend(e, "onRefresh");
                    }
                }
            }.execute(new Void[]{});
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
        if (flag == Const.EGetMycircle) {
            showUI();
            if (callback != null) {
                //圈子数据获取失败，通知父fragment进行下一步操作
                callback.doSomeFinish("", GETED_CIRCLEDATA, 0, false, "", null);
            }
        } else if (flag == Const.PGetForumList_bank) {
            overLoading(1);
        }
    }

    private boolean refresh = false;
    private boolean loading = false;

}
