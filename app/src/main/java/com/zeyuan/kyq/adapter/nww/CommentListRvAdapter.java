package com.zeyuan.kyq.adapter.nww;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.CommentListItem;
import com.zeyuan.kyq.biz.forcallback.AdapterCallback;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.view.ShowPhotoActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-12-03.
 */

public class CommentListRvAdapter extends BaseRecyclerAdapter<CommentListRvAdapter.ViewHolder> {

    private Context context;
    private List<CommentListItem> list;
    private AdapterCallback callback;
    private int pw;

    public CommentListRvAdapter(Context context, List<CommentListItem> list, int pw, AdapterCallback callback){
        this.list = list;
        this.context = context;
        this.callback = callback;

        this.pw = pw;
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view,false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_comment_list_ww,parent,false);
        return new ViewHolder(v, -1, true);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position, boolean isItem) {
        final CommentListItem item = list.get(position);
        holder.tv_num_1.setText("体感 "+getTagText(item.getTGNum()));
        holder.tv_num_2.setText("影像 "+getTagText(item.getYXNum()));
        holder.tv_num_3.setText("服务 "+getTagText(item.getFWNum()));
        holder.tv_sub.setText(item.getCommentTxt());
        holder.tv_feel_text.setText(getFeelText(item.getCommnetNum()));
        holder.tv_phone_num.setText(getPhoneText(item.getMobile()));
        holder.tv_dateline.setText(DataUtils.showDay(item.getDateline()+""));
        final List<String> pic = item.getPicUrl();
        List<String> remarks = item.getPicTxt();
        try {
            if (pic!=null&&pic.size()>0){
                holder.v_gv.setVisibility(View.VISIBLE);
                CommentImageGVAdapter adapter = new CommentImageGVAdapter(context,pic,remarks,pw);
                holder.gv.setAdapter(adapter);
                holder.gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        context.startActivity(new Intent(context, ShowPhotoActivity.class).putExtra("list",
                                (Serializable) pic).putExtra("position", position));
                    }
                });
            } else {
                holder.v_gv.setVisibility(View.GONE);
            }
        } catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    public void update(List<CommentListItem> list){
        if (list==null) list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View v,v_gv;
        TextView tv_num_1,tv_num_2,tv_num_3,tv_sub,tv_dateline,tv_phone_num,tv_feel_text;
        GridView gv;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            init(itemView,-1,isItem);
        }

        public ViewHolder(View itemView,int viewType,boolean isItem){
            super(itemView);
            init(itemView, viewType, isItem);
        }

        private void init(View itemView,int viewType,boolean isItem){
            if (isItem){

                v = itemView;
                tv_sub = (TextView) itemView.findViewById(R.id.tv_content);
                tv_num_1 = (TextView) itemView.findViewById(R.id.tv_tag_1);
                tv_num_2 = (TextView) itemView.findViewById(R.id.tv_tag_2);
                tv_num_3 = (TextView) itemView.findViewById(R.id.tv_tag_3);
                tv_dateline = (TextView) itemView.findViewById(R.id.tv_dateline);
                tv_phone_num = (TextView) itemView.findViewById(R.id.tv_phone_num);
                tv_feel_text = (TextView) itemView.findViewById(R.id.tv_feel_text);
                v_gv = itemView.findViewById(R.id.v_gv);
                gv = (GridView) itemView.findViewById(R.id.gv);
            }
        }
    }

    private String[] args = {"较差","一般","变好","很好","非常好"};
    private String[] feels = {"较差","一般","较满意","满意","很满意"};
    private String getTagText(int index){
        if (index==0 || index>5) return "";
        return args[index-1];
    }

    private String getFeelText(int index){
        if (index==0 || index>5) return "";
        return feels[index-1];
    }
    private String getPhoneText(String mobile){
        if (TextUtils.isEmpty(mobile)||mobile.length()!=11) return "";
        String str = mobile.substring(0,3) + "*******" + mobile.charAt(mobile.length()-1);
        return str;
    }


}
