package com.zeyuan.kyq.fragment.staging;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.EditInfoEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.LazyFragment;
import com.zeyuan.kyq.bean.TNMObj;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.fragment.dialog.DialogFragmentListener;
import com.zeyuan.kyq.fragment.dialog.DigitDialog;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.List;
import java.util.Map;

import static com.zeyuan.kyq.R.id.fl_move;

/**
 * Created by Administrator on 2017/8/25.
 * tnm分期选择
 */

public class TNMStagingFragment extends LazyFragment implements DialogFragmentListener, View.OnClickListener {
    private static final String TAG = "TNMStagingFragment";
    TextView tv_primary_tumor;
    TextView tv_region_lymph;
    TextView tv_move;
    EditInfoEntity editInfoEntity;

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_staging_tnm, container, false);
        tv_primary_tumor = (TextView) findViewById(R.id.tv_primary_tumor);
        tv_region_lymph = (TextView) findViewById(R.id.tv_region_lymph);
        tv_move = (TextView) findViewById(R.id.tv_move);
        findViewById(R.id.fl_primary_tumor).setOnClickListener(this);
        findViewById(R.id.fl_region_lymph).setOnClickListener(this);
        findViewById(fl_move).setOnClickListener(this);
        return rootView;
    }

    @Override
    protected void initData() {
        editInfoEntity = (EditInfoEntity) getActivity().getIntent().getSerializableExtra("editInfoEntity");
    }

    @Override
    protected void setDefaultFragmentTitle(String title) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_primary_tumor:
                showDigitTDialog();
                break;
            case R.id.fl_region_lymph:
                showDigitNDialog();
                break;
            case R.id.fl_move:
                showDigitMDialog();
                break;

        }
    }

    //显示T分期
    private void showDigitTDialog() {
        DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT_T, editInfoEntity.getCancerID());
        dialog.setListener(this);
        dialog.setIsSwitchVisible(false);
        dialog.show(getActivity().getFragmentManager(), Contants.DIGIT_T_DIALOG);
    }

    //显示N分期
    private void showDigitNDialog() {
        DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT_N, editInfoEntity.getCancerID());
        dialog.setListener(this);
        dialog.setIsSwitchVisible(false);
        dialog.show(getActivity().getFragmentManager(), Contants.DIGIT_N_DIALOG);
    }

    //显示M分期
    private void showDigitMDialog() {
        DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT_M, editInfoEntity.getCancerID());
        dialog.setListener(this);
        dialog.setIsSwitchVisible(false);
        dialog.show(getActivity().getFragmentManager(), Contants.DIGIT_M_DIALOG);
    }

    private String tTemp;
    private String nTemp;
    private String mTemp;

    @Override
    public void getDataFromDialog(DialogFragment dialog, String data, int position) {
        android.app.FragmentManager fragmentManager = getActivity().getFragmentManager();
        Fragment digitTDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_T_DIALOG);
        Fragment digitNDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_N_DIALOG);
        Fragment digitMDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_M_DIALOG);
        if (dialog == digitTDialog) {
            LogUtil.i(TAG, "digitTDialog:" + data);
            if (DigitDialog.SWITCH.equals(data)) {//切换分期
                return;
            }
            tv_primary_tumor.setText(getShow(data));
            tTemp = data;

        }

        if (dialog == digitNDialog) {
            LogUtil.i(TAG, "digitNDialog:" + data);
            if (DigitDialog.SWITCH.equals(data)) {
                return;
            }
            tv_region_lymph.setText(getShow(data));
            nTemp = data;

        }

        if (dialog == digitMDialog) {
            LogUtil.i(TAG, "digitMDialog:" + data);
            if (DigitDialog.SWITCH.equals(data)) {
                return;
            }
            tv_move.setText(getShow(data));
            mTemp = data;

        }
    }

    /**
     * 根据选中的id 来获得显示的tnm分期
     *
     * @param data tnm分期的id
     * @return 根据tnm分期的id获得的名称
     */
    private String getShow(String data) {
        if (TextUtils.isEmpty(data)) {
            return "";
        }
//        String name = getDigitValues().get(data);
//        StringBuilder temp = new StringBuilder(name);
        return getDigitValues().get(data);
//        int index = id.indexOf(" ");
//        StringBuilder sb = temp.delete(index, temp.length());
//        return sb.toString();
    }

    private Map<String, String> DigitValues;

    private Map<String, String> getDigitValues() {
        if (DigitValues == null) {
            DigitValues = (Map<String, String>) Factory.getData(Const.N_DataDigitValues);
        }
        return DigitValues;
    }

    private String digitID;

    //根据tnm分期计算出数字分期
    public String getSelectedDigitID() {
        if (!TextUtils.isEmpty(tTemp) && !TextUtils.isEmpty(nTemp) && !TextUtils.isEmpty(mTemp)) {
            List<TNMObj> list = (List<TNMObj>) Factory.getData(Const.N_DataTnmObjs);
            int size = list.size();
            TNMObj tnmTmp;
            for (int i = 0; i < size; i++) {
                tnmTmp = list.get(i);
                if ((tnmTmp.getCancerId().equals(getCancerId())) &&
                        (tnmTmp.getTid().equals("0") || tnmTmp.getTid().equals(tTemp))

                        && (tnmTmp.getNid().equals("0") || tnmTmp.getNid().equals(nTemp))

                        && (tnmTmp.getMid().equals("0") || tnmTmp.getMid().equals(mTemp))) {
                    digitID = tnmTmp.getDigitId();
                    LogUtil.i(TAG, "digitid is :" + digitID);
                    String showdigitId = getDigitValues().get(digitID);
//                    digit.setText(showdigitId);
                    return digitID;

                } else {
//                    digit.setText("未知");
                }
            }
        }
        return "";
    }

    private String getCancerId() {
        return editInfoEntity.getCancerID();
    }
}
