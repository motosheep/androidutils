package com.north.light.androidutils.recyclerview.test;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.north.light.androidutils.R;
import com.north.light.androidutils.recyclerview.x.XRecyAdapter;
import com.north.light.androidutils.recyclerview.x.XRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class XRecyActivity extends AppCompatActivity {
    private static final String TAG = XRecyActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xrecy);
        XRecyclerView recyclerView = findViewById(R.id.test_recy);
        recyclerView.setLayoutManager(new LinearLayoutManager(XRecyActivity.this,
                RecyclerView.VERTICAL, false));
        TestAdapter adapter = new TestAdapter();
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data.add(i + "");
        }
        adapter.add(data);
        XRecyAdapter xRecyclerAdapter = new XRecyAdapter(adapter);
        recyclerView.setAdapter(xRecyclerAdapter);
        xRecyclerAdapter.notifyDataSetChanged();
        recyclerView.setOnPullListener(new XRecyclerView.PullListener() {
            @Override
            public void refresh() {
                Log.d(TAG, "refresh");
            }

            @Override
            public void loadMore() {
                Log.d(TAG, "loadMore");
            }
        });
    }
}
