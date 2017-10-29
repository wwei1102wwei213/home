package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.utils.CDNHelper;
import com.zeyuan.kyq.utils.DataUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

public class PictureShowActivity extends BaseActivity {

    private static final String TAG = "PictureShowActivity";
    private static final int REQUEST_PICK = 0;
    private GridView gridview;
    private GridAdapter adapter;
    private ArrayList<String> selectedPicture = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_pic_activity);
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
//                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
//                .diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(100 * 1024 * 1024)
//                .diskCacheFileCount(300).tasksProcessingOrder(QueueProcessingType.LIFO).build();
//        ImageLoader.getInstance().init(config);
        gridview = (GridView) findViewById(R.id.gridview);
        adapter = new GridAdapter();
        gridview.setAdapter(adapter);
    }

    public void selectPicture(View view) {
        startActivityForResult(new Intent(this, SelectPictureActivity.class), REQUEST_PICK);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            selectedPicture = (ArrayList<String>) data
                    .getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);


            adapter.notifyDataSetChanged();
        }
    }

    class GridAdapter extends BaseAdapter {
        LayoutParams params = new AbsListView.LayoutParams(100, 100);

        @Override
        public int getCount() {
            return selectedPicture.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new ImageView(PictureShowActivity.this);
                ((ImageView) convertView).setScaleType(ScaleType.CENTER_CROP);
                convertView.setLayoutParams(params);
            }
//            ImageLoader.getInstance().displayImage("file://" + selectedPicture.get(position),
//                    (ImageView) convertView);
            Glide.with(PictureShowActivity.this).load(selectedPicture.get(position)).into((ImageView) convertView);
            new PhotoViewAttacher((ImageView)convertView);
            return convertView;
        }

    }

//
//    public void uploadPhoto(View v) {
//        for (final String s : selectedPicture) {
//            Log.i(TAG, s);
//           String[]aa =  s.split("\\.");
//            final String type = aa[1];
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        String time = System.currentTimeMillis()+"000";
//                        String fileName = "123456" + "ForumImg" + time + DataUtils.getRandom() +"2." + type;
//                        Log.i(TAG, "fileName:" + fileName);
//                        Log.i(TAG, "time:" + time);
//
//                        new CDNHelper(PictureShowActivity.this).uploadFile(s, fileName, new SaveCallback() {
//
//                            @Override
//                            public void onProgress(String s, int i, int i1) {
//                                Log.i(TAG, "[onProgress]" + s + "i = " + i + "i1= " + i1);
//
//                            }
//
//                            @Override
//                            public void onFailure(String s, OSSException e) {
//                                Log.i(TAG, "[onFailure]" + s);
//
//                            }
//
//                            @Override
//                            public void onSuccess(String s) {
//                                Log.i(TAG, "[onSuccess]" + s);//这个s是上传的文件名字
//                            }
//                        });
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        }
//    }
}
