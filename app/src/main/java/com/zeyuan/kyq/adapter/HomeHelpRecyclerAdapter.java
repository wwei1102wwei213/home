package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.Entity.HelpItemEntity;
import com.zeyuan.kyq.Entity.HomePageEntity;
import com.zeyuan.kyq.Entity.InformationEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.OtherUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.ArticleDetailActivity;
import com.zeyuan.kyq.widget.CustomView.CustomBannerViewPager;
import com.zeyuan.kyq.widget.DrawCircleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/28.
 *
 *
 * @author wwei
 */
public class HomeHelpRecyclerAdapter extends BaseRecyclerAdapter<HomeHelpRecyclerAdapter.HomeRecyclerViewHolder> {

    private Context context;
    private static final int TYPE_1 = 0;
    private static final int TYPE_2 = 1;
    private static final int TYPE_3 = 2;
    private static final int TYPE_4 = 3;

    private int type = 0;
    private List<InformationEntity> list;
    private List<HelpItemEntity> help;
    private RecyclerHelpAdapter rv_adapter;
    private BannerPagerAdapter vp_adapter;
    private RecyclerView recyclerView;
    private List<HomePageEntity> banners;
    private int[] imgs = {R.mipmap.guide1,R.mipmap.guide1,R.mipmap.guide1,R.mipmap.guide1};

    public void setHelp(List<HelpItemEntity> help) {
        if (help==null||help.size()==0){
            help = new ArrayList<>();
        }
        this.help = help;
    }

    public HomeHelpRecyclerAdapter(Context context,List<InformationEntity> list,
                                   RecyclerView recyclerView,List<HomePageEntity> banners){
        this.context = context;
        if (list==null) list = new ArrayList<>();
        this.list = list;
        this.recyclerView = recyclerView;
        this.banners = banners;
        help = new ArrayList<>();
        this.rv_adapter = new RecyclerHelpAdapter(context,help);
        this.vp_adapter = new BannerPagerAdapter(context,banners);
    }

