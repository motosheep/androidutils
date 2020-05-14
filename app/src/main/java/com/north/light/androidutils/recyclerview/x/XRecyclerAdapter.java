package com.north.light.androidutils.recyclerview.x;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * recyclerview adapter，实现上拉，下拉
 * 传入对象为已有内容的adapter
 */
public abstract class XRecyclerAdapter extends RecyclerView.Adapter {
    private static final String TAG = XRecyclerAdapter.class.getName();

    private RecyclerView.Adapter mAdapter;


    private static final int TYPE_HEADER = 0x0001;
    private static final int TYPE_FOOTER = 0x0002;
    private static final int TYPE_NORMAL = 0x0003;

    public XRecyclerAdapter(RecyclerView.Adapter adapter) {
        this.mAdapter = adapter;
    }

    public abstract int getHeaderViewLayout();

    public abstract int getFooterViewLayout();

    @Override
    public int getItemViewType(int position) {
        int type = TYPE_NORMAL;
        if (position == 0 && getHeaderViewLayout() != 0) {
            type = TYPE_HEADER;
        } else if (position == getItemCount() - 1 && getFooterViewLayout() != 0) {
            type = TYPE_FOOTER;
        } else {
            type = TYPE_NORMAL;
        }
        return type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case TYPE_HEADER:
                holder = new HeaderHolder(LayoutInflater.from(parent.getContext().getApplicationContext()).inflate(getHeaderViewLayout(), parent, false));
                break;
            case TYPE_FOOTER:
                holder = new FooterHolder(LayoutInflater.from(parent.getContext().getApplicationContext()).inflate(getFooterViewLayout(), parent, false));
                break;
            case TYPE_NORMAL:
                holder = mAdapter.onCreateViewHolder(parent, viewType);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "position orignal: " + position);
        if (holder instanceof HeaderHolder) {
            //有头部
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            params.height = 0;
            holder.itemView.setLayoutParams(params);
        } else if (holder instanceof FooterHolder) {
            //有尾部
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            params.height = 0;
            holder.itemView.setLayoutParams(params);
        } else {
            Log.d(TAG, "position: " + position);
            this.mAdapter.onBindViewHolder(holder, (getHeaderViewLayout() == 0 ? position : position - 1));
        }
    }

    @Override
    public int getItemCount() {
        return this.mAdapter.getItemCount() + ((getHeaderViewLayout() != 0 ? 1 : 0) + (getFooterViewLayout() != 0 ? 1 : 0));
    }

    public boolean hadHeader() {
        return getHeaderViewLayout() != 0;
    }

    public boolean hadFooter() {
        return getHeaderViewLayout() != 0;
    }


    //-----------------头部，尾部
    public class HeaderHolder extends RecyclerView.ViewHolder {
        public HeaderHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class FooterHolder extends RecyclerView.ViewHolder {
        public FooterHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
