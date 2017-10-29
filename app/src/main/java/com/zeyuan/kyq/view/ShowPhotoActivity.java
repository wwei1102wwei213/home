package com.zeyuan.kyq.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.widget.HackyViewPager;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2015/9/23.
 * 显示图片页面
 *
 */
public class ShowPhotoActivity extends BaseActivity {
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.viewpager);
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            initStatusBar();
            List<String> urls = (List<String>)getIntent().getSerializableExtra("list");
            for(int i = 0 ; i<urls.size() ; i++){
                /**将小图的url转为大图的url*/
                urls.set(i, urls.get(i).replace("thumb.",".")) ;
            }
            int position = getIntent().getIntExtra("position",0);
            viewPager = (HackyViewPager) findViewById(R.id.vp);
            viewPager.setAdapter(new MyViewPager(this, urls));
            viewPager.setCurrentItem(position);

        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "ShowPhotoActivity");
        }
    }

    class MyViewPager extends PagerAdapter {
        List<String> urls;
        MyViewPager(Context context,List<String> urls) {
            this.urls = urls;
        }
        PhotoView photoView;
        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            photoView = new PhotoView(ShowPhotoActivity.this);
            Glide.with(ShowPhotoActivity.this).load(urls.get(position)).signature(new IntegerVersionSignature(1)).into(photoView);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            photoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ShowPhotoActivity.this.finish();
                    return false;
                }
            });
            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    finish();
                }
            });
            return photoView;
        }
    }

}