    public void update(List<InformationEntity> list,int type){
        this.type = type;
        if (list==null) list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public HomeRecyclerViewHolder getViewHolder(View view) {
        return new HomeRecyclerViewHolder(view,false);
    }

    @Override
    public HomeRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {

        View v = null;
        switch (viewType){
            case TYPE_4:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_viewpager,parent,false);
                break;
            case TYPE_2:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_article_main_2,parent,false);
                break;
            case TYPE_3:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_rv,parent,false);
                break;
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_article_list,parent,false);
                break;
        }
        return new HomeRecyclerViewHolder(v,viewType,true);
    }

    @Override
    public void onBindViewHolder(HomeRecyclerViewHolder vh, int position, boolean isItem) {

        try {

            int viewType = getAdapterItemViewType(position);
            if (viewType==TYPE_4){

            }else if (viewType==TYPE_3){
                rv_adapter.update(help);
            }else {
                final InformationEntity entity;
                if (position>8){
                    entity = list.get(position-2);
                }else if (position>2&&position<8){
                    entity = list.get(position-1);
                }else {
                    entity = list.get(position);
                }
                String url = entity.getThumbURL();
                int like = entity.getLikeNum();
                String title = entity.getTitle();
                int watch = entity.getViewnum();
                switch (viewType){
                    case TYPE_2:
                        vh.watch.setText(watch+"");
                        vh.like.setText(like+"");
                        String summary = entity.getSummary();
                        vh.from.setText(TextUtils.isEmpty(summary)?"暂无摘要":summary.equals("admin")?"抗癌圈":summary);
                        break;
                    default:
                        if (watch>99999){
                            vh.watch.setText("99999+浏览");
                        }else {
                            vh.watch.setText(watch+"浏览");
                        }
                        if (like>99999){
                            vh.like.setText("99999+点赞");
                        }else {
                            vh.like.setText(like+"点赞");
                        }
                        String from = entity.getAuthor();
                        vh.from.setText(TextUtils.isEmpty(from) ? "未知来源" : from.equals("admin") ? "抗癌圈" : from);
                        break;
                }

                vh.title.setText(TextUtils.isEmpty(title) ? "" : title);
                final int id = entity.getAid();

                vh.title.setSelected(UserinfoData.getRecordArticleArray().get(id));

                if (TextUtils.isEmpty(url)){
                    vh.iv.setImageResource(R.mipmap.loading_fail);
                }else {
                    try {
                        Glide.with(context).load(url).error(R.mipmap.loading_fail).into(vh.iv);
                    }catch (Exception e){

                    }
                }


                vh.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!OtherUtils.isEmpty(""+id)){
                            context.startActivity(new Intent(context, ArticleDetailActivity.class).
                                    putExtra(Const.INTENT_ARTICLE_ID,""+id));
                        }
                    }
                });
            }

        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "onBindViewHolder");
        }
    }

    @Override
    public int getAdapterItemCount() {
        if (list==null||list.size()==0) return 0;
        return list.size()+2;
    }

    @Override
    public int getAdapterItemViewType(int position) {
        if (position==2) return TYPE_4;
        if (position==8) return TYPE_3;
        if (type==1) return TYPE_2;
        return TYPE_1;
    }

    public class HomeRecyclerViewHolder extends RecyclerView.ViewHolder{

        ImageView iv;
        TextView title,from,watch,like;
        RecyclerView rv;
        View v,v_body;
        DrawCircleView dcv;
        CustomBannerViewPager vp;

        int bannerIndex = -1;

        private final int CHANGE_BANNER = 1;

        boolean postFlag = false;//防止多次post

        private Handler mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try {
                    vp.setCurrentItem(bannerIndex);
                }catch (Exception e){

                }
            }
        };

        private Runnable bannerRun = new Runnable() {
            @Override
            public void run() {
                try {
                    if(ZYApplication.mainPageFlag){
                        if(ZYApplication.homeHelpPageFlag&&ZYApplication.homeMoveFlag){
                            bannerIndex++;
                            mHandler.sendEmptyMessage(CHANGE_BANNER);
                        }
                        mHandler.postDelayed(bannerRun,5000);
                    }
                }catch(Exception e){
                    ExceptionUtils.ExceptionToUM(e,context,"HomeRecyclerViewHolder bannerRun");
                }
            }
        };

        public HomeRecyclerViewHolder(View itemView,boolean isItem){
            super(itemView);
            init(itemView,-1,isItem);
        }

        public HomeRecyclerViewHolder(View itemView,int viewType,boolean isItem){
            super(itemView);
            init(itemView, viewType, isItem);
        }

        private void init(View convertView,int viewType,boolean isItem){
            if (isItem){
                switch (viewType){
                    case TYPE_4:
                        v_body = convertView.findViewById(R.id.v_body);
                        dcv = (DrawCircleView)convertView.findViewById(R.id.dcv);
                        dcv.setDrawCricle(banners.size(), 6, Color.parseColor("#4c4c4c"), Color.parseColor("#FFFFFF"));
                        dcv.redraw(0);
                        vp = (CustomBannerViewPager)convertView.findViewById(R.id.vp);
                        vp.setAdapter(vp_adapter);
//                        vp.setParent(recyclerView);
                        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {
                                bannerIndex = position;
                                dcv.redraw(position % banners.size());
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });
                        if (!postFlag){
                            mHandler.post(bannerRun);
                            postFlag = true;
                        }
                        break;
                    case TYPE_2:
                        v = convertView;
                        iv = (ImageView)convertView.findViewById(R.id.iv_img);
                        title = (TextView)convertView.findViewById(R.id.tv_title);
                        from = (TextView)convertView.findViewById(R.id.tv_summary);
                        like = (TextView)convertView.findViewById(R.id.tv_like);
                        watch = (TextView)convertView.findViewById(R.id.tv_watch);
                        break;
                    case TYPE_3:
                        rv = (RecyclerView)convertView.findViewById(R.id.rv_home);
                        LinearLayoutManager manager = new LinearLayoutManager(context);
                        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        rv.setLayoutManager(manager);
                        rv.setAdapter(rv_adapter);
                        break;
                    default:
                        v = convertView;
                        iv = (ImageView)convertView.findViewById(R.id.iv_article_item);
                        title = (TextView)convertView.findViewById(R.id.tv_title_article_item);
                        from = (TextView)convertView.findViewById(R.id.tv_from_article_item);
                        watch = (TextView)convertView.findViewById(R.id.tv_watch_article_item);
                        like = (TextView)convertView.findViewById(R.id.tv_like_article_item);
                        break;
                }
            }
        }
    }

}
