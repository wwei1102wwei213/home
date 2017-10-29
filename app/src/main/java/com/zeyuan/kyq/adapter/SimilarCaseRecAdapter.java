package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.SimilarCaseBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.forcallback.AdapterCallback;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.OtherUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.InfoCenterActivity;
import com.zeyuan.kyq.widget.CircleImageView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/4.
 * 相似案例适配器
 */
public class SimilarCaseRecAdapter extends BaseRecyclerAdapter<SimilarCaseRecAdapter.SimilarCaseViewHolder> {
    private LayoutInflater layoutInflater;
    private List<SimilarCaseBean.DataEntity> similars;
    private AdapterCallback callback;
    private Context context;
    private Map<String, String> cancerValues;
    private String selectedStepId = UserinfoData.getStepID();

    public SimilarCaseRecAdapter(Context context, List<SimilarCaseBean.DataEntity> datas, AdapterCallback callback) {
        this.layoutInflater = LayoutInflater.from(context);
        this.similars = datas;
        this.context = context;
        cancerValues = (Map<String, String>) Factory.getData(Const.N_DataCancerValues);
        this.callback = callback;
    }

    @Override
    public SimilarCaseViewHolder getViewHolder(View view) {
        return new SimilarCaseViewHolder(view, false);
    }

    @Override
    public SimilarCaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = layoutInflater.inflate(R.layout.item_similar_case, parent, false);
        return new SimilarCaseViewHolder(view, true);
    }

    @Override
    public void onBindViewHolder(SimilarCaseViewHolder viewHolder, final int position, boolean isItem) {
        SimilarCaseBean.DataEntity dataEntity = similars.get(position);
        viewHolder.tv_user_name.setText(dataEntity.getInfoName());
        Glide.with(context).load(dataEntity.getHeaderUrl()).signature(new IntegerVersionSignature(1))
                .error(R.mipmap.default_avatar)
                .into(viewHolder.civ_head_img);
        viewHolder.tv_cancer_type.setText(cancerValues.get(dataEntity.getCancerID()));
        viewHolder.tv_similar_percent.setText("匹配度" + dataEntity.getPercentage() + "%");
        viewHolder.tv_move.setText(dataEntity.getTransferRecord());
        String step=MapDataUtils.getAllStepName(dataEntity.getStepID());
        String stepAndGen = "";
        if (!TextUtils.isEmpty(step)) {
            if (dataEntity.getStepID().equals(selectedStepId))
                stepAndGen = "[ " + step + " ]";
            else
                stepAndGen = "[ 当前:" + step + " ]";
        }
        if (!TextUtils.isEmpty(dataEntity.getTransferGene())) {
            stepAndGen = stepAndGen + " [ " + dataEntity.getTransferGene() + " ]";
        }
        viewHolder.tv_steps.setText(stepAndGen);
        if (dataEntity.getIs_care().equals("1")) {
            viewHolder.tv_focus.setSelected(true);
            viewHolder.tv_focus.setText("已关注");
        } else {
            viewHolder.tv_focus.setSelected(false);
            viewHolder.tv_focus.setText("+ 关注");
        }
        final boolean check = viewHolder.tv_focus.isSelected();
        final String InfoID = dataEntity.getInfoID() + "";
        viewHolder.tv_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!OtherUtils.isEmpty(InfoID)) {
                    callback.forAdapterCallback(position, 0, InfoID, !check, null);
                }
            }
        });
        viewHolder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!OtherUtils.isEmpty(InfoID)) {
                    context.startActivity(new Intent(context, InfoCenterActivity.class)
                            .putExtra(Const.InfoCenterID, InfoID));
                }
            }
        });
    }

    @Override
    public int getAdapterItemCount() {
        return similars.size();
    }

    public class SimilarCaseViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civ_head_img;
        TextView tv_user_name;
        TextView tv_similar_percent;
        TextView tv_cancer_type;
        TextView tv_steps;
        TextView tv_move;
        TextView tv_focus;
        View v;

        public SimilarCaseViewHolder(View itemView, boolean isItem) {
            super(itemView);
            initView(itemView, isItem);
        }

        private void initView(View convertView, boolean isItem) {
            if (isItem) {
                v = convertView;
                civ_head_img = (CircleImageView) convertView.findViewById(R.id.civ_head_img);
                tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
                tv_similar_percent = (TextView) convertView.findViewById(R.id.tv_similar_percent);
                tv_cancer_type = (TextView) convertView.findViewById(R.id.tv_cancer_type);
                tv_steps = (TextView) convertView.findViewById(R.id.tv_steps);
                tv_move = (TextView) convertView.findViewById(R.id.tv_move);
                tv_focus = (TextView) convertView.findViewById(R.id.tv_focus);
            }

        }
    }

    /**
     * @param position 数据下标
     * @param tag      1 关注 ; 0 取消关注
     * @param flag     请求是否成功
     */
    public void update(int position, int tag, boolean flag) {
        try {
            if (flag) {
                similars.get(position).setIs_care(tag + "");
            }
            notifyDataSetChanged();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "关注回调失败");
        }
    }

    /**
     * 设置当前选择的阶段id
     *
     * @param stepId 阶段id
     */
    public void setStepId(String stepId) {
        this.selectedStepId = stepId;
    }
}
