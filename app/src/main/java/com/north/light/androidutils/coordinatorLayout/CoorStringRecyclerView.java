package com.north.light.androidutils.coordinatorLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.north.light.androidutils.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author: lzt
 * @CreateDate: 2021/7/30 10:30
 * @Version: 1.0
 * @Description:用于CoordinatorLayout，为了填充默认数据list的recyclerview
 */
public class CoorStringRecyclerView extends RecyclerView {


    public CoorStringRecyclerView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }

    /**
     * 设置数据
     */
    public void initData() {
        CoorAdapter adapter = new CoorAdapter();
        List<String> da = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            da.add(i + "");
        }
        adapter.mData = da;
        this.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public static class CoorAdapter extends RecyclerView.Adapter<CoorAdapter.CoorVH> {
        private List<String> mData = new ArrayList<>();

        @NotNull
        @Override
        public CoorVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            return new CoorVH(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.activity_coor_adapter_item,
                    parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull CoorVH holder, int position) {
            String data = mData.get(position);
            holder.mContent.setText(data);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public static class CoorVH extends ViewHolder {
            private TextView mContent;

            public CoorVH(@NonNull @NotNull View itemView) {
                super(itemView);
                mContent = itemView.findViewById(R.id.activity_coor_adapter_item_txt);
            }
        }
    }
}
