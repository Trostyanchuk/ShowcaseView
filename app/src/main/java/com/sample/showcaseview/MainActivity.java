package com.sample.showcaseview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.library.showcase.ShowcaseEventListener;
import com.library.showcase.ShowcaseParams;
import com.library.showcase.ShowcaseView;

public class MainActivity extends AppCompatActivity implements ShowcaseEventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.hello_world);

        ShowcaseParams params = new ShowcaseParams();
        new ShowcaseView(this)
                .addTargetView(textView)
                .setShowcaseEventListener(this)
                .setShowcaseParams(new ShowcaseParams())
                .show(MainActivity.this);

    }

    @Override
    public void onShowcaseDismiss() {
        Toast.makeText(this, "showcase dismissed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onShowcaseShown() {
        Toast.makeText(this, "showcase shown", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAcceptanceBtnClick() {
        Toast.makeText(this, "on acceptance click", Toast.LENGTH_LONG).show();
    }
}
