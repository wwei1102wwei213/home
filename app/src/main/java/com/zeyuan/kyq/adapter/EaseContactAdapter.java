package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.CareFollowEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.biz.forcallback.AdapterCallback;
import com.zeyuan.kyq.utils.IntegerVersionSignature;

import java.util.ArrayList;
import java.util.List;

public class EaseContactAdapter extends ArrayAdapter<CareFollowEntity> implements SectionIndexer {
    List<String> list;
    private List<CareFollowEntity> userList;
    private List<CareFollowEntity> copyUserList;
    private List<CareFollowEntity> selected_UserInfos;
    private LayoutInflater layoutInflater;
    private SparseIntArray positionOfSection;
    private SparseIntArray sectionOfPosition;
    private int res;
    private MyFilter myFilter;
    private boolean notiyfyByFilter;
    private Context context;
    private AdapterCallback adapterCallback;
    private int maxRemindNum = 10;//最大可@的数量

    public void setAdapterCallback(AdapterCallback adapterCallback) {
        this.adapterCallback = adapterCallback;
    }

    public EaseContactAdapter(Context context, int resource, List<CareFollowEntity> objects, List<CareFollowEntity> selected_UserInfos, int maxRemindNum) {
        super(context, resource, objects);
        this.res = resource;
        this.userList = objects;
        this.selected_UserInfos = selected_UserInfos;
        this.context = context;
        this.maxRemindNum = maxRemindNum;
        copyUserList = new ArrayList<>();
        copyUserList.addAll(objects);
        layoutInflater = LayoutInflater.from(context);
    }

    private static class ViewHolder {
        TextView header;//每组用户开头的英文字母
        ImageView civ_heard;
        TextView tv_name;
        TextView cb_select;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            if (res == 0)
                convertView = layoutInflater.inflate(R.layout.item_user_select, parent, false);
            else
                convertView = layoutInflater.inflate(res, null);
            holder.header = (TextView) convertView.findViewById(R.id.header);
            holder.civ_heard = (ImageView) convertView.findViewById(R.id.civ_heard);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.cb_select = (TextView) convertView.findViewById(R.id.cb_select);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final CareFollowEntity user = getItem(position);
        if (user == null)
            return convertView;
        String username = user.getInfoName();
        String header = user.getInitialLetter();
        boolean isChecked = user.isSelected();
        if (position == 0 || header != null && !header.equals(getItem(position - 1).getInitialLetter())) {
            if (TextUtils.isEmpty(header)) {
                holder.header.setVisibility(View.GONE);
            } else {
                holder.header.setVisibility(View.VISIBLE);
                holder.header.setText(header);
            }
        } else {
            holder.header.setVisibility(View.GONE);
        }
        holder.tv_name.setText(username);
        holder.cb_select.setSelected(isChecked);
        if (!TextUtils.isEmpty(user.getHeadUrl())) {
            Glide.with(context).load(user.getHeadUrl()).signature(new IntegerVersionSignature(1))
                    .error(R.mipmap.default_avatar)
                    .into(holder.civ_heard);
        } else {
            holder.civ_heard.setImageResource(R.mipmap.default_avatar);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.isSelected()) {
                    holder.cb_select.setSelected(false);
                    user.setSelected(false);
                    selected_UserInfos.remove(user);
                } else if (selected_UserInfos.size() < maxRemindNum) {
                    selected_UserInfos.add(user);
                    holder.cb_select.setSelected(true);
                    user.setSelected(true);
                } else {
                    Toast.makeText(context.getApplicationContext(), "最多可@" + maxRemindNum + "个圈友！", Toast.LENGTH_SHORT).show();
                }
                if (adapterCallback != null) {
                    adapterCallback.forAdapterCallback(0, 0, "", false, null);
                }
            }
        });

        return convertView;
    }

    @Override
    public CareFollowEntity getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public int getPositionForSection(int section) {
        return positionOfSection.get(section);
    }

    @Override
    public int getSectionForPosition(int position) {
        return sectionOfPosition.get(position);
    }

    @Override
    public Object[] getSections() {
        positionOfSection = new SparseIntArray();
        sectionOfPosition = new SparseIntArray();
        int count = getCount();
        list = new ArrayList<>();
        list.add(getContext().getString(R.string.search_header));
        positionOfSection.put(0, 0);
        sectionOfPosition.put(0, 0);
        for (int i = 0; i < count; i++) {

            String letter = getItem(i).getInitialLetter();
            int section = list.size() - 1;
            if (list.get(section) != null && !list.get(section).equals(letter)) {
                list.add(letter);
                section++;
                positionOfSection.put(section, i);
            }
            sectionOfPosition.put(i, section);
        }
        return list.toArray(new String[list.size()]);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (myFilter == null) {
            myFilter = new MyFilter(copyUserList);
        }
        return myFilter;
    }

    //用户过滤器，用来搜索用户
    protected class MyFilter extends Filter {
        //所有用户
        List<CareFollowEntity> mOriginalList = null;

        public MyFilter(List<CareFollowEntity> myList) {
            this.mOriginalList = myList;
        }

        @Override
        protected synchronized FilterResults performFiltering(CharSequence prefix) {
            //过滤后的结果
            FilterResults results = new FilterResults();
            if (mOriginalList == null) {
                mOriginalList = new ArrayList<>();
            }

            if (prefix == null || prefix.length() == 0) {//输入的过滤条件为空，显示所有用户
                results.values = copyUserList;
                results.count = copyUserList.size();
            } else {
                String prefixString = prefix.toString();
                final int count = mOriginalList.size();
                final ArrayList<CareFollowEntity> newValues = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    final CareFollowEntity user = mOriginalList.get(i);
                    String username = user.getInfoName();
                    username = username.toLowerCase();
                    prefixString = prefixString.toLowerCase();
                    if (username.contains(prefixString)) {
                        newValues.add(user);
                    }

                }
                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected synchronized void publishResults(CharSequence constraint,
                                                   FilterResults results) {
            userList.clear();
            userList.addAll((List<CareFollowEntity>) results.values);
            if (results.count > 0) {
                notiyfyByFilter = true;
                notifyDataSetChanged();
                notiyfyByFilter = false;
            } else {
                notifyDataSetInvalidated();
            }
        }
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (!notiyfyByFilter) {
            copyUserList.clear();
            copyUserList.addAll(userList);
        }
    }

    //更新数据
    public void updata(List<CareFollowEntity> data) {
        userList.clear();
        userList.addAll(data);
        notifyDataSetChanged();
    }
}
