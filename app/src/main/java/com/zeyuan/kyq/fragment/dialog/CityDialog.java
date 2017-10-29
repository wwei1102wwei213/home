package com.zeyuan.kyq.fragment.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.Entity.ProvinceEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.CityDialogLeftAdapter;
import com.zeyuan.kyq.adapter.CityDialogRightAdapter;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.SyncConfigUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: zeyuan
 * Date: 2015-11-11
 * Time: 12:40
 * FIXME
 * 患者详情 - 城市选择
 */


public class CityDialog extends DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = "CityDialog";

    public SparseArray<String> citys;
    private ListView leftListview;
    private ListView rightListview;
    private CityDialogLeftAdapter leftAdapter;
    private List<String> rightData;
    private Button cancle;
    private Button confirm;
    private List<String> leftData;
    private Map<String, List<String>> cityData;
    private View rootView;
    private CityDialogRightAdapter rightAdapter;
    private DialogFragmentListener onCitySelListener;
    private List<ProvinceEntity> provinceList;
    private boolean clickable = true;
    private boolean noitemclick = true;
    private boolean cityClick = false;

    public void setOnOnCitySelListener(DialogFragmentListener onCitySelListener) {
        this.onCitySelListener = onCitySelListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.dialog);
        dialog.setCancelable(false);
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment, null);
        initData();
        initView();
        dialog.setContentView(rootView);
        return dialog;
    }

    /***
     * 初始化数据
     */
    private void initData() {
        try {

            citys = (SparseArray<String>)Factory.getData(Const.N_DataAllCity);
            leftData = new ArrayList<>();
            rightData = new ArrayList<>();

            provinceList = (List<ProvinceEntity>) Factory.getData(Const.N_DataCity);
            for(ProvinceEntity province:provinceList){
                leftData.add(province.getId());
            }
            rightData = SyncConfigUtils.parseChildCitys(provinceList.get(0).getCityarray());
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,TAG+"initdata();数据初始化失败");
        }


    }

    private TextView title;

    /***
     * 初始化试图控件
     *
     */
    private void initView() {
        leftListview = (ListView) rootView.findViewById(R.id.left_listview);
        rightListview = (ListView) rootView.findViewById(R.id.rigth_listview);
        cancle = (Button) rootView.findViewById(R.id.cancle);
        confirm = (Button) rootView.findViewById(R.id.confirm);
        title = (TextView) rootView.findViewById(R.id.title);
        title.setText("请选择城市");
        cancle.setOnClickListener(this);
        confirm.setOnClickListener(this);

        leftAdapter = new CityDialogLeftAdapter(getActivity(), leftData,citys);
        leftListview.setAdapter(leftAdapter);

        leftListview.setOnItemClickListener(this);
        rightListview.setOnItemClickListener(this);



        /**
         * 进入窗口自动选中功能
         */
        String id = UserinfoData.getCityID(getActivity());
        if("0".equals(id)){
            rightAdapter = new CityDialogRightAdapter(getActivity(), rightData,citys);
            rightListview.setAdapter(rightAdapter);
            leftListview.setSelection(0);
            leftAdapter.setSelectedPosition(0);
        }else{
            noitemclick = false;
            int proid =getProviderListPosition(id);
            leftListview.setSelection(proid);
            leftAdapter.setSelectedPosition(proid);
            rightData.clear();
            rightData = SyncConfigUtils.parseChildCitys(provinceList.get(proid).getCityarray());
            rightAdapter = new CityDialogRightAdapter(getActivity(), rightData,citys);
            rightListview.setAdapter(rightAdapter);
            int position = getCityListPosition(rightData,id);
            rightListview.setSelection(position);
            rightAdapter.setSelectChoose(position);
        }
    }

    private int getCityListPosition(List<String> rdata,String id){
        for(int i=0;i<rdata.size();i++){
            if(id.equals(rdata.get(i))){
                return i;
            }
        }
        return 0;
    }

    /***
     * 点击事件处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancle: {
                dismiss();
                break;
            }
            case R.id.confirm: {
                try {
                    if(!clickable){
                        Toast.makeText(getActivity(),R.string.choose_city,Toast.LENGTH_SHORT).show();
                    }else {
                        if (onCitySelListener != null) {
                            if (TextUtils.isEmpty(city)){
                                if(!TextUtils.isEmpty(UserinfoData.getCityID(getActivity()))){
                                    city = UserinfoData.getCityID(getActivity());
                                }else{
                                    city = rightData.get(0);
                                }
                                province = city.substring(0,2)+"0000";
                            }else{
                                province = city.substring(0,2)+"0000";
                            }
//                            else {

                                onCitySelListener.getDataFromDialog(this, city, Integer.valueOf(province));//取巧了 第一个城市 第二个是省份
//                            }
                            Log.i(TAG, citys.get(Integer.valueOf(province)) + "   " + citys.get(Integer.valueOf(city)));
                        }
                        dismiss();
                    }
                }catch (Exception e){
                    ExceptionUtils.ExceptionSend(e,"城市选择异常");
                }

                break;
            }
        }
    }
    private String city;
    private String province;

    private int selPosition = -1;

    /***
     * listview子项点击事件处理
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(noitemclick){
            noitemclick = false;
        }
        switch (parent.getId()) {
            case R.id.left_listview: {//左边的listview的点击事件
                if (selPosition == position) return;
                selPosition = position;//防止重复点击出现bug
                leftAdapter.setSelectedPosition(position);//设置点击变白
                province = leftAdapter.getItem(position);
                rightData.clear();
                rightData = SyncConfigUtils.parseChildCitys(provinceList.get(position).getCityarray());
                rightAdapter.update(rightData);
                rightListview.setSelection(0);
                clickable = false;        /**确定按钮点击事件关闭*/
//                cityClick = false;
                break;
            }
            case R.id.rigth_listview: {//右边listview

                rightAdapter.setSelectChoose(position);//设置选中
                city = rightAdapter.getItem(position);
                clickable = true;     /**确定按钮点击事件关闭*/
                break;
            }
        }
    }


    /***
     * 通过城市ID获得省份的下标
     *
     * @param id 城市id
     * @return 省份的下标
     */
    private int getProviderListPosition(String id){
        String providerId = id.substring(0,2)+"0000";
        Log.i(TAG, "城市id:" + providerId);
        for(int i = 0;i<provinceList.size();i++){
            if(providerId.equals(provinceList.get(i).getId())){
                return  i;
            }
        }
        return 0;
    }

}
