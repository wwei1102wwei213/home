package com.zeyuan.kyq.utils;

/**
 * Created by Administrator on 2016/4/21.
 * <p>
 * 重新整理的常量库
 *
 * @author wwei
 */
public final class Const {

    public static String InfoID;

    //密匙重试次数
    public static int KEY_ANGIN_TIMES = 0;
    //密匙重试最大次数
    public static final int KEY_MAX = 5;

    //接口请求成功标识
    public static final String RESULT = "0";
    public static final String NO_STEP = "0";
    public static final String HAVE_STEP = "1";

    //默认密匙
    public static final int[] KEY_FINAL = {0x1223311, 0x889922, 0x33, 0x44};


    /*********************************网络模块相关字段***start*******************************************/
    //C接口IP端口
    public static String IP_PORT = "https://server.kaqcn.com/";
    //PHP接口IP端口
    public static final String P_IP_PORT_1 = "https://help.kaqcn.com/help/";
    public static final String P_IP_PORT_2 = "https://120.24.14.34/Api/";
    public static final String P_IP_PORT_BBS = "http://bbs.kaqcn.com/";

    public static final int GetK = 0;//获取密钥
    //C接口请求标志
    //获取基础配置
    public static final int ESyncConf = 1;
    public static final String P_SYNC_CONF = P_IP_PORT_2 + "getSyncConf";
    //获取分期配置
//    public static final int ESyncConfDigit = 2;
//    public static final String E_SYNC_CONF_DIGIT = IP_PORT + "ESyncConfDigit";
    public static final int ESyncConfDigit = 2;
    public static final String P_SYNC_CONF_DIGIT = "http://help.kaqcn.com/Api/getDigitConf";
    //登录,已废弃
    public static final int ELogin = 3;
    public static final String E_LOGIN = IP_PORT + "ELogin";
    //创建用户，已废弃
    public static final int ECreateInfo = 4;
    public static final String E_CREATE_INFO = IP_PORT + "ECreateInfo";
    //注册，已废弃
    public static final int ERegister = 5;
    public static final String E_REGISTER = IP_PORT + "ERegister";
    //主页
    public static final int EGetMainPage = 6;
    public static final String E_GET_MAIN_PAGE = "https://112.74.31.30/GetMainPage_4.php";
    //更新用户详情
    public static final int EUpdatePatientDetail = 7;
    public static final String E_UPDATA_PATIENT_DETAIL = IP_PORT + "EUpdatePatientDetail";
    //获取用户详情
    public static final int EGetPatientDetail = 8;
    public static final String E_GET_PATIENT_DETAIL = IP_PORT + "EGetPatientDetail";
    //获取阶段详情，已废弃
    public static final int EGetStepDetail = 9;
    public static final String E_GET_STEP_DETAIL = IP_PORT + "EGetStepDetail";
    //获取肿瘤进展
    public static final int EGetCancerProcess = 10;
    public static final String E_GET_CANCER_PROCESS = IP_PORT + "EGetCancerProcess";
    //添加计划用药
    public static final int ESetPlanMedicine = 11;
    public static final String E_SET_PLAN_MEDICINE = IP_PORT + "ESetPlanMedicine";
    //获取所有阶段,已废弃
    public static final int EGetAllStep = 12;
    public static final String E_GET_ALL_STEP = IP_PORT + "EGetAllStep";
    //设置基因，已废弃
    public static final int ESetQuota = 13;
    public static final String E_SET_QUOTA = IP_PORT + "ESetQuota";
    //决策树查询
    public static final int EGetConfirmSecond = 14;//
    public static final String E_GET_CONFIRM_SECOND = IP_PORT + "EGetConfirmSecond";
    //设置症状，已废弃
    public static final int ESetPerform = 15;
    public static final String E_SET_PERFORM = IP_PORT + "ESetPerform";
    //获取肿瘤决策树结果详情
    public static final int EGetResultDetail = 16;
    public static final String E_GET_RESULT_DETAIL = IP_PORT + "EGetResultDetail";
    //获取方案详情
    public static final int EGetSolutionDetail = 17;
    public static final String E_GET_SOLUTION_DETAIL = IP_PORT + "EGetSolutionDetail";
    //获取普通决策树结果详情
    public static final int EGetCommDetail = 18;
    public static final String E_GET_COMM_DETAIL = IP_PORT + "EGetCommDetail";
    //诊断结果
    public static final int EConfirmPerform = 19;
    public static final String E_CONFIRM_PERFORM = IP_PORT + "EConfirmPerform";
    //删除症状记录或肿标记录，已废弃
    public static final int EDelStepQuotaPerform = 20;
    public static final String E_DEL_STEP_QUOTA_PERFORM = IP_PORT + "EDelStepQuotaPerform";
    //更新阶段时间，已废弃
    public static final int EUpdateStepTime = 21;
    public static final String E_UPDATE_STEP_TIME = IP_PORT + "EUpdateStepTime";
    //获取我的帖子数据
    public static final int EGetMyForumNum = 22;
    public static final String E_GET_MY_FORUM_NUM = IP_PORT + "EGetMyForumNum";
    //圈子主页
    public static final int EGetMycircle = 23;
    public static final String E_GET_MY_CIRCLE = IP_PORT + "EGetMycircle";
    //文章列表，已废弃
    public static final int EGetArticleList = 24;
    public static final String E_GET_ARTICLE_LIST = IP_PORT + "EGetArticleList";
    //已废弃
    public static final int EGetArticleDetail = 25;
    public static final String E_GET_ARTICLE_DETAIL = IP_PORT + "EGetArticleDetail";
    //我的回复列表
    public static final int EGetMyReplyList = 26;
    public static final String E_GET_MY_REPLY_LIST = IP_PORT + "EGetMyReplyList";
    //帖子的回复列表
    public static final int EGetReplyList = 27;
    public static final String E_GET_REPLY_LIST = IP_PORT + "EGetReplyList";//24.帖子详情（在圈子内点击某项）-获取帖子回复列表
    //相似案例，已废弃
    public static final int EGetSimilarList = 28;
    public static final String E_GET_SIMILAR_LIST = IP_PORT + "EGetSimilarList";
    //废弃
    public static final int EGetCancerForum = 29;
    public static final String E_GET_CANCER_FORUM = IP_PORT + "EGetCancerForum";
    //废弃
    public static final int EGetCityForum = 30;
    public static final String E_GET_CITY_FORUM = IP_PORT + "EGetCityForum";
    //废弃
    public static final int EGetForumDetail = 31;
    public static final String E_GET_FORUM_DETAIL = IP_PORT + "EGetForumDetail";
    //收藏帖子
    public static final int EFavorForum = 32;
    public static final String E_FAVOR_FORUM = IP_PORT + "EFavorForum";
    //回复帖子
    public static final int EReplyForum = 33;
    public static final String E_REPLY_FORUM = IP_PORT + "EReplyForum";
    //我的帖子
    public static final int EGetMyForum = 34;
    public static final String E_GET_MY_FORUM = IP_PORT + "EGetMyForum";
    //发表帖子，已废弃
    public static final int EPostForum = 35;
    public static final String E_POST_FORUM = IP_PORT + "EPostForum";
    //收藏列表
    public static final int EGetMyFavor = 36;
    public static final String E_GET_MY_FAVOR = IP_PORT + "EGetMyFavor";
    //关注圈子
    public static final int EFollowCircle = 37;
    public static final String E_FOLLOW_CIRCLE = IP_PORT + "EFollowCircle";
    //帖子列表
    public static final int EGetForumList = 38;
    public static final String E_GET_FORUM_LIST = IP_PORT + "EGetForumList";
    //已废弃
    public static final int EGetTimeSync = 39;
    //肿瘤头条，已废弃
    public static final int EGetCancerHeaderList = 40;
    public static final String E_GET_CANCER_HEADER_LIST = IP_PORT + "EGetCancerHeaderList";
    //阶段配置数据同步接口
    public static final int ESyncConfStep = 41;
    // public static final String E_SYNC_CONF_STEP = IP_PORT + "ESyncConfStep";
    public static final String P_SYNC_CONF_STEP = "http://help.kaqcn.com/Api/getSyncConfStep";
    //查副作用并发症 Type: 1  副作用  2 并发症
    public static final int EGetSearchToPolicy = 42;
    public static final String E_GET_SEARCH_TO_POLICY = IP_PORT + "EGetSearchToPolicy";
    //查指标 Type: 1 血常规  2 尿常规  3 粪便常规  4 肝功能
    public static final int EGetSearchToSpecification = 43;
    public static final String E_GET_SEARCH_TO_SPECIFIACTION = IP_PORT + "EGetSearchToSpecification";
    //添加当前阶段
    public static final int EAddUserStep = 44;
    public static final String E_ADD_USER_STEP = IP_PORT + "EAddUserStep";
    //多发圈子
    public static final int EPostForumMoreCircle = 45;
    public static final String E_Post_Forum_More_Circle = IP_PORT + "EPostForumMoreCircle";
    //登录登出
    public static final int ESetTokenDevice = 46;
    public static final String E_Set_Token_Device = IP_PORT + "ESetTokenDevice";
    //已废弃
    public static final int EMobile = 47;
    public static final String E_MOBILE = IP_PORT + "EMobile";
    //公益列表
    public static final int EEvent = 48;
    public static final String E_EVENT = IP_PORT + "EGetActiveList";
    //提现
    public static final int EGetInfoCash = 49;

