package com.sample.showcaseview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.library.showcase.ShowcaseView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ShowcaseView showcaseView = (ShowcaseView) findViewById(R.id.showcaseView);
        TextView textView = (TextView) findViewById(R.id.hello_world);
        showcaseView.addTargetView(textView);

    }
}
