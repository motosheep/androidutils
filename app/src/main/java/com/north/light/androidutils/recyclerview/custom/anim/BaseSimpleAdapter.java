package com.north.light.androidutils.recyclerview.custom.anim;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * adapter进行封装
 * List集合与view的简单封装
 * <p>
 * 修改添加数据时，只会添加不为空的数据
 */
public abstract class BaseSimpleAdapter<W extends Object, T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {
    public Context mContext;
    protected List<W> data = new ArrayList<>();

    public BaseSimpleAdapter(Context context) {
        this.mContext = context;
    }

    public List<W> getData() {
        return data == null ? new ArrayList<W>() : data;
    }

    public void setData(List<W> list) {
        if (list == null || list.size() == 0) {
            this.data = new ArrayList<>();
            notifyDataSetChanged();
        } else {
            this.data = list;
            notifyDataSetChanged();
        }
    }

    public void addData(List<W> list) {
        if (list == null || list.size() == 0) {

        } else {
            int startPos = Math.max(data.size(), 0);
            this.data.addAll(list);
            notifyItemRangeChanged(startPos, list.size());
        }
    }

    public void addData(int pos, List<W> list) {
        if (list == null || list.size() == 0) {

        } else {
            int startPos = Math.max(data.size(), 0);
            this.data.addAll(list);
            notifyItemRangeChanged(startPos, list.size());
        }
    }

    public void clearData() {
        data.clear();
        notifyDataSetChanged();
    }

    public int size() {
        return data.size();
    }

    public int getColor(int rId) {
        return mContext.getResources().getColor(rId);
    }

    public String getString(int rId) {
        return mContext.getResources().getString(rId);
    }

    @NonNull
    @Override
    public T onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(mContext.getApplicationContext()).inflate(getLayoutId(i), viewGroup, false);
        return getViewHolder(view, i);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public abstract T getViewHolder(View view, int viewType);

    public abstract int getLayoutId(int viewType);

}
