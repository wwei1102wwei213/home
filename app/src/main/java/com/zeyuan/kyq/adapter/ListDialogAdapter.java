package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.fragment.dialog.ListDialog;
import com.zeyuan.kyq.utils.MapDataUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/1/7.
 */
public class ListDialogAdapter extends BaseAdapter {
    private List<String> data;
    private LayoutInflater inflater;
    private Context context;

    private int type;
    public ListDialogAdapter(List<String> data, Context context,int type) {
        this.data = data;
        this.type = type;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

        if (selectPostion == position) {
            holder.tv.setTextColor(context.getResources().getColor(R.color.light_green));
        } else {
            holder.tv.setTextColor(context.getResources().getColor(R.color.text_color2));
        }
        String temp = data.get(position);
        String data = "";
        switch (type) {
            case ListDialog.Step_DIALOG: {
                data = MapDataUtils.getAllStepName(temp);
                break;
            }

            default:
                data = temp;
        }


                holder.tv.setText(data);


        return convertView;
    }

    private int selectPostion = -1;

    public void setSelectPostion(int selectPostion) {
        this.selectPostion = selectPostion;
        notifyDataSetChanged();
    }


    class ViewHolder {
        TextView tv;
        CheckBox cb;
    }
}
