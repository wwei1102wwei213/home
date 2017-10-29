package com.zeyuan.kyq.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseFragment;
import com.zeyuan.kyq.view.WSZLActivity;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class GanGGFragment extends BaseFragment implements View.OnClickListener {
    private String[] zdhs = {"<34", "34-35", ">35"};//总胆红素的选项
    private String[] xqdb = {">35", "28-35", "<28"};//血清蛋白的选项
    private String[] nxm = {"1-3", "4-6", ">6"};//凝血酶原时间延长
    private String[] fs = {"无腹水", "轻度", "中等量"};//腹水程度
    private String[] gxnb = {"无", "1-2", "3-4"};//肝性脑病

    private int score = 0;

    public GanGGFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_gan_gg, container, false);
        initView();
        return rootView;
    }

    private Button finish;
    private TextView tv_zdhs;//总胆红素
    private TextView tv_xqdb;//血清蛋白
    private TextView tv_nxm;//凝血酶
    private TextView tv_fs;//腹水
    private TextView tv_gxnb;//肝性脑病
    private TextView tv_score;//得分
    private TextView tv_level;//级数


    private void initView() {
        ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("肝功能");
        finish = (Button) findViewById(R.id.finish);
        tv_zdhs = (TextView) findViewById(R.id.tv_zdhs);
        tv_xqdb = (TextView) findViewById(R.id.tv_xqdb);
        tv_nxm = (TextView) findViewById(R.id.tv_nxm);
        tv_fs = (TextView) findViewById(R.id.tv_fs);
        tv_gxnb = (TextView) findViewById(R.id.tv_gxnb);
        tv_score = (TextView) findViewById(R.id.tv_score);
        tv_level = (TextView) findViewById(R.id.tv_level);

        btn_back.setOnClickListener(this);
        tv_zdhs.setOnClickListener(this);
        tv_xqdb.setOnClickListener(this);
        tv_nxm.setOnClickListener(this);
        tv_fs.setOnClickListener(this);
        tv_gxnb.setOnClickListener(this);
        finish.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                getActivityData().removefragmet();
                break;
            }
            case R.id.finish: {
                isEmpty();
                break;
            }
            case R.id.tv_zdhs: {
                createDialog(zdhs, tv_zdhs);
                break;
            }
            case R.id.tv_xqdb: {
                createDialog(xqdb, tv_xqdb);
                break;
            }
            case R.id.tv_nxm: {
                createDialog(nxm, tv_nxm);
                break;
            }
            case R.id.tv_fs: {
                createDialog(fs, tv_fs);
                break;
            }
            case R.id.tv_gxnb: {
                createDialog(gxnb, tv_gxnb);

                break;
            }
        }
    }

    private void finishGgg() {
        getActivityData().setScore(String.valueOf(score));
        getActivityData().removefragmet();
    }


    private void createDialog(final String[] item, final TextView tv_right) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("列表选择框");
        builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv_right.setText(item[which]);
                switch (which) {
                    case 0:
                        score += 1;
                        break;
                    case 1:
                        score += 2;
                        break;
                    case 2:
                        score += 3;
                        break;
                }
                setScore();

            }
        });
        builder.create().show();
    }


    private WSZLActivity getActivityData() {
        if (getActivity() instanceof WSZLActivity) {
            return (WSZLActivity) getActivity();
        }
        return null;
    }

    /**
     * @param tv
     * @return true 表示 这个是空值 false 表示里面有值
     */
    public boolean isEmpty(TextView tv) {
        String temp = tv.getText().toString().trim();
        if (TextUtils.isEmpty(temp)) {
            return true;
        } else {
            return false;
        }


    }

    private void setScore() {
        tv_score.setText("当前得分:" + score);
        if (score < 5) {
            return;
        }
        switch (score) {
            case 5:
            case 6:
                tv_level.setText("级数:" + "A");
                break;

            case 7:
            case 8:
            case 9:
                tv_level.setText("级数:" + "B");
                break;
            default:
                tv_level.setText("级数:" + "C");

        }


    }

    public void isEmpty() {
        tv_xqdb.getText().toString();
        if (isEmpty(tv_zdhs)) {
            Toast.makeText(context, "总胆红素为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isEmpty(tv_xqdb)) {
            Toast.makeText(context, "血清蛋白为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isEmpty(tv_nxm)) {
            Toast.makeText(context, "凝血酶为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isEmpty(tv_fs)) {
            Toast.makeText(context, "腹水为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isEmpty(tv_gxnb)) {
            Toast.makeText(context, "肝性脑病为空", Toast.LENGTH_SHORT).show();
            return;
        }

        finishGgg();
    }
}
