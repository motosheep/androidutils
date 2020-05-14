package com.north.light.androidutils.recyclerview.test;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.north.light.androidutils.R;

import java.util.ArrayList;
import java.util.List;

public class TestAdapter extends RecyclerView.Adapter {
    List<String> contentList = new ArrayList<>();

    public TestAdapter(){

    }


    public void add(List<String>data){
        contentList.addAll(data);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentHolder(LayoutInflater.from(parent.getContext().getApplicationContext()).inflate(R.layout.recy_item_test_normal,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
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
