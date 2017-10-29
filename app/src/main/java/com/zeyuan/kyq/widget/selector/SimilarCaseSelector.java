package com.zeyuan.kyq.widget.selector;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.selector.popupWindow.CancerTypePopupWindow;
import com.zeyuan.kyq.widget.selector.popupWindow.MutationTypePopupWindow;
import com.zeyuan.kyq.widget.selector.popupWindow.StepTypePopupWindow;
import com.zeyuan.kyq.widget.selector.popupWindow.TransferPopupWindow;

/**
 * Created by Administrator on 2017/5/2.
 * 相似案例的条件选择器
 */
public class SimilarCaseSelector implements View.OnClickListener {
    public final static int TYPE_CANCER = 0;
    public final static int TYPE_TRANSFER = 1;
    public final static int TYPE_STEP = 2;
    public final static int TYPE_MUTATION = 3;
    private Activity context;
    private View rootView;
    private FrameLayout fl_cancer_type;
    private TextView tv_cancer_type;
    private FrameLayout fl_move;
    private TextView tv_move;
    private FrameLayout fl_step_type;
    private TextView tv_step_type;
    private FrameLayout fl_change;
    private TextView tv_change;
    private CancerTypePopupWindow similarCaseCnacerTypeDialog;
    private TransferPopupWindow transferPopupWindow;
    private StepTypePopupWindow stepTypePopupWindow;
    private MutationTypePopupWindow mutationTypePopupWindow;
    private OnSelectorItemSelectedListener onSelectorItemSelectedListener;

    public void setOnSelectorItemSelectedListener(OnSelectorItemSelectedListener onSelectorItemSelectedListener) {
        this.onSelectorItemSelectedListener = onSelectorItemSelectedListener;
    }

