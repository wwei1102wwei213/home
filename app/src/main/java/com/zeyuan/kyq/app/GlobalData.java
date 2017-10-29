package com.zeyuan.kyq.app;

import com.zeyuan.kyq.bean.TNMObj;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/25.
 *
 */
public class GlobalData  {


    /**cancerID:cancer name*/
//    public static Map<String, String> values;//这个里面存着所有的id对应的值
    public static Map<String, String> performValues;//这个里面存着所有的id对应的值  //over
//    public static Map<String, String> cityValues;//这个里面存着所有的id对应的值
    public static Map<String, String> circleValues;//这个里面存着所有的id对应的值  //over

    public static boolean IsHaveLogin;//是否登录过 去判断 是否拉取图片 true 去拉取图片 false 不拉取图片
    public static String stepUid;
    public static LinkedHashMap<String, LinkedHashMap<String, List<String>>> cancerCure;//癌症种类对应的药物选择<cancerid ,cureid,stepList> //over
    public static LinkedHashMap<String, String> cureId;//判断 药物类型 即得到cureid //over
    public static Map<String, String> cureValues;//这个里面存着所有的id对应的值 //over
    public static Map<String, String> transferpos;//转移情况 //over

//    public static LinkedHashMap<String, List<String>> cureData;//治疗方案的数据

    public static Map<String, List<String>> gene;//突变基因  //over
    public static Map<String, String> geneValues;//突变基因键值对 //over


    public static Map<String, Map<String, String>> cancerPos;//这个是癌症类型
    //    public static Map<String, List<String>> perform;//查症状所对应的症状
    public static Map<String, List<String>> cancerData;//癌症种类及对应子癌症 键是parentid 值是 对应的子癌症种类   //over
    public static Map<String, String> cancerValues;//癌症所对应的所有id  //over


    public static Map<String, List<String>> digitData;//癌症所对应的所有数字分期 键是 癌症id 值是 对应的数字分期   //over
    public static Map<String, List<String>> digitT;//癌症所对应的所有t分期 键是 癌症id 值是 对应的分期  //over
    public static Map<String, List<String>> digitN;//癌症所对应的所有n分期 键是 癌症id 值是 对应的分期  //over
    public static Map<String, List<String>> digitM;//癌症所对应的所有m分期 键是 癌症id 值是 对应的分期  //over
//    public static Map<String, String> getDigit;//通过3个tnm 来确定 tnmid 和digitid
    public static Map<String, String> DigitValues;//tnm的键值对  //over
    public static List<TNMObj> tnmObjs;  //over
    private static final String APP_ID = "wx02cca444de652c20";//微信

    public static LinkedHashMap<String, List<String>> bodyPos;//身体部位  //over
//    public static LinkedHashMap<String, List<String>> cityData;//城市 键是省份的id 值是省份对应的省市的id列表

    public static LinkedHashMap<String, String> otherStep;//城市 键是省份的id 值是省份对应的省市的id列表   //over

    public static  String UnionId; //微信拉取授权获得openid unionid

    /**
     * "id":"1000000",
     * "cureconfid":"0",
     * "data":[
     * "411",
     * "415"
     * ]
     */
    public static LinkedHashMap<String, List<String>> stepUinon;//<cancerid,step> 对应的药物
    public static Map<String, String> stepUinonValue;//<stepunionid,cureconfid>

    public static String mUserNickname;
    /**
     * 用户社交平台原始的头像url
     */
    public static String mUserHeadUrl_;
    public static String type;//1 qq登录 2为微信登录
    public static String code;//微信登陆返回的code


    /*static {
//        regToWX();
//        context = this;
        otherStep = new LinkedHashMap<>();
        tnmObjs = new ArrayList<>();
        circleValues = new LinkedHashMap<>();

//        values = new LinkedHashMap<>();//用这个是因为要排序
//        values.put("0", "0");
//        cancerPos = new LinkedHashMap<>();
//        cityValues = new LinkedHashMap<>();
        stepUinon = new LinkedHashMap<>();
        stepUinonValue = new LinkedHashMap<>();

        cureValues = new LinkedHashMap<>();
        performValues = new LinkedHashMap<>();
        cureId = new LinkedHashMap<>();

        gene = new LinkedHashMap<>();
        geneValues = new LinkedHashMap<>();
        transferpos = new LinkedHashMap<>();
        DigitValues = new LinkedHashMap<>();

//        getDigit = new LinkedHashMap<>();
        digitData = new LinkedHashMap<>();
        digitT = new LinkedHashMap<>();
        digitN = new LinkedHashMap<>();
        digitM = new LinkedHashMap<>();
        cancerValues = new LinkedHashMap<>();
        cancerCure = new LinkedHashMap<>();

//        cityData = new LinkedHashMap<>();
        bodyPos = new LinkedHashMap<>();
//        data = new ArrayList<>();
        cancerData = new LinkedHashMap<>();
//        perform = new LinkedHashMap<>();
    }*/

    public static boolean mainPageFlag = false;
}
