package com.bestv.vrcinema;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bestv.vrcinema.httpUtil.OnAPITaskDoneListener;
import com.bestv.vrcinema.constant.ConstantInterface;
import com.bestv.vrcinema.httpUtil.HttpGetTask;
import com.bestv.vrcinema.httpUtil.OnSearchMsgTaskDoneListener;
import com.bestv.vrcinema.model.CurrentMovieInfo;
import com.bestv.vrcinema.model.RecommendInfoSingleton;
import com.bestv.vrcinema.util.GoogleUnityActivityHelper;
import com.bestv.vrcinema.util.NetWorkUtils;
import com.bestv.vrcinema.util.PermissionUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujy on 2016/9/12.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressBar mainProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().setAttributes(params);


        // 1. 设置toolbar按钮的触发事件
		ImageButton button_more = (ImageButton)findViewById(R.id.button_more);
        button_more.setOnClickListener(this);
		ImageButton button_vrmode = (ImageButton)findViewById(R.id.button_vrmode);
        button_vrmode.setOnClickListener(this);
		ImageButton button_search = (ImageButton)findViewById(R.id.button_search);
        button_search.setOnClickListener(this);
        mainProgressBar = (ProgressBar)findViewById(R.id.main_progressbar);

        // 2. 初始化友盟推送
        initUPush();

        // 3. 获取推荐信息
        requestRecommendInfo();
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

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button_more:
                MySettingActivity.actionStart(MainActivity.this, "我的设置");
                break;
            case R.id.button_vrmode:
                // 清空2D界面的播放信息
                CurrentMovieInfo.reset();

                enterVRMode();
                break;
            case R.id.button_search:
                SearchContentActivity.actionStart(MainActivity.this, "Search");
                break;
            default:
                break;
        }
    }


    private void enterVRMode(){
        try {
			boolean canWriteSettings = PermissionUtil.checkWriteSettingsPermission(this);
			if(canWriteSettings){
				GoogleUnityActivityHelper.startCardboard(MainActivity.this);
			}else{
                PermissionUtil.permitWriteSettings(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
            GoogleUnityActivityHelper.startCardboard(MainActivity.this);
		}
    }

	@TargetApi(Build.VERSION_CODES.M)
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ConstantInterface.REQUEST_CODE_WRITE_SETTINGS) {
			if (PermissionUtil.checkWriteSettingsPermission(MainActivity.this)) {
				//检查返回结果
				Toast.makeText(this, "WRITE_SETTINGS permission granted", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "WRITE_SETTINGS permission not granted", Toast.LENGTH_SHORT).show();
			}

            GoogleUnityActivityHelper.startCardboard(MainActivity.this);
		}
	}

    public void initContentFragment(){
        mainProgressBar.setVisibility(View.GONE);
        replaceFragment(new ContentFragment());
    }

	private void replaceFragment(Fragment fragment){
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.fragment_layout, fragment);
		transaction.commitAllowingStateLoss();
	}

	private void initUPush(){
		PushAgent mPushAgent = PushAgent.getInstance(this);
		mPushAgent.register(new IUmengRegisterCallback() {
			@Override
			public void onSuccess(String s) {
				Log.i("PushAgent", "onSuccess");
			}

			@Override
			public void onFailure(String s, String s1) {
				Log.i("PushAgent", "onFailure");
			}
		});
	}

    private void requestRecommendInfo(){
        if(!NetWorkUtils.isNetworkConnected(MainActivity.this)){
            Toast.makeText(MainActivity.this, "网络无法连接", Toast.LENGTH_SHORT).show();
            return;
        }

        if(RecommendInfoSingleton.getInstance().getItemsCount() <= 0){
            mainProgressBar.setVisibility(View.VISIBLE);
            new HttpGetTask(new OnAPITaskDoneListener(MainActivity.this)).execute(ConstantInterface.RECOMMENDURL+"&version="+ConstantInterface.VERSION);
        }

        // 获取搜索提示
        String searchHint = RecommendInfoSingleton.getInstance().getSearchMessage();
        if(TextUtils.isEmpty(searchHint)){
            new HttpGetTask(new OnSearchMsgTaskDoneListener()).execute(ConstantInterface.SearchRecommend+"?version="+ConstantInterface.VERSION);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
