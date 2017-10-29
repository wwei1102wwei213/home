package com.zeyuan.kyq.biz.manager;

import android.text.TextUtils;

import com.zeyuan.kyq.utils.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/9.
 * 积分管理类
 */

public class IntegrationManager {
    private List<OnIntegrationChangedListener> listeners;
    private static IntegrationManager instance;

    //初始化
    public void init() {
        if (listeners == null) {
            listeners = new ArrayList<>();
        } else {
            listeners.clear();
        }

    }

    private int sum;//总积分

    public void setSum(int sum) {
        this.sum = sum;
        setChanged();
    }

    public String getSum() {
        if (sum == 0)
            return "";
        else
            return sum + "";
    }

    private IntegrationManager() {
        listeners = new ArrayList<>();
    }

    public void addOnIntegrationChangedListener(OnIntegrationChangedListener onIntegrationChangedListener) {
        if (listeners != null) {
            listeners.add(onIntegrationChangedListener);
        }
    }

    public static synchronized IntegrationManager getInstance() {
        if (instance == null)
            instance = new IntegrationManager();
        return instance;
    }

    //加积分
    public void addIntegration(String count) {
        try {
            if (!TextUtils.isEmpty(count)) {
                sum += Integer.valueOf(count);
                setChanged();
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "addIntegration");
        }

    }

    //设置积分变化
    private void setChanged() {
        try {
            if (listeners != null && listeners.size() > 0) {
                for (OnIntegrationChangedListener onIntegrationChangedListener : listeners) {
                    onIntegrationChangedListener.onIntegrationChanged(sum + "");
                }
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "setChanged");
        }

    }

    public void clearOnIntegrationChangedListener() {
        if (listeners != null && listeners.size() > 0) {
            listeners.clear();
        }
    }

    public interface OnIntegrationChangedListener {
        void onIntegrationChanged(String sum);
    }
}
