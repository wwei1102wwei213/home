package com.zeyuan.kyq.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.nww.PhotoSummaryRvAdapter;
import com.zeyuan.kyq.app.BaseActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/18 0018.
 */

public class AddPhotoSummaryActivity extends BaseActivity implements PhotoSummaryRvAdapter.PhotoSelectedListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo_summary);
        List<String> urls = (List<String>)getIntent().getSerializableExtra("list");
        int position = getIntent().getIntExtra("position",0);
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

    }

    private void initData(){

    }

    @Override
    public void onItemSelected(String url) {
        Glide.with(this).load(url).into(iv);
    }
}
