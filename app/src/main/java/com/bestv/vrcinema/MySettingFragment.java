package com.bestv.vrcinema;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bestv.vrcinema.httpReport.HttpReporterBuilder;
import com.bestv.vrcinema.httpReport.HttpReporterImpl;
import com.bestv.vrcinema.model.CurrentMovieInfo;
import com.bestv.vrcinema.util.GoogleUnityActivityHelper;
import com.bestv.vrcinema.util.NetWorkUtils;
import com.bestv.vrcinema.util.PermissionUtil;

/**
 * Created by xujunyang on 17/1/12.
 */

public class MySettingFragment extends Fragment implements View.OnClickListener {
    public static final String ARG = "arg";
    private String settingFragmentTag = "settingFragmentTag";
    private ImageButton helpButton;
    private ImageButton qqgroupButton;
    private ImageButton markButton;
    private ImageButton bugButton;
    private ImageButton surveyButton;

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.mysetting_fragment, container, false);

        helpButton = (ImageButton)view.findViewById(R.id.setting_help);
        qqgroupButton = (ImageButton)view.findViewById(R.id.setting_qqgroup);
        markButton = (ImageButton)view.findViewById(R.id.setting_mark);
        bugButton = (ImageButton)view.findViewById(R.id.setting_bug);
        surveyButton = (ImageButton)view.findViewById(R.id.setting_survey);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        helpButton.setOnClickListener(this);
        qqgroupButton.setOnClickListener(this);
        markButton.setOnClickListener(this);
        bugButton.setOnClickListener(this);
        surveyButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        String setting = "test";
        switch (v.getId()){
            case R.id.setting_help:
                setting = "help";
                openURLByWebView("http://vr.ott.bestv.com.cn/help.html");
                break;
            case R.id.setting_qqgroup:
                setting = "qqgroup";
                String url="https://jq.qq.com/?_wv=1027&k=43fOvPI";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                break;
            case R.id.setting_mark:
                setting = "mark";
                openURLByWebView("https://sojump.com/jq/11797582.aspx");
                break;
            case R.id.setting_bug:
                setting = "bug";
                openURLByWebView("https://www.sojump.hk/jq/10541628.aspx");
                break;
            case R.id.setting_survey:
                setting = "survey";
                openURLByWebView("https://sojump.com/jq/10635395.aspx");
                break;
            default:
                break;
        }

        String report = HttpReporterBuilder.buildTemporaryReport(HttpReporterBuilder.setting, setting);
        HttpReporterImpl.getInstance().reportSth(report);
    }

    private void openURLByWebView(String url){
        WebviewFragment fragment = new WebviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString( ARG, url);
        fragment.setArguments(bundle);
        replaceFragment(fragment);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.mysetting_fragment, fragment, settingFragmentTag);
        transaction.addToBackStack(settingFragmentTag);
        transaction.commit();
    }
}
