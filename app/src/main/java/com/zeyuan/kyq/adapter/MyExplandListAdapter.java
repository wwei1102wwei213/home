//package com.zeyuan.kyq.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseExpandableListAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.zeyuan.kyq.R;
//import com.zeyuan.kyq.bean.AllStepBean;
//import com.zeyuan.kyq.utils.DataUtils;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by Administrator on 2015/10/8.
// * 阶段详情的adapter
// */
//public class MyExplandListAdapter extends BaseExpandableListAdapter {
//    private List<AllStepBean.StepNumEntity> groupDatas;
//    private List<String> childDatas;
//    private Context context;
//    private LayoutInflater inflater;
//    public MyExplandListAdapter(Context context,List<AllStepBean.StepNumEntity> groupDatas,List<String> childDatas) {
//        this.context = context;
//        this.groupDatas = groupDatas;
//        this.childDatas = childDatas;
//        inflater= LayoutInflater.from(context);
//    }
//
//
//    public MyExplandListAdapter(Context context,List<AllStepBean.StepNumEntity> groupDatas) {
//        this.context = context;
//        this.groupDatas = groupDatas;
//        inflater= LayoutInflater.from(context);
//
//    }
//
//    @Override
//    public int getGroupCount() {
//        return groupDatas.size();
//    }
//
//    @Override
//    public int getChildrenCount(int groupPosition) {
//        return 1;
//    }
//
//    @Override
//    public Object getGroup(int groupPosition) {
//        return groupDatas.get(groupPosition);
//    }
//
//    @Override
//    public Object getChild(int groupPosition, int childPosition) {
//        return childPosition;
//    }
//
//    @Override
//    public long getGroupId(int groupPosition) {
//        return groupPosition;
//    }
//
//    @Override
//    public long getChildId(int groupPosition, int childPosition) {
//        return childPosition;
//    }
//
//    @Override
//    public boolean hasStableIds() {
//        return false;
//    }
//
//    @Override
//    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//        GroupViewHolder holder;
//        if(convertView == null) {
//            holder = new GroupViewHolder();
//            convertView = inflater.inflate(R.layout.item_group, null);
//            holder.img = (ImageView) convertView.findViewById(R.id.indicator);
//                //在这儿找控件
//
//            holder.time = (TextView) convertView.findViewById(R.id.time);
//            holder.step_medica = (TextView) convertView.findViewById(R.id.step_medica);
//            holder.step_number = (TextView) convertView.findViewById(R.id.step_number);
//            convertView.setTag(holder);
//        } else {
//            holder = (GroupViewHolder) convertView.getTag();
//        }
//
//        holder.step_number.setText("第"+groupDatas.get(groupPosition).getStepUID()+"阶段");
//        String beginTime = groupDatas.get(groupPosition).getBeginTime().concat("000");//加000 是因为后台给的是秒
//        String endTime = groupDatas.get(groupPosition).getEndTime().concat("000");
////        String time = beginTime.concat(endTime);
//        String time = getDate(beginTime).concat("到").concat(getDate(endTime));
//        holder.time.setText(time);
//        String medica = DataUtils.loadStringToShowString(groupDatas.get(groupPosition).getStepID());
//        holder.step_medica.setText(medica);
//
//        if(isExpanded) {
//            holder.img.setBackgroundResource(R.mipmap.up);
//        } else {
//            holder.img.setBackgroundResource(R.mipmap.down);
//        }
//
//        return convertView;
//    }
//
//    class GroupViewHolder{
//        ImageView img;
//        TextView step_medica;//阶段的药物
//        TextView time;//阶段的时间
//        TextView step_number;
//    }
//
//
//    @Override
//    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//        ChildViewHolder holder;
//        if(convertView == null) {
//            convertView = inflater.inflate(R.layout.item_child, null);
//            holder = new ChildViewHolder();
//
//            //find views
//            convertView.setTag(holder);
//        } else {
//            holder = (ChildViewHolder) convertView.getTag();
//        }
//
//        //set views
//
//        return convertView;
//    }
//
//
//
//    class ChildViewHolder{
//
//    }
//
//    @Override
//    public boolean isChildSelectable(int groupPosition, int childPosition) {
//        return false;
//    }
//
//
//    public void update(List<AllStepBean.StepNumEntity> groupDatas,List<String> childDatas) {
//        if(groupDatas != null && groupDatas.size() > 0) {
//            this.groupDatas = groupDatas;
//        }
//        if(childDatas != null && childDatas.size() > 0) {
//            this.childDatas = childDatas;
//        }
//        notifyDataSetChanged();
//    }
//
//    /**
//     * 这样处理是后台给的数据是秒
//     * @param date
//     * @return
//     */
//    private String getDate(String date) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        long dates = Long.parseLong(date);
//        return format.format(new Date(dates));
//    }
//}
