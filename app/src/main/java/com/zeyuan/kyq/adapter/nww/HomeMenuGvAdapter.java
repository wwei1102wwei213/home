package com.zeyuan.kyq.adapter.nww;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.HomePageEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.forcallback.FragmentCallBack;
import com.zeyuan.kyq.biz.manager.ClickStatisticsManager;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.view.AllMenuActivity;
import com.zeyuan.kyq.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-01-28.
 */

public class HomeMenuGvAdapter extends BaseAdapter{

    private Context context;
    private List<HomePageEntity> list;
    private int type;
    private FragmentActivity activity;
    private FragmentCallBack callBack;
    public HomeMenuGvAdapter(Context context, List<HomePageEntity> list, int type, FragmentActivity activity, FragmentCallBack callBack){
        this.context = context;
        this.list = list;
        this.type = type;
        this.activity = activity;
        this.callBack = callBack;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public HomePageEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh = null;
        if (convertView==null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_home_menu_gv, parent, false);
            vh.civ = (CircleImageView) convertView.findViewById(R.id.civ_menu_1);
            vh.v_hot = convertView.findViewById(R.id.ic_hot);
            vh.v_num = convertView.findViewById(R.id.v_num);
            vh.tv_title = (TextView) convertView.findViewById(R.id.menu_tv_1);
            vh.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            vh.v = convertView;
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder)convertView.getTag();
        }
        try {
            if (type==2&&position==3){
                vh.v_num.setVisibility(View.GONE);
                vh.v_hot.setVisibility(View.GONE);
                vh.tv_title.setText("更多");
                vh.civ.setImageResource(R.mipmap.gv_more);

                vh.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            context.startActivity(new Intent(context, AllMenuActivity.class));
                        } catch (Exception e){
                            ExceptionUtils.ExceptionSend(e, "");
                        }
                    }
                });
            } else {
                final HomePageEntity entity = list.get(position);
                String titleName = entity.getName();
                Glide.with(context).load(entity.getPic_oss()).error(R.mipmap.loading_fail).into(vh.civ);
                vh.tv_title.setText(TextUtils.isEmpty(titleName)?"未知栏目":titleName);
                vh.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            UiUtils.toMenuJump(context, entity, callBack, true, activity);
                            ClickStatisticsManager.getInstance().addClickEvent(Const.CLICK_EVENT_2, entity.getId());
                        } catch (Exception e){
                            ExceptionUtils.ExceptionSend(e, "");
                        }
                    }
                });
                if ("6".equals(entity.getId())){
                    vh.v_hot.setVisibility(View.VISIBLE);
                }else {
                    vh.v_hot.setVisibility(View.GONE);
                }
                if ("35".equals(entity.getId())&&entity.getCount()!=0){
                    vh.v_num.setVisibility(View.VISIBLE);
                    vh.tv_num.setText("新问"+entity.getCount());
                }else {
                    vh.v_num.setVisibility(View.GONE);
                }
            }
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "");
        }
        return convertView;
    }

    public void update(List<HomePageEntity> list){
        if (list==null) list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView tv_title,tv_num;
        CircleImageView civ;
        View v_num, v_hot, v;
    }
}
