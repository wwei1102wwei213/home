package com.zeyuan.kyq.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.CaseDetailBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * 药物的详细说明
 */
public class DrugActivity extends BaseActivity {

    private CaseDetailBean.MedicineNumEntity data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_drug);
        try {
            Intent intent = getIntent();
            initTitlebar("详细说明");
            data= (CaseDetailBean.MedicineNumEntity) intent.getSerializableExtra(Contants.Drug);
            initView();
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "DrugActivity");
        }
    }

    private TextView drugName;
    private TextView english_name;
    private TextView shop_name;
    private TextView drug_type;//这个根据 id判断
    private TextView brain;//入脑能力
    private TextView suit;//描述/适应症
    private TextView usage;//参考用法
    private TextView gene;//针对靶点
    private TextView caution;//注意事项

    private void initView() {
        try {
            drugName = (TextView) findViewById(R.id.comm_name);
            english_name = (TextView) findViewById(R.id.english_name);
            shop_name = (TextView) findViewById(R.id.shop_name);
            drug_type = (TextView) findViewById(R.id.drug_type);
            drugName = (TextView) findViewById(R.id.comm_name);
            drugName = (TextView) findViewById(R.id.comm_name);
            drugName = (TextView) findViewById(R.id.comm_name);
            drugName = (TextView) findViewById(R.id.comm_name);
            brain = (TextView) findViewById(R.id.brain);
            suit = (TextView) findViewById(R.id.suit);
            usage = (TextView) findViewById(R.id.usage);
            gene = (TextView) findViewById(R.id.gene);
            caution = (TextView) findViewById(R.id.caution);

            String str_drugName = getResources().getString(R.string.comm_name);
            drugName.setText(String.format(str_drugName, data.getCommonName()));
            String cureID = data.getCureConfID();
            Map<String,String > cureValues = (Map<String,String >) Factory.getData(Const.N_DataCureConf);
            String show = cureValues.get(cureID);
            drug_type.setText(show);
            String str_shop_name = getResources().getString(R.string.shop_name);
            shop_name.setText(String.format(str_shop_name,data.getStepName()));

            String str_english_name = getResources().getString(R.string.english_name);
            english_name.setText(String.format(str_english_name, data.getEnglishName()));
            String brainContent = data.getBrain();
            if (!TextUtils.isEmpty(brainContent)) {
                brain.setText(brainContent);
            }
            String suitContent = data.getSuit();
            if (!TextUtils.isEmpty(suitContent)) {
                suit.setText(suitContent);
            }

            String geneContent = data.getGene();
            if (!TextUtils.isEmpty(geneContent)) {
                gene.setText(geneContent);
            }

            String cautionContent = data.getCaution();
            if (!TextUtils.isEmpty(cautionContent)) {
                caution.setText(cautionContent);
            }
            String usageContent = data.getUsage();
            if (!TextUtils.isEmpty(usageContent)) {
                usage.setText(cautionContent);
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionToUM(e, this, "DrugActivity");
        }
    }
}
