package com.zeyuan.kyq.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseFragment;

/**
 * Created by Administrator on 2016/7/26.
 *
 * 没有数据窗口
 *
 * @author wwei
 */
public class EmptyPageFragment extends BaseFragment{

    private int resId = -1;
    private String hint;
    private String btnText;
    private EmptyClickListener lintener;
    private boolean showBtn = true;
    private int tag = 0;

    public void setShowBtn(boolean showBtn) {
        this.showBtn = showBtn;
    }

    public void setEmptyClickListener(EmptyClickListener lintener){
        this.lintener = lintener;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setBtnText(String btnText) {
        this.btnText = btnText;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_empty_page,container,false);
        initView();

        return rootView;
    }

    private void initView(){

        ImageView iv = (ImageView)findViewById(R.id.iv_empty_photo);
        TextView tv_hint = (TextView)findViewById(R.id.tv_empty_hint);
        TextView tv_btn = (TextView)findViewById(R.id.tv_empty_btn);

        if (resId!=-1){
            iv.setImageResource(resId);
        }

        if (!TextUtils.isEmpty(hint)){

            tv_hint.setText(hint);
        }

        if(showBtn){
            if (!TextUtils.isEmpty(btnText)){
                tv_btn.setText(btnText);
                tv_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lintener.onEmptyClickListener();
                    }
                });
            }
        }else {
            tv_btn.setVisibility(View.GONE);
        }



    }

    public interface EmptyClickListener{
         void onEmptyClickListener();
    }

}
