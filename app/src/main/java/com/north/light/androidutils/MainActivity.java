package com.north.light.androidutils;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.north.light.androidutils.recyclerview.test.XRecyActivity;
import com.north.light.androidutils.textview.FlowLayout;
import com.north.light.androidutils.textview.VerticalScrollTxView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VerticalScrollTxView scrollTxView = findViewById(R.id.scrollText);
        List<String> mShowList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            mShowList.add("位置: " + i);
        }
        scrollTxView.setTextList(mShowList);

        //流式布局
        FlowLayout flowLayout = findViewById(R.id.flowlayout);
        List<String> list = new ArrayList<>();
        list.add("java");
        list.add("javaEE");
        list.add("javaME");
        list.add("c");
        list.add("php");
        list.add("ios");
        list.add("c++");
        list.add("c#");
        list.add("Android");
        flowLayout.setAlignByCenter(FlowLayout.AlienState.LEFT);
        flowLayout.setAdapter(list, R.layout.flow_item, new FlowLayout.ItemView<String>() {
            @Override
            public void getCover(String item, FlowLayout.ViewHolder holder, View inflate, int position) {
                holder.setText(R.id.tv_label_name,item);
            }
        });
    }


    public void xrecyclerview(View view) {
        //xrecyclerview
        startActivity(new Intent(this, XRecyActivity.class));
    }
}
