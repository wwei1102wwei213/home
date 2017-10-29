package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.CityCancerForumBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CircleFollowCheckBox;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/17.
 * 显示圈子列表的适配器
 *
 */
public class FindCircleDetailAdapter extends BaseAdapter {
    private List<String> focusCircle;
    private LayoutInflater inflater;
    private List<CityCancerForumBean.NumEntity> Num;
    private Context context;
    private Map<String, String> circleValues;
    private Map<String, String> cancerValues = ( Map<String, String>)Factory.getData(Const.N_DataCancerValues);
    /*public static String getCancerValues(String ids) {
        Map<String, String> cancerValues = ( Map<String, String>)Factory.getData(Const.N_DataCancerValues);
        return getMapValuesStr(cancerValues, ids);
    }*/

    public FindCircleDetailAdapter(Context context, List<CityCancerForumBean.NumEntity> Num) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.Num = Num;
        this.circleValues = (Map<String, String>) Factory.getData(Const.N_DataCircleValues);
        this.cancerValues = ( Map<String, String>)Factory.getData(Const.N_DataCancerValues);
        focusCircle = UserinfoData.getFocusCircle(context);
    }

    @Override
    public int getCount() {
        return Num.size();
    }

    @Override
    public String getItem(int position) {
        return Num.get(position).getCircleID();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public interface FollowCircleListener {
        void followCircle(BaseAdapter adapter, boolean isFollow, int position);
    }

    private FollowCircleListener listener;


    public void setListener(FollowCircleListener listener) {
        this.listener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_find_circle, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.avatar);
            holder.topic_number = (TextView) convertView.findViewById(R.id.topic_number);
            holder.number = (TextView) convertView.findViewById(R.id.number);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.cancer = (TextView) convertView.findViewById(R.id.cancer);
            holder.isfollowlayout = (RelativeLayout)convertView.findViewById(R.id.isfollowlayout);
            holder.isfollow = (CircleFollowCheckBox) convertView.findViewById(R.id.isfollow);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CityCancerForumBean.NumEntity item = Num.get(position);

        holder.topic_number.setText(context.getString(R.string.forum_num) + item.getForumNum());
        holder.number.setText(context.getString(R.string.person_num) + item.getUsernum());
        String title = item.getCircleID();

        if (!TextUtils.isEmpty(title)) {
            String circleName = circleValues.get(title);
            holder.title.setText(circleName);
            holder.iv.setImageResource(UiUtils.getCancerImage(title));
        } else {
            holder.title.setText("");
            holder.iv.setImageResource(UiUtils.getCancerImage(null));
        }

        String cancer = item.getCancerID();
        if (!TextUtils.isEmpty(cancer)) {
            String cancerName = MapDataUtils.getMapValuesStr(cancerValues, cancer);
            holder.cancer.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(cancerName)){
                holder.cancer.setText(cancerName);
            }
        }else {
            holder.cancer.setVisibility(View.GONE);
        }

        holder.isfollow.setChecked(item.isFollow());
        holder.isfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isChecked = holder.isfollow.isChecked();
                if (!isChecked) {//如果选中 变为不选中状态 即为取消关注

                    item.setIsFollow(false);
                    if (listener != null) {
                        listener.followCircle(FindCircleDetailAdapter.this, false, position);
                    }
                } else {

                    item.setIsFollow(true);
                    if (listener != null) {
                        listener.followCircle(FindCircleDetailAdapter.this, true, position);
                    }
                }
            }
        });
        return convertView;
    }



    static class ViewHolder {
        TextView topic_number;//话题
        TextView number;//人数
        TextView cancer;//癌肿
        TextView title;//深圳圈
        CircleFollowCheckBox isfollow;//关注
        RelativeLayout isfollowlayout;
        ImageView iv;
    }

    public void update(List data) {
        focusCircle = UserinfoData.getFocusCircle(context);
        Num = data;
        notifyDataSetChanged();
    }

    public void update() {
        focusCircle = UserinfoData.getFocusCircle(context);
        notifyDataSetChanged();
    }

}
