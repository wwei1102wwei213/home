package com.zeyuan.kyq.fragment.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zeyuan.kyq.Entity.MainBannerEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.view.ServiceCenterActivity;
import com.zeyuan.kyq.widget.DrawCircleView;

import java.util.List;

/**
 * Created by Administrator on 2016/12/30.
 *
 *
 * @author wwei
 */
public class HomeBannerDialog extends Dialog {

    public HomeBannerDialog(Context context) {
        super(context);
    }

    public HomeBannerDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context context;
        private boolean cancelAble = true;
        private List<MainBannerEntity> list;
        private ViewPager vp_banner;
        private DrawCircleView dcv;
        private FragmentManager fm;
        public Builder(Context context){
            this.context = context;
        }

        public Builder setFm(FragmentManager fm) {
            this.fm = fm;
            return this;
        }

        public Builder setCancelAble(boolean cancelAble){
            this.cancelAble = cancelAble;
            return this;
        }

        public Builder setListData(List<MainBannerEntity> list){
            this.list = list;
            return this;
        }

        public HomeBannerDialog create(){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final HomeBannerDialog dialog = new HomeBannerDialog(context, R.style.zydialog);
            View v = inflater.inflate(R.layout.dialog_home_banner, null);
            dialog.addContentView(v, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            v.findViewById(R.id.v_more).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
//                        ((MainActivity)context).toEvent();
                        context.startActivity(new Intent(context, ServiceCenterActivity.class)
                                .putExtra(Const.FLAG_SERVICE_CENTER,1));
                    }catch (Exception e){
                        ExceptionUtils.ExceptionSend(e,"toEvent Click");
                    }
                    dialog.dismiss();
                }
            });

            v.findViewById(R.id.v_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            if (list!=null&&list.size()>0){
                /*vp_banner = (ViewPager)v.findViewById(R.id.vp_banner);
                dcv = (DrawCircleView)v.findViewById(R.id.dcv);
                ArrayList<Fragment> fragments = new ArrayList<>();
                HomeBannerFragment fragment;
                Bundle bundle;
                for(int i=0;i<list.size();i++){
                    fragment = new HomeBannerFragment();
                    fragment.setPageFlag(false);
                    MainBannerEntity entity = list.get(i);
                    bundle = new Bundle();
                    bundle.putSerializable(HomeBannerFragment.BANNER_DATA, entity);
                    fragment.setArguments(bundle);
                    fragments.add(fragment);
                }
                dcv.setDrawCricle(fragments.size(), 6, Color.parseColor("#4c4c4c"), Color.parseColor("#FFFFFF"));
                dcv.redraw(0);
                MyFragmentAdapter mAdapter = new MyFragmentAdapter(fm,fragments);
                vp_banner.setAdapter(mAdapter);
                vp_banner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        dcv.redraw(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });*/
            }

            if(cancelAble){
                dialog.setCancelable(true);
            }else{
                dialog.setCancelable(false);
            }

            dialog.setContentView(v);

            return dialog;
        }


    }

}
