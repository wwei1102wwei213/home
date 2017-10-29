package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.HomePageEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.manager.ClickStatisticsManager;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.UiUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/1/12.
 *
 * @author wwei
 */
public class BannerPagerAdapter extends PagerAdapter {

    private int[] images;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<HomePageEntity> banners;

    public BannerPagerAdapter(Context context, int[] images) {
        this.mContext = context;
        this.images = images;
        mInflater = LayoutInflater.from(mContext);
    }

    public BannerPagerAdapter(Context context, List<HomePageEntity> banners) {
        this.mContext = context;
        this.banners = banners;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        LogCustom.i("ZYS","destroyItem:"+position);
        container.removeView((View) object);
    }

    @Override
    public void finishUpdate(View container) {
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = mInflater.inflate(R.layout.home_ads_view, view, false);
        try {
            ImageView imageView = (ImageView) imageLayout
                    .findViewById(R.id.ads_view);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            final int index = position % banners.size();
            String url = banners.get(index).getPic_oss();
            if (TextUtils.isEmpty(url)){
                imageView.setImageResource(R.mipmap.loading_fail);
            }else {
                try {
                    Glide.with(mContext).load(url).error(R.mipmap.loading_fail).into(imageView);
                }catch (Exception e){
                    ExceptionUtils.ExceptionSend(e,"instantiateItem Glide");
                }
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ClickStatisticsManager.getInstance().addClickEvent(Const.CLICK_EVENT_1,banners.get(index).getId());
                        UiUtils.toMenuJump(mContext, banners.get(index),null,false,null);
                     //   Factory.postPhp(BannerPagerAdapter.this, Const.PApi_inkevideo_liveidid);
                    }catch (Exception e){

                    }
                }
            });
            view.addView(imageLayout, 0);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"instantiateItem");
        }
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View container) {
    }

}
