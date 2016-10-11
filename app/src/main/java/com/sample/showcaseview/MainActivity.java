package com.sample.showcaseview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.library.showcase.ShowcaseEventListener;
import com.library.showcase.ShowcaseParams;
import com.library.showcase.ShowcaseView;
import com.sample.showcaseview.utils.TypefaceUtil;

public class MainActivity extends AppCompatActivity implements ShowcaseEventListener {

    private View.OnClickListener onTextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ShowcaseParams params = new ShowcaseParams();
            createShowcaseForViewWithParams(textView, params);
        }
    };

    private View.OnClickListener onButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ShowcaseParams params = new ShowcaseParams()
                    .setMessageText(getString(R.string.btn_message_text))
                    .setButtonTypeface(TypefaceUtil.getTypeface(MainActivity.this, getString(R.string.font_medium)))
                    .setMessageTypeface(TypefaceUtil.getTypeface(MainActivity.this, getString(R.string.font_light)))
                    .setButtonTextColorRes(android.R.color.black)
                    .setMessageTextColorRes(android.R.color.holo_blue_dark);
            createShowcaseForViewWithParams(button, params);
        }
    };

    private View.OnClickListener onIvClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ShowcaseParams params = new ShowcaseParams()
                    .setFocusCircleSize((int) getResources().getDimension(R.dimen.custom_focus_circle));
            createShowcaseForViewWithParams(imageView, params);
        }
    };

    private Toolbar toolbar;

    private TextView textView;
    private Button button;
    private ImageView imageView;

    private ShowcaseView showcaseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setupToolbar();

        textView = (TextView) findViewById(R.id.text_hello_world);
        textView.setOnClickListener(onTextClickListener);

        button = (Button) findViewById(R.id.simple_btn);
        button.setOnClickListener(onButtonClickListener);

        imageView = (ImageView) findViewById(R.id.android_img);
        imageView.setOnClickListener(onIvClickListener);
    }

    private void setupToolbar() {
        toolbar.getMenu().clear();
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ShowcaseParams params = new ShowcaseParams()
                        .setButtonText(getString(R.string.toolbar_btn_text))
                        .setMessageText(getString(R.string.toolbar_message_text))
                        .setButtonTypeface(TypefaceUtil.getTypeface(MainActivity.this, getString(R.string.font_medium)))
                        .setMessageTypeface(TypefaceUtil.getTypeface(MainActivity.this, getString(R.string.font_light)))
                        .setButtonTextColorRes(android.R.color.black)
                        .setMessageTextColorRes(android.R.color.holo_blue_dark)
                        .setBackgroundColorRes(R.color.red_dark)
                        .setCirclesColorRes(R.color.red_dark)
                        .setBackgroundAlpha(0.8f);
                createShowcaseForViewWithParams(toolbar.findViewById(R.id.option), params);
                return false;
            }
        });
    }

    private void createShowcaseForViewWithParams(View targetView, ShowcaseParams params) {
        showcaseView = new ShowcaseView(this)
                .addTargetView(targetView)
                .setShowcaseEventListener(this)
                .setShowcaseParams(params);
        showcaseView.show(MainActivity.this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (showcaseView != null) {
            showcaseView.startAnimation();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (showcaseView != null) {
            showcaseView.stopAnimation();
        }
    }

    @Override
    public void onShowcaseDismiss() {
        Toast.makeText(this, "showcase dismissed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShowcaseShown() {
        Toast.makeText(this, "showcase shown", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAcceptanceBtnClick() {
        Toast.makeText(this, "on acceptance click", Toast.LENGTH_SHORT).show();
    }
}
