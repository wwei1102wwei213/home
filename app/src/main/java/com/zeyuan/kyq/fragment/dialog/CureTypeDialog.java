package com.zeyuan.kyq.fragment.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.CureTypeLeftAdapter;
import com.zeyuan.kyq.adapter.CureTypeRightAdapter;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2015/10/6.
 * <p/>
 * 这个是选药物的dialog
 * 已不复写
 */
public class CureTypeDialog extends DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    public CureTypeDialog() {

    }

    private static final String TAG = "ChooseMedicaDialog";
    private ListView leftListview;
    private ListView rightListview;
    private CureTypeLeftAdapter leftAdapter;
    private Button cancle;
    private Button confirm;
    private List<String> leftData;
    private LinkedHashMap<String, List<String>> cureData;

    private DialogFragmentListener drugsNameListener;
    private CureTypeRightAdapter rightAdapter;


    public void setDrugsNameListener(DialogFragmentListener drugsNameListener) {
        this.drugsNameListener = drugsNameListener;
    }

    private void initData() {
        leftData = new ArrayList<>();

        cureData = (LinkedHashMap<String, List<String>>) Factory.getData(Const.N_DataCancerStepNew, getCancerid());//这个填上对应癌症
        if (cureData == null || cureData.size() == 0)
            return;
        Set<String> leftSet = cureData.keySet();//
        for (String str : leftSet) {//把set转换为list
            leftData.add(str);
        }
        Collections.sort(leftData, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return Integer.valueOf(lhs) - Integer.valueOf(rhs);
            }
        });
    }

    private String cancerId;


    private String getCancerid() {
        if (TextUtils.isEmpty(cancerId)) {
            cancerId = UserinfoData.getCancerID(getActivity());
            if (TextUtils.isEmpty(cancerId)) {
                Toast.makeText(getActivity(), "没有选择癌肿", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        }
        return cancerId;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.dialog);
        dialog.setCancelable(false);
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment, null);
        try {
            initData();
            initView();
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "onCreateDialog");
        }
        dialog.setContentView(rootView);
        return dialog;
    }

    private View rootView;

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

        leftAdapter = new CureTypeLeftAdapter(getActivity(), leftData);
        leftListview.setAdapter(leftAdapter);

        /**
         * 右边的listview
         */
        if (cureData!=null&&cureData.size()>0){
            List<String> temp = cureData.get(leftData.get(0));
            Collections.sort(temp, new Comparator<String>() {
                @Override
                public int compare(String lhs, String rhs) {
                    return Integer.valueOf(lhs) - Integer.valueOf(rhs);
                }
            });
            rightAdapter = new CureTypeRightAdapter(getActivity(), temp);
            rightListview.setAdapter(rightAdapter);
        }




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
                    if (!TextUtils.isEmpty(cure)) {
//                        UserinfoData.saveStepID(getActivity(), cure);
                        drugsNameListener.getDataFromDialog(this, cure, 0);
                    }
                }
                dismiss();
                break;
        }
    }

    private int selPosition = 0;
    private String cure;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.left_listview: {//左边的listview的点击事件
                if (selPosition == position)
                    return;
                selPosition = position;//防止重复点击出现bug
                leftAdapter.setSelectedPosition(position);//这个是为了点击效果(点击变白)
                String chooseItem = leftAdapter.getItem(position);
                List<String> list = cureData.get(chooseItem);
                if (list == null) {
                    list = new ArrayList<>();
                }
                Collections.sort(list, new Comparator<String>() {
                    @Override
                    public int compare(String l1, String l2) {
                        return Integer.valueOf(l1) - Integer.valueOf(l2);
                    }
                });
                rightAdapter.update(list);
                break;
            }
            case R.id.rigth_listview: {//右边listview
                rightAdapter.setSelectChoose(position);//设置选中
                cure = rightAdapter.getItem(position);
                break;
            }
        }
    }

    public void setCancerID(String cancerID) {
        cancerId = cancerID;
    }

}
