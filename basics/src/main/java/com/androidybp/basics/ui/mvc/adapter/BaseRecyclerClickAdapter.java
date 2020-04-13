package com.androidybp.basics.ui.mvc.adapter;

import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 支持多次添加header  View
 */

public abstract class BaseRecyclerClickAdapter<T extends BaseRecyclerClickAdapter.MyAdaHolder, D>
        extends RecyclerView.Adapter<BaseRecyclerAdaHolder> {

    protected BaseRecyclerClickListener listener;

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
     * 添加监听事件
     * @param listener
     */
    public void setOnClickListener(BaseRecyclerClickListener listener){
        this.listener = listener;
    }

    /**
     * 删除监听事件
     */
    public void removeOnClickListener(){
        this.listener = null;
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
     * 根据实际的 坐标 返回去掉header的坐标位置
     * @param layoutPosition
     * @return
     */
    public int getListClickPosition(int layoutPosition){
        if (mHeaderViews != null) {
            layoutPosition = layoutPosition - mHeaderViews.size();
        }
        return layoutPosition;
    }

    /**
     * 最后一个添加的在最上面
     *
     * @param headerView
     */
    public void setHeaderView(View headerView) {
        if (headerView != null) {
            if (mHeaderViews == null)
                mHeaderViews = new ArrayList<>();
            if (mHeaderViews.size() >= 3)
                throw new RuntimeException("Header个数最多为3个");
            mHeaderViews.add(headerView);
            notifyItemInserted(0);
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


    @Override
    public int getItemCount() {
        if (mDatas != null && mHeaderViews != null) {
            return mDatas.size() + mHeaderViews.size();
        } else if (mDatas != null && mHeaderViews == null) {
            return mDatas.size();
        } else if (mDatas == null && mHeaderViews != null) {
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

    public class MyAdaHolder extends BaseRecyclerAdaHolder {

        public MyAdaHolder(View itemView) {
            super(itemView);
            setItemViewClick(itemView);
        }

        protected void setItemViewClick(View itemView) {
            if(itemView != null){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(listener != null){
                            int listClickPosition = getListClickPosition(getLayoutPosition());
                            listener.onItemClick(v, listClickPosition, mDatas.get(listClickPosition));
                        }
                    }
                });
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if(listener != null){
                            int listClickPosition = getListClickPosition(getLayoutPosition());
                            return listener.onItemLongClick(v, listClickPosition, mDatas.get(listClickPosition));
                        }
                        return false;
                    }
                });
            }
        }
    }

    public interface BaseRecyclerClickListener{
        void onItemClick(View view, int position, Object d);

        boolean onItemLongClick(View view, int position, Object d);
    }

}
