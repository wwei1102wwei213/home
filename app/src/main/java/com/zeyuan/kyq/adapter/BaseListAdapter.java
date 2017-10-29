package com.zeyuan.kyq.adapter;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * ���б������ࣨ�追������Ŀ������ʹ�ã�
 *
 * @param <Holder>
 * @param <Item>
 * @author guogzhao
 * @create 2013-06-09 13:53
 */
public abstract class BaseListAdapter<Holder, Item> extends BaseAdapter {
    //	private Logggz mLogggz;
    private Activity mActivity;
    private Fragment mFragment;
    private Context mContext;
    private LayoutInflater mInflater;

    protected LayoutInflater getInflater() {
        return mInflater;
    }

    private List<Item> mItemList = new ArrayList<Item>();
    private Holder holder;

    protected int mItemLayoutId;

    public BaseListAdapter(Activity activity, int itemLayoutId) {
        mActivity = activity;
        init(activity, itemLayoutId);
    }

    public BaseListAdapter(Fragment fragment, int itemLayoutId) {
        mFragment = fragment;
        init(fragment.getActivity(), itemLayoutId);
    }

    private void init(Context context, int itemLayoutId) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        mItemLayoutId = itemLayoutId;

//		mLogggz = new Logggz(this.getClass());
    }

    public Activity getActivity() {
        return mActivity;
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public Context getContext() {
        return mContext;
    }

    private PagerAdapter mPagerAdapter;

    public void setParentAdapter(PagerAdapter pagerAdapter) {
        mPagerAdapter = pagerAdapter;
    }

    // protected abstract int getItemLayoutId();
    //
    // public void setItemLayoutId(int resId) {
    // mItemLayoutId = resId;
    // }

    public List<Item> getItemList() {
        return mItemList;
    }

    public void setItemList(List<Item> listItem) {
        this.mItemList = listItem;
    }

    public void itemListClear() {
        this.mItemList.clear();
    }

    ;

    public void itemListAddAll(Collection collection) {
        this.mItemList.addAll(collection);
    }

    ;

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return mItemList.get(position);
    }

    public Item getItemX(int position) {
        return mItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // AnalysisHelper.onEventBegin(getContext(), this.getClass(), "getView");
        try {
            if (convertView == null) {
                convertView = mInflater.inflate(mItemLayoutId, null);
                holder = getHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            setHolderView(position, holder, mItemList.get(position));
        } catch (Exception ex) {
//			Logggz.printStackTrace(ex);
        }
        // AnalysisHelper.onEventEnd(getContext(), this.getClass(), "getView");

        return convertView;
    }

    // public abstract class Holder {
    // }

    protected abstract Holder getHolder(View v);

    protected abstract void setHolderView(int position, Holder holder, Item item);

    protected class OnItemButtonClickListener implements View.OnClickListener {
        private String mButton;
        private int mPosition;
        private String[][] mParams;

        public OnItemButtonClickListener(int position) {
            this.mPosition = position;
        }

        public OnItemButtonClickListener(String button, int position) {
            this.mButton = button;
            this.mPosition = position;
        }

        public OnItemButtonClickListener(String button, int position, String[][] params) {
            this.mButton = button;
            this.mPosition = position;
            this.mParams = params;
        }

        @Override
        public void onClick(View v) {
//            if (mButton != null) {
//				// ͳ�ư�ť�¼�
//				AnalysisHelper.onEventItemButtonClick(BaseListAdapter.this, mButton, mPosition);
//            }

            // ִ�а�ť�¼�
            onItemButtonClick(mPosition, v);
        }

    }

    protected class OnItemButtonLongClickListener implements View.OnLongClickListener {
        private String mButton;
        private int mPosition;
        private String[][] mParams;

        public OnItemButtonLongClickListener(int position) {
            this.mPosition = position;
        }

        public OnItemButtonLongClickListener(String button, int position) {
            this.mButton = button;
            this.mPosition = position;
        }

        public OnItemButtonLongClickListener(String button, int position, String[][] params) {
            this.mButton = button;
            this.mPosition = position;
            this.mParams = params;
        }

        @Override
        public boolean onLongClick(View v) {
            return onItemButtonLongClick(mPosition, v);
        }

//        @Override
//        public boolean onLongClick(View v) {
//        }
    }

    protected abstract void onItemButtonClick(int position, View v);

    protected abstract boolean onItemButtonLongClick(int position, View v);

}
