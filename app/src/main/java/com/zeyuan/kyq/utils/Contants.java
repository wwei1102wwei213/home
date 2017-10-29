package com.zeyuan.kyq.utils;

import android.os.Environment;

/**
 * Created by Administrator on 2015/9/6.
 * url类
 * <p/>
 * 一些字段 和url 统一管理类
 */
public final class Contants {

    public final static String strTitle = "strTitle";//收藏帖子 需要传给后台的标题
    public final static String isCancel = "isCancel";// 是否收藏  收藏是0 取消是1
    public final static int diolog_type = 1; //这个是显示添加症状的dialog
    public final static String toinfoid = "toinfoid";
    public final static String NO_DATA = "0";
    public final static String CancerTypeID = "CancerTypeID";
    public final static String RemindTime = "RemindTime";
    public final static String ChannelID = "ChannelID";


    public final static  String ISCANCER = "isCancer";
    /**
     * ggz:app的一些配置(类似的配置统一放在这里)
     */
    public static final class AppCfg {
        public static final String WX__APPID = "wx02cca444de652c20";

        public static final String WX__SECRET = "d851db5eb3481ee0f3427241096ec13d";
        public static final String WX__GRANT_TYPE = "authorization_code";
        public static final String QQ__APPID = "1104903720";

        public static final String QQ__OAUTH_CONSUMER_KEY = QQ__APPID;
        /**
         * 调用其微信登录需要的字段
         */
        public static final String WX__SCOPE = "snsapi_userinfo";
        public static final String WX__STATE = "carjob_wx_login1";
        /**
         * 1是ios 2 是android
         */
        public static final String APP_TYPE__ANDROID = "2";

        public static final String LOGIN__QQ_TYPE = "1";

        public static final String LOGIN__WX_TYPE = "2";

        //1微信 2qq
        public static final int LOGIN_TYPE__WX = 1;
        public static final int LOGIN_TYPE__QQ = 2;
    }

    /**
     * 阶段的一些配置
     */
    public static final class StepCfg {
        public static final String EDIT_SYMPTOM_TAG = "symptom";
        public static final String EDIT_ZHIBIAO_TAG = "zhibiao";
    }



    /**
     * 访问用户资料(获取登录用户的昵称、头像、性别)
     * access_token=*********
     * oauth_consumer_key=***
     * openid=***************
     */
    public static final String QQ__GET_USERINFO = "https://graph.qq.com/user/get_user_info";


    public static final String RESULT = "0";
    public static final String SAVE_INFO_ID = "infoid";//储存inforid的字段 在sp中


    public static final String BASE_PATH = "http://server.kaqcn.com:999/";//第三次的域名
//    public static final String BASE_PATH = "http://120.24.17.254:999/";//第二次的域名
    //    public static final String BASE_PATH_SECOND = "http://120.24.17.254:999/";//第一次的域名
    public static final String CREATE_INFO = BASE_PATH + "CreateInfo";//1.建立档案
    public static final String SYNC_CONFIG = BASE_PATH + "SyncConf";//2获取配置信息
    public static final String MAIN_PATH = BASE_PATH + "GetMainPage";//3.主页-获取主页
    public static final String PATIENT_DETAIL = BASE_PATH + "GetPatientDetail";//4.患者详情-获取患者详情
    //public static final String TIME_SYNC = BASE_PATH + "GetTimeSync";//5
    public static final String UPDATE_PATIENT_DETAIL = BASE_PATH + "UpdatePatientDetail";//6.患者详情-更改患者详情
    public static final String STEP_DETAIL = BASE_PATH + "GetStepDetail";//7.获取某个阶段详情
    public static final String UPDATE_STEP_DETAIL = BASE_PATH + "UpdateStepDetail";//8.	设置某个阶段详情
    public static final String SIMILAR_LIST = BASE_PATH + "GetSimilarList";//9.	相似案例（由主页相似案例更多进入）-获取当前用户相似案例详情
   // public static final String SIMILAR_CASE = BASE_PATH + "GetSimilarCase";//10.相似案例（由主页相似案例更多进入）-	获取当前用户相似案例详情
//    public static final String RECOVER_CENTER_DETAIL = BASE_PATH + "GetRecoverCenter";//12.获取当前用户康复中心详情
    public static final String MY_CIRCLE = BASE_PATH + "GetMycircle";//13.圈子-获取我的圈子
    public static final String MY_CIRCLE_NUM = BASE_PATH + "GetMyForumNum";//我的（由圈子的头像进入）-获取我的数据

