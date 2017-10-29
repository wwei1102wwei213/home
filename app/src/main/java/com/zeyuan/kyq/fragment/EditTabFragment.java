package com.zeyuan.kyq.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.Entity.ArticleTypeEntity;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.DragAdapter;
import com.zeyuan.kyq.adapter.OtherAdapter;
import com.zeyuan.kyq.bean.PhpUserInfoBean;
import com.zeyuan.kyq.biz.Factory;
import com.zeyuan.kyq.biz.HttpResponseInterface;
import com.zeyuan.kyq.biz.forcallback.FragmentCallBack;
import com.zeyuan.kyq.fragment.dialog.ZYDialog;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CustomView.DragGrid;
import com.zeyuan.kyq.widget.CustomView.OtherGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/9.
 *
 * 编辑栏目窗口
 *
 * @author wwei
 */
public class EditTabFragment extends DialogFragment implements HttpResponseInterface,AdapterView.OnItemClickListener{

    public static final String type = "EditTabFragment";
    public static final String SHOW_CAT = "show_cat";
    public static final String HIDE_CAT = "hide_cat";
    private static EditTabFragment instance;
    private FragmentCallBack callback;
    private List<ArticleTypeEntity> showCat;
    private List<ArticleTypeEntity> hideCat;
    private DragGrid dgd;
    private OtherGridView gv;
    private boolean isMove;
    private List<ArticleTypeEntity> oldCat;

    public void setShowCat(List<ArticleTypeEntity> showCat) {
        this.showCat = new ArrayList<>();
        oldCat = showCat;
        if (showCat!=null&&showCat.size()>0){
            this.showCat.addAll(showCat);
        }
    }

    public void setHideCat(List<ArticleTypeEntity> hideCat) {
        this.hideCat = new ArrayList<>();
        if (hideCat!=null&&hideCat.size()>0){
            this.hideCat.addAll(hideCat);
        }
    }

