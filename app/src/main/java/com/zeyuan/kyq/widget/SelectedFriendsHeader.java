package com.zeyuan.kyq.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.CareFollowEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.forcallback.AdapterCallback;
import com.zeyuan.kyq.utils.IntegerVersionSignature;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/1.
 * 已选择的好友
 */

public class SelectedFriendsHeader {
    private Context context;
    private View rootView;
    private ViewGroup view_group;
    private LayoutInflater layoutInflater;
    private List<CareFollowEntity> selected_UserInfos;
    private List<View> viewList = new ArrayList<>();
    private AdapterCallback adapterCallback;
    public void setAdapterCallback(AdapterCallback adapterCallback) {
        this.adapterCallback = adapterCallback;
    }
    public SelectedFriendsHeader(Context context, List<CareFollowEntity> selected_UserInfos, ViewGroup parent) {
        this.context = context;
        this.selected_UserInfos = selected_UserInfos;
        this.layoutInflater = LayoutInflater.from(context);
        rootView = layoutInflater.inflate(R.layout.view_select_friend_heard, parent, false);
        view_group = (ViewGroup) rootView.findViewById(R.id.view_group);
    }

    public void refreshView() {
        view_group.removeAllViews();
        createAllItems();
        if (selected_UserInfos.size() > 0)
            rootView.setVisibility(View.VISIBLE);
        else
            rootView.setVisibility(View.GONE);
        for (int i = 0; i < selected_UserInfos.size(); i++) {
            view_group.addView(setData(viewList.get(i), selected_UserInfos.get(i)));
        }
    }

    private void createAllItems() {
        if (selected_UserInfos.size() > viewList.size()) {
            int count = selected_UserInfos.size() - viewList.size();
            for (int i = 0; i < count; i++) {
                viewList.add(createItem());
            }
        }
    }

    private View createItem() {
        ViewHolder viewHolder = new ViewHolder();
        View view = layoutInflater.inflate(R.layout.item_user_select, view_group, false);
        viewHolder.civ_heard = (ImageView) view.findViewById(R.id.civ_heard);
        viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
        viewHolder.cb_select = (TextView) view.findViewById(R.id.cb_select);
        view.setTag(viewHolder);
        return view;
    }

    private View setData(View view, final CareFollowEntity CareFollowEntity) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.tv_name.setText(CareFollowEntity.getInfoName());
        viewHolder.cb_select.setSelected(CareFollowEntity.isSelected());
        if (!TextUtils.isEmpty(CareFollowEntity.getHeadUrl())) {
            Glide.with(context).load(CareFollowEntity.getHeadUrl()).signature(new IntegerVersionSignature(1))
                    .error(R.mipmap.default_avatar)
                    .into(viewHolder.civ_heard);
        } else {
            viewHolder.civ_heard.setImageResource(R.mipmap.default_avatar);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.cb_select.setSelected(false);
                CareFollowEntity.setSelected(false);
                selected_UserInfos.remove(CareFollowEntity);
                refreshView();
                if (adapterCallback != null) {
                    adapterCallback.forAdapterCallback(0, 0, "", false, null);
                }
            }
        });
        return view;
    }

    public View getView() {
        return rootView;
    }

    private static class ViewHolder {
        ImageView civ_heard;
        TextView tv_name;
        TextView cb_select;
    }
}