    public SimilarCaseSelector(Activity context, ViewGroup group) {
        this.context = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.selector_similar_case, group, false);
        fl_cancer_type = (FrameLayout) rootView.findViewById(R.id.fl_cancer_type);
        tv_cancer_type = (TextView) rootView.findViewById(R.id.tv_cancer_type);
        fl_move = (FrameLayout) rootView.findViewById(R.id.fl_move);
        tv_move = (TextView) rootView.findViewById(R.id.tv_move);
        fl_step_type = (FrameLayout) rootView.findViewById(R.id.fl_step_type);
        tv_step_type = (TextView) rootView.findViewById(R.id.tv_step_type);
        fl_change = (FrameLayout) rootView.findViewById(R.id.fl_change);
        tv_change = (TextView) rootView.findViewById(R.id.tv_change);
        fl_cancer_type.setOnClickListener(this);
        fl_change.setOnClickListener(this);
        fl_move.setOnClickListener(this);
        fl_step_type.setOnClickListener(this);
        tv_cancer_type.setText(MapDataUtils.getCancerValues(UserinfoData.getCancerID(context)));
        tv_step_type.setText(MapDataUtils.getAllStepName(UserinfoData.getStepID(context)));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_cancer_type:
                setSelect(0);
                break;
            case R.id.fl_move:
                setSelect(1);
                break;
            case R.id.fl_step_type:
                setSelect(2);
                break;
            case R.id.fl_change:
                setSelect(3);
                break;
        }
    }

    private void setSelect(int tab) {
        clearSelectStatus(tab);
        switch (tab) {
            case 0:
                if (tv_cancer_type.isSelected()) {
                    tv_cancer_type.setSelected(false);
                } else {
                    tv_cancer_type.setSelected(true);
                    showCancerDialog();
                }
                break;
            case 1:
                if (tv_move.isSelected()) {
                    tv_move.setSelected(false);
                } else {
                    tv_move.setSelected(true);
                    showTransferPop();
                }
                break;
            case 2:
                if (tv_step_type.isSelected()) {
                    tv_step_type.setSelected(false);
                } else {
                    tv_step_type.setSelected(true);
                    showStepPop();
                }
                break;
            case 3:
                if (tv_change.isSelected()) {
                    tv_change.setSelected(false);
                } else {
                    tv_change.setSelected(true);
                    showMutationPop();
                }
                break;
        }
    }

    private void clearSelectStatus(int tab) {
        switch (tab) {
            case 0:
                tv_move.setSelected(false);
                tv_step_type.setSelected(false);
                tv_change.setSelected(false);
                break;
            case 1:
                tv_cancer_type.setSelected(false);
                tv_step_type.setSelected(false);
                tv_change.setSelected(false);
                break;
            case 2:
                tv_cancer_type.setSelected(false);
                tv_move.setSelected(false);
                tv_change.setSelected(false);
                break;
            case 3:
                tv_cancer_type.setSelected(false);
                tv_move.setSelected(false);
                tv_step_type.setSelected(false);
                break;
        }

    }

    public View getView() {
        return rootView;
    }

    //隐藏选择器
    public void hide() {
        rootView.setVisibility(View.GONE);
    }

    //显示选择器
    public void show() {
        rootView.setVisibility(View.VISIBLE);
    }

    private String cancerId = UserinfoData.getCancerID(context);

    private void initCancerDialog() {
        similarCaseCnacerTypeDialog = new CancerTypePopupWindow(context);
        similarCaseCnacerTypeDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tv_cancer_type.setSelected(false);
            }
        });
        if (onSelectorItemSelectedListener != null) {
            similarCaseCnacerTypeDialog.setOnSelectorItemSelectedListener(new OnSelectorItemSelectedListener() {
                @Override
                public void onSelectorItemSelected(String id, String name, int type) {
                    cancerId = id;
                    tv_cancer_type.setText(name);
                    onSelectorItemSelectedListener.onSelectorItemSelected(id, name, type);
                }
            });
        }
    }

    private void showCancerDialog() {
        if (similarCaseCnacerTypeDialog == null) {
            initCancerDialog();
        }
        similarCaseCnacerTypeDialog.showPopupWindow(rootView);
    }

    private void initTransferPop() {
        transferPopupWindow = new TransferPopupWindow(context);
        transferPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tv_move.setSelected(false);
            }
        });
        if (onSelectorItemSelectedListener != null) {
            transferPopupWindow.setOnSelectorItemSelectedListener(new OnSelectorItemSelectedListener() {
                @Override
                public void onSelectorItemSelected(String id, String name, int type) {
                    tv_move.setText(name);
                    onSelectorItemSelectedListener.onSelectorItemSelected(id, name, type);
                }
            });
        }
    }

    private void showTransferPop() {
        if (transferPopupWindow == null) {
            initTransferPop();
        }
        transferPopupWindow.showPopupWindow(rootView);
    }


    private void initStepPop() {
        stepTypePopupWindow = new StepTypePopupWindow(context, cancerId);
        stepTypePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tv_step_type.setSelected(false);
            }
        });
        if (onSelectorItemSelectedListener != null) {
            stepTypePopupWindow.setOnSelectorItemSelectedListener(new OnSelectorItemSelectedListener() {
                @Override
                public void onSelectorItemSelected(String id, String name, int type) {
                    onSelectorItemSelectedListener.onSelectorItemSelected(id, name, type);
                    tv_step_type.setText(name);
                }
            });
        }
    }

    private void showStepPop() {
        if (stepTypePopupWindow == null) {
            initStepPop();
        }
        stepTypePopupWindow.showPopupWindow(rootView, cancerId);
    }

    private void initMutationPop() {
        mutationTypePopupWindow = new MutationTypePopupWindow(context, cancerId);
        mutationTypePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tv_change.setSelected(false);
            }
        });
        if (onSelectorItemSelectedListener != null) {
            mutationTypePopupWindow.setOnSelectorItemSelectedListener(new OnSelectorItemSelectedListener() {
                @Override
                public void onSelectorItemSelected(String id, String name, int type) {
                    onSelectorItemSelectedListener.onSelectorItemSelected(id, name, type);
                    tv_change.setText(name);
                }
            });
        }
    }

    private void showMutationPop() {
        if (mutationTypePopupWindow == null) {
            initMutationPop();
        }
        mutationTypePopupWindow.showPopupWindow(rootView, cancerId);
    }


    public interface OnSelectorItemSelectedListener {
        void onSelectorItemSelected(String id, String name, int type);
    }
}
