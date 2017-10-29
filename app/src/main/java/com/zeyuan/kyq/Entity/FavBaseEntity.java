package com.zeyuan.kyq.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/18.
 *
 *
 * @author wwei
 */
public class FavBaseEntity implements Serializable{

    private String iResult;
    private List<FavEntity> data;

    public String getiResult() {
        return iResult;
    }

    public List<FavEntity> getData() {
        return data;
    }

    public void setData(List<FavEntity> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FavBaseEntity{" +
                "iResult='" + iResult + '\'' +
                ", data=" + data +
                '}';
    }
}
