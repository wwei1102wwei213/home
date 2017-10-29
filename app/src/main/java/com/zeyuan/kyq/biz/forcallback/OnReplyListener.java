package com.zeyuan.kyq.biz.forcallback;

import com.zeyuan.kyq.Entity.MsgEntity;

/**
 * Created by Administrator on 2017/7/20.
 * 消息中心回复别人的监听器
 */

public interface OnReplyListener {
    void onReply(MsgEntity msgEntity);
}
