package com.zeyuan.kyq.fragment.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.MainBannerEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseZyFragment;
import com.zeyuan.kyq.biz.manager.ClickStatisticsManager;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.UiUtils;

/**
 * Created by Administrator on 2016/9/6.
 *
 * @author wwei
 */
public class EventItemFragment extends BaseZyFragment {

    public static final String BANNER_DATA = "banner_data";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.item_page_type_img, container, false);
        Bundle bundle = getArguments();
        final MainBannerEntity entity = (MainBannerEntity) bundle.getSerializable(BANNER_DATA);
        if (entity == null) return rootView;
        ImageView iv = (ImageView) rootView.findViewById(R.id.iv_banner_bg);
        if (!TextUtils.isEmpty(entity.getImgurl())) {
            Glide.with(getActivity()).load(entity.getImgurl()).signature(new IntegerVersionSignature(1)).into(iv);
        }
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiUtils.toBannerJump(context, entity);
                ClickStatisticsManager.getInstance().addClickEvent(Const.CLICK_EVENT_1, entity.getId());
            }
        });
        /*if(!TextUtils.isEmpty(entity.getTagtype())){
            final int type = Integer.valueOf(entity.getTagtype());
            switch (type){
                case 1:
                    if(!TextUtils.isEmpty(entity.getId())){
                        int id = Integer.valueOf(entity.getId());

                            iv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String tempUrl = entity.getTagurl();
                                    if(!TextUtils.isEmpty(tempUrl)){
                                        if(tempUrl.contains("?")){
                                            tempUrl += "&kaq="+getRandomMath()+UserinfoData.getInfoID(getActivity());
                                        }else {
                                            tempUrl += "?kaq="+getRandomMath()+UserinfoData.getInfoID(getActivity());
                                        }
                                        Factory.onEvent(getActivity(), Const.EVENT_MainTopBanner,
                                                Const.EVENTFLAG, "{type:" + type + ";url:" + tempUrl + "}");
                                        startActivity(new Intent(getActivity(), ShowDiscuzActivity.class).
                                                putExtra(Const.SHOW_HTML_MAIN_TOP, tempUrl));
                                    }
                                }
                            });

                    }
                    break;
                case 2:
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!TextUtils.isEmpty(entity.getTagurl())){
                                Factory.onEvent(getActivity(),Const.EVENT_MainTopBanner,
                                        Const.EVENTFLAG,"{type:"+type+";DISCUZ_ID:"+entity.getTagurl()+"}");
                                startActivity(new Intent(getActivity(),ShowDiscuzActivity.class).
                                        putExtra(Const.INTENT_SHOW_DISCUZ_ID, entity.getTagurl()));
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
                                startActivity(new Intent(getActivity(), ForumDetailActivity.class).putExtra(Const.FORUM_ID,entity.getTagurl()));
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
                                        Const.EVENTFLAG,"{type:"+type+";url:"+entity.getTagurl()+";infotext:"+entity.getInfotext()+"}");
                                intent.putExtra(Const.HEAD_LIST_TAG_URL, entity.getTagurl()).putExtra(Const.HEAD_LIST_INFO_TEXT, entity.getInfotext());
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
                            startActivity(new Intent(getActivity(), NewCircleActivity.class).putExtra(Contants.CircleID, entity.getTagurl()));
                        }
                    });

                    break;
            }
        }*/

        return rootView;
    }

    private String getRandomMath() {
        String temp = (int) (Math.random() * 89999 + 10000) + "";
        return temp;
    }

}
