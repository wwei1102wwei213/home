package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.Advertising;
import com.zeyuan.kyq.Entity.HomePageEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.manager.ClickStatisticsManager;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.UiUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/5/11.
 * 圈圈助手广告
 */

public class AdverticingAdapter extends PagerAdapter {
    private List<Advertising> advertisings;
    private Context mContext;
    private LayoutInflater mInflater;

    public AdverticingAdapter(Context context, List<Advertising> advertisings) {
        this.mContext = context;
        this.advertisings = advertisings;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View imageLayout = mInflater.inflate(R.layout.home_ads_view, container, false);
        try {
            ImageView imageView = (ImageView) imageLayout
                    .findViewById(R.id.ads_view);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            final int index = position % advertisings.size();
            String url = advertisings.get(index).getPic_oss();
            imageView.setImageResource(R.mipmap.loading_fail);
            if (TextUtils.isEmpty(url)) {
                imageView.setImageResource(R.mipmap.loading_fail);
            } else {
                try {
                    Glide.with(mContext).load(url).error(R.mipmap.loading_fail).into(imageView);
                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "instantiateItem Glide");
                }
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        HomePageEntity homePageEntity = new HomePageEntity();
                        homePageEntity.setSkiptype(advertisings.get(index).getSkiptype());
                        homePageEntity.setSign_a(advertisings.get(index).getSign_a());
                        homePageEntity.setSign_b(advertisings.get(index).getSign_b());
                        UiUtils.toMenuJump(mContext, homePageEntity, null, false, null);
                        ClickStatisticsManager.getInstance().addClickEvent(Const.CLICK_EVENT_6, advertisings.get(index).getId());
                    } catch (Exception e) {
                        ExceptionUtils.ExceptionSend(e, "AdverticingAdapter");
                    }
                }
            });
            container.addView(imageLayout, 0);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "instantiateItem");
        }
        return imageLayout;
    }
}
