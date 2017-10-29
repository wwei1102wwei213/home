package com.zeyuan.kyq.biz.forcallback;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/17.
 *
 * 立即用药回调接口
 *
 * @author wwei
 */
public interface PlanMainInterface extends Serializable{

    void PlanClickBack(String planID);

}
