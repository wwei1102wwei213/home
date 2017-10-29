package com.zeyuan.kyq.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.widget.HackyViewPager;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/***
 * 图像显示
 *
 */
public class ShowImageActivity extends BaseActivity {

	private HackyViewPager viewPager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setStatusBarTranslucent();
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.image_viewpager);
			getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
			initStatusBar();
			String imageurl = getIntent().getStringExtra(Contants.Avatar);
			List<String> urls = new ArrayList<>();
			if(imageurl!=null){
				Log.i(Const.TAG.ZY_OTHER, "图片URL：" + imageurl);
				imageurl = imageurl.replace("thumb.",".");
				urls.add(imageurl);
			}
			Log.i(Const.TAG.ZY_OTHER, "图片URL：" + imageurl);
			viewPager = (HackyViewPager) findViewById(R.id.vp_img);
			viewPager.setAdapter(new MyViewPager(this, urls));
		}catch (Exception e){
			ExceptionUtils.ExceptionToUM(e, this, "ShowImageActivity");
		}
	}

	class MyViewPager extends PagerAdapter {
		List<String> urls;
		MyViewPager(Context context,List<String> urls) {
			this.urls = urls;
		}
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
			PhotoView photoView = new PhotoView(container.getContext());
			try {
				Glide.with(ShowImageActivity.this).load(urls.get(position)).signature(new IntegerVersionSignature(1)).into(photoView);
				container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

				photoView.setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						ShowImageActivity.this.finish();
						return false;
					}
				});

                photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View view, float x, float y) {
                        ShowImageActivity.this.finish();
                    }
                });
			}catch (Exception e){
				ExceptionUtils.ExceptionToUM(e, ShowImageActivity.this, "ShowImageActivity");
			}
			return photoView;
		}
	}
}
