package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.forcallback.CircleGridCallback;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.widget.FlowLayout;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/4.
 *
 * 发布帖子时选择圈子的适配器
 *
 * @author wwei
 */
public class ChooseCircleAdapter extends BaseAdapter{

    private Context context;
    private CircleGridCallback callback;
    private List<String> list;
    private List<List<String>> lists;
    private List<String> checked;
    private Map<String,String> map;

    public ChooseCircleAdapter(Context context,CircleGridCallback callback,List<String> list,List<List<String>> lists,List<String> checked){
        this.context = context;
        this.callback = callback;
        this.list = list;
        this.lists = lists;
        this.checked = checked;
        map = (Map<String,String>) Factory.getData(Const.N_DataCircleValues);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<String> getChecked(){
        return checked;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder vh;
        if(convertView==null){
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_choose_circle,null);
            vh.tv = (TextView)convertView.findViewById(R.id.tv_name);
            vh.gv = (FlowLayout)convertView.findViewById(R.id.gv_child);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }
        final String id = list.get(position);
        String name = map.get(id);
        if(TextUtils.isEmpty(id)||TextUtils.isEmpty(name)){
            vh.tv.setText("未找到该分类");
            vh.gv.removeAllViews();
        }else {
            vh.tv.setText(name);
            final List<String> temp = lists.get(position);
            if(temp!=null&&temp.size()!=0){
                vh.gv.removeAllViews();
                for (int i=0;i<temp.size();i++){
                    final CheckBox box = (CheckBox)View.inflate(context,R.layout.item_gv_check_box,null);
                    final String child = temp.get(i);
                    final String childName = getChildName(child);

                    if(checked.contains(child)){
                        box.setChecked(true);
                        box.setText(child);
                    }else {
                        box.setChecked(false);
                        box.setText(childName);
                    }
                    box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                box.setText(childName);
                                if(!checked.contains(child)){
                                    checked.add(child);
                                }
                                callback.setItemChange(child,true,id);
                            }else {
                                if(checked.contains(child)){
                                    checked.remove(child);
                                }
                                box.setText(childName);
                                callback.setItemChange(child,false,id);
                            }
                        }
                    });
                    vh.gv.addView(box);
                }
            }else {
                vh.gv.removeAllViews();
            }
        }
        return convertView;
    }

    private String getChildName(String id){
        if(TextUtils.isEmpty(id)) return "";
        String temp = map.get(id);
        if (TextUtils.isEmpty(temp)) return "";
        return temp;
    }

    class ViewHolder{
        TextView tv;
        FlowLayout gv;
    }
}
