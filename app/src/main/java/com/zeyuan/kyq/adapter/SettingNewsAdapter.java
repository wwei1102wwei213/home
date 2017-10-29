package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umeng.message.entity.UMessage;
import com.zeyuan.kyq.Entity.PushNewEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.db.DBHelper;
import com.zeyuan.kyq.utils.DataUtils;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/29.
 *
 *
 *
 * @author wwei
 */
public class SettingNewsAdapter extends BaseAdapter{

    private Context context;
    private List<PushNewEntity> list;
    private Map<String,String> map;
    private JumpCallBack callback;

    public SettingNewsAdapter(Context context,List<PushNewEntity> list){
        this.context = context;
        this.list = list;
        this.callback = (JumpCallBack)context;
    }

    public interface JumpCallBack{
        void toJump(int jump,UMessage msg);
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        try {
            ViewHolder vh;
            if(convertView==null){
                vh = new ViewHolder();
                convertView = View.inflate(context,R.layout.item_news_list,null);
                vh.title = (TextView)convertView.findViewById(R.id.tv_news_item);
                vh.text = (TextView)convertView.findViewById(R.id.tv_news_text);
                vh.time = (TextView)convertView.findViewById(R.id.tv_news_time);
                vh.line = convertView.findViewById(R.id.line);
                vh.read = convertView.findViewById(R.id.is_read);
                vh.v = convertView;
                convertView.setTag(vh);
            }else {
                vh = (ViewHolder)convertView.getTag();
            }
            final PushNewEntity entity = list.get(position);
            final UMessage msg = new UMessage(new JSONObject(entity.getMsg()));
            map = msg.extra;
            String mTitle = msg.title;
            String mText = msg.text;
            String mTime = entity.getTime()+"";
            if(TextUtils.isEmpty(mTitle)){
                vh.title.setText("");
            }else {
                vh.title.setText(mTitle);
            }
            if (entity.getRead()==1){
                vh.read.setBackgroundResource(R.drawable.bg_btn_point);
            }else {
                vh.read.setBackgroundResource(R.drawable.bg_btn_point_red);
            }
            if(TextUtils.isEmpty(mText)){
                vh.text.setText("");
            }else {
                vh.text.setText(mText);
            }
            if(TextUtils.isEmpty(mTime)){
                vh.time.setText("");
            }else {
                vh.time.setText(DataUtils.showFormatTime(mTime));
            }
            if(map!=null&&!TextUtils.isEmpty(map.get("jump"))){
                String temp = map.get("jump");
                final int t = Integer.valueOf(temp);
                if(0==t||8==t){
                    vh.text.setSingleLine(false);
                    vh.v.setClickable(false);
                }else {
                    vh.v.setClickable(true);
                    vh.v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (entity.getRead()==0){
                                DBHelper.getInstance().updataPushTable(ZYApplication.application,entity);
                                entity.setRead(1);
                                list.set(position, entity);
                                notifyDataSetChanged();
                            }
                            callback.toJump(t,msg);
                        }
                    });
                    vh.text.setSingleLine(true);
                }
            }else {
                vh.v.setClickable(false);
                vh.text.setSingleLine(false);
            }
            if(position == getCount()-1){
                vh.line.setVisibility(View.INVISIBLE);
            }else {
                vh.line.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){

        }

        return convertView;
    }

    class ViewHolder{
        View v;
        TextView title;
        TextView text;
        TextView time;
        View line;
        View read;
    }
}
