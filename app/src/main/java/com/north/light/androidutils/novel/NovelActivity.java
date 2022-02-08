package com.north.light.androidutils.novel;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.north.light.androidutils.R;
import com.north.light.androidutils.novel.text.read.ReaderView;

import java.util.ArrayList;
import java.util.List;

public class NovelActivity extends AppCompatActivity {

    private static final String TAG = NovelActivity.class.getSimpleName();
    private ReaderView readView;

    private List<String> data = new ArrayList<>();
    private int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel);
        readView = findViewById(R.id.activity_novel);
        //设置显示数据
        data.add("从前有座山");
        data.add("山上有座庙");
        data.add("庙里面有个和尚");
        data.add("从前有座山");
        data.add("山上有座庙");
        data.add("庙里面有个和尚");
        data.add("从前有座山");
        data.add("山上有座庙");
        data.add("庙里面有个和尚");

        readView.setReadEventListener(new ReaderView.ReadEventListener() {
            @Override
            public void change(int type) {
                if (pos == 0) {
                    //第一个
                    if (type == 1) {
                        pos = pos + 1;
                    }
                } else if (pos == data.size() - 1) {
                    //最后一个
                    if (type == -1) {
                        pos = pos - 1;
                    }
                } else {
                    //中间
                    if (type == -1) {
                        pos = pos - 1;
                    } else if (type == 1) {
                        pos = pos + 1;
                    }
                }
            }

            @Override
            public String preData() {
                return getShowContent(-1, pos);
            }

            @Override
            public String curData() {
                return getShowContent(0, pos);
            }

            @Override
            public String nextData() {
                return getShowContent(1, pos);
            }
        });
    }

    /**
     * 获取不同的数据
     */
    private String getShowContent(int type, int pos) {
        switch (type) {
            case -1:
                if (data.size() != 0) {
                    if (pos == 0) {
                        return data.get(pos);
                    }
                    return data.get(pos - 1);
                }
                break;
            case 0:
                if (data.size() != 0) {
                    if (pos == 0) {
                        return data.get(pos);
                    }
                    return data.get(pos);
                }
                break;
            case 1:
                if (data.size() != 0) {
                    if (pos == data.size() - 1) {
                        return data.get(pos);
                    }
                    return data.get(pos + 1);
                }
                break;
        }
        return "";
    }
}