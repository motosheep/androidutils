package com.north.light.androidutils;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.north.light.androidutils.download.DownloadManager;
import com.north.light.androidutils.download.ProgressBarListener;
import com.north.light.androidutils.recyclerview.test.XRecyActivity;
import com.north.light.androidutils.viewpager.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        BannerViewPager viewPager = findViewById(R.id.banner);
//        List<String> a = new ArrayList<>();
//        a.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1021768252,432753213&fm=26&gp=0.jpg");
//        a.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1596688315897&di=551f51d38487e1a62fbdeec2c00c147b&imgtype=0&src=http%3A%2F%2Fattachments.gfan.com%2Fforum%2F201608%2F04%2F2339412b3z30zbd2j6k652.jpg");
//        a.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1021768252,432753213&fm=26&gp=0.jpg");
//        a.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1596688315897&di=551f51d38487e1a62fbdeec2c00c147b&imgtype=0&src=http%3A%2F%2Fattachments.gfan.com%2Fforum%2F201608%2F04%2F2339412b3z30zbd2j6k652.jpg");
//        a.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1021768252,432753213&fm=26&gp=0.jpg");
//        a.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1596688315897&di=551f51d38487e1a62fbdeec2c00c147b&imgtype=0&src=http%3A%2F%2Fattachments.gfan.com%2Fforum%2F201608%2F04%2F2339412b3z30zbd2j6k652.jpg");
//        viewPager.setImageView(a, new BannerViewPager.LoadImageListener() {
//            @Override
//            public void loadImage(String url, ImageView pic) {
//                pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                Glide.with(MainActivity.this).load(url).override(200, 200).into(pic);
//            }
//        });

//        VerticalScrollTxView scrollTxView = findViewById(R.id.scrollText);
//        List<String> mShowList = new ArrayList<>();
//        for (int i = 0; i < 2; i++) {
//            mShowList.add("位置: " + i);
//        }
//        scrollTxView.setTextList(mShowList);
//
//        //流式布局
//        FlowLayout flowLayout = findViewById(R.id.flowlayout);
//        List<String> list = new ArrayList<>();
//        list.add("java");
//        list.add("javaEE");
//        list.add("javaME");
//        list.add("c");
//        list.add("php");
//        list.add("ios");
//        list.add("c++");
//        list.add("c#");
//        list.add("Android");
//        flowLayout.setAlignByCenter(FlowLayout.AlienState.LEFT);
//        flowLayout.setAdapter(list, R.layout.flow_item, new FlowLayout.ItemView<String>() {
//            @Override
//            public void getCover(String item, FlowLayout.ViewHolder holder, View inflate, int position) {
//                holder.setText(R.id.tv_label_name,item);
//            }
//        });

        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        new DownloadManager().download("https://pcclient.download.youku.com/youkuclient/youkuclient_setup_8.0.7.11061.exe",
                                new ProgressBarListener() {
                                    @Override
                                    public void getMax(int length) {
                                        Log.d("TAG-----", "download_max:" + length);
                                    }

                                    @Override
                                    public void getDownload(int length) {
                                        Log.d("TAG-----", "download_callback:" + length);
                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("TAG-----", "download_Exception:" + e);
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TAG-----", "download_Exception:" + e);
        }
    }


    public void xrecyclerview(View view) {
        //xrecyclerview
        startActivity(new Intent(this, XRecyActivity.class));
    }
}
