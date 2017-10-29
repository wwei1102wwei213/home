package com.zeyuan.kyq.utils;

import android.util.SparseArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/3/17.
 * 主配置信息获取的帮助类
 *
 */
public class SyncConfigUtils {

    private static final String CityIDData = "CityIDData";
    private static final String DATA = "data";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String CITY = "City";

    /***
     * 封装所有城市及省份的健值对
     * 用于id索引,快速定位城市及省份
     *
     * @param config
     * @return 城市及省份的健值对
     * @throws JSONException
     */
    public static SparseArray<String> parseCitys(String config){
        try {
            JSONObject cityIDData = new JSONObject(config);
            JSONArray cityData = cityIDData.getJSONArray(DATA);//city字段下的data
            SparseArray<String> citys = new SparseArray<>();
            JSONObject dataItem;
            JSONObject city;
            for (int i = 0; i < cityData.length(); i++) {
                dataItem = cityData.getJSONObject(i);//data下的每个item
                String id = dataItem.getString(ID);//省份的id
                String name = dataItem.getString(NAME);//对应省份的name
                citys.put(Integer.valueOf(id),name);
                city = dataItem.getJSONObject(CITY);//这个object里面装着一个省下的城市
                String key;
                Iterator<String> iteratorKey = city.keys();//
                while (iteratorKey.hasNext()) {//这儿不是顺序的.
                    key = iteratorKey.next();
                    citys.put(Integer.valueOf(key),city.getString(key));
                }
            }
            return citys;
        }catch (JSONException e){
            ExceptionUtils.ExceptionSend(e, "MapDataUtils:城市索引类解析异常");
        }
         return null;
    }

    /***
     * 每个省份的子城市
     *
     * @param config
     * @return 子城市的ID列表
     * @throws JSONException
     */
    public static List<String> parseChildCitys(String config){
        try {
            JSONObject object = new JSONObject(config);
            List<String> keyList = new ArrayList<>();
            String key;
            Iterator<String> iteratorKey = object.keys();//
            while (iteratorKey.hasNext()) {//这儿不是顺序的.
                key = iteratorKey.next();
                keyList.add(key);
            }
            Collections.sort(keyList);
            return keyList;
        }catch (JSONException e){
            ExceptionUtils.ExceptionSend(e, "MapDataUtils:子城市类解析异常");
        }
        return null;
    }
}
