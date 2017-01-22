package com.bestv.vrcinema;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bestv.vrcinema.constant.ConstantInterface;
import com.bestv.vrcinema.httpReport.HttpReporterBuilder;
import com.bestv.vrcinema.httpReport.HttpReporterImpl;
import com.bestv.vrcinema.httpUtil.HttpGetTask;
import com.bestv.vrcinema.httpUtil.OnAPITaskDoneListener;
import com.bestv.vrcinema.httpUtil.OnWelcomeTaskDoneListener;
import com.bestv.vrcinema.model.CurrentMovieInfo;
import com.bestv.vrcinema.model.WelcomeInfo;
import com.bestv.vrcinema.util.GoogleUnityActivityHelper;
import com.bestv.vrcinema.util.PermissionUtil;
import com.bestv.vrcinema.util.QueryUtil;
import com.bestv.vr.utils.Global;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

/**
 * Created by xujunyang on 17/1/12.
 */

public class SplashActivity extends VRBaseActivity implements View.OnClickListener{
    private final int SPLASH_DISPLAY_LENGHT = 2000; // 延迟2秒
    private int jumpLeft = 0;
    FrameLayout startLayout;
    FrameLayout adLayout;
    Button adButton;
    ImageView adImage;
    TextView adJump;

    Handler myHandler = new Handler();
    Runnable splashRun = new Runnable() {
        public void run() {
            //判断是否有开机广告,否则跳转到主界面
            if(WelcomeInfo.getInstance().isShowAd()){
                handleAd();
            }else{
                startMainActivity();
            }
        }
    };
    Runnable adRun = new Runnable() {
        public void run() {
            startMainActivity();
        }
    };
    Runnable jumpRun = new Runnable() {
        @Override
        public void run() {
            jumpLeft--;
            if(jumpLeft<=0){
                startMainActivity();
            }else{
                adJump.setText(jumpLeft + "秒后跳过");
                myHandler.postDelayed(jumpRun, 1000);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().setAttributes(params);

        // 1. 显示版本号
        TextView showVersion = (TextView)findViewById(R.id.show_version);
        showVersion.setText(ConstantInterface.ShowedVersion);

        startLayout = (FrameLayout)findViewById(R.id.startLayout);
        adLayout = (FrameLayout)findViewById(R.id.adLayout);
        adButton = (Button)findViewById(R.id.adButton);
        adImage = (ImageView)findViewById(R.id.adImage);
        adJump = (TextView)findViewById(R.id.adJump);
        adLayout.setVisibility(View.GONE);

        // 2. 获取开机信息
        initRecommendInfo();

        // 延迟跳转
        myHandler.postDelayed(splashRun, SPLASH_DISPLAY_LENGHT);
    }

    // 基于友盟的session的统计
    @Override
    protected void onResume(){
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void startMainActivity(){
        Intent mainIntent = new Intent(SplashActivity.this,
                MainActivity.class);
        SplashActivity.this.startActivity(mainIntent);
        SplashActivity.this.finish();
    }


    private void initRecommendInfo(){
        String welcomeURL = ConstantInterface.WELCOMEURL + "?version=" + ConstantInterface.VERSION
                + "&bestvr=" + QueryUtil.getQueryParam();
        new HttpGetTask(new OnWelcomeTaskDoneListener(SplashActivity.this)).execute(welcomeURL);
    }

    public void preloadAdImage(){
        // 预加载广告图
        if(WelcomeInfo.getInstance().isShowAd() && !TextUtils.isEmpty(WelcomeInfo.getInstance().getAdImgURL())) {
            ImageLoader.getInstance().displayImage(WelcomeInfo.getInstance().getAdImgURL(), adImage, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String var1, View var2) {

                }

                @Override
                public void onLoadingFailed(String var1, View var2, FailReason var3) {
                }


                @Override
                public void onLoadingComplete(String var1, View var2, Bitmap var3) {
                    WelcomeInfo.getInstance().setLoadImageSuccess(true);
                }


                @Override
                public void onLoadingCancelled(String var1, View var2) {

                }
            });
        }
    }

    private void handleAd(){
        if(WelcomeInfo.getInstance().isLoadImageSuccess()){
            ImageLoader.getInstance().displayImage(WelcomeInfo.getInstance().getAdImgURL(), adImage);

            startLayout.setVisibility(View.GONE);
            adLayout.setVisibility(View.VISIBLE);
            adButton.setOnClickListener(SplashActivity.this);
            adJump.setOnClickListener(this);

            jumpLeft = WelcomeInfo.getInstance().getAdTimeout();
            adJump.setText(jumpLeft + "秒后跳过");
            myHandler.postDelayed(jumpRun, 1000);

            myHandler.postDelayed(adRun, WelcomeInfo.getInstance().getAdTimeout()*1000);
        }else{
            startMainActivity();
        }

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.adButton:
                if (WelcomeInfo.getInstance().getAdType().contains(WelcomeInfo.OPENTAG)
                        && WelcomeInfo.getInstance().getAdValue() != "")
                {
                    Uri uri = Uri.parse(WelcomeInfo.getInstance().getAdValue());
                    startActivity(new Intent(Intent.ACTION_VIEW,uri));
                }else if(WelcomeInfo.getInstance().getAdType().contains(WelcomeInfo.PLAYTAG)){
                    // 取消跳转到MainActivity的调用
                    myHandler.removeCallbacks(adRun);
                    // 取消倒计时
                    myHandler.removeCallbacks(jumpRun);

                    // 先启动MainActivity，避免从播放场景返回到splashActivity
                    startMainActivity();

                    CurrentMovieInfo.updateInfo(WelcomeInfo.getInstance ().getVideoCode(), WelcomeInfo.getInstance ().getVideoType(),
                            WelcomeInfo.getInstance ().getVideoURL(), WelcomeInfo.getInstance ().getVideoName(), 0);
                    try {
                        boolean canWriteSettings = PermissionUtil.checkWriteSettingsPermission(SplashActivity.this);
                        if(canWriteSettings){
                            GoogleUnityActivityHelper.startCardboard(SplashActivity.this);
                        }else{
                            PermissionUtil.permitWriteSettings(SplashActivity.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                String report = HttpReporterBuilder.buildTemporaryReport(HttpReporterBuilder.adTag, WelcomeInfo.getInstance().getAdType());
                HttpReporterImpl.getInstance().reportSth(report);
                break;
            case R.id.adJump:
                // 取消跳转到MainActivity的调用
                myHandler.removeCallbacks(adRun);

                // 取消倒计时
                myHandler.removeCallbacks(jumpRun);

                startMainActivity();
                break;
            default:
                break;
        }
    }
}
