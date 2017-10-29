package com.zeyuan.kyq.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.CircleTypeAdapter;
import com.zeyuan.kyq.biz.forcallback.CancerZmInterface;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;

/**
 * Created by Administrator on 2016/6/21.
 *
 * 该窗口已废弃
 *
 * @author wwei
 */
public class CancerZmFragment extends DialogFragment implements AdapterView.OnItemClickListener{

    public static final String type = "CancerZmFragment";
    public static CancerZmFragment instance;
    private CancerZmInterface callback;
    private int flag;
    private ListView lv;
    private String[] typeIDs;
    private CircleTypeAdapter adapter;


    public static CancerZmFragment getInstance(CancerZmInterface callback,int flag){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Const.CHOOSECANCER,callback);
        bundle.putInt(Const.CIRCLETYPE,flag);
        instance = new CancerZmFragment();
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            Bundle bundle = getArguments();
            callback = (CancerZmInterface)bundle.getSerializable(Const.CHOOSECANCER);
            flag = bundle.getInt(Const.CIRCLETYPE);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.cancer_dialog);
        dialog.setCancelable(false);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_cancer_zm, null);
        initView(view);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT ;
        lp.gravity = Gravity.TOP;
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.mycircletype);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }
    private void initView(View view){
        try {
            view.findViewById(R.id.tv_top_space).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            view.findViewById(R.id.rl_other_space).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            view.findViewById(R.id.ll_body).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            lv = (ListView)view.findViewById(R.id.lv_circle_type);
            String[] titles;
            int[] imgs;

            if(flag == 7003){
                titles = new String[]{
                        "全部癌种",
                        "肺癌",
                        "肝癌",
                        "肠癌",
                        "乳腺癌",
                        "胃癌",
                        "食管癌",
                        "其他癌种"
                };
                imgs = new int[]{
                        R.mipmap.cancer_all,
                        R.mipmap.cancer_fei,
                        R.mipmap.cancer_gan,
                        R.mipmap.cancer_chang,
                        R.mipmap.cancer_bobo,
                        R.mipmap.cancer_wei,
                        R.mipmap.cancer_shiguan,
                        R.mipmap.cancer_other
                };
                typeIDs = new String[]{"", "10377", "10378", "10379", "10380", "10381", "10382", "10383"};
            }else if(flag == 7007){
                titles = new String[]{
                        "全部", "保健品", "常用药品", "医疗器械", "其他"
                };
                imgs = new int[]{
                        R.mipmap.zh_all,
                        R.mipmap.zh_baojian,
                        R.mipmap.zh_changyong,
                        R.mipmap.zh_qixie,
                        R.mipmap.zh_other
                };
                typeIDs = new String[]{"", "10384", "10385", "10386", "10387"};
            }else{
                titles = new String[]{};
                imgs = new int[]{};
                typeIDs = new String[]{};
            }
            adapter = new CircleTypeAdapter(getActivity(),imgs,titles);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(this);

        }catch (Exception e) {
            ExceptionUtils.ExceptionToUM(e, getActivity(), "ShareFragment");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(position>typeIDs.length-1){

        }else{
            callback.chooseCallBack(adapter.getItem(position),typeIDs[position],position);
            dismiss();
        }
    }

    @Override
    public void onPause() {
        dismiss();
        super.onPause();
    }
}
