package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UiUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/10.
 *
 * @author wwei
 */
public class SymptomBodyRecyclerAdapter extends RecyclerView.Adapter<SymptomBodyRecyclerAdapter.ViewHolder>{

    private Context context;
    private List<String> list;
    private int index = 0;
    private onTabClickListener callback;
    private Map<String,String> map;
    private boolean isChoose = false;//选择症状的标识
    private LinkedHashMap<String, List<String>> allData;
    private int[] numbers;
    private List<String> check;

    public SymptomBodyRecyclerAdapter(Context context,List<String> list,int index,onTabClickListener callback){
        this.context = context;
        if (list==null||list.size()==0) list = new ArrayList<>();
        this.list = list;
        this.index = index;
        this.callback = callback;
        map = (LinkedHashMap<String,String>) Factory.getData(Const.N_DataBodyPosValues);
    }

    public SymptomBodyRecyclerAdapter(Context context,List<String> list,int index,
                                      onTabClickListener callback,boolean isChoose,List<String> check){
        this.context = context;
        if (list==null||list.size()==0) list = new ArrayList<>();
        this.list = list;
        this.index = index;
        this.check = check;
        this.callback = callback;
        this.isChoose = isChoose;
        map = (LinkedHashMap<String,String>) Factory.getData(Const.N_DataBodyPosValues);
        allData = MapDataUtils.getBodyPos();
        numbers = new int[list.size()];
        for ( int i = 0 ; i < list.size() ; i++ ){
            List<String> temp = allData.get(list.get(i));
            if (temp==null||temp.size()==0){
                numbers[i] = 0;
            }else {
                int j = 0;
                for (String str:temp){
                    if (check.contains(str)){
                        j++;
                    }
                }
                numbers[i] = j;
            }
        }
    }

    @Override
    public SymptomBodyRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_symptom_left, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, final int position) {

        try {
            final String id = list.get(position);
            if (position == index){
                vh.iv.setImageResource(UiUtils.getSymptomImage(id,true));
                vh.tv.setSelected(true);
                vh.bg.setSelected(true);
            }else {
                vh.iv.setImageResource(UiUtils.getSymptomImage(id,false));
                vh.tv.setSelected(false);
                vh.bg.setSelected(false);
            }

            if (position==0&&!isChoose){
                vh.tv.setText("全部");
            }else {
                vh.tv.setText(getBodyName(id));
            }

            if (isChoose&&numbers[position]!=0){
                vh.num.setVisibility(View.VISIBLE);
                vh.num.setText(numbers[position]+"");
            }else {
                vh.num.setVisibility(View.GONE);
            }

            vh.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (index!=position){
                        update(position);
                        callback.onRecyclerLeftClick(id, position);
                    }
                }
            });
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "Bind");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void update(int index){
        this.index = index;
        notifyDataSetChanged();
    }

    public void updateNumber(int index,List<String> check){
//        this.index = index;
        this.check = check;
        List<String> temp = allData.get(list.get(index));
        if (temp==null||temp.size()==0){
            numbers[index] = 0;
        }else {
            int j = 0;
            for (String str:temp){
                if (check.contains(str)){
                    j++;
                }
            }
            numbers[index] = j;
        }
        notifyDataSetChanged();
    }

    public void update(List<String> list,int index){
        this.list = list;
        this.index = index;
        notifyDataSetChanged();
    }

    private String getBodyName(String id){
        String name = "";
        if (map!=null&&map.size()>0){
            name = map.get(id);
        }
        if (TextUtils.isEmpty(name)) name = "";
        return name;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv,num;
        ImageView iv;
        View bg;
        View v;

        public ViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            bg = itemView.findViewById(R.id.v_bg);
            tv = (TextView)itemView.findViewById(R.id.tv_child);
            num = (TextView)itemView.findViewById(R.id.tv_num);
            iv = (ImageView)itemView.findViewById(R.id.iv_child);
        }

    }

    public interface onTabClickListener{
        void onRecyclerLeftClick(String id,int pos);
    }
}