    //PHP网络接口请求标志
    //短信验证接口
    public static final int PMobile = 55;
    public static final String P_Mobile_Url = P_IP_PORT_1 + "mobile_code_create";
    //用户登录接口
    public static final int PCheckOpen = 56;
    public static final String P_Check_Open_Url = P_IP_PORT_1 + "check_user_info";
    //用户登录验证接口
    public static final int PCheckInfoPin = 57;
    public static final String P_Check_Info_Pin_Url = P_IP_PORT_1 + "change_user_info";
    //用户创建接口
    public static final int PCreateUserInfo = 58;
    public static final String P_Create_User_Info_Url = P_IP_PORT_1 + "create_user_info";
    //提现接口
    public static final int PTakeCash = 59;
    public static final String P_Take_Cash_Url = P_IP_PORT_1 + "atm_wx";
    //绑定微信接口
    public static final int PBindWX = 60;
    public static final String P_BindWX = P_IP_PORT_1 + "WX_Bind";
    //个人中心接口
    public static final int PUserInfo = 61;
    public static final String P_UserInfo = P_IP_PORT_2 + "UserInfo";
    //粉丝列表接口
    public static final int PFollowList = 62;
    public static final String P_FollowList = P_IP_PORT_2 + "followList";
    //关注列表接口
    public static final int PCareList = 63;
    public static final String P_CareList = P_IP_PORT_2 + "CareList";
    //关注
    public static final int PCareAdd = 64;
    public static final String P_CareAdd = P_IP_PORT_2 + "CareAdd";
    //取消关注
    public static final int PCareDel = 65;
    public static final String P_CareDel = P_IP_PORT_2 + "CareDel";
    //个人文章列表
    public static final int PArticleInfo = 66;
    public static final String P_ArticleInfo = P_IP_PORT_2 + "ArticleInfo";
    //首页文章列表接口
    public static final int PHomeArticleInfo = 67;
    public static final String P_HomeArticleInfo = P_IP_PORT_2 + "HomeArticleInfo";
    //改变文章导航栏接口
    public static final int PEditUserCat = 68;
    public static final String P_EditUserCat = P_IP_PORT_2 + "EditUserCat";
    //我关注好友的帖子列表 --- 圈子
    public static final int PCareArc = 69;
    public static final String P_CareArc = P_IP_PORT_2 + "CareArc";
    //文章评论列表
    public static final int PGetPorCommentList = 70;
    public static final String P_GetPorCommentList = P_IP_PORT_2 + "getPorCommentList";
    //添加文章评论
    public static final int PAddPorComment = 71;
    public static final String P_AddPorComment = P_IP_PORT_2 + "addPorComment";
    /*ContentID  是 文章ID  或者 是 帖子 ID  或者 是文章评论ID  或者 是 帖子评论ID
    TypeID   类型ID1.文章2.文章评论3帖子4帖子评论*/
    //点赞接口
    public static final int PGiveLike = 72;
    public static final String P_GiveLike = P_IP_PORT_2 + "GiveLike";
    //用户评论回复列表
    public static final int PUserCommentInfo = 73;
    public static final String P_UserCommentInfo = P_IP_PORT_2 + "UserCommentInfo";
    //文章信息
    public static final int PArticleOtherInfo = 74;
    public static final String P_ArticleOtherInfo = P_IP_PORT_2 + "ArticleOtherInfo";
    //发现(和本地圈) 帖子列表
    public static final int PGetForumList_bank = 75;
    public static final String P_GetForumList_bank = P_IP_PORT_2 + "CareFind";
    //圈友
    public static final int PCircleFRList = 76;
    public static final String P_CircleFRList = P_IP_PORT_2 + "circleFRList";
    //用户收藏
    public static final int PGetUserFavInfo = 77;
    public static final String P_GetUserFavInfo = P_IP_PORT_2 + "getUserFavInfo";
    //用户登录统计
    public static final int PUserLoginAdd = 78;
    public static final String P_UserLoginAdd = P_IP_PORT_2 + "UserLoginAdd";
    //消息中心
    public static final int PShowInfoMsg = 80;
    public static final String P_Show_Info_Msg = P_IP_PORT_2 + "showInfoMsg";
    //
    public static final int PArticleAccurate = 81;
    public static final String P_Article_Accurate = P_IP_PORT_2 + "ArticleAccurate";
    //新编辑病历,用于拉取用户阶段信息
    public static final int PGetAppStepUser = 82;
    public static final String P_GetAppStepUser = P_IP_PORT_2 + "getAppStepUser";
    //编辑阶段信息
    public static final int PEditAppStepUser = 83;
    public static final String P_EditAppStepUser = P_IP_PORT_2 + "editAppStepUser";
    //删除阶段信息
    public static final int PDelAppStepUser = 84;
    public static final String P_DelAppStepUser = P_IP_PORT_2 + "delAppStepUser";
    //新增阶段信息
    public static final int PAddAppStepUser = 85;
    public static final String P_AddAppStepUser = P_IP_PORT_2 + "addAppStepUser";
    //常规记录
    public static final int PAddPresentationOther = 86;
    public static final String P_AddPresentationOther = P_IP_PORT_2 + "AddPresentationOther";
    //病理报告
    public static final int PGetPresentationOther = 87;
    public static final String P_GetPresentationOther = P_IP_PORT_2 + "GetPresentationOther";
    //病历报告是否显示
    public static final int PShowPresentationOther = 88;
    public static final String P_ShowPresentationOther = P_IP_PORT_2 + "ShowPresentationOther";
    //废弃
    public static final int PEditPresentationOther = 89;
    public static final String P_EditPresentationOther = P_IP_PORT_2 + "EditPresentationOther";
    //删除非特殊类型记录
    public static final int PDelPresentationOther = 90;
    public static final String P_DelPresentationOther = P_IP_PORT_2 + "DelPresentationOther";
    //个人资料
    public static final int PGetUserSelfForApp = 91;
    public static final String P_GetUserSelfForApp = P_IP_PORT_2 + "getUserSelfForApp";
    //修改个人资料
    public static final int PEditUserSelfForApp = 92;
    public static final String P_EditUserSelfForApp = P_IP_PORT_2 + "editUserSelfForApp";
    //患者资料
    public static final int PGetUserInfoForApp = 93;
    public static final String P_GetUserInfoForApp = P_IP_PORT_2 + "getUserInfoForApp";
    //修改患者资料
    public static final int PEditUserInfoForApp = 94;
    public static final String P_EditUserInfoForApp = P_IP_PORT_2 + "editUserInfoForApp";
    //记录症状
    public static final int PAddStep2Perform = 95;
    public static final String P_AddStep2Perform = P_IP_PORT_2 + "AddStep2Perform";
    //新增转移情况
    public static final int PAddTransferRecord = 96;
    public static final String P_AddTransferRecord = P_IP_PORT_2 + "AddTransferRecord";
    //新增基因情况
    public static final int PAddTransferGen = 97;
    public static final String P_AddTransferGen = P_IP_PORT_2 + "AddTransferGen";
    //获取历史记录数据
    public static final int PGetCancerInfoForApp = 98;
    public static final String P_GetCancerInfoForApp = P_IP_PORT_2 + "getCancerInfoForApp";
    //记录肿瘤指标
    public static final int PAddCancerMark = 99;
    public static final String P_AddCancerMark = P_IP_PORT_2 + "AddCancerMark";
    //记录肿瘤大小
    public static final int PAddQuotaMasterSlave = 100;
    public static final String P_AddQuotaMasterSlave = P_IP_PORT_2 + "AddQuotaMasterSlave";
    //患者病情信息
    public static final int PGetRecordBookForApp = 101;
    public static final String P_GetRecordBookForApp = P_IP_PORT_2 + "getRecordBookForApp";
    //患者病历阶段内详情
    public static final int PGetStepUserOtherInfo = 102;
    public static final String P_GetStepUserOtherInfo = P_IP_PORT_2 + "getStepUserOtherInfo";
    //删除转移记录
    public static final int PDelTransferRecord = 103;
    public static final String P_DelTransferRecord = P_IP_PORT_2 + "DelTransferRecord";
    //删除肿瘤大小记录
    public static final int PDelQuotaMasterSlave = 104;
    public static final String P_DelQuotaMasterSlave = P_IP_PORT_2 + "DelQuotaMasterSlave";
    //删除指标记录
    public static final int PDelCancerMark = 105;
    public static final String P_DelCancerMark = P_IP_PORT_2 + "DelCancerMark";
    //删除基因记录
    public static final int PDelTransferGen = 106;
    public static final String P_DelTransferGen = P_IP_PORT_2 + "DelTransferGen";
    //删除症状记录
    public static final int PDelStep2Perform = 107;
    public static final String P_DelStep2Perform = P_IP_PORT_2 + "DelStep2Perform";
    //AddMarkTypeByUser
    public static final int PAddMarkTypeByUser = 108;
    public static final String P_AddMarkTypeByUser = P_IP_PORT_2 + "AddMarkTypeByUser";
    //肿瘤肿标图标是否显示
    public static final int PShowMarkAndTumourInfo = 109;
    public static final String P_ShowMarkAndTumourInfo = P_IP_PORT_2 + "ShowMarkAndTumourInfo";
    //获取服务中心数据
    public static final int PApiServeCenterlist = 110;
    public static final String P_ApiServeCenterlist = P_IP_PORT_2 + "Api_ServeCenter_list";
    //映客支付数据转发接口
    public static final int PCateinkeorder = 111;
    public static final String P_Cateinkeorder = P_IP_PORT_2 + "cateinkeorder";
    //映客直播确认状态接口
    public static final int PApi_inkevideo_liveidid = 112;
    public static final String P_Api_inkevideo_liveidid = P_IP_PORT_2 + "Api_inkevideo_liveidid";
    //首页快速入口
    public static final int PApi_App_shortcut = 113;
    public static final String P_Api_App_shortcut = P_IP_PORT_2 + "Api_App_shortcut";
    //病情总体情况
    public static final int PApi_getStepSummary = 114;
    public static final String P_Api_getStepSummary = P_IP_PORT_2 + "getStepSummary";
    //相似案例列表接口
    public static final int PApi_getSimilarCase = 115;
    public static final String p_Api_getSimilarCase = P_IP_PORT_2 + "getSimUser";
    //活动列表接口
    public static final int PApi_getAdvertising_more = 116;
    public static final String p_Api_getAdvertising_more = P_IP_PORT_2 + "Advertising_more";
    //项目--圈子
    public static final int PApi_getProject = 117;
    public static final String p_Api_getProject = P_IP_PORT_2 + "getProject";
    //圈子的标签和红点
    public static final int PApi_getCircleShortcut = 118;
    public static final String p_Api_get_Circle_shortcut = P_IP_PORT_2 + "get_Circle_shortcut";
    //帖子列表
    public static final int PApi_getThreadByType = 119;
    public static final String p_Api_getThreadByType = P_IP_PORT_2 + "getThreadByType";
    //获取帖子详情
    public static final int PApi_getThreadInfo = 120;
    public static final String p_Api_getThreadInfo = P_IP_PORT_2 + "ThreadInfo";
    //发布重新编辑后的帖子
    public static final int PApi_editThread = 121;
    public static final String p_Api_editThread = P_IP_PORT_2 + "editThread";
    //删帖
    public static final int PApi_delThread = 122;
    public static final String p_Api_delThread = P_IP_PORT_2 + "delThread";
    //新获取帖子回复列表
    public static final int PApi_getReplyList_2 = 123;
    public static final String p_Api_getReplyList2 = P_IP_PORT_BBS + "GetReplyList2.php";
    //根据条件获取病历记录
    public static final int PApi_getUserInfoByTypeForApp = 124;
    public static final String p_Api_getUserInfoByTypeForApp = P_IP_PORT_2 + "getUserInfoByTypeForApp";
    //上传点击统计记录
    public static final int PApi_LookCount = 125;
    public static final String p_Api_LoolCount = "http://www.kaqcn.com/Api/Api_LookCount";
    //获取有赞的登录信息
    public static final int Api_YouzanLogin = 126;
    public static final String Url_YouzanLogin = "https://uic.youzan.com/sso/open/login";
    //获取消息
    public static final int PApi_ShowInfoMsg = 127;
    public static final String p_Api_ShowInfoMsg = P_IP_PORT_2 + "showInfoMsg";
    //发帖新接口(多发圈子)
    public static final int PApi_ThreadForMoreCircle = 128;
    public static final String p_Api_ThreadForMoreCircle = P_IP_PORT_BBS + "threadForMoreCircle.php";
    //分享成功后积分统计typeID为死值 7
    public static final int PApi_AppShareIntegral = 129;
    public static final String p_Api_AppShareIntegral = P_IP_PORT_2 + "appShareIntegral";
    //消息开关
    public static final int PApi_UM_Switch = 130;
    public static final String p_Api_UM_Switch = P_IP_PORT_2 + "off_Umeng";
    //修改用户信息
    public static final int PApi_EditInfo = 131;
    public static final String p_Api_EditInfo = P_IP_PORT_2 + "EditInfo";
    //获取该状态当前人数
    public static final int PApi_GetCurrentNum = 132;
    public static final String p_Api_getCurrentNum = P_IP_PORT_2 + "getCurrentNum";
    //查询药物的有效信息
    public static final int PApi_getStepForUserNum = 133;
    public static final String p_Api_getStepForUserNum = P_IP_PORT_2 + "getStepForUserNum";
    //查询推荐的好友
    public static final int PApi_getRecommendUser = 134;
    public static final String p_Api_getRecommendUser = P_IP_PORT_2 + "getRecommendUser";
    //修改手机号码
    public static final int PApi_Edit_Moblie = 135;
    public static final String p_Api_Edit_Moblie = P_IP_PORT_2 + "Edit_Moblie";


/*********************************网络模块相关字段***end*******************************************/


