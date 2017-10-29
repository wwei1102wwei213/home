package com.zeyuan.kyq.biz.forcallback;

import com.zeyuan.kyq.Entity.RecordItemEntity;

/**
 * Created by Administrator on 2017/3/14.
 */
public interface onEditItemListener {

    void onEditItem(RecordItemEntity entity,int position ,int itemType,boolean isChild,int childPos);

}
