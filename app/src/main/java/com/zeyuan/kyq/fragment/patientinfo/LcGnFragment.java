package com.zeyuan.kyq.fragment.patientinfo;

import android.animation.LayoutTransition;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.TNMObj;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.forcallback.ChooseTimeInterface;
import com.zeyuan.kyq.fragment.ChooseTimeFragment;
import com.zeyuan.kyq.fragment.dialog.CityDialog;
import com.zeyuan.kyq.fragment.dialog.DialogFragmentListener;
import com.zeyuan.kyq.fragment.dialog.DigitDialog;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.List;
import java.util.Map;

/**
 * 建立档案的最后三个fragment
 * 填写地址和分期的fragment
 */
public class LcGnFragment extends PatientInfoFragment implements View.OnClickListener, DialogFragmentListener ,ChooseTimeInterface{
    private static final String TAG = "LcGnFragment";


    private View need_dismiss;//需要消失和显示的整体组件
    private TextView digit_desc;//分期的说明文本
    private LinearLayout ll;
    private  FrameLayout whole_content;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_time_and_city, container, false);
        initOtherTitle();
        initNewView();
        return rootView;
    }

    private void initOtherTitle(){
        TextView title = (TextView)findViewById(R.id.tv_other_title);
        title.setText("患者信息");
        findViewById(R.id.iv_other_title_back).setVisibility(View.GONE);
        LinearLayout ll_return_back = (LinearLayout)findViewById(R.id.ll_return_back);
        ll_return_back.setVisibility(View.VISIBLE);
        ll_return_back.setOnClickListener(this);
    }

    private LinearLayout ll_period_choose;
    private TextView tv_info_diagnose_time;
    private TextView tv_info_in_city;
//    private TextView tv_info_name;
    private TextView tv_next_page;
    private LinearLayout ll_temp_miss;
    private View top;
    private TextView tv_cancer_case;
    private TextView tv_far_move;
    private TextView tv_linba;
    private TextView tv_zero;

    private LinearLayout ll_zero;
    private LinearLayout ll_one;
    private LinearLayout ll_two;
    private LinearLayout ll_three;
    private TextView tv_gene_down;
    private TextView tv_next_page2;
    private TextView tv_info_name;
