package com.zeyuan.kyq.fragment.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.DialogLeftAdapter;
import com.zeyuan.kyq.adapter.DialogRightAdapter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.LogCustom;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2015/10/6.
 * <p/>
 * 这个是选药物的dialog
 * 已不复写
 */
public class ChooseMedicaDialog extends DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    /**
     * 这个是dialog的类型。
     */
    public static final String TYPE_MEDICA = "cure";//这个是选药

    public static final String CancerPos = "CancerPos";//这个是选药

    public static final String CITY = "City";//这个是城市


    private static final String TAG = "ChooseMedicaDialog";
    private static final String TYPE = "type";
//    private String type;
    private ListView leftListview;
    private ListView rightListview;
    private DialogLeftAdapter leftAdapter;
    private List<List<String>> mRightDatas;
    private Button cancle;
    private Button confirm;
    private List<String> leftData;
    private LinkedHashMap<String, List<String>> cureData;
    private DialogFragmentListener drugsNameListener;
    private DialogRightAdapter rightAdapter;
    private String chooseIds = "";

    private List<String> leftChecked;
    private static ChooseMedicaDialog instance;


    public  ChooseMedicaDialog() {

    }

    public void setChooseIds(String chooseIds){
        LogCustom.i("ZYS","ChooseIDS:"+chooseIds);
        this.chooseIds = chooseIds;
        try {
            leftChecked = new ArrayList<>();
            if (!TextUtils.isEmpty(chooseIds)){
                String[] args = chooseIds.split(",");
                for (int i = 0;i<args.length;i++){
                    leftChecked.add(args[i]);
                }
            }
        }catch (Exception e){

        }
    }

    public void setLeftChecked(List<String> leftChecked){
        this.leftChecked = leftChecked;
    }

    public void setCureData(LinkedHashMap<String, List<String>> cureData) {
        this.cureData = cureData;
    }

    /*public ChooseMedicaDialog(LinkedHashMap<String, List<String>> data) {
        cureData = data;
    }*/

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        if (getArguments() != null) {
//            type = getArguments().getString(TYPE);
//        }
//        super.onCreate(savedInstanceState);
//    }

//    public interface DrugsNameListener {
//        void getDrugsName(List data, int id);
//    }

    /*public static ChooseMedicaDialog getInstance(List<String>[] data){
        Bundle bundle = new Bundle();
        bundle.putSerializable("leftChooseData",data);
        if (instance==null){
            instance = new ChooseMedicaDialog();
        }
        instance.setArguments(bundle);
        return instance;
    }*/

    public void setDrugsNameListener(DialogFragmentListener drugsNameListener) {
        this.drugsNameListener = drugsNameListener;
    }



    private List<String>[] data;//这个数组表示多个右边选中的项目。data[0]就是右边listview中选中的每个item项目
    public void setData(List<String>[] data){
        this.data = data;
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle!=null){
            data = (List<String>[])bundle.getSerializable("leftChooseData");
        }
    }*/

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

    private View rootView;

    private void initData() {
        leftData = new ArrayList<>();

//        LogUtil.i("MainActivity", (cureData.size()) + "");

        Set<String> leftSet = cureData.keySet();
        for (String str : leftSet) {//把set转换为list
            leftData.add(str);
        }

        /**
         * 初始化data
         */
        if (data==null){
            data = new List[leftData.size()];//
            for (int i = 0; i < leftData.size(); i++) {
                List<String> temp = cureData.get(leftData.get(i));
                data[i] = new ArrayList<>();
                if (leftChecked!=null&&leftChecked.size()>0){
                    for (int j = 0;j<leftChecked.size();j++){
                        if (temp.contains(leftChecked.get(j))){
                            data[i].add(leftChecked.get(j));
                        }
                    }
                }
            }
        }

        mRightDatas = new ArrayList<>();//new出一个数组里面装着他们的adapter
        /**
         * 得到左边的键
         */
        for (int i = 0; i < leftData.size(); i++) {
//            List<String> datas = cureData.get(leftData.get(i));

            mRightDatas.add(cureData.get(leftData.get(i)));
        }
    }

    private String getCancerid() {
        String cancerid = UserinfoData.getCancerID(getActivity());
        if (TextUtils.isEmpty(cancerid)) {
            Toast.makeText(getActivity(), "没有选择癌肿", Toast.LENGTH_SHORT).show();
            dismiss();
        }
        return cancerid;
    }

    private void initView() {
        leftListview = (ListView) rootView.findViewById(R.id.left_listview);
        rightListview = (ListView) rootView.findViewById(R.id.rigth_listview);
        cancle = (Button) rootView.findViewById(R.id.cancle);
        confirm = (Button) rootView.findViewById(R.id.confirm);

        cancle.setOnClickListener(this);
        confirm.setOnClickListener(this);
        /**
         * 左边的listview
         *
         */

        leftAdapter = new DialogLeftAdapter(getActivity(), leftData, Contants.diolog_type,data);
        leftListview.setAdapter(leftAdapter);

        /**
         * 右边的listview
         */

        rightAdapter = new DialogRightAdapter(getActivity(), mRightDatas.get(0),Contants.diolog_type,chooseIds);
        rightListview.setAdapter(rightAdapter);


        leftListview.setOnItemClickListener(this);
        rightListview.setOnItemClickListener(this);
        leftAdapter.setSelectedPosition(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancle:
                dismiss();
                break;
            case R.id.confirm:
                if (drugsNameListener != null) {
                    if (!TextUtils.isEmpty(getAllDrugs())) {
                        chooseIds = getAllDrugs();
                        drugsNameListener.getDataFromDialog(this, getAllDrugs(), 0);
                    }
                }
                dismiss();
//                dismissAllowingStateLoss();
                break;
        }
    }

    private int selPosition = 0;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.left_listview: {//左边的listview的点击事件
                if (selPosition == position)
                    return;
                selPosition = position;//防止重复点击出现bug

                leftAdapter.setSelectedPosition(position);//这个是为了点击效果(点击变白)

                rightAdapter.updateCount(data[position], mRightDatas.get(position));

                break;
            }
            case R.id.rigth_listview: {//右边listview

                DialogRightAdapter adapter = (DialogRightAdapter) parent.getAdapter();
                int count = mRightDatas.indexOf(adapter.getData());

                CheckBox cb = (CheckBox) view.findViewById(R.id.cb);
                if (cb.isChecked()) {
                    data[count].remove(adapter.getItem(position));
                    cb.setChecked(false);
                    leftAdapter.updateCount(data);

                } else {
                    data[count].add(adapter.getItem(position));
                    cb.setChecked(true);
                    leftAdapter.updateCount(data);
                }

                break;
            }
        }
    }

//    private void change

    /**
     * 没有判断checkDrugs.size()是否大于0 在调用前判断下
     */
    private String getAllDrugs() {

        List<String> list = new ArrayList();
        for (int i = 0; i < data.length; i++) {
            for (String str : data[i]) {
                list.add(str);
            }
        }
        LogUtil.i(TAG, "选中药物的个数是：" + list.size());

        return DataUtils.listToLoadString(list);
    }

}
