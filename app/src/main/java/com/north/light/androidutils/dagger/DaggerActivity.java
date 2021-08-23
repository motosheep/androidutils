package com.north.light.androidutils.dagger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.north.light.androidutils.R;

import javax.inject.Inject;

public class DaggerActivity extends AppCompatActivity {

    @Inject
    DaggerBean daggerBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger);

        DaggerDaggerTestComponent.create().inject(this);
        daggerBean.hey();
    }
}
