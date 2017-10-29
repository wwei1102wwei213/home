package com.zeyuan.kyq.biz.forcallback;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/4.
 *
 * 编辑输入返回接口
 *
 * @author wwei
 */
public interface EditBackInterface extends Serializable{
    void editCallBack(String text);
}
