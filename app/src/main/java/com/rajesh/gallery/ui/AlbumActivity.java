package com.rajesh.gallery.ui;

import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rajesh.gallery.MyApp;
import com.rajesh.gallery.R;
import com.rajesh.zlbum.ui.AlbumFragment;

import java.util.ArrayList;

/**
 * 相册
 *
 * @author zhufeng on 2017/10/22
 */
public class AlbumActivity extends AppCompatActivity {
    private AlbumFragment mAlbumView;
    private LinearLayout mActionBar;
    private ImageView mBackBtn;
    private TextView mTitleTv;
    private ArrayList<Uri> mData = null;
    private int curr = 0;
    private int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_album);
        translucentStatusBar();
        initData();
        setupView();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void initData() {
        mData = (ArrayList<Uri>) getIntent().getSerializableExtra("res");
        curr = getIntent().getIntExtra("index", 0);
        total = mData.size();
        if (mData == null || total == 0) {
            finish();
            return;
        }

    }

    private void setupView() {
        mActionBar = (LinearLayout) findViewById(R.id.action_holder);
        mBackBtn = (ImageView) findViewById(R.id.back);
        mTitleTv = (TextView) findViewById(R.id.title);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mActionBar.setPadding(0, dp2Px(20), 0, 0);
        } else {
            mActionBar.setPadding(0, 0, 0, 0);
        }

        mTitleTv.setText(String.format(getString(R.string.index), curr + 1, total));

        if (mAlbumView == null) {
            mAlbumView = AlbumFragment.newInstance(mData);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.content, mAlbumView, AlbumFragment.class.getName());
            transaction.commit();
        }

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAlbumView.setOnAlbumEventListener(new AlbumFragment.OnAlbumEventListener() {
            @Override
            public void onClick() {
                if (mActionBar.getVisibility() == View.VISIBLE) {
                    hideActionBar();
                } else {
                    showActionBar();
                }
            }

            @Override
            public void onPageChanged(int page) {
                mTitleTv.setText(String.format(getString(R.string.index), curr + 1, total));
            }

            @Override
            public void onStartPull() {
                hideActionBar();
            }

            @Override
            public void onPullFinished() {
                finish();
            }
        });
    }

    private void translucentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void showActionBar() {
        if (mActionBar.getVisibility() == View.VISIBLE) {
            return;
        }
        mActionBar.setVisibility(View.VISIBLE);
        Animation startAnim = AnimationUtils.loadAnimation(this, R.anim.action_bar_show);
        startAnim.setFillAfter(true);
        mActionBar.startAnimation(startAnim);
    }

    private void hideActionBar() {
        if (mActionBar.getVisibility() == View.GONE) {
            return;
        }
        Animation hideAnim = AnimationUtils.loadAnimation(this, R.anim.action_bar_hide);
        hideAnim.setFillAfter(true);
        hideAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mActionBar.setVisibility(View.GONE);
                mActionBar.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mActionBar.startAnimation(hideAnim);
    }

    public int dp2Px(int dp) {
        DisplayMetrics dm = MyApp.getAppContext().getResources().getDisplayMetrics();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
        return px;
    }

}