//    private CircleImageView iv_info_image;
    private void initNewView(){

        LinearLayout ll_info_diagnose_time = (LinearLayout)findViewById(R.id.ll_info_diagnose_time);
        LinearLayout ll_info_in_city = (LinearLayout)findViewById(R.id.ll_info_in_city);

        tv_info_diagnose_time = (TextView)findViewById(R.id.tv_info_diagnose_time);

        tv_info_in_city = (TextView)findViewById(R.id.tv_info_in_city);

//        tv_info_name = (TextView)findViewById(R.id.tv_info_name);
//        String mName = UserinfoData.getInfoname(getActivity());
//        if(!TextUtils.isEmpty(mName)){
//            tv_info_name.setText(mName);
//        }
//        iv_info_image = (CircleImageView)findViewById(R.id.iv_info_head_img);
        /*String mUrl = UserinfoData.getAvatarUrl(getActivity());
        if(!TextUtils.isEmpty(mUrl)){
            try {
                Glide.with(this).load(mUrl).error(R.mipmap.head_img_init_diag).into(iv_info_image);
            }catch (Exception e){

            }
        }*/

        LinearLayout ll_period_choose = (LinearLayout)findViewById(R.id.ll_period_choose);
        tv_next_page = (TextView)findViewById(R.id.tv_next_page);
        ll_temp_miss = (LinearLayout)findViewById(R.id.ll_be_to_gone);
        top = findViewById(R.id.top_title);

        ll = (LinearLayout) findViewById(R.id.ll_body);
        whole_content = (FrameLayout) findViewById(R.id.whole_content);
        LayoutTransition mLayoutTransition = new LayoutTransition();
        ll.setLayoutTransition(mLayoutTransition);
        whole_content.setLayoutTransition(mLayoutTransition);
        need_dismiss = LayoutInflater.from(context).inflate(R.layout.view_gene_add, null);

        digit_desc = (TextView) need_dismiss.findViewById(R.id.digit_id);
        tv_gene_down = (TextView)need_dismiss.findViewById(R.id.tv_gene_add);
        tv_zero = (TextView)need_dismiss.findViewById(R.id.zero_current_case);
        tv_cancer_case = (TextView)need_dismiss.findViewById(R.id.tv_cancer_case);
        tv_linba = (TextView)need_dismiss.findViewById(R.id.tv_linba);
        tv_far_move = (TextView)need_dismiss.findViewById(R.id.tv_far_move);
        ll_zero = (LinearLayout)need_dismiss.findViewById(R.id.ll_zero_digit);
        ll_one = (LinearLayout)need_dismiss.findViewById(R.id.ll_one_digit);
        ll_two = (LinearLayout)need_dismiss.findViewById(R.id.ll_two_digit);
        ll_three = (LinearLayout)need_dismiss.findViewById(R.id.ll_three_digit);
        tv_next_page2 = (TextView)need_dismiss.findViewById(R.id.tv_next_page2);

        ll_info_diagnose_time.setOnClickListener(this);
        ll_info_in_city.setOnClickListener(this);
        ll_period_choose.setOnClickListener(this);
        tv_next_page.setOnClickListener(this);
        tv_gene_down.setOnClickListener(this);
        findViewById(R.id.ll_temp_back).setOnClickListener(this);
        ll_zero.setOnClickListener(this);
        ll_two.setOnClickListener(this);
        ll_three.setOnClickListener(this);
        ll_one.setOnClickListener(this);
        tv_next_page2.setOnClickListener(this);
    }


    private boolean flag = true;
    private boolean flags = true;//ture表示显示数字分期 false 表示显示t分期

    private ChooseTimeFragment fragment;
    @Override
    public void onClick(View v) {
        try {

            switch (v.getId()) {
                case R.id.ll_temp_back:
                case R.id.ll_return_back:
                    if (onLastStepClickListener != null) {
                        onLastStepClickListener.onLastStepClickListener(this);
                    }
                    break;
                case R.id.tv_next_page2:
                case R.id.tv_next_page:
                    if (!TextUtils.isEmpty(PeriodID) && !TextUtils.isEmpty(PeriodType)) {
                        setPeriodType(PeriodType);
                        setPeriodID(PeriodID);
                    }
                    if(!TextUtils.isEmpty(city)){
                        setCity(city);
                    }
                    if(!TextUtils.isEmpty(discoverTime)){
                        setDiscoverTime(discoverTime);
                    }
                    if (onNextStepClickListener != null) {
                        onNextStepClickListener.onNextStepClickListener(this);
                        if(v.getId()==R.id.tv_next_page){
                            tv_next_page.setClickable(false);
                        }else {
                            tv_next_page2.setClickable(false);
                        }
                    }
                    break;
                /**N分类*/
                case R.id.ll_two_digit://区域淋巴结 第二个
                    showDigitNDialog();
                    break;
                /**M分类*/
                case R.id.ll_three_digit://远处转移 第三个
                    showDigitMDialog();
                    break;
                /**D分类或者T分类*/
                case R.id.ll_one_digit: {//第一个
                    showDigitTDialog();
                    break;
                }
                case R.id.ll_zero_digit:{
                    showDigitDataDialog();
                    break;
                }
                case R.id.ll_info_in_city: {
                    CityDialog dialog = new CityDialog();
                    dialog.setOnOnCitySelListener(this);
                    dialog.show(getActivity().getFragmentManager(), Contants.CITY_DIALOG);
                    break;
                }
                case R.id.ll_info_diagnose_time:{
                    if(fragment==null){
                        fragment = ChooseTimeFragment.getInstance(this);
                    }
                    fragment.show(getFragmentManager(),ChooseTimeFragment.type);

                    break;
                }

                case R.id.ll_period_choose:{
                    openGene();
                    break;
                }
                case R.id.tv_gene_add:{
                    closeGene();
                    break;
                }

                default:
                    throw new RuntimeException();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "lcgnfragment");
        }
    }


    private String discoverTime;
    @Override
    public void timeCallBack(String time) {
        tv_info_diagnose_time.setText(time);
        discoverTime = DataUtils.showTimeToLoadTime(time);
    }

    /**
     * 当显示填写分期时，点击隐藏分期
     */
    private void closeGene() {
        try {
            findViewById(R.id.ll_temp_back).setVisibility(View.GONE);
            top.setVisibility(View.VISIBLE);
            whole_content.removeView(need_dismiss);
            ll_temp_miss.setVisibility(View.VISIBLE);

        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "lcgnfragment");
        }
    }

    /**
     * 点击填写分期 显示组件
     */
    private void openGene() {
        try {
            top.setVisibility(View.GONE);
            ll.removeView(need_dismiss);
            ll_temp_miss.setVisibility(View.GONE);
            ll.addView(need_dismiss);
            findViewById(R.id.ll_temp_back).setVisibility(View.VISIBLE);

        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "lcgnfragment");
        }
    }

    private String city;

    @Override
    public void getDataFromDialog(DialogFragment dialog, String data, int position) {
        try {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment citydialog = fragmentManager.findFragmentByTag(Contants.CITY_DIALOG);
            Fragment digitDataDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_DIALOG);
            Fragment digitTDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_T_DIALOG);
            Fragment digitNDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_N_DIALOG);
            Fragment digitMDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_M_DIALOG);

            if (dialog == citydialog) {//取巧了 第一个城市 第二个是省份
                city = data;
                setProvince(String.valueOf(position));
                SparseArray<String> citys = (SparseArray<String>)Factory.getData(Const.N_DataAllCity);
                tv_info_in_city.setText(citys.get(Integer.valueOf(data)));
                return;
            }
            if (dialog == digitDataDialog) {//数字分期
                if (DigitDialog.SWITCH.equals(data)) {
                    ll_one.setVisibility(View.VISIBLE);
                    ll_two.setVisibility(View.VISIBLE);
                    ll_three.setVisibility(View.VISIBLE);
                    ll_zero.setVisibility(View.GONE);
                    digit_desc.setText("");
                    flags = false;
                    return;
                }
                PeriodType = "1";
                PeriodID = data;
                String temp = MapDataUtils.getDigitValues(data);
                digit_desc.setText(temp);
                tv_zero.setText(temp);
            }

            if (dialog == digitTDialog) {
                if (DigitDialog.SWITCH.equals(data)) {
                    setDigitGone();
                    return;
                }
                getPatientInfoActivity().setPeriodType("2");
                tv_cancer_case.setText(getShow(data));
                tTemp = data;
                chooseTNMFinish();
            }

            if (dialog == digitNDialog) {
                PeriodType = "2";
                if (DigitDialog.SWITCH.equals(data)) {
                    setDigitGone();
                    return;
                }
                tv_linba.setText(getShow(data));
                nTemp = data;
                chooseTNMFinish();
            }

            if (dialog == digitMDialog) {
                if (DigitDialog.SWITCH.equals(data)) {
                    setDigitGone();
                }
                tv_far_move.setText(getShow(data));
                PeriodType = "2";
                mTemp = data;
                chooseTNMFinish();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "lcgnfragment");
        }
    }

    private void setDigitGone() {
        try {
            ll_one.setVisibility(View.GONE);
            ll_two.setVisibility(View.GONE);
            ll_three.setVisibility(View.GONE);
            flags = true;
            ll_zero.setVisibility(View.VISIBLE);
            digit_desc.setText("");
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "lcgnfragment");
        }
    }

    /**
     * 这个是显示数字分期
     */
    private void showDigitDataDialog() {
        try {
            DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT,getPatientInfoActivity().getCancerID());
            dialog.setListener(this);
            dialog.show(getFragmentManager(), Contants.DIGIT_DIALOG);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "lcgnfragment");
        }
    }

    /***
     * T分期
     */
    private void showDigitTDialog() {
        DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT_T,getPatientInfoActivity().getCancerID());
        dialog.setListener(this);
        dialog.show(getFragmentManager(), Contants.DIGIT_T_DIALOG);
    }

    /***
     * N分期
     */
    private void showDigitNDialog() {
        try {
            DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT_N,getPatientInfoActivity().getCancerID());
            dialog.setListener(this);
            dialog.show(getFragmentManager(), Contants.DIGIT_N_DIALOG);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "lcgnfragment");
        }
    }

    /**
     * M分期
     */
    private void showDigitMDialog() {
        try {
            DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT_M,getPatientInfoActivity().getCancerID());
            dialog.setListener(this);
            dialog.show(getFragmentManager(), Contants.DIGIT_M_DIALOG);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "lcgnfragment");
        }
    }
    private String cancerId = UserinfoData.getCancerID(context);
    private String tTemp;
    private String nTemp;
    private String mTemp;


    private void chooseTNMFinish() {
        try {
            if (!TextUtils.isEmpty(cancerId) && !TextUtils.isEmpty(tTemp) && !TextUtils.isEmpty(nTemp) && !TextUtils.isEmpty(mTemp)) {
                List<TNMObj> list = (List<TNMObj>) Factory.getData(Const.N_DataTnmObjs);
                int size = list.size();
                TNMObj tnmTmp = null;
                for (int i = 0; i < size; i++) {
                    tnmTmp = list.get(i);
                    if ((tnmTmp.getCancerId().equals(cancerId)) &&
                            (tnmTmp.getTid().equals("0") || tnmTmp.getTid().equals(tTemp))
                            && (tnmTmp.getNid().equals("0") || tnmTmp.getNid().equals(nTemp))
                            && (tnmTmp.getMid().equals("0") || tnmTmp.getMid().equals(mTemp))) {
                        String digitID = tnmTmp.getDigitId();
                        String showdigitId = getDigitValues().get(digitID);
                        PeriodID = digitID;
                        if(!TextUtils.isEmpty(showdigitId)){
                            digit_desc.setText(showdigitId);
                        }
                        break;
                    } else {
                        PeriodID = "";
                        String temp =  "未知";
                        digit_desc.setText(temp);
                    }
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "lcgnfragment");
        }
    }

    private Map<String, String> DigitValues;
    private Map<String, String> getDigitValues(){
        if(DigitValues==null){
            DigitValues = (Map<String, String>)Factory.getData(Const.N_DataDigitValues);
        }
        return DigitValues;
    }

    public void getResume() {
        if (tv_next_page != null) {
            tv_next_page.setClickable(true);
        }
        if (tv_next_page2 != null) {
            tv_next_page2.setClickable(true);
        }
    }

    private String PeriodType;
    private void setPeriodType(String periodType) {
        try {
            getPatientInfoActivity().setPeriodType(periodType);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "lcgnfragment");
        }
    }
    private String PeriodID;

    private void setPeriodID(String periodType) {
        try {
            getPatientInfoActivity().setPeriodID(periodType);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "lcgnfragment");
        }
    }

    private void setCity(String periodType) {
        try {
            getPatientInfoActivity().setCity(periodType);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "lcgnfragment");
        }
    }
    private void setProvince(String periodType) {
        try {
            getPatientInfoActivity().setProvince(periodType);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "lcgnfragment");
        }
    }

    private void setDiscoverTime(String time) {
        try {
            getPatientInfoActivity().setDiscoverTime(time);
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, getActivity(), "discovertimefragment");
        }
    }

    private String getDiscoverTime(){
        return getPatientInfoActivity().getDiscoverTime();
    }

    /**
     * 根据选中的id 来获得显示的tnm分期
     *
     * @param data
     * @return
     */
    private String getShow(String data) {
        if (TextUtils.isEmpty(data)) {
            return "";
        }
        String id = getDigitValues().get(data);
        StringBuffer temp = new StringBuffer(id);
        int index = id.indexOf(" ");
        StringBuffer sb = temp.delete(index, temp.length());
        return sb.toString();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if(tv_info_diagnose_time!=null&&!TextUtils.isEmpty(getDiscoverTime())){
                tv_info_diagnose_time.setText(DataUtils.getDateString(getDiscoverTime().concat("000")));
            }
            String temp = getPatientInfoActivity().getCity();
            if(tv_info_in_city!=null&&!TextUtils.isEmpty(temp)){
                tv_info_in_city.setText(MapDataUtils.getCityName(temp));
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"LcGn onResume");
        }
    }


}