    public static EditTabFragment getInstance(FragmentCallBack callback){
        try {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Const.FRAGMENT_CALL_BACK,callback);
            instance = new EditTabFragment();
            instance.setArguments(bundle);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "getInstance");
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if(getArguments()!=null){
                Bundle bundle = getArguments();
                callback = (FragmentCallBack)bundle.getSerializable(Const.FRAGMENT_CALL_BACK);
            }
        }catch (Exception e){

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.cancer_dialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        try {
            dialog.setCancelable(false);
            View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_tab, null);
            initData();
            initView(rootView);
            dialog.setContentView(rootView);
            Window window = dialog.getWindow();
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
            window.setWindowAnimations(R.style.my_style_top_to_bottom);
            dialog.setCanceledOnTouchOutside(true);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"dialog");
        }
        return dialog;
    }

    private DragAdapter dragAdapter;
    private OtherAdapter otherAdapter;

    private void initView(View v){
        try {
            dgd = (DragGrid) v.findViewById(R.id.dgd);
            gv = (OtherGridView)v.findViewById(R.id.gv_other);
            dragAdapter = new DragAdapter(getActivity(),showCat);
            otherAdapter = new OtherAdapter(getActivity(),hideCat);
            dgd.setAdapter(dragAdapter);
            gv.setAdapter(otherAdapter);
            dgd.setOnItemClickListener(this);
            gv.setOnItemClickListener(this);

            View statusBar1 = v.findViewById(R.id.statusBar1);
            ViewGroup.LayoutParams params1=statusBar1.getLayoutParams();
            params1.height=getStatusBarHeight();
            statusBar1.setLayoutParams(params1);

            final TextView tv_ok = (TextView)v.findViewById(R.id.tv_ok);
            v.findViewById(R.id.v_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (IsChanged()){
                        toClose();
                    }else {
                        dismiss();
                    }
                }
            });
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_ok.setClickable(false);
                    if (IsChanged()){
                        Factory.postPhp(EditTabFragment.this, Const.PEditUserCat);
                    }else {
                        dismiss();
                    }
                }
            });
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"initView");
        }
    }

    private ZYDialog mDialog;
    private void toClose(){
        mDialog = new ZYDialog.Builder(getActivity())
                .setTitle("您的栏目定制已变更")
                .setMessage("是否保存当前设置？")
                .setNegativeButton("不保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                        EditTabFragment.this.dismiss();
                    }
                })
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                        Factory.postPhp(EditTabFragment.this, Const.PEditUserCat);
                    }
                })
                .create();
        mDialog.show();
    }

    //判断栏目是否变更
    private boolean IsChanged(){
        if (showCat!=null&&oldCat!=null&&showCat.size()==oldCat.size()){
            for (int i = 0; i<showCat.size() ;i++){
                if (showCat.get(i).getCatid()!=oldCat.get(i).getCatid()){
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private void initData(){}

    @Override
    public Map getParamInfo(int tag) {
        Map<String,String> map = new HashMap<>();
        if (tag == Const.PEditUserCat){
            map.put(Contants.InfoID, UserinfoData.getInfoID(getActivity()));
            map.put("ShowCat", dragAdapter.getChannnelLst().toString());
            map.put("HideCat", otherAdapter.getChannnelLst().toString());
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag) {

        try {
            if (flag == Const.PEditUserCat){
                PhpUserInfoBean bean = (PhpUserInfoBean)response;
                if (Const.RESULT.equals(bean.getiResult())){
                    Toast.makeText(getActivity(),"栏目编辑成功",Toast.LENGTH_SHORT).show();
                    callback.dataCallBack(null, Const.FRAGMENT_EDIT_TAB, null, null);
                }else {
                    Toast.makeText(getActivity(),"栏目编辑失败",Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"EditTab toActivity");
        }


    }

    @Override
    public void showLoading(int flag) {

    }

    @Override
    public void hideLoading(int flag) {

    }

    @Override
    public void showError(int flag) {
        if (flag == Const.PEditUserCat){
            Toast.makeText(getActivity(),"栏目编辑失败",Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        //如果点击的时候，之前动画还没结束，那么就让点击事件无效
        if(isMove){
            return;
        }
        switch (parent.getId()) {
            case R.id.dgd:
//                if (position != 0 && position != 1) {//position为 0，1 的不可以进行任何操作
                final ImageView moveImageView = getView(view);
                if (moveImageView != null) {
                    TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    final ArticleTypeEntity channel = ((DragAdapter) parent.getAdapter()).getItem(position);//获取点击的频道内容
                    otherAdapter.setVisible(false);
                    //添加到最后一个
                    otherAdapter.addItem(channel);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            try {
                                int[] endLocation = new int[2];
                                //获取终点的坐标
                                gv.getChildAt(gv.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                MoveAnim(moveImageView, startLocation , endLocation, channel,dgd);
                                dragAdapter.setRemove(position);
                            } catch (Exception e) {
                                ExceptionUtils.ExceptionSend(e,"Runnable");
                            }
                        }
                    }, 50L);
                }
//                }
                break;
            case R.id.gv_other:
                final ImageView moveImageView1 = getView(view);
                if (moveImageView1 != null){
                    TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    final ArticleTypeEntity channel = ((OtherAdapter) parent.getAdapter()).getItem(position);
                    dragAdapter.setVisible(false);
                    //添加到最后一个
                    dragAdapter.addItem(channel);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            try {
                                int[] endLocation = new int[2];
                                //获取终点的坐标
                                dgd.getChildAt(dgd.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                MoveAnim(moveImageView1, startLocation , endLocation, channel,gv);
                                otherAdapter.setRemove(position);
                            } catch (Exception e) {
                                ExceptionUtils.ExceptionSend(e,"Runnable");
                            }
                        }
                    }, 50L);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 点击ITEM移动动画
     * @param moveView
     * @param startLocation
     * @param endLocation
     * @param moveChannel
     * @param clickGridView
     */
    private void MoveAnim(View moveView, int[] startLocation,int[] endLocation, final ArticleTypeEntity moveChannel,
                          final GridView clickGridView) {
        int[] initLocation = new int[2];
        //获取传递过来的VIEW的坐标
        moveView.getLocationInWindow(initLocation);
        //得到要移动的VIEW,并放入对应的容器中
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
        //创建移动动画
        TranslateAnimation moveAnimation = new TranslateAnimation(
                startLocation[0], endLocation[0], startLocation[1],
                endLocation[1]);
        moveAnimation.setDuration(300L);//动画时间
        //动画配置
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);//动画效果执行完毕后，View对象不保留在终止的位置
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isMove = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(mMoveView);
                // instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
                if (clickGridView instanceof DragGrid) {
                    otherAdapter.setVisible(true);
                    otherAdapter.notifyDataSetChanged();
                    dragAdapter.remove();
                } else {
                    dragAdapter.setVisible(true);
                    dragAdapter.notifyDataSetChanged();
                    otherAdapter.remove();
                }
                isMove = false;
            }
        });
    }

    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器
     * @param viewGroup
     * @param view
     * @param initLocation
     * @return
     */
    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams
                (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     */
    private ViewGroup getMoveViewGroup() {
        ViewGroup moveViewGroup = (ViewGroup) getActivity().getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(getActivity());
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    /**
     * 获取点击的Item的对应View，
     * @param view
     * @return
     */
    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(getActivity());
        iv.setImageBitmap(cache);
        return iv;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /** 退出时候保存选择后数据库的设置  */
    private void saveChannel() {

    }

    @Override
    public void onPause() {
        if (mDialog!=null){
            mDialog.dismiss();
        }
        dismiss();
        super.onPause();
    }
}
