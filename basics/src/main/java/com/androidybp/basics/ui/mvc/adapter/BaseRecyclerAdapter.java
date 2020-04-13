package com.androidybp.basics.ui.mvc.adapter;

import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 支持多次添加header  View
 */

public abstract class BaseRecyclerAdapter<T extends BaseRecyclerAdaHolder, D>
        extends RecyclerView.Adapter<BaseRecyclerAdaHolder> {

    public static final int TYPE_NORMAL = -1;

    public final int[] types = new int[]{0, 1, 2};

    public List<D> mDatas;

    protected ArrayList<View> mHeaderViews;

    @Override
    public int getItemViewType(int position) {
        if (mHeaderViews == null || mHeaderViews.size() < 1) {
            int i = position;
            if (mHeaderViews != null) {
                i = position - mHeaderViews.size();
            }
            return getAdapterItemViewType(i);
        } else {
            int i = position - mHeaderViews.size();
            if (i < 0) {
                return Math.abs(i) - 1 + 1000;
            }
            return getAdapterItemViewType(i);
        }
    }


    /**
     * 多条目类型 返回方法
     * @param position
     * @return
     */
    public int getAdapterItemViewType(int position){
        return TYPE_NORMAL;
    }

    /**
     * 获取条目类型
     * @param position
     * @return
     */
    public int getAdapterItemType(int position){
        int i = position;
        if (mHeaderViews != null) {
            i = position + mHeaderViews.size();
        }
       return getItemViewType(i);
    }

    /**
     * 获取当前 view 在recycler中实际的位置
     * @param position
     * @return
     */
    public int getAdapterPosition(int position){
        if (mHeaderViews != null) {
            position = position + mHeaderViews.size();
        }
        return position;
    }

    /**
     * 获取当前view的类型 -1为正常条目  凡事大于-1的都为 header添加时的角标
     * @param position
     * @return
     */
    public int getViewType(int position){
        if(mHeaderViews == null || mHeaderViews.size() < 1){
            return -1;
        } else {
            if(position < mHeaderViews.size()){
                return position;
            } else {
                return -1;
            }
        }
    }

    /**
     * 最后一个添加的在最上面
     *
     * @param headerView
     */
    public void addHeaderView(View headerView) {
        if (headerView != null) {
            if (mHeaderViews == null)
                mHeaderViews = new ArrayList<>();
            if (mHeaderViews.size() >= 3)
                throw new RuntimeException("Header个数最多为3个");
            mHeaderViews.add(headerView);
//            notifyItemInserted(0);
            notifyDataSetChanged();
        }
    }

    @Override
    public BaseRecyclerAdaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType >= 1000) {
            return new BaseRecyclerAdaHolder(mHeaderViews.get(viewType - 1000));

        } else {
            return setItemHolder(parent, viewType);
        }
    }
    @Override
    public void onBindViewHolder(BaseRecyclerAdaHolder holder, int position) {
        int i = position;
        if (mHeaderViews != null) {
            i = position - mHeaderViews.size();
        }
        if (i < 0) {
            return;
        } else {
            setBindViewHolder((T) holder, i);
        }
    }


    @Override
    public int getItemCount() {
        if (mDatas != null && mHeaderViews != null) {
            return mDatas.size() + mHeaderViews.size();
        } else if (mDatas != null && (mHeaderViews == null || mHeaderViews.isEmpty())) {
            return mDatas.size();
        } else if ((mDatas == null || mDatas.isEmpty()) && mHeaderViews != null) {
            return mHeaderViews.size();
        } else {
            return 0;
        }

    }

    public void clearList() {
        if (mDatas != null && mDatas.size() > 0)
            mDatas.clear();
        if (mHeaderViews != null && mHeaderViews.size() > 0)
            mHeaderViews.clear();
    }

    public void clearData() {
        clearList();
        notifyDataSetChanged();
    }

    public void setDate(List<D> list) {
        if (mDatas != null) {
            mDatas.clear();
        }
        if (list != null)
            mDatas = list;
        notifyDataSetChanged();
//        notifyItemRangeInserted();
    }

    public void addDate(List<D> list) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        if (list != null && list.size() > 0) {
            mDatas.addAll(list);
            notifyDataSetChanged();
        }
//        notifyItemRangeInserted(getItemCount(), getItemCount());
    }

    /**
     * 给对应holder 里面控件设置内容
     *
     * @param holder
     * @param positioin
     */
    public abstract void setBindViewHolder(T holder, int positioin);

    /**
     * 设置条目的 Holder对象
     *
     * @param parent
     * @return
     */
    public abstract T setItemHolder(ViewGroup parent, int viewType);

}
