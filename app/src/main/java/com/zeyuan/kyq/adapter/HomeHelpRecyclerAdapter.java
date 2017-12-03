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
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.meelive.ingkee.sdk.plugin.InKeSdkPluginAPI;
import com.zeyuan.kyq.Entity.HelpItemEntity;
import com.zeyuan.kyq.Entity.HomePageEntity;
import com.zeyuan.kyq.Entity.InformationEntity;
import com.zeyuan.kyq.Entity.LiveBaseBean;
import com.zeyuan.kyq.Entity.LiveItemEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.manager.ClickStatisticsManager;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.OtherUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.ArticleDetailActivity;
import com.zeyuan.kyq.view.ForumDetailActivity;
import com.zeyuan.kyq.view.ShowDiscuzActivity;
import com.zeyuan.kyq.widget.CustomView.CustomBannerViewPager;
import com.zeyuan.kyq.widget.DrawCircleView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/28.
 *
 *
 * @author wwei
 */
public class HomeHelpRecyclerAdapter extends BaseRecyclerAdapter<HomeHelpRecyclerAdapter.HomeRecyclerViewHolder> implements HttpResponseInterface {

    private Context context;
    private static final int TYPE_1 = 0;
    private static final int TYPE_2 = 1;
    private static final int TYPE_3 = 2;
    private static final int TYPE_4 = 3;
    private static final int TYPE_5 = 5;

