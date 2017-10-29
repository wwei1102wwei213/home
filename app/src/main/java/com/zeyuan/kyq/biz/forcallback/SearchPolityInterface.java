package com.zeyuan.kyq.biz.forcallback;

import com.zeyuan.kyq.Entity.StepItemEntity;
import com.zeyuan.kyq.bean.PolicyDataEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/6/14.
 *
 *
 *
 * @author wwei
 */
public interface SearchPolityInterface {

    void toDrugDetail(StepItemEntity entity,int type);
    void toLookMore(List<PolicyDataEntity> list,String name,int type);
    void toResultDetail(Object entity,PolicyDataEntity pEntity,int type,int flag);

}
