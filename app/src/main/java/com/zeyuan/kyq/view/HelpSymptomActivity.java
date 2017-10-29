package com.zeyuan.kyq.view;

import android.os.Bundle;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;

/**
 * Created by Administrator on 2016/3/21.
 *
 * 帮助页,该页面已废弃
 *
 * @author wwei
 */
public class HelpSymptomActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_symptom);
        initViews();
    }

    /***
     * 初始化视图控件
     *
     */
    private void initViews(){

        /*try {
            final int[] poss = {0,4,8,10,17,20,21,24,26,28,31,33,35,37,39};
            int pos = getIntent().getIntExtra("HelpList_position",0);
            int listpos = getIntent().getIntExtra("position",0);
            int newpos;

            *//**设置viewpager*//*
            ViewPager vp = (ViewPager)findViewById(R.id.vp_help_symptom);
            ArrayList<Fragment> fragments = new ArrayList<>();

            int[] res_tops = {R.mipmap.help101,//0
                    R.mipmap.help201,R.mipmap.help202,R.mipmap.help203,R.mipmap.help204,//4
                    R.mipmap.help301,R.mipmap.help302,R.mipmap.help303,R.mipmap.help304,//8
                    R.mipmap.help401,R.mipmap.help402,//10
                    R.mipmap.help501,R.mipmap.help502,R.mipmap.help503,R.mipmap.help504,
                    R.mipmap.help505,R.mipmap.help506,R.mipmap.help507,//17
                    R.mipmap.help601,R.mipmap.help602,R.mipmap.help603,//20
                    R.mipmap.help701,//21
                    R.mipmap.help801,R.mipmap.help802,R.mipmap.help803,//24
                    R.mipmap.help901,R.mipmap.help902,//26
                    R.mipmap.help1001,R.mipmap.help1002,//28
                    R.mipmap.help1101,R.mipmap.help1102,R.mipmap.help1103,//31
                    R.mipmap.help1201,R.mipmap.help1202,//33
                    R.mipmap.help1301,R.mipmap.help1302,//35
                    R.mipmap.help1401,R.mipmap.help1402,//37
                    R.mipmap.help1501,R.mipmap.help1502,//39
            };


            String[] titles = new String[]{"登录或注册","初步建立患者信息","填写分期","修改患者信息","查询肿瘤发展、副作用或并发症的解决办法","查看或编辑患者病历",
                    "编辑指标或症状记录","更改或添加病例","添加症状","指标记录","与癌友交流或求助癌友","关注自己感兴趣的圈子","查看收藏的文章、已发布的帖子或回复的内容",
                    "如何推荐给好友或癌友","向官方反馈意见或建议"};

            String[] steps = new String[]{
                    "",//1
                    "步骤一：填写癌症种类",//2
                    "步骤二：填写癌症确诊时间",
                    "步骤三：填写患者所在地",
                    "步骤四：填写治疗方案",
                    "步骤一：填写数字分期",//3
                    "步骤二：数字分期页面",
                    "步骤三：切换TNM分期",
                    "步骤四：TNM分期页面",
                    "",//4
                    "",
                    "步骤一：点击小医生查症状按钮",//5
                    "步骤二：选择症状部位",
                    "步骤三：选择出现的症状",
                    "步骤四：选择可能的诊断结果",
                    "步骤五：选择可能的治疗方案",
                    "步骤六：查看方案详情",
                    "步骤七：查看详细说明",
                    "步骤一：点击我的病历",//6
                    "步骤二：点击底部“+”按钮可增加阶段，添加症状，以及记录指标",
                    "步骤三：可选择增加患者治疗阶段，添加患者症状以及记录患者指标",
                    "",//7
                    "步骤一：在APP首页点击点击更改阶段按钮",
                    "步骤二：点击相应地方，修改阶段",
                    "步骤三：点击增加阶段按钮可以添加相应治疗阶段",
                    "步骤一：点击添加症状按钮",//8
                    "步骤二：点击相应地方，添加症状",
                    "步骤一：点击指标记录按钮",//9
                    "步骤二：点击相应地方，记录指标",
                    "步骤一：点击进入圈子页面",//10
                    "步骤二：选择发布帖子按钮",
                    "步骤三：在发布页面你可以帖子",
                    "步骤一：点击关注圈子按钮",//11
                    "步骤二：点击关注",
                    "步骤一：点击左上角自己头像按钮",//12
                    "步骤二：可查看收藏的帖子、文章，回复的帖子",
                    "步骤一：点击打开更多页面",//13
                    "步骤二：点击推荐给好友使用",
                    "步骤一：点击打开更多页面，点击意见反馈",//14
                    "步骤二：在这里可以写下您宝贵的建议"
            };

            String[] mstexts = new String[]{
                    "打开抗癌圈可以使用微信登录，QQ登录和用户登录，官方推荐使用“微信登录”或 “QQ ”登录",//1
                    "正确的填写癌症患者的相关信息，能够更有助于抗癌圈对于癌症患者的精准判断，正确的指导",//2
                    "",
                    "",
                    "",
                    "更详细的患者资料，分期情况能够更有助于抗癌圈对于癌症患者的 精准判断，正确的指导",//3
                    "",
                    "",
                    "",
                    "点击头像部分，即可进入患者资料页面",//4
                    "进入患者详情页面，即可对患者资料进行修改还保存",
                    "",//5
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",//-
                    "",//6
                    "",
                    "",//-
                    "可选择增加患者治疗阶段，添加患者症状已经记录患者指标",//7
                    "",//8
                    "",
                    "",
                    "",//9
                    "1.可以记录   2.治疗方法   3. 症状期间伴随的其他症状   4. 记录心情状态",
                    "",//10
                    "可以详细的填写主肿瘤大小，副肿瘤等信息",
                    "在圈子页面可以查看到相同病友的相似病历，可以借鉴他人宝贵的抗癌经验，抗癌路上不孤单",//11
                    "",
                    "先选择要发布的圈子，再写好标题和内容（还可以上传图片）即可发布",
                    "在圈子页面右上角点击加关注圈子按钮",//12
                    "在圈子列表页面点击自己感兴趣的圈子，即可关注该圈子动态",
                    "",//13
                    "",
                    "",//14
                    "即可将抗癌圈APP分享给微信，QQ好友，还可以分享到朋友圈和QQ空间",
                    "",//15
                    "欢迎写下对抗癌圈的建议和意见，抗癌圈会第一时间给予回复或帮助，也可以加入抗癌圈QQ交流群：284445750"
            };

            HelperSymptomFragment hsf;
            if(listpos==0){
                newpos = 1;
                hsf = new HelperSymptomFragment();
                hsf.setResoures_mid(R.mipmap.helpdark01);
                hsf.setTitle(titles[0]);
                hsf.setStep(steps[0]);
                hsf.setMstext(mstexts[0]);
                hsf.setResoures_top(res_tops[0]);
                fragments.add(hsf);
            }else{
                for(int i=poss[listpos-1]+1;i<pos+1;i++){
                    hsf = new HelperSymptomFragment();
                    if(i==0){
                        hsf.setResoures_mid(R.mipmap.helpdark01);
                        hsf.setTitle(titles[0]);
                    }else if(i>0&&i<5){
                        hsf.setResoures_mid(R.mipmap.helpdark02);
                        hsf.setTitle(titles[1]);
                    }else if(i>4&&i<=8){
                        hsf.setTitle(titles[2]);
                        hsf.setResoures_mid(R.mipmap.helpdark03);
                    }else if(i>8&&i<=10){
                        hsf.setTitle(titles[3]);
                        hsf.setResoures_mid(R.mipmap.helpdark04);
                    }else if(i>10&&i<=17){
                        hsf.setTitle(titles[4]);
                        hsf.setResoures_mid(R.mipmap.helpdark05);
                    }else if(i>17&&i<=20){
                        hsf.setTitle(titles[5]);
                        hsf.setResoures_mid(R.mipmap.helpdark06);
                    }else if(i==21){
                        hsf.setTitle(titles[6]);
                        hsf.setResoures_mid(R.mipmap.helpdark07);
                    }else if(i>21&&i<=24){
                        hsf.setTitle(titles[7]);
                        hsf.setResoures_mid(R.mipmap.helpdark08);
                    }else if(i>24&&i<=26){
                        hsf.setTitle(titles[8]);
                        hsf.setResoures_mid(R.mipmap.helpdark09);
                    }else if(i>26&&i<=28){
                        hsf.setTitle(titles[9]);
                        hsf.setResoures_mid(R.mipmap.helpdark10);
                    }else if(i>28&&i<=31){
                        hsf.setTitle(titles[10]);
                        hsf.setResoures_mid(R.mipmap.helpdark11);
                    }else if(i>31&&i<=33){
                        hsf.setTitle(titles[11]);
                        hsf.setResoures_mid(R.mipmap.helpdark12);
                    }else if(i>33&&i<=35){
                        hsf.setTitle(titles[12]);
                        hsf.setResoures_mid(R.mipmap.helpdark13);
                    }else if(i>35&&i<=37){
                        hsf.setTitle(titles[13]);
                        hsf.setResoures_mid(R.mipmap.helpdark14);
                    }else if(i>37&&i<=39){
                        hsf.setTitle(titles[14]);
                        hsf.setResoures_mid(R.mipmap.helpdark15);
                    }
                    hsf.setStep(steps[i]);
                    hsf.setMstext(mstexts[i]);
                    hsf.setResoures_top(res_tops[i]);
                    fragments.add(hsf);
                }
                newpos = poss[listpos]-poss[listpos-1];
            }

            final DrawCircleView dcv = (DrawCircleView)findViewById(R.id.dcv_help);
            dcv.setDrawCricle(newpos,10, Color.parseColor("#22000000"),Color.parseColor("#17cbd1"));

            MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(),fragments);
            vp.setAdapter(adapter);
//        vp.setCurrentItem(pos);
            *//**设置页面切换监听*//*
//        final int nums = res_tops.length;
            final TextView tvnum = (TextView)findViewById(R.id.tv_help_bom_text);

            vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
            });

            ImageView back = (ImageView)findViewById(R.id.iv_help_bom_menu);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"initviews,helpsymptomactivity");
        }*/
    }
}
