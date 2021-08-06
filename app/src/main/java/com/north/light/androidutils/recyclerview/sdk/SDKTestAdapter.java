package com.north.light.androidutils.recyclerview.sdk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.north.light.androidutils.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SDKTestAdapter extends RecyclerView.Adapter<SDKTestAdapter.ContentHolder> {
    List<String> contentList = new ArrayList<>();

    public SDKTestAdapter(){

    }

    @NonNull
    @NotNull
    @Override
    public ContentHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ContentHolder(LayoutInflater.from(parent.getContext().getApplicationContext())
                .inflate(R.layout.recy_item_test_normal,
                        parent, false));
    }

    public void add(List<String>data){
        contentList.addAll(data);
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull ContentHolder holder, int position) {
        ((ContentHolder) holder).content.setText(contentList.get(position));
    }


    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public class ContentHolder extends RecyclerView.ViewHolder {
        TextView content;

        public ContentHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.item_normal_text);
        }
    }
}
