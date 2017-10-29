package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.LogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/9.
 */
public class CancerDialogRightAdapter extends BaseAdapter {
    private static final String TAG = "ChooseMedicaDialog";
    private Map<Integer, Boolean> state = new HashMap<Integer, Boolean>();//这个是保存check数据的 不被取消掉
    private int selectedPosition = -1;
    private List<String> datas;
    private Context context;
    private LayoutInflater inflater;
    private List<String> data;//选中之后传过来的数据
    private Map<String,String> cancerValues;

    public CancerDialogRightAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
        this.cancerValues = (Map<String,String>) Factory.getData(Const.N_DataCancerValues);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public String getItem(int position) {
        LogUtil.e("datas' size",datas.size() +"");
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.dialog_rightlistview_item, null);
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
            holder.cb.setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv.setText(cancerValues.get(datas.get(position)));


        /**
         * 选中时改变背景
         */
        if (selectedPosition == position) {
            holder.tv.setSelected(true);
            holder.tv.setPressed(true);
//            convertView.setBackgroundResource(R.color.white);
        } else {
//            convertView.setBackgroundResource(R.color.main_color);
            holder.tv.setSelected(false);
            holder.tv.setPressed(false);
        }
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!(data != null && data.size() > 0)) {
                        state.put(position, isChecked);
                    }
                } else {
                    if (!(data != null && data.size() > 0)) {
                        state.remove(position);
                    }
                }
            }
        });


        if (data != null && data.size() > 0) {
            if (data.contains(datas.get(position))) {
//                LogUtil.i("ChooseMedicaDialog", "被选中的data.size : " + data.size());
                holder.cb.setChecked(true);
            } else {
                holder.cb.setChecked(false);
            }
        } else {
//            LogUtil.i(TAG, "设置按钮是否选中");
            holder.cb.setChecked(state.get(position) == null ? false : true);//第一次进入列表的时候防止出现问题 即data为null的时候
        }

        return convertView;
    }


//    public void update(List<String> datas) {
//        this.datas = datas;
//        notifyDataSetChanged();
//    }


    public void updateCount(List<String> data, List<String> datas) {
        state.clear();
        this.data = data;
        Log.d(TAG, "data[count].size :  " + data.size());


        if (datas != null)
            this.datas = datas;
        notifyDataSetChanged();
    }


    public void update(List<String> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public List getData() {
        return datas;
    }

//    private int setPosition = -1;



    /**
     * 这个方法是为了左边的点击后变白
     */
    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView tv;
        CheckBox cb;
    }
}
