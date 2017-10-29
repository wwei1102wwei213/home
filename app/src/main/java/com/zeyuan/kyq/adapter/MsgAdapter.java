package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.umeng.message.entity.UMessage;
import com.zeyuan.kyq.Entity.MsgClickEntity;
import com.zeyuan.kyq.Entity.MsgEntity;
import com.zeyuan.kyq.Entity.PushNewEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.biz.forcallback.OnReplyListener;
import com.zeyuan.kyq.db.DBHelper;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.UiUtils;
import com.zeyuan.kyq.widget.CircleImageView;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/6.
 * 消息适配器
 */

public class MsgAdapter extends BaseRecyclerAdapter<MsgAdapter.MsgViewHolder> {
    private LayoutInflater inflater;
    private List<MsgEntity> msgEntityList;
    private Context context;
    private OnReplyListener onReplyListener;
    private SettingNewsAdapter.JumpCallBack callback;

    public MsgAdapter(Context context, List<MsgEntity> msgEntityList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.msgEntityList = msgEntityList;
        this.callback = (SettingNewsAdapter.JumpCallBack) context;
    }

    public void setOnReplyListener(OnReplyListener onReplyListener) {
        this.onReplyListener = onReplyListener;
    }

    @Override
    public MsgViewHolder getViewHolder(View view) {
        return new MsgViewHolder(view, false);
    }

