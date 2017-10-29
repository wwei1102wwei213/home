package com.zeyuan.kyq.utils;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * 文字处理方法
 *
 * Created by Administrator on 2017/2/6.
 */
public class TextSpannerUtils {

    public static final String REX_ALL = "<span style=\"([^\"]+)\">";
    public static final String STYLE_COLOR = "color";
    public static final String STYLE_SIZE = "font-size";
    public static final String STYLE_FAMILY = "font-family";

    public static String getSpannerText(String str){
        if (TextUtils.isEmpty(str)) return "";
        try {
            str = str.replaceAll("</span>","</font>");
            str = getRecursiveText(str);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"文字处理类出错");
        }
        return str;
    }

    private static String getRecursiveText(String s){
        if (TextUtils.isEmpty(s)) return "";
        try {
            Pattern p = Pattern.compile(REX_ALL);
            Matcher m = p.matcher(s);
            boolean find = m.find();
            if (find){
                String s0 = m.group(0);
                String s1 = m.group(1);
                if (!TextUtils.isEmpty(s1)){
                    String ch = "<font";
                    String[] args0 = s1.split(";");
                    Map<String,String> map = new HashMap<>();
                    if (args0.length>0){
                        for (String arg_str:args0){
                            String[] args1 = arg_str.split(":");
                            if (args1.length>0&&args1.length==2){
                                map.put(args1[0],args1[1]);
                            }
                        }
                    }
                    if (map.size()>0){
                        if (!TextUtils.isEmpty(map.get(STYLE_COLOR))) ch += " color=\"" +map.get(STYLE_COLOR) +"\"";
                    }
                    ch += ">";
                    s = s.replace(s0,ch);
                }
                s = getRecursiveText(s);
                return s;
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"文字处理类出错");
        }
        return s;
    }

}
