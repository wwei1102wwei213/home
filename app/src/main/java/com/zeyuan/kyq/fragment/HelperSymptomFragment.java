package com.zeyuan.kyq.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseZyFragment;

/**
 * Created by Administrator on 2016/3/21.
 * 帮助页面fragment
 *
 * @author wwei
 */
public class HelperSymptomFragment extends BaseZyFragment{


    private int resoures_top;
    private int resoures_mid;
    private String title = "登录或注册";
    private String step = "步骤一：登录或注册";
    private String mstext ="登录或注册";

    public int getResoures_top() {
        return resoures_top;
    }

    public void setResoures_top(int resoures_top) {
        this.resoures_top = resoures_top;
    }

    public int getResoures_mid() {
        return resoures_mid;
    }

    public void setResoures_mid(int resoures_mid) {
        this.resoures_mid = resoures_mid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getMstext() {
        return mstext;
    }

    public void setMstext(String mstext) {
        this.mstext = mstext;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_help_symptom,container,false);

        /*try {

            ImageView iv_top = (ImageView)view.findViewById(R.id.iv_fragment_help_top);
//        iv_top.setImageResource(resoures_top);
            Glide.with(context).load(resoures_top).error(R.mipmap.loading_fail).into(iv_top);
            ImageView iv_mid = (ImageView)view.findViewById(R.id.iv_help_mid_img);
//        iv_mid.setImageResource(resoures_mid);
            Glide.with(context).load(resoures_mid).error(R.mipmap.loading_fail).into(iv_mid);
            TextView tv = (TextView)view.findViewById(R.id.tv_fragment_title);
            tv.setText(title);
            TextView tv1 = (TextView)view.findViewById(R.id.tv_help_text1);
            if("".equals(step)){
                tv1.setVisibility(View.GONE);
            }else{
                tv1.setVisibility(View.VISIBLE);
                tv1.setText(step);
            }
            TextView tv2 = (TextView)view.findViewById(R.id.tv_help_text2);
            if("".equals(mstext)){
                tv2.setVisibility(View.GONE);
            }else{
                tv2.setVisibility(View.VISIBLE);
                tv2.setText(mstext);
            }

        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"Helper");
        }*/
        return view;
    }
}
