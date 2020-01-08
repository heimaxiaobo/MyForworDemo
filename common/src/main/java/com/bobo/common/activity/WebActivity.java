package com.bobo.common.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.bobo.common.R;
import com.just.agentweb.AgentWeb;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Web页面
 * @author 陈锦波  2019/1/4
 */
public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        String url = getIntent().getStringExtra("url");
        //加载网页
        AgentWeb.with(this)
                .setAgentWebParent((LinearLayout) findViewById(R.id.llWeb), new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(url);
    }
}
