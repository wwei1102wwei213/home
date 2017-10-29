package com.zeyuan.kyq.biz;

import android.graphics.Bitmap;

import com.ta.common.AsyncTask;
import com.zeyuan.kyq.utils.BlurUtil.BlurColor;

/**
 * Created by Administrator on 2016/5/26.
 *
 * 模糊背景处理
 *
 * @author wwei
 */
public class BlurBiz extends AsyncTask<Void,Void,Bitmap>{

    private BlurInterface callback;
    private BlurColor bc;

    public BlurBiz(BlurInterface callback,BlurColor bc){
        this.callback = callback;
        this.bc = bc;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        bc.initBlur();
        Bitmap bmp = bc.getBitmap();
        return bmp;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        callback.setBlur(bitmap);
    }

    public interface BlurInterface{
        void setBlur(Bitmap bitmap);
    }
}