    /*********************************数据模块相关字段***start*******************************************/

    //基本数据常量
    public static final String DataDefaultCancer = "Data_Default_Cancer";
    public static final String DataCancerValues = "Data_Cancer_Values";
    public static final String DataCancerData = "Data_Cancer_Data";
    public static final String DataPerformValues = "Data_Perform_Values";
    public static final String DataBodyPos = "Data_Body_Pos";
    public static final String DataTransferPos = "Data_Transfer_Pos";
    public static final String DataGeneValues = "Data_Gene_Values";
    public static final String DataGene = "Data_Gene";
    public static final String DataCircleValues = "Data_Circle_Values";
    public static final String DataCity = "Data_City";
    public static final String DataAllCity = "Data_All_City";
    public static final String DataCureConf = "Data_Cure_Conf";
    public static final String DataCircleCancer = "Data_Circle_Cancer";
    public static final String DataCircleClass = "Data_Circle_Class";
    public static final String DataCancerParent = "Data_Cancer_Parent";
    public static final String DataCircleForSearch = "Data_Circle_For_Search";
    public static final String DataBodyPosValues = "Data_Body_Pos_Values";
    //分期数据常量
    public static final String DataDigitData = "Data_Digit_Data";
    public static final String DataDigitValues = "Data_Digit_Values";
    public static final String DataTnmObjs = "Data_Tnm_Objs";
    public static final String DataDigitT = "Data_Digit_T";
    public static final String DataDigitN = "Data_Digit_N";
    public static final String DataDigitM = "Data_Digit_M";
    //step数据常量
    public static final String DataDrugNames = "Data_Drug_Names";
    public static final String DataCancerStepNew = "Data_Cancer_Step_New";
    public static final String DataSearchDrug = "Data_Search_Drug";
    public static final String DataSearchDrugList = "Data_Search_Drug_List";
    public static final String DataSearchArticle = "DataSearchArticle";
    public static final String DataStepData = "Data_Step_Data";

