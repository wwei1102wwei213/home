package com.zeyuan.kyq.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.Entity.CancerSizeItemEntity;
import com.zeyuan.kyq.Entity.ChartEntity;
import com.zeyuan.kyq.Entity.ChartFloatEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ConstUtils;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/16.
 *
 * @author wwei
 */
public class RecordChartAdapter extends BaseAdapter implements HttpResponseInterface{

    public static final int CHART_CANCER = Const.RECORD_TYPE_14;
    public static final int CHART_QUOTA = Const.RECORD_TYPE_15;

    private Context context;
    private int flag;
    private List<List<CancerSizeItemEntity>> data;
    private List<List<ChartEntity>> list1;
    private List<List<ChartFloatEntity>> list2;
    private boolean isChanged = false;
    private Animation a;
    private Animation b;
    private boolean animFlag = false;
    private int ShowPosition = -1;
    private SparseBooleanArray array = new SparseBooleanArray();

    public boolean isChanged() {
        return isChanged;
    }

    public RecordChartAdapter(Context context,List<List<CancerSizeItemEntity>> data,int flag){
        try {
            this.context = context;
            this.flag = flag;
//            this.flag = flag;
            if (data==null) data = new ArrayList<>();
            this.data = data;
            array = new SparseBooleanArray();
            if (flag==CHART_QUOTA){
                list2 = new ArrayList<>();
                if (data.size()>0){
                    for (int i=0;i<data.size();i++){
                        list2.add(ConstUtils.getQuotaChartData(data.get(i)));
                        if (data.get(i).get(0).getIs_show()==1){
                            array.put(i,true);
                        }
                    }
                }
            }else {
                list1 = new ArrayList<>();
                if (data.size()>0){
                    for (int i=0;i<data.size();i++){
                        list1.add(ConstUtils.getCancerChartData(data.get(i)));
                        if (data.get(i).get(0).getIs_show()==1){
                            array.put(i,true);
                        }
                    }
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"RecordChartAdapter");
        }
    }

    @Override
    public int getCount() {
        if (flag==CHART_QUOTA){
            return list2.size();
        }
        return list1.size();
    }

    @Override
    public Object getItem(int position) {
        if (flag==CHART_QUOTA){
            return list2.get(position);
        }
        return list1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder vh;
        try {
            if (convertView==null){
                vh = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.layout_record_chart, parent, false);
                vh.color_1 = convertView.findViewById(R.id.v_color_1);
                vh.color_2 = convertView.findViewById(R.id.v_color_2);
                vh.show = convertView.findViewById(R.id.v_chart_show_change);
                vh.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
                vh.select_bg = (TextView)convertView.findViewById(R.id.tv_select_bg);
                vh.select_point = (TextView)convertView.findViewById(R.id.tv_select_point);
                vh.select_point_right = (TextView)convertView.findViewById(R.id.tv_select_point_right);
                vh.rv = (RecyclerView)convertView.findViewById(R.id.rv);
                LinearLayoutManager manager = new LinearLayoutManager(context);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                vh.rv.setLayoutManager(manager);
                convertView.setTag(vh);
            }else {
                vh = (ViewHolder)convertView.getTag();
            }

            final int TypeID = data.get(position).get(0).getTypeID();
            vh.color_1.setBackgroundResource(UiUtils.getBGColorForTypeID(position));
            vh.color_2.setBackgroundResource(UiUtils.getColorForTypeID(position));
            String nameStr;
            if (flag==CHART_QUOTA){
                nameStr = UiUtils.getNameENforTypeID(TypeID) + "(" + UiUtils.getUnitForType(TypeID) +")";
                vh.tv_name.setText(nameStr);
//                vh.tv_title.setText(UiUtils.getNameENforTypeID(TypeID));
            }else {
                nameStr= data.get(position).get(0).getName();
                vh.tv_name.setText(TextUtils.isEmpty(nameStr) ? "未知(mm*mm)" : nameStr + "(mm*mm)");
//                vh.tv_title.setText(TextUtils.isEmpty(nameStr) ? "未知(mm*mm)" : nameStr + "(mm*mm)");
            }

            vh.select_bg.setBackgroundResource(UiUtils.getSelectBgForPosition(position));
            vh.select_point_right.setBackgroundResource(UiUtils.getShowRightForPosition(position));
            if (array.get(position)){
                vh.select_bg.setSelected(true);
                vh.select_point.setVisibility(View.GONE);
                vh.select_point_right.setVisibility(View.VISIBLE);
            }else {
                vh.select_bg.setSelected(false);
                vh.select_point.setVisibility(View.VISIBLE);
                vh.select_point_right.setVisibility(View.GONE);
            }
            vh.show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowID = TypeID;
                    if (array.get(position)) {
                        Is_Show = 2;
                    } else {
                        Is_Show = 1;
                    }
                    ShowPosition = position;
                    Factory.postPhp(RecordChartAdapter.this, Const.PShowMarkAndTumourInfo);
                }
            });

            CancerSizeChartAdapter adapter;
            if (flag==CHART_QUOTA){
                adapter = new CancerSizeChartAdapter(context,
                        list2.get(position),UiUtils.getTypeIDForTypeID(position),true);
            }else {
                adapter = new CancerSizeChartAdapter(context,
                        list1.get(position),UiUtils.getTypeIDForTypeID(position));
            }
            vh.rv.setAdapter(adapter);

        }catch (Exception e){

        }


        return convertView;
    }

    private int ShowID;
    private int Is_Show;
    @Override
    public Map getParamInfo(int tag) {
        Map<String,String> map = new HashMap<>();
        if (tag == Const.PShowMarkAndTumourInfo){
            map.put(Contants.InfoID, UserinfoData.getInfoID(context));
            map.put("typeClass",(flag==CHART_QUOTA?2:1)+"");
            map.put("typeID",ShowID+"");
            map.put("is_show",Is_Show+"");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PShowMarkAndTumourInfo){
            PhpUserInfoBean bean = (PhpUserInfoBean)response;
            if (Const.RESULT.equals(bean.getiResult())){
                isChanged = true;
                if (Is_Show==2){
                    showToast("取消显示成功");
                    array.put(ShowPosition,false);
                }else {
                    showToast("显示成功");
                    array.put(ShowPosition,true);
                }
                notifyDataSetChanged();
            }else {
                if (Is_Show==2){
                    showToast("取消显示失败");
                }else {
                    showToast("显示失败");
                }
            }
        }
    }

    private ProgressDialog mProgressDialog;
    @Override
    public void showLoading(int flag) {
        if (flag == Const.PShowMarkAndTumourInfo){
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("正在设置...");
            mProgressDialog.show();
        }
    }

    @Override
    public void hideLoading(int flag) {
        if (flag == Const.PShowMarkAndTumourInfo){
            if (mProgressDialog!=null) mProgressDialog.dismiss();
        }
    }

    @Override
    public void showError(int flag) {
        if (flag == Const.PShowMarkAndTumourInfo){
            if (mProgressDialog!=null) mProgressDialog.dismiss();
        }
        showToast("网络请求失败");
    }

    class ViewHolder{
        View color_1,color_2,show;
        TextView tv_name,select_bg,select_point,select_point_right;
        RecyclerView rv;
    }

    public void update(List<List<CancerSizeItemEntity>> data){
        try {

            if (data==null) data = new ArrayList<>();
            this.data = data;
            array = new SparseBooleanArray();
            if (flag==CHART_QUOTA){
                list2 = new ArrayList<>();
                if (data.size()>0){
                    for (int i=0;i<data.size();i++){
                        list2.add(ConstUtils.getQuotaChartData(data.get(i)));
                        if (data.get(i).get(0).getIs_show()==1){
                            array.put(i,true);
                        }
                    }
                }
            }else {
                list1 = new ArrayList<>();
                if (data.size()>0){
                    for (int i=0;i<data.size();i++){
                        list1.add(ConstUtils.getCancerChartData(data.get(i)));
                        if (data.get(i).get(0).getIs_show()==1){
                            array.put(i,true);
                        }
                    }
                }
            }
            notifyDataSetChanged();
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"RecordChartAdapter");
        }
    }

    private void showToast(String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
