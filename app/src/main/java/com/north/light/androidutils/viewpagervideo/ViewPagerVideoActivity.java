package com.north.light.androidutils.viewpagervideo;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;

import com.north.light.androidutils.R;
import com.north.light.androidutils.log.LogUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * viewpager播放video
 */
public class ViewPagerVideoActivity extends AppCompatActivity {
    private CusViewPager page1;
    private Map<Integer, LinearLayout> viewMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_video);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void init() {

        List<View> viewList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            View cacheView = View.inflate(ViewPagerVideoActivity.this, R.layout.viewpager_item, null);
            viewList.add(cacheView);
        }
        CusPagerAdapter adapter = new CusPagerAdapter(ViewPagerVideoActivity.this, viewList);
        page1 = findViewById(R.id.activity_view_pager_video_content);
        page1.setAdapter(adapter);
        page1.setPageListener(new CusViewPager.PageListener() {
            @Override
            public void pageChange(int oldPos, int newPos) {
                LogUtil.d("pageChange old pos:" + oldPos + " new pos:" + newPos);
                LinearLayout root = viewMap.get(oldPos);
                if (root == null) {
                    return;
                }
                root.removeAllViews();
            }
        });
    }

    private class CusPagerAdapter extends PagerAdapter {
        private Context context;
        private List<View> viewList = new ArrayList<>();

        public CusPagerAdapter(Context context, List<View> viewList) {
            this.context = context;
            this.viewList = viewList;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }


        @Override
        public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            viewMap.remove(position);
            container.removeView((View) object);
        }

        @NotNull
        @Override
        public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
            View root = viewList.get(position);
            container.addView(root);
            TextView tx = root.findViewById(R.id.viewpager_item_tx);
            LinearLayout mVideoRoot = root.findViewById(R.id.viewpager_item_video_root);
            tx.setText(String.valueOf(position));
            viewMap.put(position, mVideoRoot);
            tx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == 1) {
                        tx.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    } else if (position == 2) {
                        tx.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    } else if (position == 3) {
                        tx.setBackgroundColor(getResources().getColor(R.color.color_F06091));
                    } else if (position == 4) {
                        tx.setBackgroundColor(getResources().getColor(R.color.color_a8a8a8));
                    } else {
                        tx.setBackgroundColor(getResources().getColor(R.color.blue_light));
                    }
                    dealClickInfo(position);
                }
            });
            return viewList.get(position);
        }
    }

    /**
     * 处理点击事件
     */
    private void dealClickInfo(int position) {
        LinearLayout root = viewMap.get(position);
        if (root == null) {
            return;
        }
        CusVideoView videoView = new CusVideoView(this);
        videoView.setPlayRes(R.mipmap.ic_heart1,R.mipmap.ic_heart_sel);
        videoView.addToParent(root);
        if (position % 2 == 1) {
            String path1 = Environment.getExternalStorageDirectory().getPath() +
                    "/DCIM/Camera/share_893adea390ed3b7a0f2da1223092a3ef.mp4";
            videoView.play(path1);
        } else if (position % 2 == 0) {
            String path2 = Environment.getExternalStorageDirectory().getPath() +
                    "/DCIM/Camera/share_adb4ba0e521ba0003e0ab7ca98843738.mp4";
            videoView.play(path2);
        } else {
            String path3 = Environment.getExternalStorageDirectory().getPath() +
                    "/DCIM/Camera/share_adb4ba0e521ba0003e0ab7ca98843738.mp4";
            videoView.play(path3);
        }
    }
}