    /*----------------------------分期数据-------------------------------*/
    //分期数据Map,键为分期ID
    public static final int N_DataDigitValues = 2;
    //分期数据Map,键为癌种ID
    public static final int N_DataDigitData = 3;
    //TNM分期关联数字分期的数据集合
    public static final int N_DataTnmObjs = 4;
    //T分期
    public static final int N_DataDigitT = 5;
    //N分期
    public static final int N_DataDigitN = 6;
    //M分期
    public static final int N_DataDigitM = 7;
    /*----------------------------基本数据-------------------------------*/
    //默认癌种数据Map,键为癌种ID
    public static final int N_DataDefaultCancer = 13;
    //癌种数据Map,键为癌种ID
    public static final int N_DataCancerValues = 14;
    //癌种数据Map,键为父类癌种ID  ***
    public static final int N_DataCancerData = 15;
    //症状数据Map,键为症状ID
    public static final int N_DataPerformValues = 16;
    //圣体部位数据Map,键为身体部位ID LinkedHashMap<String, List<String>>
    public static final int N_DataBodyPos = 17;
    //转移部位数据Map,键为转移部位ID  ***
    public static final int N_DataTransferPos = 18;
    //总基因数据Map，键为基因ID
    public static final int N_DataGeneValues = 19;
    //根据癌种基因数据Map，键为癌种ID  ***
    public static final int N_DataGene = 20;
    //圈子数据Map,便于取值
    public static final int N_DataCircleValues = 21;
    //已废弃
    public static final int N_DataOtherStep = 22;
    //城市数据List,便于搜索
    public static final int N_DataCity = 23;
    //城市数据SpareArray,便于取值，键为CityID
    public static final int N_DataAllCity = 24;
    //阶段类型数据Map,键为阶段类型ID
    public static final int N_DataCureConf = 25;
    //癌种圈子对应的主题分类圈子Map，键为圈子ID,ID=0为其他圈子
    public static final int N_DataCircleCancer = 26;
    //抗癌圈数据Map,键为父类癌种ID
    public static final int N_DataCircleClass = 27;
    //父类癌种Map
    public static final int N_DataCancerParent = 28;
    //圈子数据集合，为原搜索页面封装该类型，有空可去掉这个标识的数据，避免和21数据重复
    public static final int N_DataCircleForSearch = 29;
    //身体部位数据Map
    public static final int N_DataBodyPosValues = 30;
    /*----------------------------阶段数据-------------------------------*/
    //阶段数据SpareArray,键为StepID(int型)
    public static final int N_DataDrugNames = 41;
    //已废弃
    public static final int N_DataDrugOther = 42;
    //癌种对应的阶段数据  ***
    public static final int N_DataCancerStepNew = 43;
    //阶段数据SpareArray,键为CureConfID(int型,阶段类型ID)
    public static final int N_DataSearchDrug = 44;
    //可查药阶段数据集合
    public static final int N_DataSearchDrugList = 46;
    //获取是否解析完成标识
    public static final int UPDATA_STEP = 100;
    //总阶段数据
    public static final int N_DataStepData = 45;

