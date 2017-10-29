package com.zeyuan.kyq.fragment.msg;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.MsgClickEntity;
import com.zeyuan.kyq.Entity.MsgEntity;
import com.zeyuan.kyq.Entity.PushNewEntity;
import com.zeyuan.kyq.Entity.TypeContent;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.MsgAdapter;
import com.zeyuan.kyq.app.LazyFragment;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.bean.MsgBean;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.bean.ReplyForum;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.OnReplyListener;
import com.zeyuan.kyq.biz.manager.IntegrationManager;
import com.zeyuan.kyq.db.DBHelper;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ConstUtils;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DecryptUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.Secret.HttpSecretUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.IntegrationPopupWindow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zeyuan.kyq.utils.Contants.index;
import static com.zeyuan.kyq.utils.Contants.toinfoid;
import static com.zeyuan.kyq.utils.Contants.touser;

/**
 * Created by Administrator on 2017/7/4.
 * 消息列表页面
 */

public class MsgFragment extends LazyFragment implements HttpResponseInterface {
    private int type;//0 所有消息，1 @我的，2 评论 ，3 粉丝， 4 点赞
    private int page = 0;//数据页码
    //推送的消息
    List<MsgEntity> pushMsgData;
    private XRefreshView xrv_msg;
    private boolean loading = false;
    private List<MsgEntity> msgEntityList = new ArrayList<>();
    private MsgAdapter msgAdapter;
    private TextView tv_empty_view;
    //回复的输入框视图
    private MsgEntity beReplyMsgEntity;
    private LinearLayout v_edit;
    TextView btn_no_dialog;
    private TextView tv_edit_title;//回复的对象的名字
    TextView send_message;
    private EditText edit_txt;

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_msg_list, container, false);
        xrv_msg = (XRefreshView) findViewById(R.id.xrv_msg);
        RecyclerView rcv_msg = (RecyclerView) findViewById(R.id.rcv_msg);
        tv_empty_view = (TextView) findViewById(R.id.tv_empty_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rcv_msg.setLayoutManager(layoutManager);
        rcv_msg.setHasFixedSize(true);
        msgAdapter = new MsgAdapter(getActivity(), msgEntityList);
        msgAdapter.setOnReplyListener(new OnReplyListener() {
            @Override
            public void onReply(MsgEntity msgEntity) {
                showReplyView(msgEntity);
            }
        });
        rcv_msg.setAdapter(msgAdapter);
        xrv_msg.setPinnedTime(1000);
        xrv_msg.setPullRefreshEnable(false);
        xrv_msg.setPullLoadEnable(true);
        xrv_msg.setMoveForHorizontal(true);
        msgAdapter.setCustomLoadMoreView(new XRefreshViewFooter(context));
        xrv_msg.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        loading = true;
                        page++;
                        Factory.postPhp(MsgFragment.this, Const.PApi_ShowInfoMsg);

                    }
                }, 1000);
            }
        });
        v_edit = (LinearLayout) findViewById(R.id.v_edit);
        btn_no_dialog = (TextView) findViewById(R.id.btn_no_dialog);
        tv_edit_title = (TextView) findViewById(R.id.tv_edit_title);
        send_message = (TextView) findViewById(R.id.send_message);
        edit_txt = (EditText) findViewById(R.id.edit_txt);
        findViewById(R.id.v_edit_empty_place).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v_edit.setVisibility(View.GONE);
                switchInput(false);
            }
        });
        btn_no_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v_edit.setVisibility(View.GONE);
                switchInput(false);
            }
        });
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edit_txt.getText().toString().trim())) {
                    showToast("请输入评论内容！");
                } else if (beReplyMsgEntity != null && beReplyMsgEntity.getType() == 3) {
                    Factory.postPhp(MsgFragment.this, Const.PAddPorComment);
                } else if (beReplyMsgEntity != null && beReplyMsgEntity.getType() == 4) {
                    Factory.post(MsgFragment.this, Const.EReplyForum);
                }
                send_message.setEnabled(false);
            }
        });
        xrv_msg.setStateCallback(new XRefreshView.XRefreshViewScrollStateChangedCallback() {
                                     @Override
                                     public void XRVScrollStateChangedCallback(int scrollState) {
                                         try {
                                             if (scrollState == 2) {
                                                 Glide.with(context).pauseRequests();
                                             } else {
                                                 Glide.with(context).resumeRequests();
                                             }
                                         } catch (Exception e) {
                                             LogCustom.e("xrv_msg.setStateCallback", e.toString());
                                         }

                                     }
                                 }

        );

        return rootView;
    }

    @Override
    protected void initData() {
        Bundle arguments = getArguments();
        type = arguments.getInt("type");
        if (type == 0) {//全部消息页面,添加本地记录的推送消息
            initPushMsgData();
        }
        page = 0;
        Factory.postPhp(this, Const.PApi_ShowInfoMsg);


    }

    @Override
    protected void setDefaultFragmentTitle(String title) {

    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> params = new HashMap<>();
        if (tag == Const.PApi_ShowInfoMsg) {
            params.put("InfoID", UserinfoData.getInfoID(getActivity().getApplicationContext()));//155 208490 UserinfoData.getInfoID(getActivity().getApplicationContext())
            params.put("page", page + "");
            params.put("type", type + "");
        } else if (tag == Const.PAddPorComment) {
            params.put(Contants.InfoID, UserinfoData.getInfoID(getActivity().getApplicationContext()));
            params.put(Contants.InfoName, UserinfoData.getInfoname(getActivity().getApplicationContext()));
            params.put("ArtID", beReplyMsgEntity.getTypeContent().getAid());
            params.put("MessAge", edit_txt.getText().toString().trim());
            if (!TextUtils.isEmpty(beReplyMsgEntity.getTypeContent().getGroupid())) {
                params.put("ToUserID", beReplyMsgEntity.getInfoId());
                params.put("ToUserName", beReplyMsgEntity.getInfoName());
                params.put("GroupID", beReplyMsgEntity.getTypeContent().getGroupid());
            }

        }
        return params;
    }

    @Override
    public byte[] getPostParams(int flag) {
        String[] args = null;
        if (flag == Const.EReplyForum) {

            List<String> list = new ArrayList<>();
            list.add(Contants.InfoID);
            list.add(UserinfoData.getInfoID(getActivity().getApplicationContext()));
            list.add(index);
            list.add(beReplyMsgEntity.getTypeContent().getPid());
            list.add(Contants.fromuser);
            list.add(UserinfoData.getInfoname(getActivity().getApplicationContext()));
            list.add(Contants.Content);
            String decodeContetn = DecryptUtils.encode(edit_txt.getText().toString().trim());
            list.add(decodeContetn.trim());
            if (!TextUtils.isEmpty(beReplyMsgEntity.getInfoName())) {
                list.add(touser);
                list.add(beReplyMsgEntity.getInfoName());
            }
            if (!TextUtils.isEmpty(beReplyMsgEntity.getInfoId())) {
                list.add(toinfoid);
                list.add(beReplyMsgEntity.getInfoId());
            }

            args = ConstUtils.getParamsForList(list);

        }
        return HttpSecretUtils.getParamString(args);
    }

    @Override
    public void toActivity(Object response, int flag) {
        try {
            if (flag == Const.PApi_ShowInfoMsg) {
                MsgBean msgBean = (MsgBean) response;
                if (msgBean.getiResult().equals(Const.RESULT)) {
                    if (msgBean.getData() != null && msgBean.getData().size() > 0) {
                        overLoading(0);
                        dealWithData(msgBean.getData());
                    } else {
                        overLoading(2);
                    }
                } else {
                    overLoading(1);
                }
            } else if (flag == Const.PAddPorComment) {
                PhpUserInfoBean bean = (PhpUserInfoBean) response;
                if (Const.RESULT.equals(bean.getiResult())) {
                    showToast("回复成功");
                    //清空输入框
                    clearReplyData();
                    //收起输入法
                    switchInput(false);
                    //隐藏输入框
                    v_edit.setVisibility(View.GONE);
                    String jfString;
                    if (!TextUtils.isEmpty(bean.getJf()) && !bean.getJf().equals("0")) {
                        jfString = "评论成功 +" + bean.getJf() + "积分";
                        IntegrationManager.getInstance().addIntegration(bean.getJf());
                    } else {
                        jfString = "评论成功 已达每日积分上限";
                    }
                    IntegrationPopupWindow integrationPopupWindow = new IntegrationPopupWindow(getActivity(), jfString);
                    integrationPopupWindow.showPopupWindow(getActivity());

                } else {
                    showToast("回复失败！");
                }
                send_message.setEnabled(true);
            } else if (flag == Const.EReplyForum) {
                ReplyForum replyForum = (ReplyForum) response;
                if (Contants.OK_DATA.equals(replyForum.getIResult())) {
                    showToast("回复成功");
                    //清空输入数据
                    clearReplyData();
                    //收起输入法 刷新帖子
                    switchInput(false);
                    //隐藏输入框
                    v_edit.setVisibility(View.GONE);
                    String jfString;
                    if (!TextUtils.isEmpty(replyForum.getJf()) && !replyForum.getJf().equals("0")) {
                        jfString = "回帖成功 +" + replyForum.getJf() + "积分";
                        IntegrationManager.getInstance().addIntegration(replyForum.getJf());
                    } else {
                        jfString = "回帖成功 已达每日积分上限";
                    }
                    IntegrationPopupWindow integrationPopupWindow = new IntegrationPopupWindow(getActivity(), jfString);
                    integrationPopupWindow.showPopupWindow(getActivity());

                    Factory.postPhp(MsgFragment.this, Const.PApi_getReplyList_2);
                } else {
                    showToast("回复失败！");
                }
                send_message.setEnabled(true);
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "");
        }
    }

    @Override
    public void showLoading(int flag) {

    }

    @Override
    public void hideLoading(int flag) {

    }

    @Override
    public void showError(int flag) {

        if (flag == Const.PApi_ShowInfoMsg) {
            overLoading(1);
        } else {
            send_message.setEnabled(true);
            showToast("回复失败！");
        }

    }

    private void overLoading(int errorCode) {
        if (errorCode == 0) {
            if (loading) {
                xrv_msg.stopLoadMore();
            }
        } else if (errorCode == 1) {
            if (loading) {
                page--;
                xrv_msg.stopLoadMore();
            }
        } else if (errorCode == 2) {
            if (loading) {
                page--;
                xrv_msg.setLoadComplete(true);
            }
        }
        if (loading) loading = false;
    }

    List<MsgClickEntity> newMsgClickEntity = new ArrayList<>();//新添加的消息点击数据

    //处理获取到的消息数据
    private void dealWithData(List<MsgEntity> data) {
        setClickStatus(data);
        sortMsgData(data);
    }

    //设置数据的点击状态
    private void setClickStatus(List<MsgEntity> data) {
        List<MsgClickEntity> msgClickEntityList = DBHelper.getInstance().queryMsgClick(ZYApplication.application);
        if (newMsgClickEntity != null) {
            newMsgClickEntity.clear();
        } else {
            newMsgClickEntity = new ArrayList<>();
        }
        if (msgClickEntityList != null && msgClickEntityList.size() > 0) {
            for (MsgEntity msgEntity : data) {
                for (int i = 0; i < msgClickEntityList.size(); i++) {
                    MsgClickEntity msgClickEntity = msgClickEntityList.get(i);
                    if (msgEntity.getMsgid() == msgClickEntity.getMsgid()) {
                        msgEntity.setRead(msgClickEntity.getRead());
                        break;
                    } else if (i == msgClickEntityList.size() - 1) {//本地没保存本消息的点击记录
                        LogCustom.i("zys", "本地没有保存该消息！msgid=" + msgEntity.getMsgid());
                        MsgClickEntity temp = new MsgClickEntity();
                        temp.setMsgid(msgEntity.getMsgid());
                        temp.setRead(msgEntity.getRead());
                        newMsgClickEntity.add(temp);
                    }
                }

            }
        } else {
            LogCustom.i("zys", "本地没有保存任何消息的点击记录！");
            for (MsgEntity msgEntity : data) {
                MsgClickEntity temp = new MsgClickEntity();
                temp.setMsgid(msgEntity.getMsgid());
                temp.setRead(msgEntity.getRead());
                newMsgClickEntity.add(temp);
            }
        }
        DBHelper.getInstance().insertMsgClickList(ZYApplication.application, newMsgClickEntity);//把新的点击记录插入数据库
    }

    //消息排序
    private void sortMsgData(List<MsgEntity> data) {
        if (type == 0) {//“全部”消息里添加有推送消息，需要重新排序
            msgEntityList.addAll(data);
            if (pushMsgData != null && pushMsgData.size() > 0) {//有推送消息
                Collections.sort(msgEntityList, new Comparator<MsgEntity>() {
                    @Override
                    public int compare(MsgEntity p1, MsgEntity p2) {
                        return (int) (Long.valueOf(p2.getTime()) - Long.valueOf(p1.getTime()));
                    }
                });
            }

        } else {//非“全部”消息页面
            if (page == 0) {//加载第一页数据，清空列表
                msgEntityList.clear();
                msgEntityList.addAll(data);
            } else {//加载非第一页数据
                msgEntityList.addAll(data);
            }
        }
        msgAdapter.notifyDataSetChanged();
        if (msgEntityList.size() == 0) {
            //显示没有消息textView
            tv_empty_view.setVisibility(View.VISIBLE);
        } else {
            //隐藏没有消息textView
            tv_empty_view.setVisibility(View.GONE);
        }
    }

    //初始化推送数据
    private void initPushMsgData() {
        pushMsgData = new ArrayList<>();
        List<PushNewEntity> pushData = DBHelper.getInstance().queryPush(ZYApplication.application);
        for (PushNewEntity pushNewEntity : pushData) {
            MsgEntity msgEntity = new MsgEntity();
            TypeContent content = new TypeContent();
            msgEntity.setType(99);
            msgEntity.setInfoName("抗癌圈");
            msgEntity.setRead(pushNewEntity.getRead() + "");
            msgEntity.setTime(pushNewEntity.getTime() + "");
            content.setMsg(pushNewEntity.getMsg());
            msgEntity.setTypeContent(content);
            msgEntity.setTag(pushNewEntity.getTag());
            msgEntity.setFlag(pushNewEntity.getFlag());
            pushMsgData.add(msgEntity);
        }
        msgEntityList.clear();
        msgEntityList.addAll(pushMsgData);
        if (msgEntityList.size() > 1) {
            Collections.sort(msgEntityList, new Comparator<MsgEntity>() {
                @Override
                public int compare(MsgEntity p1, MsgEntity p2) {
                    return (int) (Long.valueOf(p2.getTime()) - Long.valueOf(p1.getTime()));
                }
            });
        }
        if (msgEntityList.size() == 0) {
            //显示没有消息textView
            tv_empty_view.setVisibility(View.VISIBLE);
        } else {
            //隐藏没有消息textView
            tv_empty_view.setVisibility(View.GONE);
        }
        msgAdapter.notifyDataSetChanged();
    }

    //显示评论输入框
    private void showReplyView(MsgEntity beReplyMsgEntity) {
        if (beReplyMsgEntity != null) {
            this.beReplyMsgEntity = beReplyMsgEntity;
            v_edit.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(beReplyMsgEntity.getInfoName())) {
                tv_edit_title.setText("回复: " + beReplyMsgEntity.getInfoName() + "");
            } else {
                tv_edit_title.setText("回复: ");
            }
            edit_txt.requestFocus();
            switchInput(true);
        } else {
            switchInput(false);
            v_edit.setVisibility(View.GONE);
        }
    }

    private void clearReplyData() {
        tv_edit_title.setText("");
        edit_txt.setText("");
        beReplyMsgEntity = null;
    }

    //开关键盘
    private void switchInput(boolean show) {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                if (show) {
                    imm.showSoftInput(edit_txt, 0);
                } else {
                    imm.hideSoftInputFromWindow(edit_txt.getWindowToken(), 0);
                }
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "输入法开关错误");
        }
    }
}
