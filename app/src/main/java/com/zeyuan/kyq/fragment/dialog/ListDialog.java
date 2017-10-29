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

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.ListDialogAdapter;
import com.zeyuan.kyq.utils.LogUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/1/7.
 */
public class ListDialog extends DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static final int Step_DIALOG = 0;//里面是药物的dialog
//    public static final int CHEMICA_DIALOG = 1;//最后有效化疗药的dialog
//    public static final int ZDY_DX = 1;//自定义单选 显示时不对数据进行加工
public static final int OTHER_DIALOG = 1;//里面是药物的dialog


    public static final int TRANS_POS = 1;
    private static final String TAG = "ListDialog";

    private View rootView;
    private ListView leftListview;
    private ListView rightListview;
    private List<String> data;
    private ListDialogAdapter adapter;
    private DialogFragmentListener listener;

    public void setListener(DialogFragmentListener listener) {
        this.listener = listener;
    }

    public ListDialog() {

    }

    private int type;
//    private int posion;

    /**
     *
     * @param data 数据源
     * @param type 类型
     * @param posion  有选项时选中的位置
     */
    /*public ListDialog(List<String> data, int type,int posion) {
        super();

    }*/

    public void setData(List<String> data, int type,int posion){
        this.data = data;
        this.type = type;
        this.selectPosition = posion;
    }

    public static ListDialog newInstance(Bundle args) {
//        Bundle args = new Bundle();
        ListDialog fragment = new ListDialog();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.dialog);
        dialog.setCancelable(false);
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment, null);
        initData();
        adapter = new ListDialogAdapter(data, getActivity(),type);
        initView();
        dialog.setContentView(rootView);
        return dialog;
    }

    private Button cancle;
    private Button confirm;
    private TextView title;

    private void initView() {
        leftListview = (ListView) rootView.findViewById(R.id.left_listview);
        leftListview.setDivider(null);
        leftListview.setVisibility(View.GONE);
        rightListview = (ListView) rootView.findViewById(R.id.rigth_listview);
        cancle = (Button) rootView.findViewById(R.id.cancle);
        confirm = (Button) rootView.findViewById(R.id.confirm);
        title = (TextView) rootView.findViewById(R.id.title);
        setTitle();
        rightListview.setAdapter(adapter);
        adapter.setSelectPostion(selectPosition);
        rightListview.setOnItemClickListener(this);
        rightListview.setSelection(selectPosition);
        cancle.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancle: {
                dismiss();
                break;
            }

            case R.id.confirm: {
                if (selectPosition == -1) {//说明这个是取消
                    listener.getDataFromDialog(this, "", 0);
                    dismiss();
                    return;
                }
                String selectItem = adapter.getItem(selectPosition);
                if (listener != null && !TextUtils.isEmpty(selectItem)) {
                    LogUtil.i(TAG, selectItem);
                    listener.getDataFromDialog(this, selectItem, 0);
                }
                dismiss();
                break;
            }
        }
    }

    private String titles;

    public void setTitles(String titles) {
        this.titles = titles;
    }

    /**
     * 设置标题
     */
    private void setTitle() {
        if (!TextUtils.isEmpty(titles)) {
            title.setText(titles);
        }
    }

    private int selectPosition;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == selectPosition) {
            adapter.setSelectPostion(-1);
            selectPosition = -1;
            return;
        }
        adapter.setSelectPostion(position);
        selectPosition = position;
//        selectItem = adapter.getItem(position);

    }
}