    public static final String CANCER_FORUM = BASE_PATH + "GetCancerForum";//14.更多圈子（由圈子的右上角图标进入）-获取抗癌圈的帖子数量和人数
    public static final String CITY_FORUM = BASE_PATH + "GetCityForum";//.更多圈子（由圈子的右上角图标进入）-获取同城圈的帖子数量和人数
    public static final String FORUM_DETAIL = BASE_PATH + "GetForumDetail";//16.帖子详情（在圈子内点击某项）-获取帖子内容
    public static final String GET_REPLY_LIST = BASE_PATH + "GetReplyList";//24.帖子详情（在圈子内点击某项）-获取帖子回复列表
    public static final String FOLLOW_FORUM = BASE_PATH + "FavorForum";//帖子详情（在圈子内点击某项）收藏某个帖子

    public static final String REPLY_FORUM = BASE_PATH + "ReplyForum";//19.帖子详情（在圈子内点击某项）—回复某个帖子

    public static final String PostForum = BASE_PATH + "PostForum";//20.发布帖子（圈子点击右下角图标）-


    public static final String GET_MY_FORUM = BASE_PATH + "GetMyForum";//21我的帖子（圈子右上角进入我的 在点击帖子）
    public static final String MY_FOLLOW_FORUM = BASE_PATH + "GetMyFavor";//我的收藏（圈子右上角进入我的 在点击 收藏）
    public static final String GET_MY_REPLY_LIST = BASE_PATH + "GetMyReplyList";//评论/回复（圈子右上角进入我的 在点击 评论/回复）

    //-------------------------------------------------------------------------------
    public static final String some = BASE_PATH + "testcgi";//.暂时无用
    public static final String UPDATE_STEP_TIME = BASE_PATH + "UpdateStepTime";//22.暂时无用
    public static final String GET_MY_FROUM_NUM = BASE_PATH + "GetMyForumNum";//23.暂时无用
    public static final String SET_QUOTA = BASE_PATH + "SetQuota";//25.暂时无用
    public static final String MEDICINE_DETAIL = BASE_PATH + "GetMedicineDetail";//15.暂时无用
    public static final String GET_ALL_STEP = BASE_PATH + "GetAllStep";//26.暂时无用
    public static final String RECOVER_CENTER_LIST = BASE_PATH + "GetRecoverCenterList";//11.暂时无用
    public static final String FORUM_LIST = BASE_PATH + "GetForumList";//17.暂时无用
    public static final String Set_Perform = BASE_PATH + "SetPerform";//暂时无用
    public static final String FavorForum = BASE_PATH + "FavorForum";//暂时无用

    //-------------------------------------------------------------------------------

    public static final class UserStep{
        public static final String GET_ALL_STEP = BASE_PATH + "GetAllStep";//27.得到所有的阶段信息
        public static final String GET_STEP_DETAIL = BASE_PATH + "GetStepDetail";//28.用户阶段详情

        public static final String UPDATE_STEP_TIME = BASE_PATH + "UpdateStepTime";//29.

        public static final String SET_QUOTA = BASE_PATH + "SetQuota";//30.指标记录（主页-指标记录）设置指标记录
        public static final String SET_PERFRORM = BASE_PATH + "SetPerform";//31.添加症状（主页-添加症状）设置症状记录

        public static final String DEL_STEP_QUOTA_PERFORM = BASE_PATH + "DelStepQuotaPerform";//在我的病例中删除某个指标或者症状
    }

    public static final String Follow_CIRCLE = BASE_PATH + "FollowCircle";//收藏某个圈子


//    public static final String WEIXIN_CODE = "https://api.weixin.qq.com/sns/oauth2/access_token";//这个是微信根据code获取其他数据的接口