    @Override
    public MsgViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = inflater.inflate(R.layout.item_msg, parent, false);
        return new MsgViewHolder(view, true);
    }

    @Override
    public void onBindViewHolder(final MsgViewHolder holder, int position, boolean isItem) {
        try {
            final MsgEntity msgEntity = msgEntityList.get(position);
            if (!TextUtils.isEmpty(msgEntity.getTopimg())) {//用户头像
                Glide.with(context).load(msgEntity.getTopimg()).signature(new IntegerVersionSignature(1))
                        .error(R.mipmap.default_avatar)
                        .into(holder.civ_avatar);

            } else if (msgEntity.getType() == 99) {
                holder.civ_avatar.setImageResource(R.mipmap.ic_msg_avatar_gf);
            } else {
                holder.civ_avatar.setImageResource(R.mipmap.default_avatar);
            }
            if (!TextUtils.isEmpty(msgEntity.getInfoId())) {//头像的点击事件
                holder.civ_avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (msgEntity.getRead().equals("0")) {
                            setMsgClicked(msgEntity);
                            holder.fl_msg.setSelected(true);
                        }
                        UiUtils.jumpToInfoCenter(context, msgEntity.getInfoId());
                    }
                });
            } else {
                holder.civ_avatar.setOnClickListener(null);
            }
            if (!TextUtils.isEmpty(msgEntity.getInfoName())) {//用户名字
                holder.tv_info_name.setText(msgEntity.getInfoName());
            } else {
                holder.tv_info_name.setText("");
            }
            holder.tv_time.setText(DataUtils.showFormatTime(msgEntity.getTime()));//接到消息的时间
            if (!TextUtils.isEmpty(msgEntity.getRead()) && msgEntity.getRead().equals("0")) {//本消息没点击过
                holder.fl_msg.setSelected(false);
            } else {
                holder.fl_msg.setSelected(true);
            }
            holder.v_tag.setVisibility(View.GONE);//默认隐藏官方标识
            holder.fl_msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (msgEntity.getRead().equals("0")) {
                        setMsgClicked(msgEntity);
                        holder.fl_msg.setSelected(true);
                    }
                }
            });
            switch (msgEntity.getType()) {
                case 1://文章点赞
                    holder.tv_reply.setVisibility(View.GONE);
                    holder.tv_hint.setVisibility(View.GONE);
                    holder.ll_be_replied_msg.setVisibility(View.VISIBLE);
                    holder.ll_name.setVisibility(View.VISIBLE);
                    if (msgEntity.getTypeContent() != null) {
                        holder.ll_be_replied_msg.setVisibility(View.VISIBLE);
                        ImageSpan imageSpan = new ImageSpan(context, R.mipmap.ic_msg_zan, DynamicDrawableSpan.ALIGN_BASELINE);
                        SpannableString spannableString = new SpannableString("  点了个赞");
                        spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        holder.tv_msg_content.setText(spannableString);
                        if (!TextUtils.isEmpty(msgEntity.getTypeContent().getTitle()))
                            holder.tv_be_replied_msg_content.setText(msgEntity.getTypeContent().getTitle());
                        else
                            holder.tv_be_replied_msg_content.setText("");
                        if (!TextUtils.isEmpty(msgEntity.getTypeContent().getPic())) {
                            holder.iv_be_replied_msg.setVisibility(View.VISIBLE);
                            Glide.with(context).load(msgEntity.getTypeContent().getPic()).signature(new IntegerVersionSignature(1))
                                    .error(R.mipmap.default_avatar)
                                    .into(holder.iv_be_replied_msg);
                        } else {
                            holder.iv_be_replied_msg.setVisibility(View.GONE);
                        }
                        if (!TextUtils.isEmpty(msgEntity.getTypeContent().getAid())) {//文章跳转
                            holder.ll_be_replied_msg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (msgEntity.getRead().equals("0")) {
                                        setMsgClicked(msgEntity);
                                        holder.fl_msg.setSelected(true);
                                    }
                                    UiUtils.jumpToArticleDetail(context, msgEntity.getTypeContent().getAid());
                                }
                            });

                        } else {//不跳转
                            holder.ll_be_replied_msg.setOnClickListener(null);
                        }
                    } else {
                        holder.tv_msg_content.setText("");
                        holder.ll_be_replied_msg.setVisibility(View.GONE);
                    }
                    break;
                case 2://关注
                    holder.tv_hint.setVisibility(View.VISIBLE);
                    holder.tv_reply.setVisibility(View.GONE);
                    holder.ll_be_replied_msg.setVisibility(View.GONE);
                    holder.ll_name.setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(msgEntity.getInfoName()))
                        holder.tv_msg_content.setText(msgEntity.getInfoName());
                    else
                        holder.tv_msg_content.setText("");
                    break;
                case 3://文章评论、回复
                case 4://帖子评论、回复
                    holder.tv_hint.setVisibility(View.GONE);
                    holder.ll_name.setVisibility(View.VISIBLE);
                    if (msgEntity.getTypeContent() != null) {
                        holder.tv_reply.setVisibility(View.VISIBLE);
                        holder.ll_be_replied_msg.setVisibility(View.VISIBLE);
                        SpannableStringBuilder ssb = new SpannableStringBuilder("");

                        if (!TextUtils.isEmpty(msgEntity.getTypeContent().getTouserid())) {
                            if (msgEntity.getTypeContent().getTouserid().equals("0")) {//别人回复自己
                                ssb.append("回复了你: ");
                            } else {//别人回复别人
                                String toUserName = msgEntity.getTypeContent().getTousername();
                                String temp = "回复了 " + toUserName + ": ";
                                ssb.append(temp);
                                if (!TextUtils.isEmpty(toUserName)) {
                                    ssb.setSpan(new ClickableSpan() {
                                        @Override
                                        public void onClick(View widget) {
                                            if (msgEntity.getRead().equals("0")) {
                                                setMsgClicked(msgEntity);
                                                holder.fl_msg.setSelected(true);
                                            }
                                            UiUtils.jumpToInfoCenter(context, msgEntity.getTypeContent().getTouserid());
                                        }

                                        @Override
                                        public void updateDrawState(TextPaint ds) {
                                            super.updateDrawState(ds);
                                            ds.setUnderlineText(false);// 设置文字下划线不显示
                                            ds.setColor(ContextCompat.getColor(context, R.color.text_blue));// 设置字体颜色
                                        }
                                    }, 4, 6 + toUserName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                                }

                            }

                        }

                        if (!TextUtils.isEmpty(msgEntity.getTypeContent().getMsg())) {//消息的内容（评论、回复）
                            ssb.append(msgEntity.getTypeContent().getMsg());
                        }
                        holder.tv_msg_content.setMovementMethod(LinkMovementMethod.getInstance());
                        holder.tv_msg_content.setText(ssb);

                        if (!TextUtils.isEmpty(msgEntity.getTypeContent().getTitle()))//文章/帖子的标题
                            holder.tv_be_replied_msg_content.setText(msgEntity.getTypeContent().getTitle());
                        else
                            holder.tv_be_replied_msg_content.setText("");
                        if (!TextUtils.isEmpty(msgEntity.getTypeContent().getPic())) {//文章封面图片
                            holder.iv_be_replied_msg.setVisibility(View.VISIBLE);
                            Glide.with(context).load(msgEntity.getTypeContent().getPic()).signature(new IntegerVersionSignature(1))
                                    .error(R.mipmap.default_avatar)
                                    .into(holder.iv_be_replied_msg);
                        } else {
                            holder.iv_be_replied_msg.setVisibility(View.GONE);
                        }
                        if (msgEntity.getType() == 3 && !TextUtils.isEmpty(msgEntity.getTypeContent().getAid())) {//文章跳转
                            holder.ll_be_replied_msg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (msgEntity.getRead().equals("0")) {
                                        setMsgClicked(msgEntity);
                                        holder.fl_msg.setSelected(true);
                                    }
                                    UiUtils.jumpToArticleDetail(context, msgEntity.getTypeContent().getAid());
                                }
                            });

                        } else if (msgEntity.getType() == 4 && !TextUtils.isEmpty(msgEntity.getTypeContent().getPid())) {//帖子跳转
                            holder.ll_be_replied_msg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (msgEntity.getRead().equals("0")) {
                                        setMsgClicked(msgEntity);
                                        holder.fl_msg.setSelected(true);
                                    }
                                    UiUtils.jumpToForumDetail(context, msgEntity.getTypeContent().getPid(), "");
                                }
                            });

                        } else {//不跳转
                            holder.ll_be_replied_msg.setOnClickListener(null);
                        }
                    } else {
                        holder.tv_msg_content.setText("");
                        holder.ll_be_replied_msg.setVisibility(View.GONE);
                        holder.tv_reply.setVisibility(View.GONE);
                    }
                    break;
                case 5://有人在帖子里@了自己
                    holder.tv_hint.setVisibility(View.GONE);
                    holder.tv_reply.setVisibility(View.GONE);
                    holder.tv_msg_content.setText("在帖子中提到了你");
                    if (msgEntity.getTypeContent() != null) {
                        holder.ll_be_replied_msg.setVisibility(View.VISIBLE);
                        if (!TextUtils.isEmpty(msgEntity.getTypeContent().getTitle()))
                            holder.tv_be_replied_msg_content.setText(msgEntity.getTypeContent().getTitle());
                        else
                            holder.tv_be_replied_msg_content.setText("");

                        holder.iv_be_replied_msg.setVisibility(View.GONE);

                        if (!TextUtils.isEmpty(msgEntity.getTypeContent().getPid())) {//帖子跳转
                            holder.ll_be_replied_msg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (msgEntity.getRead().equals("0")) {
                                        setMsgClicked(msgEntity);
                                        holder.fl_msg.setSelected(true);
                                    }
                                    UiUtils.jumpToForumDetail(context, msgEntity.getTypeContent().getPid(), "");
                                }
                            });

                        } else {//不跳转
                            holder.ll_be_replied_msg.setOnClickListener(null);
                        }
                    } else {
                        holder.ll_be_replied_msg.setVisibility(View.GONE);
                    }
                    break;
                case 99://官方推送的消息
                    holder.tv_hint.setVisibility(View.GONE);
                    holder.tv_reply.setVisibility(View.GONE);
                    holder.ll_be_replied_msg.setVisibility(View.GONE);
                    holder.ll_name.setVisibility(View.VISIBLE);
                    holder.v_tag.setVisibility(View.VISIBLE);
                    if (msgEntity.getTypeContent() != null && !TextUtils.isEmpty(msgEntity.getTypeContent().getMsg())) {
                        final UMessage msg = new UMessage(new JSONObject(msgEntity.getTypeContent().getMsg()));
                        String mText = msg.text;
                        if (!TextUtils.isEmpty(mText))
                            holder.tv_msg_content.setText(mText);
                        else holder.tv_msg_content.setText("");
                        Map<String, String> map = msg.extra;
                        if (map != null && !TextUtils.isEmpty(map.get("jump"))) {
                            String temp = map.get("jump");
                            final int t = Integer.valueOf(temp);
                            if (0 == t || 8 == t) {
                                holder.fl_msg.setClickable(false);
                            } else {
                                holder.fl_msg.setClickable(true);
                                holder.fl_msg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (msgEntity.getRead().equals("0")) {
                                            PushNewEntity entity = new PushNewEntity();
                                            entity.setMsg(msgEntity.getTypeContent().getMsg());
                                            entity.setFlag(msgEntity.getFlag());
                                            entity.setTag(msgEntity.getTag());
                                            entity.setTime(Long.valueOf(msgEntity.getTime()));
                                            DBHelper.getInstance().updataPushTable(ZYApplication.application, entity);
                                            msgEntity.setRead("1");
                                            notifyDataSetChanged();
                                        }
                                        callback.toJump(t, msg);
                                    }
                                });

                            }
                        } else {
                            holder.fl_msg.setClickable(false);
                        }

                    } else
                        holder.tv_msg_content.setText("");

                    break;
                default://未知类型的消息
                    holder.tv_hint.setVisibility(View.GONE);
                    holder.tv_reply.setVisibility(View.GONE);
                    holder.ll_be_replied_msg.setVisibility(View.GONE);
                    holder.ll_name.setVisibility(View.VISIBLE);
                    if (msgEntity.getTypeContent() != null && !TextUtils.isEmpty(msgEntity.getTypeContent().getMsg()))
                        holder.tv_msg_content.setText(msgEntity.getTypeContent().getMsg());
                    else
                        holder.tv_msg_content.setText("");
                    break;
            }
            if (holder.tv_reply.getVisibility() == View.VISIBLE) {
                holder.tv_reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (msgEntity.getRead().equals("0")) {
                            setMsgClicked(msgEntity);
                            holder.fl_msg.setSelected(true);
                        }
                        if (onReplyListener != null) {
                            onReplyListener.onReply(msgEntity);
                        }
                    }
                });
            } else {
                holder.tv_reply.setOnClickListener(null);
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "MsgAdapter onBindViewHolder");
        }
    }

    private void setMsgClicked(MsgEntity msgEntity) {

        msgEntity.setRead("1");
        MsgClickEntity msgClickEntity = new MsgClickEntity();
        msgClickEntity.setMsgid(msgEntity.getMsgid());
        msgClickEntity.setRead("1");
        DBHelper.getInstance().updateMsgClickTable(ZYApplication.application, msgClickEntity);

    }

    @Override
    public int getAdapterItemCount() {
        return msgEntityList.size();
    }

    class MsgViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_time;
        private FrameLayout fl_msg;
        private CircleImageView civ_avatar;
        private LinearLayout ll_name;
        private TextView tv_info_name;
        private View v_tag;//用户的标志用来区别官方和普通用户
        private TextView tv_reply;//回复按钮
        private TextView tv_msg_content;
        private TextView tv_hint;
        private LinearLayout ll_be_replied_msg;
        private ImageView iv_be_replied_msg;
        private TextView tv_be_replied_msg_content;

        private MsgViewHolder(View itemView, boolean isItem) {
            super(itemView);
            initView(itemView, isItem);
        }

        private void initView(View itemView, boolean isItem) {
            if (isItem) {
                tv_time = (TextView) itemView.findViewById(R.id.tv_time);
                fl_msg = (FrameLayout) itemView.findViewById(R.id.fl_msg);
                civ_avatar = (CircleImageView) itemView.findViewById(R.id.civ_avatar);
                ll_name = (LinearLayout) itemView.findViewById(R.id.ll_name);
                tv_info_name = (TextView) itemView.findViewById(R.id.tv_info_name);
                v_tag = itemView.findViewById(R.id.v_tag);
                tv_reply = (TextView) itemView.findViewById(R.id.tv_reply);
                tv_msg_content = (TextView) itemView.findViewById(R.id.tv_msg_content);
                tv_hint = (TextView) itemView.findViewById(R.id.tv_hint);
                ll_be_replied_msg = (LinearLayout) itemView.findViewById(R.id.ll_be_replied_msg);
                iv_be_replied_msg = (ImageView) itemView.findViewById(R.id.iv_be_replied_msg);
                tv_be_replied_msg_content = (TextView) itemView.findViewById(R.id.tv_be_replied_msg_content);
            }
        }
    }
}
