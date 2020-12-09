package com.north.light.androidutils.recyclerview.test;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import com.north.light.androidutils.R;
import com.north.light.androidutils.recyclerview.x.XRecyAdapter;
import com.north.light.androidutils.recyclerview.x.XRecyclerView;


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
        XRecyAdapter xRecyclerAdapter = new XRecyAdapter(adapter);
        recyclerView.setAdapter(xRecyclerAdapter);
        xRecyclerAdapter.notifyDataSetChanged();
        recyclerView.setOnPullListener(new XRecyclerView.PullListener() {
            @Override
            public void refresh() {
                Log.d(TAG,"refresh");
            }

            @Override
            public void loadMore() {
                Log.d(TAG,"loadMore");
            }
        });
    }
}