    /*配置数据分类标识*/
    //阶段数据标识
    public static final int N_DataConfStep = 40;
    //基本数据标识
    public static final int N_DataSyncConf = 30;
    //分期数据标识
    public static final int N_DataDigitConf = 0;
    //文章url
    public static final int N_UrlAirtcleIndex = 31;
    //帖子url
    public static final int N_UrlForumIndex = 32;

    /*********************************数据模块相关字段***end*******************************************/


    //友盟埋点常量字段
    public static final String EVENTFLAG = "InfoMsg";
    public static final String EVENT_AllStep = "AllStep";
    public static final String EVENT_CancerHeadline = "CancerHeadline";
    public static final String EVENT_CreateInfo = "CreateInfo";
    public static final String EVENT_DiagnoseResultDetail = "DiagnoseResultDetail";
    public static final String EVENT_HomePage_UseMedicine = "HomePage_UseMedicine";
    public static final String EVENT_HomePage_ViewSolution = "HomePage_ViewSolution";
    public static final String EVENT_MoreCircle = "MoreCircle";
    public static final String EVENT_ShareApp = "ShareApp";
    public static final String EVENT_ShareResult = "ShareResult";
    public static final String EVENT_SimilarCase = "SimilarCase";
    public static final String EVENT_ShareMRB = "ShareMRB";
    public static final String EVENT_SymptomsQuery = "SymptomsQuery";
    public static final String EVENT_UpdateStep = "UpdateStep";
    public static final String EVENT_KnowledgeBase = "KnowledgeBase";
    public static final String EVENT_SideEffect = "SideEffect";
    public static final String EVENT_RecoveryCenter = "RecoveryCenter";
    public static final String EVENT_ShareArticle = "ShareArticle";
    public static final String EVENT_ShareForum = "ShareForum";
    public static final String EVENT_PatientDetail = "PatientDetail";
    public static final String EVENT_AddMedicineToPlan = "AddMedicineToPlan";
    public static final String EVENT_SymptomsDiagnoseResult = "SymptomsDiagnoseResult";
    public static final String EVENT_ConfirmSymptom = "ConfirmSymptom";
    public static final String EVENT_EnterCirclePerLaunch = "EnterCirclePerLaunch";
    public static final String EVENT_MainTopBanner = "MainTopBanner";
    public static final String EVENT_WenZheng = "WenZheng";
    public static final String EVENT_SearchQuota = "SearchQuota";
    public static final String EVENT_LinChuangZhaoMu = "LinChuangZhaoMu";
    public static final String EVENT_ZhiHuan = "ZhiHuan";

