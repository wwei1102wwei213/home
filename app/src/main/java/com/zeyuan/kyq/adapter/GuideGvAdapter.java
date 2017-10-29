package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.RecommendUserInfoEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.widget.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/8/21.
 * 关注用户引导的选择适配器
 */

public class GuideGvAdapter extends BaseAdapter {
    private Context context;
    private List<RecommendUserInfoEntity> userInfoEntities;
    private LayoutInflater layoutInflater;

    public GuideGvAdapter(Context context, List<RecommendUserInfoEntity> userInfoEntities) {
        this.context = context;
        this.userInfoEntities = userInfoEntities;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return userInfoEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return userInfoEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_guide_focus_friends, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.fl_avatar = convertView.findViewById(R.id.fl_avatar);
            viewHolder.civ_avatar = (CircleImageView) convertView.findViewById(R.id.civ_avatar);
            viewHolder.cb_select = (CheckBox) convertView.findViewById(R.id.cb_select);
            viewHolder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final RecommendUserInfoEntity userInfoEntity = userInfoEntities.get(position);
        if (!TextUtils.isEmpty(userInfoEntity.getInfoName())) {
            viewHolder.tv_user_name.setText(userInfoEntity.getInfoName());
        } else {
            viewHolder.tv_user_name.setText("");
        }
        if (!TextUtils.isEmpty(userInfoEntity.getOss_request_url())) {
            Glide.with(context).load(userInfoEntity.getOss_request_url()).into(viewHolder.civ_avatar);
        }
        viewHolder.cb_select.setChecked(userInfoEntity.isSelected());
        viewHolder.fl_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.cb_select.setChecked(!viewHolder.cb_select.isChecked());

            }
        });
        viewHolder.cb_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userInfoEntity.setSelected(isChecked);
            }
        });
        return convertView;
    }

    class ViewHolder {
        private View fl_avatar;
        private CircleImageView civ_avatar;
        private CheckBox cb_select;
        private TextView tv_user_name;

    }
}
