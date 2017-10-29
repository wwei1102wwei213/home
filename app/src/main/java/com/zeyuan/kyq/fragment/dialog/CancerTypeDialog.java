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
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.CancerDialogLeftAdapter;
import com.zeyuan.kyq.adapter.CancerDialogRightAdapter;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-11-10
 * Time: 11:57
 * FIXME
 */


public class CancerTypeDialog extends DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = "CancerTypeDialog";
    private View rootView;
    public Map<String,Integer> selectData;//标示 记录左侧列表哪个自选项被选中

    private ListView leftListview;
    private ListView rightListview;
    private CancerDialogLeftAdapter leftAdapter;
    private List<String> rightData;
    private Button cancle;
    private Button confirm;
    private List<String> leftData;
    private Map<String, List<String>> cancerData;
    //    private DrugsNameListener drugsNameListener;
    private CancerDialogRightAdapter rightAdapter;

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
    private TextView title;
    private void initView() {
        leftListview = (ListView) rootView.findViewById(R.id.left_listview);
        leftListview.setDivider(null);
        rightListview = (ListView) rootView.findViewById(R.id.rigth_listview);
        cancle = (Button) rootView.findViewById(R.id.cancle);
        confirm = (Button) rootView.findViewById(R.id.confirm);
        title = (TextView) rootView.findViewById(R.id.title);
        title.setText("选择患者的癌症种类");
        cancle.setOnClickListener(this);
        confirm.setOnClickListener(this);
        /**
         * 左边的listview
         *
         */
        leftAdapter = new CancerDialogLeftAdapter(getActivity(), leftData);
        leftListview.setAdapter(leftAdapter);

        /**
         * 右边的listview
         */
        rightAdapter = new CancerDialogRightAdapter(getActivity(), rightData);

        rightListview.setAdapter(rightAdapter);

        leftListview.setOnItemClickListener(this);
        rightListview.setOnItemClickListener(this);
//        leftAdapter.setSelectedPosition(0);
    }

    private DialogFragmentListener onCancerTyperListener;


    public void setOnCancerTyperListener(DialogFragmentListener onCancerTyperListener) {
        this.onCancerTyperListener = onCancerTyperListener;
    }

    private void initData() {
        selectData = new HashMap<>();
        leftData = new ArrayList<>();
        leftData.add("0");
        rightData = new ArrayList<>();
        cancerData = (Map<String, List<String>>) Factory.getData(Const.N_DataCancerData);
        LogUtil.i(TAG,cancerData.size());
        if (cancerData != null && cancerData.size() > 0) {
//            LogUtil.i(TAG,cancerData.get("0").toString());
            rightData.addAll(cancerData.get("0"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancle:
                dismiss();

                break;
            case R.id.confirm:
                if (TextUtils.isEmpty(CancerType)) {
                    Toast.makeText(getActivity(), "请选择子癌肿", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (onCancerTyperListener != null) {
                    onCancerTyperListener.getDataFromDialog(this,CancerType,0);
                }
                dismiss();
                break;
        }
    }
    private String CancerType;
    private boolean flag = false;
    private int selPosition = -1;//确定选中的癌症种类的位置

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.left_listview: {//左边的listview的点击事件
//                leftAdapter.setSelectedPosition(position);//这个是为了点击效果(点击变白)  变图
                String chooseItem = leftAdapter.getItem(position);//
                List list = cancerData.get(chooseItem);
                if (list != null && list.size() > 0) {
                    rightAdapter.update(list);
                    flag = false;
                }
                selPosition = position;
                for (int i = position + 1; i < leftData.size(); i++) {
                    leftData.remove(i);
                    i--;
                }
                leftAdapter.update(leftData);
                if (selectData.containsKey(chooseItem)) {
                    int selectPosition =  selectData.get(chooseItem);
                    rightAdapter.setSelectedPosition(selectPosition);
                }
                break;
            }


            case R.id.rigth_listview: {//右边listview
                rightAdapter.setSelectedPosition(position);//这个是为了点击效果 字体变蓝
                selectData.put(leftAdapter.getItem(leftAdapter.getCount()-1),position);
                String chooseItem = rightAdapter.getItem(position);
                Map<String,String> cancerValues = (Map<String,String>)Factory.getData(Const.N_DataCancerValues);
                title.setText("您当前选择的癌症：" + cancerValues.get(chooseItem));
                CancerType = chooseItem;
                if (cancerData.get(chooseItem) == null) {//说明这个是最底层的item
                    return;
                }

                if (leftData.contains(chooseItem)) {  //
                    int index = leftData.indexOf(chooseItem);
                    LogUtil.i(TAG, index + "");
                    for (int i = index; i < leftData.size(); i++) {
                        leftData.remove(i);
                    }
                    leftAdapter.update(leftData);
//                    leftAdapter.setSelectedPosition(leftData.size() - 1);
                    selPosition = leftData.size() - 1;

                    return;
                }
                leftData.add(chooseItem);
                leftAdapter.update(leftData);
//                leftAdapter.setSelectedPosition(leftData.size() - 1);
                selPosition = leftData.size() - 1;

                rightData.clear();
                rightData.addAll(cancerData.get(chooseItem));//这代码的意思是 选中右边后 跑到下一级的癌症种类
                rightAdapter.setSelectedPosition(-1);
                rightAdapter.update(rightData);
                break;
            }
        }
    }
}