    //service控制字
    public final static String SERVICE_START = "zy_service_start";
    public final static String SERVICE_APP_EXIT = "zy_service_app_exit";
    public final static String SERVICE_STEP_UPDATA_START = "zy_service_step_updata_start";
    public final static String SERVICE_CORRECT_HEAD_IMG = "zy_service_correct_head_img";

    //副作用和并发症的参数值
    public static final int TYPE_EFFECT = 8;
    public static final int TYPE_COMPLICATION = 11;
    public static final int TYPE_ISCANCER = 0;
    public static final String ISNEWVERSION = "IsNewVersion";
    public static final String ISNEWVERSION_FINAL = "1";
    public static final String POLICY_TYPE_DRUG = "2";
    public static final String INTENT_ISCANCER = "intent_iscancer";
    public static final String INTENT_EFFECT = "intent_effect";
    public static final String INTENT_BING_FA_ZHENG = "intent_bing_fa_zheng";
    public static final String INTENT_OR_TYPE = "intent_or_type";

    //搜索记录保存标识
    public static final String SEARCH_TYPE = "search_type";
    public static final String RECORD_DRUG = "record_drug";
    public static final String RECORD_SYMPTOM = "record_symptom";
    public static final String RECORD_ARTICLE = "record_article";
    public static final String RECORD_CIRCLE = "record_circle";
    public static final String RECORD_QUOTA = "record_quota";
    public static final int SEARCH_DRUG = 1;
    public static final int SEARCH_SYMPTOM = 2;
    public static final int SEARCH_ARTICLE = 3;
    public static final int SEARCH_CIRCLE = 4;
    public static final int SEARCH_QUOTA = 5;

    //分隔符常量
    public static final String BREAK_BIG = ",,,";
    public static final String BREAK_SMALL = "@@@";

    //帖子标识判断常量
    public static final int FLAG_FORUM_TOP = 1;
    public static final int FLAG_FORUM_JING = 2;
    public static final int FLAG_FORUM_IMG = 4;

    //其他静态常量
    public static final String IsAttachMedRecord = "IsAttachMedRecord";
    public static final String IMG_URL_HEAD = "http://zeyuan1.oss-cn-shenzhen.aliyuncs.com";
    public static final String ISHAVESTEP = "IsHaveStep";
    public static final String LoginToken = "LoginToken";
    public static final String InfoCenterID = "InfoCenterID";
    public static final String CareListType = "CareListType";
    public static final String HomeRecordTime = "HomeRecordTime";
    public static final String TypeID = "TypeID";//好友列表页的参数或帖子列表类型
    public static final String CircleID = "CircleID";//圈子ID
    public static final int SELECT_OTHER_FRAGMENT = 98;
    public static final int SELECTED_CITY = 100;
    public static final String Circle_Friend = "-1";
    public static final String Circle_Project = "-2";
    public static final String Circle_Local = "-3";

    //Type (1.影像2.出院3.诊断书4.检验报告5.血常规6.粪便常规7.尿常规8.肝功能9.肾功能10.其他指标)
    //记录分类字段
    public static final int RECORD_TYPE_1 = 4;//血常规
    public static final int RECORD_TYPE_2 = 5;//粪便常规
    public static final int RECORD_TYPE_3 = 6;//尿常规
    public static final int RECORD_TYPE_4 = 7;//肝功能
    public static final int RECORD_TYPE_5 = 8;//肾功能
    public static final int RECORD_TYPE_6 = 9;//其他功能
    public static final int RECORD_TYPE_7 = 2;//诊断书
    public static final int RECORD_TYPE_8 = 3;//病理检验报告
    public static final int RECORD_TYPE_9 = 1;//出院报告
    public static final int RECORD_TYPE_11 = 11;    //基因突变
    public static final int RECORD_TYPE_12 = 12;    //转移
    public static final int RECORD_TYPE_13 = 13;    //症状
    public static final int RECORD_TYPE_14 = 14;    //肿瘤大小
    public static final int RECORD_TYPE_15 = 15;    //指标

