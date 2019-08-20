package com.volive.mrhow.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.volive.mrhow.R;
import com.volive.mrhow.adapters.FaqAdapter;
import com.volive.mrhow.adapters.QualityAdapter;

public class VideoQualityActivity extends AppCompatActivity {

    ImageView image_back;
    RecyclerView quality_recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_quality);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//only working in api level 26
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.lightgreen));
        }

        String []qualities =getResources().getStringArray(R.array.qualityArray);

        initializeUI();
        initializeValues();

        if(qualities.length>0) {
            QualityAdapter qualityAdapter = new QualityAdapter(VideoQualityActivity.this, qualities);
            quality_recyclerview.setAdapter(qualityAdapter);
            qualityAdapter.notifyDataSetChanged();
        }

    }

    private void initializeValues() {
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initializeUI() {
        image_back = findViewById(R.id.image_back);
        quality_recyclerview = findViewById(R.id.quality_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(VideoQualityActivity.this);
        quality_recyclerview.setLayoutManager(mLayoutManager);
        quality_recyclerview.setItemAnimator(new DefaultItemAnimator());



    }
}
