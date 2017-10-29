package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/9.
 */
public class DialogRightAdapter extends BaseAdapter {
    private static final String TAG = "ChooseMedicaDialog";
    private Map<Integer, Boolean> state = new HashMap<Integer, Boolean>();//这个是保存check数据的 不被取消掉

    private List<String> datas;
    private Context context;
    private LayoutInflater inflater;
    private List<String> data;//选中之后传过来的数据
//    private Map<String,String > cureValues;
    private  Map<String, String> performValues;
    private String chooseIds;

    public DialogRightAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }
//    private int type;
    public DialogRightAdapter(Context context, List<String> datas,int type) {
        this.context = context;
        this.datas = datas;
        this.performValues = (Map<String, String>)Factory.getData(Const.N_DataPerformValues);
        inflater = LayoutInflater.from(context);
    }

    public DialogRightAdapter(Context context, List<String> datas,int type,String chooseIds) {
        this.context = context;
        this.datas = datas;
        this.performValues = (Map<String, String>)Factory.getData(Const.N_DataPerformValues);
        inflater = LayoutInflater.from(context);
        try {
            if (!TextUtils.isEmpty(chooseIds)){
                String[] args = chooseIds.split(",");
                data = new ArrayList<>();
                for (int i = 0 ;i<args.length;i++){
                    data.add(args[i]);
                }
            }else {
                data = new ArrayList<>();
            }
        }catch (Exception e){

        }
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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String perform = performValues.get(datas.get(position));
        if(!TextUtils.isEmpty(perform)){
            holder.tv.setText(perform);
        }else {
            holder.tv.setText("");
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


    class ViewHolder {
        TextView tv;
        CheckBox cb;
    }
}
