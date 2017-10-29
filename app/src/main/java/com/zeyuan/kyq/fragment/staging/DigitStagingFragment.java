package com.zeyuan.kyq.fragment.staging;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeyuan.kyq.Entity.EditInfoEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.LazyFragment;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.fragment.dialog.DialogFragmentListener;
import com.zeyuan.kyq.fragment.dialog.DigitDialog;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.UiUtils;

import java.util.Map;

/**
 * Created by Administrator on 2017/8/25.
 * 数字分期
 */

public class DigitStagingFragment extends LazyFragment {

    TextView tv_selected_staging;
    TextView tv_select_period;
    String cancerName;
    String selectedDigitID;
    EditInfoEntity editInfoEntity;


    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_staging_digit, container, false);
        tv_selected_staging = (TextView) findViewById(R.id.tv_selected_staging);
        tv_select_period = (TextView) findViewById(R.id.tv_select_period);
        editInfoEntity = (EditInfoEntity) getActivity().getIntent().getSerializableExtra("editInfoEntity");
        ImageView iv_body = (ImageView) findViewById(R.id.iv_body);
        int cancerImgId = UiUtils.getCancerTypeImage(editInfoEntity.getCancerID());
        iv_body.setImageResource(cancerImgId);
        tv_select_period.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDigitTDialog();
            }
        });
        return rootView;
    }

    @Override
    protected void initData() {
        cancerName = ((Map<String, String>) Factory.getData(Const.N_DataCancerValues)).
                get(editInfoEntity.getCancerID());
    }

    //显示T分期
    private void showDigitTDialog() {
        DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT, editInfoEntity.getCancerID());
        dialog.setListener(new DialogFragmentListener() {
            @Override
            public void getDataFromDialog(DialogFragment dialog, String data, int position) {
                LogUtil.i("showDigitTDialog", data);
                String temp = getDigitValues().get(data);
                SpannableString ss = new SpannableString(cancerName + temp);
                if (!TextUtils.isEmpty(cancerName) && !TextUtils.isEmpty(temp))
                    ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.text_blue)), cancerName.length(), cancerName.length() + temp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_selected_staging.setText(ss);
                selectedDigitID = data;
            }
        });
        dialog.setIsSwitchVisible(false);
        dialog.show(getActivity().getFragmentManager(), Contants.DIGIT_DIALOG);
    }

    @Override
    protected void setDefaultFragmentTitle(String title) {

    }


    private Map<String, String> DigitValues;

    private Map<String, String> getDigitValues() {
        if (DigitValues == null) {
            DigitValues = (Map<String, String>) Factory.getData(Const.N_DataDigitValues);
        }
        return DigitValues;
    }

    public String getSelectedDigitID() {
        return selectedDigitID;
    }
}
