package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.nww.PhotoSummaryRvAdapter;
import com.zeyuan.kyq.app.BaseActivity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/11/18 0018.
 */

public class AddPhotoSummaryActivity extends BaseActivity implements PhotoSummaryRvAdapter.PhotoSelectedListener{

    private List<String> summary;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo_summary);
        List<String> urls = (List<String>)getIntent().getSerializableExtra("list");
        summary = (List<String>)getIntent().getSerializableExtra("list_summary");
        int position = getIntent().getIntExtra("position",0);
        index = position;
        initView(urls, position);
        initData();
    }

    private EditText et;
    private ImageView iv;
    private void initView(List<String> urls, int position){

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toComplete();
            }
        });

        et = (EditText) findViewById(R.id.et_remark);
        String oldSummary = summary.get(position);
        if (!TextUtils.isEmpty(oldSummary)) et.setText(oldSummary);
        iv = (ImageView) findViewById(R.id.iv);

        if (urls==null||urls.size()==0) return;

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(manager);
        PhotoSummaryRvAdapter adapter = new PhotoSummaryRvAdapter(this, urls, position,this);
        rv.setAdapter(adapter);
        Glide.with(this).load(urls.get(position)).into(iv);

    }

    private void toComplete(){
        summary.set(index, et.getText().toString());
        showToast("添加描述完成");
        Intent intent = new Intent();
        intent.putExtra("list_summary_back", (Serializable) summary);
        setResult(31, intent);
        finish();
    }

    private void initData(){

    }

    @Override
    public void onItemSelected(String url, int position) {
        Glide.with(this).load(url).into(iv);
        summary.set(index, et.getText().toString());
        index = position;
        String oldSummary = summary.get(position);
        if (!TextUtils.isEmpty(oldSummary)) {
            et.setText(oldSummary);
        } else {
            et.setText("");
        }
    }
}
