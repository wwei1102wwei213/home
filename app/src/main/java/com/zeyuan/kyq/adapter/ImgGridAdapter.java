package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.IntegerVersionSignature;

import java.util.List;

/**
 * Created by Administrator on 2015/11/25.
 *
 */
public class ImgGridAdapter extends BaseAdapter {
    private List<String> imgsUrl;
    private Context context;
    private LayoutInflater inflater; // 视图容器

    public ImgGridAdapter(Context context, List<String> url) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.imgsUrl = url;
    }

    @Override
    public int getCount() {
        return imgsUrl.size();
    }

    @Override
    public String getItem(int position) {
        return imgsUrl.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.imageview2, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Glide.with(context).load(imgsUrl.get(position)).signature(new IntegerVersionSignature(1)).into(holder.image);

        return convertView;
    }


    public void update(List data) {
        this.imgsUrl = data;
        notifyDataSetChanged();
    }
    class ViewHolder {
        public ImageView image;
    }
}
