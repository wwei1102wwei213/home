package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.StepItemEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.PolicyDataEntity;
import com.zeyuan.kyq.biz.forcallback.SearchPolityInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/14.
 *
 * 查副作用和并发症的适配器
 *
 * @author wwei
 */
public class SearchPolocyAdapter extends BaseAdapter{

    private Context context;
    private List<StepItemEntity> list;
    private int type;
    private SearchPolityInterface callback;
    private LayoutInflater inflater;
    /***
     *
     * @param context
     * @param list
     * @param type 1为副作用 2为并发症
     */
    public SearchPolocyAdapter(Context context,List<StepItemEntity> list,int type){
        this.context = context;
        this.list = list;
        this.type = type;
        this.callback = (SearchPolityInterface)context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public StepItemEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try {
            ViewHolder vh;
            if(convertView == null){
                convertView = inflater.inflate(R.layout.item_list_step_policy,null);
                vh = new ViewHolder();
                vh.tv_step_name = (TextView)convertView.findViewById(R.id.tv_name_step_policy);
                vh.lv_item = (LinearLayout)convertView.findViewById(R.id.lv_child_step_policy);
                vh.ll_look_more = (RelativeLayout)convertView.findViewById(R.id.ll_child_look_more);
                vh.tv_look_more = (TextView)convertView.findViewById(R.id.tv_child_look_more);
                vh.tv_temp = (TextView)convertView.findViewById(R.id.tv_temp);
                vh.tv_nodata = (TextView)convertView.findViewById(R.id.tv_nodata_text);
                convertView.setTag(vh);
            }else {
                vh = (ViewHolder)convertView.getTag();
            }

            final StepItemEntity entity = list.get(position);
            String stepId = entity.getStepID();
            final String stepName = entity.getStepName();
            List<PolicyDataEntity> data = entity.getData();
            if(!TextUtils.isEmpty(stepName)){
                if(type==1){
                    vh.tv_step_name.setText("与"+stepName+"相关的副作用");
                }else{
                    vh.tv_step_name.setText("与"+stepName+"相关的并发症");
                }
            }else {
                if(type==1){
                    vh.tv_step_name.setText("与 相关的副作用");
                }else{
                    vh.tv_step_name.setText("与 相关的并发症");
                }
            }

            vh.tv_temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.toDrugDetail(entity,type);
                }
            });


            if(data!=null&&data.size()!=0){
                List<PolicyDataEntity> tempData = new ArrayList<>();
                vh.lv_item.setVisibility(View.VISIBLE);
                if(data.size()>4){
                    vh.ll_look_more.setVisibility(View.VISIBLE);
                    vh.tv_look_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callback.toLookMore(entity.getData(), stepName, type);
                        }
                    });
                    for(int i=0;i<4;i++){
                        tempData.add(data.get(i));
                    }
                }else{
                    tempData = data;
                    vh.ll_look_more.setVisibility(View.GONE);
                }

                String name;
                vh.lv_item.removeAllViews();
                for(int i = 0; i<tempData.size() ;i++){
                    View v = inflater.inflate(R.layout.item_child_policy,null);
                    TextView tv = (TextView)v.findViewById(R.id.tv_child_policy);
                    final PolicyDataEntity pEntity = tempData.get(i);
                    name = pEntity.getPolicyName();
                    if(!TextUtils.isEmpty(name)){
                        tv.setText(name);
                    }else{
                        tv.setText("");
                    }
                    if(i%2==0){
                        tv.setBackgroundColor(Color.parseColor("#ffffff"));
                    }else {
                        tv.setBackgroundColor(Color.parseColor("#F5F5F5"));
                    }
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callback.toResultDetail(entity, pEntity, type,0);
                        }
                    });
                    vh.lv_item.addView(v);
                }
                vh.tv_nodata.setVisibility(View.GONE);

            }else{
                vh.tv_nodata.setVisibility(View.VISIBLE);
                if(type==1){
                    vh.tv_nodata.setText(R.string.nodata_for_fuzuoyong);
                }else {
                    vh.tv_nodata.setText(R.string.nodata_for_bingfazheng);
                }
                vh.ll_look_more.setVisibility(View.GONE);
                vh.lv_item.setVisibility(View.GONE);
            }

        }catch (Exception e){
            Log.i("ZYS","刷新出错");
        }

        return convertView;
    }

    public void updata(List<StepItemEntity> list){
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView tv_step_name;
        LinearLayout lv_item;
        RelativeLayout ll_look_more;
        TextView tv_look_more;
        TextView tv_temp;
        TextView tv_nodata;
    }

}