    private int flag = 0;

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

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void update(List<InformationEntity> list, int type){
        this.type = type;
        if (list==null) list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    public void updateBanners(List<HomePageEntity> banners) {
        this.banners = banners;
        vp_adapter.update(this.banners);
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
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_live_list,parent,false);
                break;
            case TYPE_5:
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
            if (viewType==TYPE_3){
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
                if (watch==0){
                    vh.watch.setText("");
                }else if (watch>99999){
                    vh.watch.setText("99999+浏览");
                }else {
                    vh.watch.setText(watch+"浏览");
                }
                String author = entity.getAuthor();
                vh.from.setText(TextUtils.isEmpty(author) ? "未知来源" : author.equals("admin") ? "抗癌圈" : author);
                int start = entity.getStarttime();
                final int end = entity.getEndtime();
                int liveType = getLiveType(start,end);
                switch (liveType){
                    case 2:
                        vh.start.setVisibility(View.VISIBLE);
                        vh.start.setText("直播时间：" + getStringForStartTime(start));
                        vh.gif.setVisibility(View.GONE);
                        vh.v_gif.setVisibility(View.GONE);
                        vh.iv_live.setVisibility(View.VISIBLE);
                        vh.iv_live.setImageResource(R.mipmap.img_live_type_yellow);
                        vh.tv_live.setBackgroundResource(R.drawable.live_forecase_bg);
                        vh.tv_live.setText("直播预告");
                        vh.tv_live.setTextColor(context.getResources().getColor(R.color.white));
                        break;
                    case 1:
                        vh.start.setVisibility(View.VISIBLE);
                        vh.start.setText("直播时间：" + getStringForStartTime(start));
                        vh.gif.setVisibility(View.VISIBLE);
                        vh.gif.loadUrl("file:///android_asset/test/gif_view.html");
                        vh.v_gif.setVisibility(View.VISIBLE);
                        vh.iv_live.setVisibility(View.GONE);
                        vh.tv_live.setBackgroundResource(R.drawable.live_living_bg);
                        vh.tv_live.setText("正在直播");
                        vh.tv_live.setTextColor(context.getResources().getColor(R.color.white));
                        break;
                    default:
                        vh.start.setVisibility(View.GONE);
                        vh.gif.setVisibility(View.GONE);
                        vh.v_gif.setVisibility(View.GONE);
                        vh.iv_live.setVisibility(View.VISIBLE);
                        vh.iv_live.setImageResource(R.mipmap.img_live_type_light_blue);
                        vh.tv_live.setBackgroundResource(R.drawable.live_old_bg);
                        vh.tv_live.setText("往期回顾");
                        vh.tv_live.setTextColor(context.getResources().getColor(R.color.live_old));
                        break;
                }

                vh.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (entity.getVid()!=0) {
                            VID = entity.getVid();
                            Factory.postPhp(HomeHelpRecyclerAdapter.this, Const.PApi_inkevideo_liveidid);
                            ClickStatisticsManager.getInstance().addClickEvent(Const.CLICK_EVENT_3,entity.getVid()+"");
                        }
                    }
                });
                if (TextUtils.isEmpty(url)){
                    vh.iv.setImageResource(R.mipmap.loading_fail);
                }else {
                    try {
                        Glide.with(context).load(url).error(R.mipmap.loading_fail).into(vh.iv);
                    }catch (Exception e){

                    }
                }
                vh.title.setText(TextUtils.isEmpty(title) ? "" : title);
                final int id = entity.getAid();
                vh.title.setSelected(UserinfoData.getRecordArticleArray().get(id));
            } else {
                if (viewType==TYPE_4){
                    if (flag==0){
                        vh.v_body.setVisibility(View.VISIBLE);
                        vh.dcv.setDrawCricle(banners.size(), 6, Color.parseColor("#4c4c4c"), Color.parseColor("#FFFFFF"));
                        vh.dcv.redraw(bannerIndex==-1?0:bannerIndex);
                    } else {
                        vh.v_body.setVisibility(View.GONE);
                    }
                }else if (viewType==TYPE_5){
                    if (flag==0){
                        vh.rv.setVisibility(View.VISIBLE);
                        rv_adapter.update(help);
                    } else {
                        vh.rv.setVisibility(View.GONE);
                    }
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
        if (position==8) return TYPE_5;
        if (type==TYPE_3) return TYPE_3;
        if (type==TYPE_2) return TYPE_2;
        return TYPE_1;
    }

    private int VID = 0;
    @Override
    public Map getParamInfo(int tag) {
        Map<String,String> map = new HashMap<>();
        if (tag == Const.PApi_inkevideo_liveidid){
            map.put(Contants.InfoID,UserinfoData.getInfoID(context));
            map.put("vid",VID+"");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {
        if (flag == Const.PApi_inkevideo_liveidid){
            LiveBaseBean bean = (LiveBaseBean)response;
            if (Const.RESULT.equals(bean.IResult())){
                LiveItemEntity entity = bean.getData();
                if (!TextUtils.isEmpty(entity.getType())){
                    int type = Integer.valueOf(entity.getType());

                    switch (type){
                        case 0:
                            if (!TextUtils.isEmpty(entity.getLiveid())){
                                InKeSdkPluginAPI.start(context, ZYApplication.YK_UserInfo, false, entity.getLiveid());
                            }else {
                                showToast("直播id为空");
                            }
                            break;
                        case 1:
                            if (!TextUtils.isEmpty(entity.getSign())){
                                context.startActivity(new Intent(context, ShowDiscuzActivity.class)
                                        .putExtra(Const.SHOW_HTML_MAIN_TOP, entity.getSign()));
                            }
                            break;
                        case 2:
                            if (!TextUtils.isEmpty(entity.getSign())){
                                context.startActivity(new Intent(context, ArticleDetailActivity.class)
                                        .putExtra(Const.INTENT_ARTICLE_ID,entity.getSign()));
                            }
                            break;
                        case 3:
                            if (!TextUtils.isEmpty(entity.getSign())){
                                context.startActivity(new Intent(context, ForumDetailActivity.class)
                                        .putExtra(Const.FORUM_ID,entity.getSign())
                                        .putExtra(Const.AUTHORID,entity.getAnchorid()));
                            }
                            break;


                    }
                }
            }
        }
    }

    private void showToast(String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(int flag) {

    }

    @Override
    public void hideLoading(int flag) {

    }
    //列表项是否可点击 网络请求时置否
    private boolean clickAble = true;
    @Override
    public void showError(int flag) {
        clickAble = true;
    }

    private int getLiveType(int start , int end){
        int now = Integer.valueOf(System.currentTimeMillis()/1000+"");
        if (start>now) return 2;
        if (start!=0&&start<=now&&end>=now) return 1;
        return 0;
    }

    private String getStringForStartTime(int start){
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm");
        long dates = Long.parseLong((start + "").concat("000"));
        String time = "";
        try {
            time = format.format(new Date(dates));
        }catch (Exception e){

        }
        return time;
    }

    protected int bannerIndex = -1;
    public class HomeRecyclerViewHolder extends RecyclerView.ViewHolder{

        ImageView iv;
        TextView title,from,watch,like;
        RecyclerView rv;
        View v,v_body;
        DrawCircleView dcv;
        CustomBannerViewPager vp;

        ImageView iv_live;
        TextView start,tv_live;
        View v_gif;
        WebView gif;

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
                        v = convertView;
                        iv = (ImageView)convertView.findViewById(R.id.iv_article_item);
                        title = (TextView)convertView.findViewById(R.id.tv_title_article_item);
                        from = (TextView)convertView.findViewById(R.id.tv_from_article_item);
                        tv_live = (TextView)convertView.findViewById(R.id.tv_live_item);
                        iv_live = (ImageView)convertView.findViewById(R.id.iv_live_type);
                        start = (TextView)convertView.findViewById(R.id.tv_live_start_time);
                        watch = (TextView)convertView.findViewById(R.id.tv_watch_article_item);
                        gif = (WebView)convertView.findViewById(R.id.gif_live);
                        v_gif = convertView.findViewById(R.id.v_gif);
                        break;
                    case TYPE_5:
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
