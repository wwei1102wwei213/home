package com.zeyuan.kyq.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.HomePageEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseZyFragment;
import com.zeyuan.kyq.biz.forcallback.FragmentCallBack;
import com.zeyuan.kyq.biz.manager.ClickStatisticsManager;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.UiUtils;

/**
 * Created by Administrator on 2016/12/30.
 * 首页广告弹窗的内容页
 *
 * @author wwei
 */
public class HomeBannerFragment extends BaseZyFragment {

    public static final String IMG_URL = "image_url";
    public static final String BANNER_DATA = "banner_data";
    public static final String BANNER_CALLBACK = "banner_callback";
    private FragmentCallBack callback;

    public void setCallback(FragmentCallBack callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.item_home_banner_iv, container, false);
        Bundle bundle = getArguments();
        final HomePageEntity entity = (HomePageEntity) bundle.getSerializable(BANNER_DATA);
        ImageView iv = (ImageView) rootView.findViewById(R.id.iv);
        if (entity == null) {
            iv.setImageResource(R.mipmap.loading_fail);
            return rootView;
        }


        try {
            if (!TextUtils.isEmpty(entity.getPic_oss()) && context != null) {
                try {
                    Glide.with(context).load(entity.getPic_oss()).error(R.mipmap.loading_fail)
                            .signature(new IntegerVersionSignature(1)).into(iv);
                    LogCustom.i("ZYS", "IMG:" + entity.getPic_oss());
                } catch (Exception e) {
                    ExceptionUtils.ExceptionSend(e, "Glide HomeBannerFragment");
                }
            } else {
                iv.setImageResource(R.mipmap.loading_fail);
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "HomeBannerFragment");
        }

//        if (!TextUtils.isEmpty(entity.getTagtype())) {
//            final int type = Integer.valueOf(entity.getTagtype());

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  String tempUrl = entity.getTagurl();
                try{
                    ClickStatisticsManager.getInstance().addClickEvent(Const.CLICK_EVENT_1, entity.getId());
                    UiUtils.toMenuJump(context, entity, null, false, null);
                }catch (Exception e){
                    ExceptionUtils.ExceptionSend(e,"HomeBannerFragment");
                }

//                    if (!TextUtils.isEmpty(tempUrl)) {
//                        switch (type) {
//                            case 1:
//                                if (!TextUtils.isEmpty(entity.getId())) {
//                                    int id = Integer.valueOf(entity.getId());
//                                    switch (id) {
//                                        case 4:
//                                        case 5:
//                                        case 6:
//                                            break;
//                                        default:
//                                            if (tempUrl.contains("?")) {
//                                                tempUrl += "&kaq=" + getRandomMath()
//                                                        + UserinfoData.getInfoID(getActivity()) + "&lt=2&Type=2";
//                                            } else {
//                                                tempUrl += "?kaq=" + getRandomMath()
//                                                        + UserinfoData.getInfoID(getActivity()) + "&lt=2&Type=2";
//                                            }
//                                            Factory.onEvent(getActivity(), Const.EVENT_MainTopBanner,
//                                                    Const.EVENTFLAG, "{type:" + type + ";where:main;url:" + tempUrl + "}");
//                                            startActivity(new Intent(getActivity(), ShowDiscuzActivity.class)
//                                                    .putExtra(Const.SHOW_HTML_MAIN_TOP, tempUrl));
//                                            break;
//                                    }
//                                }
//                                break;
//                            case 2:
//                                if (!TextUtils.isEmpty(entity.getTagurl())) {
//                                    Factory.onEvent(getActivity(), Const.EVENT_MainTopBanner,
//                                            Const.EVENTFLAG, "{type:" + type + ";DISCUZ_ID:" + entity.getTagurl() + "}");
//                                    startActivity(new Intent(getActivity(), ArticleDetailActivity.class).
//                                            putExtra(Const.INTENT_ARTICLE_ID, entity.getTagurl()));
//                                }
//                                break;
//                            case 3:
//                                if (!TextUtils.isEmpty(entity.getTagurl())) {
//                                    Factory.onEvent(getActivity(), Const.EVENT_MainTopBanner,
//                                            Const.EVENTFLAG, "{type:" + type + ";FORUM_ID:" + entity.getTagurl() + "}");
//                                    startActivity(new Intent(getActivity(), ForumDetailActivity.class)
//                                            .putExtra(Const.FORUM_ID, entity.getTagurl()));
//                                }
//                                break;
//                            case 4:
//                                Intent intent = new Intent(getActivity(), HeadLineActivity.class);
//                                if (!TextUtils.isEmpty(entity.getInfotext()) && !TextUtils.isEmpty(entity.getTagurl())) {
//                                    Factory.onEvent(getActivity(), Const.EVENT_MainTopBanner,
//                                            Const.EVENTFLAG, "{type:" + type + ";url:" + entity.getTagurl()
//                                                    + ";infotext:" + entity.getInfotext() + "}");
//                                    intent.putExtra(Const.HEAD_LIST_TAG_URL, entity.getTagurl())
//                                            .putExtra(Const.HEAD_LIST_INFO_TEXT, entity.getInfotext());
//                                }
//                                startActivity(intent);
//                                break;
//                            case 5:
//                                Factory.onEvent(getActivity(), Const.EVENT_MainTopBanner,
//                                        Const.EVENTFLAG, "{type:" + type + ";CircleID:" + entity.getTagurl() + "}");
//                                startActivity(new Intent(getActivity(), NewCircleActivity.class)
//                                        .putExtra(Contants.CircleID, entity.getTagurl()));
//                                break;
//                        }

                //  }
            }
        });

            /*switch (type){
                case 1:
                    if(!TextUtils.isEmpty(entity.getId())){
                        int id = Integer.valueOf(entity.getId());
                        if(id==4){

                        }else if(id==5){

                        }else if(id==6){

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
                                        Factory.onEvent(getActivity(), Const.EVENT_MainTopBanner,
                                                Const.EVENTFLAG, "{type:" + type + ";where:main;url:" + tempUrl + "}");
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
            }*/
        //}

        return rootView;
    }

    private String getRandomMath() {
        String temp = (int) (Math.random() * 89999 + 10000) + "";
        return temp;
    }

}