    /**
     * 通过code获取access_token
     * 由于access_token有效期（目前为2个小时）较短
     * ?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
     */
    public static final String WEIXIN_CODE = "https://api.weixin.qq.com/sns/oauth2/access_token";

    /**
     * 刷新access_token有效期
     * refresh_token拥有较长的有效期（30天），当refresh_token失效的后，需要用户重新授权
     * ?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN
     */
    public static final String WEIXIN__REFRESH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token";

    /**
     * 获取用户个人信息（UnionID机制）
     * ?access_token=ACCESS_TOKEN&openid=OPENID
     */
    public static final String WEIXIN__USERINFO = "https://api.weixin.qq.com/sns/userinfo";

    public static final String LOGIN = BASE_PATH + "Login";//登录——拿openid登录公司服务器
    //112.74.31.30
//    public static final String LOGIN = "http://112.74.31.30:999/Login";//登录——拿openid登录公司服务器

    public static final String MAIN_MORE = BASE_PATH + "GetArticleList";//更多内容(主页的文章列表更多进入)-文章列表

    public static final String ARTICLE_DETIAL = BASE_PATH + "GetArticleDetail";//文章详情

    //下面的api.txt 有详细说明

    public static final String GET_CANCER_PROCESS = BASE_PATH + "GetCancerProcess";//查症状

    public static final String GET_CONFIRM_SECOND = BASE_PATH + "GetConfirmSecond";//完善资料2

    public static final String GET_RESULT_DETAIL = BASE_PATH + "GetResultDetail";//肿瘤决策树
    public static final String SET_PLAN_MEDICINE = BASE_PATH + "SetPlanMedicine";//添加到计划用药
    public static final String GET_COMMDETAIL = BASE_PATH + "GetCommDetail";//普通决策树

    public static final String GET_SOLUTION_DETAIL = BASE_PATH + "GetSolutionDetail";//方案详情
    public static final String CONFIRM_PERFORM = BASE_PATH + "ConfirmPerform";//确认有该症状


    //---------------------------------------------------------------------------------------------------------------
    public static final String CITY_DIALOG = "city_dialog";
    public static final String CANCER_DIALOG = "cancer_dialog";
    public static final String DIGIT_T_DIALOG = "digit_t_dialog";
    public static final String DIGIT_N_DIALOG = "digit_n_dialog";
    public static final String DIGIT_M_DIALOG = "digit_m_dialog";
    public static final String DIGIT_DIALOG = "digit_dialog";
    public static final String MEDICA_DIALOG = "medica_dialog";
    public static final String GENE_DIALOG = "gene_dialog";
    public static final String TARGET_DIALOG = "tagret_dialog";
    public static final String CHEMISTRY_DIALOG = "chemistry_dialog";//最后有效的化疗药
    public static final String SELF_SELECT_DIALOG = "self_select_dialog";//自定义单选


    //-----------------------------------------------------------------------------------
    /**
     * 向微信请求数据的字段
     */
    public static final String appid = "appid";
    public static final String secret = "secret";
    public static final String code = "code";
    public static final String grant_type = "grant_type";


    //下面是一些需要提交给后台的字段
    public static final String InfoID = "InfoID"; //
    public static final String OtherUID = "OtherUid";

    public static final String OK_DATA = "0";//数据正确

    //传给后台的字段 声明是常量 编辑阶段的常量
    public static final String StepUnionID = "StepUnionID";
    public static final String StepUID = "StepUID";
    public static final String StartTime = "StartTime";//EndTime0
    public static final String EndTime = "EndTime";//EndTime0
    public static final String IsMedicineValid = "IsMedicineValid";//num
    public static final String num = "num";//num
    public static final String hasEffect = "1";
    public static final String noEffect = "0";



    //同城圈和抗癌圈的常量
    public static final String Tag = "Tag";
    public static final String Title = "Title";
    public static final String Content = "Content";
    public static final String IsAttachMedRecord = "IsAttachMedRecord";

    //帖子详情
    public static final String index = "index";
    public static final String fromuser = "fromuser";
    public static final String touser = "touser";
    public static final String URLNum = "URLNum";
    public static final String fn0 = "fn0";
    public static final String fn1 = "fn1";
    public static final String bc0 = "fn1";
    public static final String bc1 = "bc1";
    public static final String page = "page";
    public static final String CircleID = "CircleID";
    public static final String followOrcancel = "followOrcancel";


