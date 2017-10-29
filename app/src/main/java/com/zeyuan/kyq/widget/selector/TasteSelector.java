package com.zeyuan.kyq.widget.selector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.HomePageEntity;
import com.zeyuan.kyq.Entity.Shortcut;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.TasteGvAdapter;
import com.zeyuan.kyq.biz.forcallback.SomeFinishCallback;
import com.zeyuan.kyq.biz.manager.ClickStatisticsManager;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.UiUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/5/10.
 * 选择器
 */

public class TasteSelector {

    private Context context;
    private View rootView;
    private LayoutInflater layoutInflater;
    private TextView tv_more;
    private TasteGvAdapter tasteGvAdapter;
    private SomeFinishCallback callback;

    public void setCallback(SomeFinishCallback callback) {
        this.callback = callback;
    }

    public TasteSelector(Context context, List<Shortcut> shortcuts) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        tasteGvAdapter = new TasteGvAdapter(context, shortcuts);
    }

    public View getView(ViewGroup viewGroup) {
        if (rootView == null) {
            createView(viewGroup);
        }
        return rootView;
    }

    private void createView(ViewGroup viewGroup) {
        rootView = layoutInflater.inflate(R.layout.item_accurate_view, viewGroup, false);
        GridView gv_taste = (GridView) rootView.findViewById(R.id.gv_taste);
        tv_more = (TextView) rootView.findViewById(R.id.tv_more);
        gv_taste.setAdapter(tasteGvAdapter);
        if (tasteGvAdapter.getCount() >= 8) {//如果数量少于8，则不显示展开按钮
            tv_more.setVisibility(View.VISIBLE);
        } else {
            tv_more.setVisibility(View.GONE);
        }
        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_more.isSelected()) {//当前是展开状态
                    tv_more.setSelected(false);
                    tv_more.setText("展开更多内容");
                    tasteGvAdapter.showMore(false);
                } else {
                    tv_more.setSelected(true);
                    tv_more.setText("收起更多内容");
                    tasteGvAdapter.showMore(true);
                }
            }
        });
        gv_taste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Shortcut shortcut = (Shortcut) tasteGvAdapter.getItem(position);
                    switch (shortcut.getId()) {
                        case Const.Circle_Friend:
                            if (callback != null) {
                                callback.doSomeFinish("", Const.SELECT_OTHER_FRAGMENT, 0, false, Const.Circle_Friend, null);
                            }
                            break;
                        case Const.Circle_Project:
                            if (callback != null) {
                                callback.doSomeFinish("", Const.SELECT_OTHER_FRAGMENT, 0, false, Const.Circle_Project, null);
                            }
                            break;
                        case Const.Circle_Local:
                            if (callback != null) {
                                callback.doSomeFinish("", Const.SELECT_OTHER_FRAGMENT, 0, false, Const.Circle_Local, null);
                            }
                            break;
                        default:
                            HomePageEntity homePageEntity = new HomePageEntity();
                            homePageEntity.setSkiptype(shortcut.getSkiptype());
                            homePageEntity.setSign_a(shortcut.getSign_a());
                            homePageEntity.setSign_b(shortcut.getSign_b());
                            homePageEntity.setName(shortcut.getName());
                            ClickStatisticsManager.getInstance().addClickEvent(Const.CLICK_EVENT_5, shortcut.getId());
                            UiUtils.toMenuJump(context, homePageEntity, null, false, null);
                            break;
                    }
                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "onItemClick");
                }
            }
        });
    }

    public void refreshView() {
        if (tasteGvAdapter != null) {
            tasteGvAdapter.notifyDataSetChanged();
            if (tv_more != null) {
                if (tasteGvAdapter.getCount() >= 8) {//如果数量少于8，则不显示展开按钮
                    tv_more.setVisibility(View.VISIBLE);
                } else {
                    tv_more.setVisibility(View.GONE);
                }
                tv_more.setSelected(false);
                tv_more.setText("展开更多内容");
            }
        }

    }

    //获取快速入口数量
    public int getCount() {
        if (tasteGvAdapter == null) {
            return 0;
        } else {
            return tasteGvAdapter.getCount();
        }
    }
}
