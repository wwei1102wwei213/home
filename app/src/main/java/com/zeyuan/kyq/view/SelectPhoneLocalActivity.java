package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.zeyuan.kyq.Entity.PhoneCountryEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.PhoneCountryAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.utils.Cn2Spell;
import com.zeyuan.kyq.widget.EaseSidebar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/26.
 * 选择手机号国际代号
 */

public class SelectPhoneLocalActivity extends BaseActivity {
    PhoneCountryAdapter pha;
    List<PhoneCountryEntity> phoneCountryEntityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_phone_local);
        initView();
        getCountryList();
    }

    private void initView() {
        ImageView back = (ImageView) findViewById(R.id.btn_back);
        ListView lv_phone_local = (ListView) findViewById(R.id.lv_phone_local);
        EaseSidebar sidebar = (EaseSidebar) findViewById(R.id.sidebar);
        pha = new PhoneCountryAdapter(this, 0, phoneCountryEntityList);
        lv_phone_local.setAdapter(pha);
        sidebar.setListView(lv_phone_local);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        lv_phone_local.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhoneCountryEntity phoneCountryEntity = phoneCountryEntityList.get(position);
                setResult(RESULT_OK, new Intent().putExtra("local_name", phoneCountryEntity.getName()).putExtra("local_code", phoneCountryEntity.getCode()));
                finish();
            }
        });
    }

    /**
     * 获取国家列表
     */
    private void getCountryList() {
        Observable.create(new ObservableOnSubscribe<List<PhoneCountryEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<List<PhoneCountryEntity>> e) throws Exception {
                List<PhoneCountryEntity> phoneCountryEntityList = new ArrayList<>();
                //读取国际代号资源数据
                String[] countryList = getResources().getStringArray(R.array.country_code_list_zh);
                //将读取的数据转化成PhoneCountryEntity对象
                for (String cs : countryList) {
                    String[] country = cs.split("\\*");
                    String countryName = country[0];
                    String countryNumber = country[1];
                    String initialLetter = (Cn2Spell.getPinYinFirstLetter(countryName)).toUpperCase();
                    if (TextUtils.isEmpty(initialLetter) || initialLetter.charAt(0) < 'A' || initialLetter.charAt(0) > 'Z') {
                        initialLetter = "#";
                    }
                    PhoneCountryEntity phoneCountryEntity = new PhoneCountryEntity();
                    phoneCountryEntity.setName(countryName);
                    phoneCountryEntity.setCode(countryNumber);
                    phoneCountryEntity.setInitialLetter(initialLetter);
                    phoneCountryEntityList.add(phoneCountryEntity);

                }
                //将读取到的PhoneCountryEntity数据排序
                Collections.sort(phoneCountryEntityList, new Comparator<PhoneCountryEntity>() {

                    @Override
                    public int compare(PhoneCountryEntity lhs, PhoneCountryEntity rhs) {//按首字母排序
                        if (lhs.getInitialLetter().equals(rhs.getInitialLetter())) {//首字母相同
                            return lhs.getName().compareTo(rhs.getName());
                        } else {
                            if ("#".equals(lhs.getInitialLetter())) {
                                return -1;
                            } else if ("#".equals(rhs.getInitialLetter())) {
                                return 1;
                            }
                            return lhs.getInitialLetter().compareTo(rhs.getInitialLetter());
                        }
                    }
                });
                //添加常用国际代号
                phoneCountryEntityList.addAll(0, getDefList());
                e.onNext(phoneCountryEntityList);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<PhoneCountryEntity>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<PhoneCountryEntity> phoneCountryEntities) {
                        phoneCountryEntityList.clear();
                        phoneCountryEntityList.addAll(phoneCountryEntities);
                        pha.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    //生成常用的国际代号
    private List<PhoneCountryEntity> getDefList() {
        List<PhoneCountryEntity> list = new ArrayList<>();
        PhoneCountryEntity pce1 = new PhoneCountryEntity();
        pce1.setCode("86");
        pce1.setName("中国大陆");
        pce1.setInitialLetter("#");
        list.add(pce1);
        PhoneCountryEntity pce2 = new PhoneCountryEntity();
        pce2.setCode("852");
        pce2.setName("香港");
        pce2.setInitialLetter("#");
        list.add(pce2);
        PhoneCountryEntity pce3 = new PhoneCountryEntity();
        pce3.setCode("853");
        pce3.setName("澳门");
        list.add(pce3);
        pce3.setInitialLetter("#");
        PhoneCountryEntity pce4 = new PhoneCountryEntity();
        pce4.setCode("886");
        pce4.setName("台湾");
        pce4.setInitialLetter("#");
        list.add(pce4);
        return list;
    }
}
