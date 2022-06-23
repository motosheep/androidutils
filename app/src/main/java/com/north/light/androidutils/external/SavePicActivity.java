package com.north.light.androidutils.external;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.north.light.androidutils.R;
import com.north.light.androidutils.log.LogUtil;
import com.north.light.androidutils.viewshot.ViewShotUtils;

public class SavePicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_pic);

        Button saveBt = findViewById(R.id.activity_save_pic_bt);
        saveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存bitmap到本地
                ViewShotUtils.viewSnapshot(findViewById(R.id.activity_save_pic_root), new ViewShotUtils.ViewSnapListener() {
                    @Override
                    public void success(Bitmap bitmap) {
                        LogUtil.d("获取图片成功");
                        String path = MediaPicUtils.saveImagesToSys(SavePicActivity.this, bitmap);
                        LogUtil.d("保存图片成功： " + path);
//                        MediaPicUtils.deletePicWithUri(SavePicActivity.this, path);
                    }

                    @Override
                    public void failed() {
                        LogUtil.d("获取图片错误");
                    }
                });
            }
        });
    }
}