package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.ExceptionUtils;

/**
 * Created by Administrator on 2017/2/14.
 *
 *
 * @author wwei
 */
public class RecordClassifyStaticAdapter extends BaseAdapter{

    private Context context;
    private int[] res_bg;
    private int[] res_left;
    private String[] titles;
    private String[] remarks;

    public RecordClassifyStaticAdapter(Context context){
        this.context = context;
        res_bg = new int[]{R.drawable.record_classify_bg_1,R.drawable.record_classify_bg_2,
                R.drawable.record_classify_bg_3,R.drawable.record_classify_bg_4,
                R.drawable.record_classify_bg_5,R.drawable.record_classify_bg_6,
                R.drawable.record_classify_bg_7,R.drawable.record_classify_bg_8,
                R.drawable.record_classify_bg_9,R.drawable.record_classify_bg_10};
        res_left = new int[]{R.mipmap.record_classify_left_1,R.mipmap.record_classify_left_2,
                R.mipmap.record_classify_left_3,R.mipmap.record_classify_left_4,
                R.mipmap.record_classify_left_5,R.mipmap.record_classify_left_6,
                R.mipmap.record_classify_left_7,
                R.mipmap.record_classify_left_9,R.mipmap.record_classify_left_10,R.mipmap.record_classify_left_11
                };
        titles = new String[]{"新增转移","记录症状","基因情况","常规指标","肿瘤大小、影像报告",
                "肿瘤指标","诊断书","病理、检验报告","出院报告","心情记录"};
        remarks = new String[]{"记录发生转移的位置","可拍摄并记录症状的严重程度","记录每次基因突变的情况",
                "可记录尿常规、血常规等","可记录肿瘤大小和CT等报告","可记录肿瘤指标，如CEA,AFP,CA19-9等","医院/医生诊断书",
                "病理报告、活检标本、其他检验报告","出院小结，病历报告","发布帖子，与大家交流"};
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh = null;

        if (convertView == null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gv_record_classify,parent,false);
            vh.v = convertView.findViewById(R.id.v_bg);
            vh.iv = (ImageView)convertView.findViewById(R.id.iv);
            vh.title = (TextView)convertView.findViewById(R.id.tv_title);
            vh.remark = (TextView)convertView.findViewById(R.id.tv_remark);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }

        try {
            vh.v.setBackgroundResource(res_bg[position]);
            vh.iv.setImageResource(res_left[position]);
            vh.title.setText(titles[position]);
            vh.remark.setText(remarks[position]);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"RecordClassifyStaticAdapter getView");
        }

        return convertView;

    }

    class ViewHolder{
        View v;
        ImageView iv;
        TextView title;
        TextView remark;
    }

}