    //Intent消息常量串
    public static final String RECORD_CLASSIFY_TYPE = "Record_Classify_Type";//记录分类Intent传递标识串
    public static final String RECORD_CLASSIFY_QUOTA_STATUS = "Record_Classify_Quota_Status";//记录指标选择传递标识串
    public static final String RECORD_CLASSIFY_QUOTA_CHECKED = "Record_Classify_Quota_Checked";//记录肿标选择列表标识串
    public static final String RECORD_CLASSIFY_GENE_TRANSLATE_CHECKED = "Record_Classify_Gene_Translate_Checked";//记录基因或战役选择列表标识串
    public static final String RECORD_CLASSIFY_CANCER_SIZE = "Record_Classify_CancerSizeData";//记录分类Intent传递标识串
    public static final String RECORD_CLASSIFY_DATA = "Record_Classify_Data";//记录数据Intent传递标识串
    public static final String RECORD_REQUEST_FLAG = "RECORD_REQUEST_FLAG";//带返回值的启动Intent传递标识串
    public static final String RECORD_REQUEST_QUOTA_TYPE = "Record_Request_Quota_Type";//选择指标类型Intent传递标识串
    public static final String RECORD_CLASSIFY_QUOTA_TYPE_LIST = "Record_Classify_Quota_Type_List";//已选指标集合Intent传递标识串
    public static final String RESULT_FLAG_FOR_DATA_CHANGED = "result_flag_for_data_changed";//病历页面返回标识串
    public static final String RECORD_EDIT_FROM_MEDICAL = "Record_Edit_From_Medical";//来自病历的编辑标识
    public static final String RECORD_EDIT_POSITION = "Record_Edit_Position";//编辑病历列表的下标
    public static final String RECORD_EDIT_CHILD_POSITION = "Record_Edit_Child_Position";//编辑病历列表的子列表下标
    public static final String RECORD_EDIT_IS_CHILD = "Record_Edit_Is_Child";//是否编辑子列表标识
    public static final String RECORD_EDIT_RESULT_DATA = "Record_Edit_Result_Data";//编辑完成的数据返回
    public static final String RECORD_CHART_TYPE = "Record_Chart_Type";//图表类型标识
    public static final String PATIENT_OTHER_STRICKEN_CHECKED = "Patient_Other_Stricken_Checked";//其他重疾选中列表标识
    public static final String PATIENT_OTHER_STRICKEN_REMARK = "Patient_Other_Stricken_Remark";//其他重疾描述标识
    public static final String RESULT_OTHER_STRICKEN_CHECKED = "Result_Other_Stricken_Checked";//其他重疾选中返回标识
    public static final String RESULT_OTHER_STRICKEN_REMARK = "Result_Other_Stricken_Remark";//其他重疾描述返回标识
    public static final String RESULT_ALL_MENU_FLAG = "Result_all_menu_flag";//全部功能退出时返回标识

    //Intent消息常量标识
    public static final int REQUEST_CODE_RECORD_ACTIVITY = 101;//记录页面返回CODE
    public static final int REQUEST_CODE_CHOOSE_QUOTA_TYPE = 102;//选择指标类型CODE
    public static final int REQUEST_CODE_CHOOSE_SYMPTOM = 103;//症状选择返回CODE
    public static final int REQUEST_CODE_MEDICAL_RECORD_ACTIVITY = 99;//病历页面的请求CODE
    public static final int RESULT_CODE_PATHOLOGY_TO_MEDICAL = 104;//从病理报告返回到病历标识
    public static final int RESULT_CODE_PATIENT_DATA_TO_MEDICAL = 105;//从患者详情返回到病历标识
    public static final int RESULT_CODE_EDIT_STEP_TO_MEDICAL = 106;//从编辑阶段返回到病历标识
    public static final int RESULT_CODE_RERORD_CLASSIFY_TO_MEDICAL = 107;//从记录分类页面返回病历
    public static final int RESULT_CODE_EDIT_RECORD_TO_MEDICAL = 108;//从编辑记录页面返回病历
    public static final int RESULT_CODE_RECORD_CHART_TO_MEDICAL = 109;//从肿瘤大小变化页面返回病历
    public static final int RESULT_CODE_OTHER_STRICKEN_TO_PATIENT = 110;//从其他重疾返回患者资料
    public static final int REQUEST_CODE_ALL_MENU_FLAG = 111;//全部功能的请求CODE
    public static final int RESULT_CODE_ALL_MENU_FLAG = 112;//从全部功能返回主页

    //Intent消息参数名常量
    public static final String INTENT_FROM = "intent_from";
    public static final int FM = 127;
    public static final String INTENT_SHOW_DISCUZ_FLAG = "ShowDiscuzFLAG";
    public static final String INTENT_SHOW_DISCUZ_ID = "ShowDiscuzID";
    public static String SHOW_DISCUZ_FORUM;
    public static String SHOW_DISCUZ_ARTICLE;
    public static String SIMALAR_TITLE = "SIMALAR_TITLE";
    public static String INTENT_TO_ARTCILE_HEADFLAG = "INTENT_TO_ARTCILE_HEADFLAG";
    public static String ARTCILE_TITLE = "ARTCILE_TITLE";
    public static String ARTICLE_SUMMARY = "ARTICLE_SUMMARY";
    public static final String AUTHORID = "authorId";
    public static final String FORUM_ID = "index";
    public static final String FORUM_CIRCLE_ID = "FORUM_CIRCLE_ID";
    public static final String FORUM_OWNER_ID = "FORUM_OWNER_ID";
    public static final String SEARCH_POLICY_TYPE = "SEARCH_POLICY_TYPE";
    public static final String HEAD_LIST_TAG_URL = "head_list_tag_url";
    public static final String HEAD_LIST_INFO_TEXT = "head_list_info_text";
    public static final String SHOW_HTML_MAIN_TOP = "show_html_main_top";
    public static final String SET_QUOTA_TYPE = "set_quota_type";
    public static final String RECORD_URL = "record_url";
    public static final String DEFAULT_CIRCLE = "default_circle";
    public static final String INTENT_BIND_CANCLE = "intent_bind_cancle";
    public static final String INTENT_ARTICLE_ID = "intent_article_id";
    public static final String INTENT_AUTHOR_ID = "intent_author_id";
    public static final String INTENT_ARTICLE_TYPE_ENTITY = "intent_article_type_entity";
    public static final String INTENT_INKE_PAY = "intent_inke_pay";
    public final static String RESULT_PARAMS_FOR_SHARE = "result_params_for_share";//分享标记
    public final static String FROM_FT = "ft";//发帖标识
    public final static String JF = "jf";//发帖成功跳转后，显示的积分数量

