package com.zeyuan.kyq.fragment.empty;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseFragment;
import com.zeyuan.kyq.view.ReleaseForumActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmptyResulDetalFragment extends BaseFragment implements View.OnClickListener {
    public interface OnFinishListener {
        void onFinish();
    }

    private OnFinishListener listener;

    public void setListener(OnFinishListener listener) {
        this.listener = listener;
    }

    public EmptyResulDetalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_empty_resul_detal, container, false);
        initView();
        return rootView;
    }
    private TextView toCircle;
    private TextView updateInfo;
    private void initView() {
        updateInfo = (TextView) findViewById(R.id.update_info);
        toCircle = (TextView) findViewById(R.id.to_circle);
        updateInfo.setOnClickListener(this);
        toCircle.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_info: {
                if (listener != null) {
                    listener.onFinish();
                }
                break;
            }
            case R.id.to_circle: {
                startActivity(new Intent(context, ReleaseForumActivity.class));
                break;
            }
        }
    }

}
