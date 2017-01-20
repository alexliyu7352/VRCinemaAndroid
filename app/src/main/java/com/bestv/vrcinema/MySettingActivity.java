package com.bestv.vrcinema;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by xujunyang on 17/1/4.
 */

public class MySettingActivity extends AppCompatActivity implements View.OnClickListener{
    public static void actionStart(Context context, String subTitle){
        Intent intent = new Intent(context, MySettingActivity.class);
        intent.putExtra("subtitle", subTitle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_mysetting);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().setAttributes(params);

        ImageButton title_back = (ImageButton)findViewById(R.id.title_back);
        title_back.setOnClickListener(this);

        replaceFragment(new MySettingFragment());
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
            case R.id.title_back:
                // Catch back action and pops from backstack
                // (if you called previously to addToBackStack() in your transaction)
                if (getSupportFragmentManager().getBackStackEntryCount() > 0){
                    getSupportFragmentManager().popBackStack();
                }
                // Default action on back pressed
                else {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.mysetting_fragment, fragment);
        transaction.commit();
    }

}
