package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/15.
 */
public class RecordPhotoAdapter extends BaseAdapter{

    private static final int ADDVIEW = 0;
    private static final int NOMALVIEW = 1;
    private LayoutInflater inflater; // 视图容器
    private int selectedPosition = -1;// 选中的位置
    private boolean shape;
    private List<String> imgsUrl;
    private Context context;
    private boolean mFlag = false;
    //网络图片
    private List<String> mUrl;

    public RecordPhotoAdapter(Context context, List<String> url) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.imgsUrl = url;
    }

    public RecordPhotoAdapter(Context context, List<String> url,boolean mFlag) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.imgsUrl = url;
        this.mUrl = new ArrayList<>();
        if (url!=null&&url.size()>0){
            for (String item:url){
                mUrl.add(item);
            }
        }
        this.mFlag = mFlag;
    }

    public int getCount() {
        return (imgsUrl.size() + 1);
    }

    public int getLastPosition() {
        return imgsUrl.size();
    }

    public int getUrlCount(){
        return mUrl.size();
    }

    public List<String> getUrlList(){
        if (mUrl==null) mUrl = new ArrayList<>();
        return mUrl;
    }

    public List<String> getList(){
        return imgsUrl;
    }

    public void removeItemForPosition(int position){
        try {
            LogCustom.e("EXC","mUrl:"+mUrl.size()+",imgsUrl:"+imgsUrl.size()+",pos:"+position);
            if (mUrl.size()==0){
                imgsUrl.remove(position);
            }else {
                if (position<mUrl.size()){
                    mUrl.remove(position);
                }
                imgsUrl.remove(position);
            }
            notifyDataSetChanged();
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"removeItemForPosition");
        }
    }

    @Override
    public int getViewTypeCount() {
        return NOMALVIEW + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == imgsUrl.size()) {
            return ADDVIEW;
        } else {
            return NOMALVIEW;
        }
    }

    public Object getItem(int arg0) {
        return imgsUrl.get(arg0);
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public void updateDate(List<String> url) {
        this.imgsUrl = url;
        this.notifyDataSetChanged();
    }

    /**
     * ListView Item设置
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        // final int coord = position;
        ViewHolder holder = null;
        int viewType = getItemViewType(position);
        if (viewType == ADDVIEW) {
            convertView = inflater.inflate(R.layout.record_photo_image, parent, false);
            ImageView addview = (ImageView) convertView.findViewById(R.id.img);
            addview.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.add_photo_new));
            return convertView;

        } else if (viewType == NOMALVIEW) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.record_photo_image, parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.img);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Glide.with(context).load(imgsUrl.get(position))
                    .into(holder.image);
            return convertView;
        }
        return convertView;

    }

    class ViewHolder {
        public ImageView image;
    }

}