    //服务中心字段
    public static final String FLAG_SERVICE_CENTER = "Flag_Service_Center";

    //fragment常量字段
    public static final String CHOOSETIME = "chooseTIME";
    public static final String CHOOSECANCER = "chooseCANCER";
    public static final String CIRCLETYPE = "circleTYPE";
    public static final String FRAGMENT_CALL_BACK = "fragment_call_back";
    public static final String FRAGMENT_TYPE = "fragment_type";
    public static final String FRAGMENT_TAG = "fragment_tag";
    public static final String FRAGMENT_FLAG = "fragment_flag";
    public static final String FRAGMENT_DATA = "fragment_data";
    public static final int FRAGMENT_CHOOSE_CANCER = 1;
    public static final int FRAGMENT_INFO_STEP = 2;
    public static final int FRAGMENT_EDIT_TAB = 3;
    public static final int FRAGMENT_HOME_BANNER = 4;
    public static final int FRAGMENT_CANCEL = -5;

    //分享功能常量字段
    public static final int SHARE_APP = 1;//App推荐
    public static final int SHARE_RECORD = 2;//分享病历
    public static final int SHARE_ARTICLE = 3;//分享文章
    public static final int SHARE_FORUM = 4;//分享帖子
    public static final int SHARE_RESULT = 5;//分享决策树
    public static final String SHAREURL = "shareURL";
    public static final String SHARETITLE = "shareTITLE";
    public static final String SHARECONTENT = "shareCONTENT";
    public static final String SHAREIMGURL = "shareIMGURL";
    public static final String SHAREFLAG = "shareFLAG";

    //患者详情旧接口SetBit值
    public static final int AVATAR_CHANGE = 1 << 0;//头像修改
    public static final int NAME_CHANGE = 1 << 1;//姓名
    public static final int SEX_CHANGE = 1 << 2;//性别
    public static final int AGE_CHANGE = 1 << 3;//age
    public static final int LOCATION_CHANGE = 1 << 4;//location
    public static final int CANCER_TYPE_CHANGE = 1 << 5;//cancer_type
    public static final int CANCER_POS = 1 << 6;//转移
    public static final int GENE_CHANGE = 1 << 7;//gene
    public static final int DISCOVER_TIME_CHANGE = 1 << 8;//discoverTime
    public static final int TNM_CHANGE = 1 << 9;//period type he id 分期

    //微信和QQ登录接口标识常量
    public static final int GUIDE_GET_USER_INFO_WX = 110;
    public static final int GUIDE_GET_USER_INFO_QQ = 111;

    //文章H5页面获取数据的Js字符串
    public static final String ZY_JS_ARTICLE = "var kaqsummary;" +
            "var kaqheadurl;" +
            "var kaqa = document.querySelector(\"meta[name='description']\");" +
            "var kaqb = document.querySelector(\"meta[name='headerurl']\");" +
            "if (kaqa==null) {kaqsummary=null;}else {kaqsummary = kaqa.content;}" +
            "if(kaqb==null){kaqheadurl=null;}else{kaqheadurl = kaqb.content;}" +
            "window.kaq.showSource(kaqsummary,kaqheadurl);";
    //帖子H5页面获取数据的Js字符串
    public static final String ZY_JS_FORUM = "var kaqsummary;" +
            "var kaqheadurl;" +
            "var kaqa = document.querySelector(\"meta[name='description']\");" +
            "var kaqb = document.querySelector(\"meta[name='headerurl']\");" +
            "if (kaqa==null) {kaqsummary=null;}else {kaqsummary = kaqa.content;}" +
            "if(kaqb==null){kaqheadurl=null;}else{kaqheadurl = kaqb.content;}" +
            "window.kaq.showSource(kaqsummary,kaqheadurl);";
    //点击事件统计位置
    public static final String CLICK_EVENT_1 = "1";//横幅，主页弹窗等
    public static final String CLICK_EVENT_2 = "2";//主页快速入口
    public static final String CLICK_EVENT_3 = "3";//直播
    public static final String CLICK_EVENT_4 = "4";//公益活动
    public static final String CLICK_EVENT_5 = "5";//圈子快速入口
    public static final String CLICK_EVENT_6 = "6";//广告列表

    //日志输出tag
    public static class TAG {

        //请求模块
        public static final String ZY_HTTP = "ZYHttpClientManager";
        //数据模块
        public static final String ZY_DATA = "ZYD";

        //测试模块
        public static final String ZY_TEST = "ZYT";

        //统计模块
        public static final String ZY_UMENG = "ZYU";
        //分享业务
        public static final String ZY_SHARE = "ZYA";
        //页面生命周期
        public static final String ZY_VIEW_LIFE = "ZYV";
        //其他
        public static final String ZY_OTHER = "ZYO";
    }

}
