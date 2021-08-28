package com.north.light.androidutils.ble;

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
public class BLEStringRecyclerView extends RecyclerView {
    CoorAdapter adapter = new CoorAdapter();
    private static OnClickEvent onClickEvent;

    public BLEStringRecyclerView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        this.setAdapter(adapter);
    }

    /**
     * 设置数据
     */
    public void initData(List<String> data) {
        adapter.mData.addAll(data);
        adapter.notifyDataSetChanged();
    }

    /**
     * 重置数据
     */
    public void resetData() {
        adapter.mData = new ArrayList<>();
        adapter.notifyDataSetChanged();
    }

    public static class CoorAdapter extends Adapter<CoorAdapter.CoorVH> {
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
            holder.mContent.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickEvent != null) {
                        onClickEvent.click(data);
                    }
                }
            });
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

    public void setOnClickEvent(OnClickEvent event) {
        onClickEvent = event;
    }

    public void removeOnClickEvent() {
        onClickEvent = null;
    }

    public interface OnClickEvent {
        void click(String data);
    }
}
