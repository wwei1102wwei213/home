package com.zeyuan.kyq.fragment.msg;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.MsgEntity;
import com.zeyuan.kyq.Entity.PushNewEntity;
import com.zeyuan.kyq.Entity.TypeContent;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.MsgAdapter;
import com.zeyuan.kyq.app.LazyFragment;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.db.DBHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2017/7/18.
 * 通过友盟手动推送的消息
 */

public class PushMsgFragment extends LazyFragment {
    private List<MsgEntity> pushMsgData = new ArrayList<>();
    private MsgAdapter msgAdapter;
    private TextView tv_empty_view;

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_push_msg, container, false);
        RecyclerView rcv_msg = (RecyclerView) findViewById(R.id.rcv_msg);
        tv_empty_view = (TextView) findViewById(R.id.tv_empty_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rcv_msg.setLayoutManager(layoutManager);
        rcv_msg.setHasFixedSize(true);
        msgAdapter = new MsgAdapter(getActivity(), pushMsgData);
        rcv_msg.setAdapter(msgAdapter);
        //        List<PushNewEntity> data = DBHelper.getInstance().queryPush(ZYApplication.application);
//        ListView lv = (ListView) findViewById(R.id.lv_news);
//        lv.setEmptyView(findViewById(R.id.tv_empty_view));
//        if (data != null && data.size() != 0) {
//            Collections.sort(data, new Comparator<PushNewEntity>() {
//                @Override
//                public int compare(PushNewEntity p1, PushNewEntity p2) {
//                    return (int) p2.getTime() - (int) p1.getTime();
//                }
//            });
//
//            SettingNewsAdapter adapter = new SettingNewsAdapter(getActivity(), data);
//            lv.setAdapter(adapter);
//            LogCustom.i("ZYS", "PUSH DATA:" + data.toString());
//
//        }
        return rootView;

    }

    @Override
    protected void initData() {
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
        if (pushMsgData != null && pushMsgData.size() > 0) {
            tv_empty_view.setVisibility(View.GONE);
            Collections.sort(pushMsgData, new Comparator<MsgEntity>() {
                @Override
                public int compare(MsgEntity p1, MsgEntity p2) {
                    return (int) (Long.valueOf(p2.getTime()) - Long.valueOf(p1.getTime()));
                }
            });
        } else {
            tv_empty_view.setVisibility(View.VISIBLE);
        }
        msgAdapter.notifyDataSetChanged();
    }

    @Override
    protected void setDefaultFragmentTitle(String title) {

    }

}
