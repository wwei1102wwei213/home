package com.zeyuan.kyq.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.MainBannerEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseZyFragment;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.ArticleDetailActivity;
import com.zeyuan.kyq.view.ForumDetailActivity;
import com.zeyuan.kyq.view.HeadLineActivity;
import com.zeyuan.kyq.view.MainActivity;
import com.zeyuan.kyq.view.NewCircleActivity;
import com.zeyuan.kyq.view.ShowDiscuzActivity;

/**
 * Created by Administrator on 2016/5/26.
 *
 *
 *
 * @author wwei
 */
public class TypeImgFragment extends BaseZyFragment{

    public static final String IMG_URL = "image_url";
    public static final String BANNER_DATA = "banner_data";
    public static final String BANNER_CALLBACK = "banner_callback";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.item_page_type_img,container,false);
        Bundle bundle = getArguments();
        final MainBannerEntity entity = (MainBannerEntity)bundle.getSerializable(BANNER_DATA);
        if (entity == null) return rootView;
        ImageView iv = (ImageView)rootView.findViewById(R.id.iv_banner_bg);
        try {
            if(!TextUtils.isEmpty(entity.getImgurl())&&context!=null){
                Glide.with(context).load(entity.getImgurl())
                        .signature(new IntegerVersionSignature(1)).into(iv);
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"TypeImgFragment");
        }

        if(!TextUtils.isEmpty(entity.getTagtype())){
            final int type = Integer.valueOf(entity.getTagtype());
            switch (type){
                case 1:
                    if(!TextUtils.isEmpty(entity.getId())){
                        int id = Integer.valueOf(entity.getId());
                        if(id==4){
                            if(!TextUtils.isEmpty(entity.getInfotext())){
                                TextView tv_gonggao = (TextView)rootView.findViewById(R.id.tv_gonggao_main_top);
                                tv_gonggao.setVisibility(View.VISIBLE);
                                tv_gonggao.setText(entity.getInfotext());
                            }
                        }else if(id==5){
                            if(!TextUtils.isEmpty(UserinfoData.planId)){
                                TextView tv_plan = (TextView)rootView.findViewById(R.id.tv_plan_main_top);
                                tv_plan.setVisibility(View.VISIBLE);
                                String planID = UserinfoData.planId;
                                tv_plan.setText(MapDataUtils.getAllStepName(planID));
                                final TextView tv_now_drug = (TextView)rootView.findViewById(R.id.tv_now_drug);
                                tv_now_drug.setVisibility(View.VISIBLE);
                                tv_now_drug.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(!TextUtils.isEmpty(UserinfoData.planId)){
                                            ((MainActivity)TypeImgFragment.this.getActivity())
                                                    .setPlanDrug(UserinfoData.planId);
                                        }
                                    }
                                });
                            }else{
                                TextView tv_gonggao = (TextView)rootView.findViewById(R.id.tv_gonggao_main_top);
                                tv_gonggao.setVisibility(View.VISIBLE);
                                tv_gonggao.setText(entity.getInfotext());

                            }
                        }else if(id==6){
                            if(!TextUtils.isEmpty(UserinfoData.performid)){
                                TextView tv_plan = (TextView)rootView.findViewById(R.id.tv_plan_main_top);
                                tv_plan.setVisibility(View.VISIBLE);
                                final String performid = UserinfoData.performid;
                                tv_plan.setGravity(Gravity.LEFT);
                                tv_plan.setText(String.format(getString(R.string.advice_tips),
                                        MapDataUtils.getPerform(performid)));
                                final TextView tv_now_drug = (TextView)rootView.findViewById(R.id.tv_now_drug);
                                tv_now_drug.setVisibility(View.VISIBLE);
                                tv_now_drug.setText("查看解决办法");
                                tv_now_drug.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(!TextUtils.isEmpty(UserinfoData.performid)){
                                            ((MainActivity)TypeImgFragment.this.getActivity()).
                                                    setPerformChange(UserinfoData.performid);
                                        }
                                    }
                                });
                            }else{
                                TextView tv_gonggao = (TextView)rootView.findViewById(R.id.tv_gonggao_main_top);
                                tv_gonggao.setVisibility(View.VISIBLE);
                                tv_gonggao.setText(entity.getInfotext());
                            }

                        }else{
                            iv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String tempUrl = entity.getTagurl();
                                    if(!TextUtils.isEmpty(tempUrl)){

                                        if(tempUrl.contains("?")){
                                            tempUrl += "&kaq="+ getRandomMath() +
                                                    UserinfoData.getInfoID(getActivity()) + "&lt=2&Type=2";
                                        }else {
                                            tempUrl += "?kaq="+getRandomMath() +
                                                    UserinfoData.getInfoID(getActivity()) + "&lt=2&Type=2";
                                        }
                                        Factory.onEvent(getActivity(),Const.EVENT_MainTopBanner,
                                                Const.EVENTFLAG,"{type:"+type+";where:main;url:"+tempUrl+"}");
                                        startActivity(new Intent(getActivity(), ShowDiscuzActivity.class).
                                                putExtra(Const.SHOW_HTML_MAIN_TOP, tempUrl));
                                    }
                                }
                            });
                        }
                    }
                    break;
                case 2:
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!TextUtils.isEmpty(entity.getTagurl())){
                                Factory.onEvent(getActivity(),Const.EVENT_MainTopBanner,
                                        Const.EVENTFLAG,"{type:"+type+";DISCUZ_ID:"+entity.getTagurl()+"}");
                                startActivity(new Intent(getActivity(),ArticleDetailActivity.class).
                                        putExtra(Const.INTENT_ARTICLE_ID, entity.getTagurl()));
                            }
                        }
                    });
                    break;
                case 3:
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!TextUtils.isEmpty(entity.getTagurl())){
                                Factory.onEvent(getActivity(),Const.EVENT_MainTopBanner,
                                        Const.EVENTFLAG,"{type:"+type+";FORUM_ID:"+entity.getTagurl()+"}");
                                startActivity(new Intent(getActivity(), ForumDetailActivity.class)
                                        .putExtra(Const.FORUM_ID,entity.getTagurl()));
                            }
                        }
                    });
                    break;
                case 4:
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), HeadLineActivity.class);
                            if (!TextUtils.isEmpty(entity.getInfotext()) && !TextUtils.isEmpty(entity.getTagurl())) {
                                Factory.onEvent(getActivity(),Const.EVENT_MainTopBanner,
                                        Const.EVENTFLAG,"{type:"+type+";url:"+entity.getTagurl()
                                                +";infotext:"+entity.getInfotext()+"}");
                                intent.putExtra(Const.HEAD_LIST_TAG_URL, entity.getTagurl())
                                        .putExtra(Const.HEAD_LIST_INFO_TEXT, entity.getInfotext());
                            }
                            startActivity(intent);
                        }
                    });
                    break;
                case 5:
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Factory.onEvent(getActivity(),Const.EVENT_MainTopBanner,
                                    Const.EVENTFLAG,"{type:"+type+";CircleID:"+entity.getTagurl()+"}");
                            startActivity(new Intent(getActivity(), NewCircleActivity.class)
                                    .putExtra(Contants.CircleID, entity.getTagurl()));
                        }
                    });
                    break;
            }
        }

        return rootView;
    }

    private String getRandomMath(){
        String temp = (int)(Math.random()*89999 + 10000)+"";
        return temp;
    }

}
