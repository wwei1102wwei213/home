package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.LogUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/9/23.
 * 发帖时显示图片的gridview适配器
 */
public class GridAdapter extends BaseAdapter {
    private static final int ADDVIEW = 0;
    private static final int NOMALVIEW = 1;
    private LayoutInflater inflater; // 视图容器
    private int selectedPosition = -1;// 选中的位置
    private boolean shape;

    private List<String> imgsUrl;
    private Context context;

    public GridAdapter(Context context, List<String> url) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.imgsUrl = url;
    }

    public int getCount() {
            /*
             * if (0 == mSelectPath.size()) { return 1; }
			 */
        return imgsUrl.size();
    }

    @Override
    public int getViewTypeCount() {
        return NOMALVIEW;
    }

    @Override
    public int getItemViewType(int position) {
//        if (position == imgsUrl.size()) {
//            return ADDVIEW;
//        } else {
        return NOMALVIEW;
//        }
    }

    public Object getItem(int arg0) {

        return imgsUrl.get(arg0);
    }

    public long getItemId(int arg0) {

        return 0;
    }

    public void updateDate(List<String> url) {
        this.imgsUrl = url;
        LogUtil.i("ReleaseForumActivity", url.toString());
        this.notifyDataSetChanged();
    }

    /**
     * ListView Item设置
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        // final int coord = position;
        ViewHolder holder;
//        int viewType = getItemViewType(position);
//        if (viewType == ADDVIEW) {
//            convertView = inflater.inflate(R.layout.imageview, parent, false);
//            ImageView addview = (ImageView) convertView.findViewById(R.id.img);
//            addview.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.add_photo_new));
//            return convertView;
//
//        } else
//            if (viewType == NOMALVIEW) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.imageview2, parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.img);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Glide.with(context).load(imgsUrl.get(position))
                    .into(holder.image);


            return convertView;
        //}
        //return convertView;
    }

    class ViewHolder {
        public ImageView image;
    }
}

