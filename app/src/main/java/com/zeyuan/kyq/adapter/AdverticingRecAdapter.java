package com.zeyuan.kyq.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.Advertising;
import com.zeyuan.kyq.Entity.HomePageEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.manager.ClickStatisticsManager;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.UiUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/5/11.
 * 活动列表适配器
 */

public class AdverticingRecAdapter extends BaseRecyclerAdapter<AdverticingRecAdapter.AdverticingViewHolder> {
    private List<Advertising> advertisings;
    private LayoutInflater layoutInflater;
    private Context context;

    public AdverticingRecAdapter(Context context, List<Advertising> advertisings) {
        this.advertisings = advertisings;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public AdverticingViewHolder getViewHolder(View view) {
        return new AdverticingViewHolder(view, false);
    }

    @Override
    public AdverticingViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = layoutInflater.inflate(R.layout.item_adverticing, parent, false);
        return new AdverticingViewHolder(view, true);
    }

    @Override
    public void onBindViewHolder(AdverticingViewHolder holder, int position, boolean isItem) {
        final Advertising advertising = advertisings.get(position);
        Glide.with(context).load(advertising.getPic_oss()).signature(new IntegerVersionSignature(1))
                .error(R.mipmap.default_avatar)
                .into(holder.iv_adverciting);
        holder.adv_title.setText(advertising.getTitle());
        holder.adv_info.setText(advertising.getInfotext());
        holder.convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePageEntity homePageEntity = new HomePageEntity();
                homePageEntity.setSkiptype(advertising.getSkiptype());
                homePageEntity.setSign_a(advertising.getSign_a());
                homePageEntity.setSign_b(advertising.getSign_b());
                UiUtils.toMenuJump(context, homePageEntity, null, false, null);
                ClickStatisticsManager.getInstance().addClickEvent(Const.CLICK_EVENT_6, advertising.getId());
            }
        });
    }

    @Override
    public int getAdapterItemCount() {
        return advertisings.size();
    }

    class AdverticingViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_adverciting;
        private TextView adv_title;
        private TextView adv_info;
        private View convertView;

        public AdverticingViewHolder(View itemView, boolean isItem) {
            super(itemView);
            initView(itemView, isItem);
        }

        private void initView(View itemView, boolean isItem) {
            if (isItem) {
                convertView = itemView;
                iv_adverciting = (ImageView) itemView.findViewById(R.id.iv_adverciting);
                adv_title = (TextView) itemView.findViewById(R.id.adv_title);
                adv_info = (TextView) itemView.findViewById(R.id.adv_info);
            }
        }

    }
}