    public final static String OpenID = "OpenID";
    public final static String LoginType = "LoginType";
    public final static String AppType = "AppType";

    public final static String UnionID = "UnionID";
    public final static String hasRecord = "hasRecord";//表示是否有档案


    public final static String SetBit = "SetBit";
    public final static String InfoName = "InfoName";
    public final static String CancerID = "CancerID";
    public final static String TransferPos = "TransferPos";
    public final static String Gene = "Gene";
    public final static String DiscoverTime = "DiscoverTime";

    public final static String Sex = "Sex";
    public final static String Age = "Age";
    public final static String City = "City";


    //    public final static String periodid = "periodid";
    public final static String type = "type";
    public final static String Headimgurl = "Headimgurl";
    public static final String URL = "URL";
    public static final String ARTICLIE_ID = "ArticleID";
    public static final String StepID = "StepID";

    public static final String PerformID = "PerformID";
    public static final String FindSymtomBean = "FindSymtomBean";
    public static final String PeriodType = "PeriodType";
    public static final String PeriodID = "PeriodID";
    public static final String PlanStepID = "PlanStepID";
    public static final String IS_CANCER = "IS_CANCER";
    public static final String Drug = "Drug";
    public static final String TemplateID = "TemplateID";
    public static final String BodyStatusID = "BodyStatusID";
    public static final String SelfSelectNum = "SelfSelectNum";
    public static final String QuestionID = "QuestionID";
    public static final String AnswerID = "AnswerID";
    public static final String GeneID = "GeneID";
    public static final String TargetID = "TargetID";
    public static final String ChemistryID = "ChemistryID";
    public static final String CommBean = "CommBean";
    public static final String CancerResuletBean = "CancerResuletBean";
    public static final String WSZLBean = "WSZLBean";
    public static final String CaseDetailBean = "CaseDetailBean";
    public static final String List = "List";
    public static final String CureConfID = "CureConfID";
    public static final String Type = "Type";
    public static final String CommPolicyID = "CommPolicyID";
    public static final String CityID = "CityID";
    public static final String ProvinceID = "ProvinceID";
    public static final String PolicyType = "PolicyType";
    public static final String Avatar = "Avatar";
    public static final String ConfirmPerformID = "ConfirmPerformID";
    public static final String Province = "Province";
    public static final String CureStartTime = "CureStartTime";

    public static final String CatID = "catid";
    public static final String Page = "page";
    public static final String PageSize = "pagesize";

    /** 记录历史回复数量 ww*/
    public static final String ReplyNum = "ReplyNum";
    public static final String RedPointShow = "RedPointShow";

    /**SyncConf数据关键字*/
    public static final String SyncConfData = "SyncConfData";
    public final static String KAQ_DISK_IMG_BIG = Environment.getExternalStorageDirectory().getPath() + "/kaq/photo/"; /**大图片的本地存储*/
    public final static String KAQ_DISK_IMG_SMALL = Environment.getExternalStorageDirectory().getPath() + "/kaq/zyphoto/"; /**小图片的本地存储*/
    public final static String KAQ_DISK_IMG_TEMP = Environment.getExternalStorageDirectory().getPath() + "/kaq/temp/";
    /**sconf是否封装完毕*/
    public final static String IsHaveSconf = "IsHaveSconf";
    public final static String SaveKeyString = "SaveKeyString";
    /**分享文字*/


    /**syncconfig新接口*/
//    public static final String IP_PORT = "http://120.24.17.254:999/";

    /*public static final  String SYNC_CONFDIGIT = "http://120.24.17.254:999/SyncConfDigit";
    //http://120.24.17.254:999/SyncConf
    public static final  String SYNC_CONF = "http://120.24.17.254:999/ESyncConfDigit";
    //"http://120.24.17.254:999/EGetMainPage"
    public static final  String GET_MAIN_PAGE = IP_PORT + "EGetMainPage";*/

    /**新请求接口地址*/

}
