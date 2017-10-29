package com.zeyuan.kyq.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.SearchArticleAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.ArticleListBean;
import com.zeyuan.kyq.biz.SearchStringBiz;
import com.zeyuan.kyq.db.MemoryCache;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/8.
 *
 * 搜索文章页面
 *
 * @author wwei
 */
public class SearchArticleActivity extends BaseActivity implements SearchStringBiz.SearchStringInterface{

    private List<String> pylist;
    private List<String> firstlist;
    private boolean searchflag = false;
    private SearchArticleAdapter adapter;
    private List<ArticleListBean.ArticlenumEntity> searchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_article);
        initdata();
        initview();
    }


    private List<ArticleListBean.ArticlenumEntity> list;
    private void initdata(){
        try {
            list = (ArrayList<ArticleListBean.ArticlenumEntity>) MemoryCache.getObjectFromCache(Const.DataSearchArticle);
            /**起个工作线程，拿索引字符串集合*/
            Log.i("SAA", "开始：" + System.currentTimeMillis() + "");
            new SearchStringBiz(SearchStringBiz.ARTICLE,this,list).execute();

        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"SearchArticleActivity");
        }
    }

    private ImageView imgclose;
    private EditText et;
    private void initview(){
        try {
            /**设置返回图标*/
            imgclose = (ImageView)findViewById(R.id.back_search_article_text);

            ImageView imgback = (ImageView)findViewById(R.id.iv_back_search_article);
            imgback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            /**设置listview展示*/
            searchlist = new ArrayList<>();
            ListView listview = (ListView)findViewById(R.id.lv_search_article);
            adapter = new SearchArticleAdapter(this,searchlist);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ArticleListBean.ArticlenumEntity entity = adapter.getItem(position);
                    //TODO:搜索记录保存
                    startActivity(new Intent(SearchArticleActivity.this, ShowDiscuzActivity.class)
                            .putExtra(Const.INTENT_SHOW_DISCUZ_ID, entity.getArticleID())
                            .putExtra(Const.ARTICLE_SUMMARY,entity.getSummary())
                            .putExtra(Const.ARTCILE_TITLE, entity.getTitle()));
                }
            });

            /**设置输入控件*/
            et = (EditText)findViewById(R.id.et_search_article);
            et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {

                    String text = et.getText() + "";
                    if ("".equals(text)) {
                        if (imgclose.getVisibility() != View.GONE) {
                            imgclose.setVisibility(View.GONE);
                        }
                        return;
                    }
                    int length = et.getText().length();
                    if (length > 0) {
                        imgclose.setVisibility(View.VISIBLE);
                    }
                    searchlist = getSearchList(text);
                    adapter.updata(searchlist);
                }
            });

            imgclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    et.setText("");
                    imgclose.setVisibility(View.GONE);

                }
            });

            TextView tv = (TextView)findViewById(R.id.tv_dismiss_input);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm!=null){
                        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                    }

                }
            });
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"SearchArticleActivity");
        }
    }

    /***
     * 字符串搜索匹配
     *
     * @param str
     * @return
     */
    private List<ArticleListBean.ArticlenumEntity> getSearchList(String str){
        List<ArticleListBean.ArticlenumEntity> temp = new ArrayList<>();
        try {
            String name;
            String pyname;
            String pyfrist;
            str = str.replace(" ","");

            for(int i=0;i<list.size();i++){
                name = list.get(i).getTitle().toLowerCase();
                if(name.startsWith(str.toLowerCase())||name.contains(str.toLowerCase())){
                    temp.add(list.get(i));
                }else{
                    if(searchflag){
                        pyfrist = firstlist.get(i);
                        if(pyfrist.startsWith(str.toLowerCase())){
                            temp.add(list.get(i));
                        }else{
                            pyname = pylist.get(i);
                            if(pyname.contains(str.toLowerCase())){
                                temp.add(list.get(i));
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e,this,"SearchArticleActivity");
        }
        return temp;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(list!=null){
            list = null;
        }
        if(searchlist!=null){
            searchlist = null;
        }
        if(pylist!=null){
            pylist = null;
        }
        if(firstlist!=null){
            firstlist = null;
        }
    }

    @Override
    public void setData(List<List<String>> o) {
        try {
            pylist = o.get(0);
            firstlist = o.get(1);
            searchflag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
