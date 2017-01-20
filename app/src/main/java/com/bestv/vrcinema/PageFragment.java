package com.bestv.vrcinema;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bestv.vrcinema.httpReport.HttpReporterBuilder;
import com.bestv.vrcinema.httpReport.HttpReporterImpl;
import com.bestv.vrcinema.model.CurrentMovieInfo;
import com.bestv.vrcinema.model.MovieInfo;
import com.bestv.vrcinema.util.GoogleUnityActivityHelper;
import com.bestv.vrcinema.util.NetWorkUtils;
import com.bestv.vrcinema.util.PermissionUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by xujunyang on 17/1/4.
 */

public class PageFragment extends Fragment {
    private int index = 0;
    private boolean firstDisappear = true;
    private MovieInfo movieInfo;

    private RelativeLayout infoArea;
    private ImageView imageView;
    private TextView durationView;
    private TextView ratingView;
    private TextView titleView;

    private TextView directorView;
    private TextView actorView;
    private TextView descriptionView;

    private ImageButton playButton;


    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.page_fragment, container, false);

        initUI(view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        playButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!NetWorkUtils.isNetworkConnected(getActivity())){
                    Toast.makeText(getActivity(), "请检查网络连接后重试", Toast.LENGTH_SHORT).show();
                    return;
                }

                CurrentMovieInfo.updateInfo(movieInfo.code, movieInfo.type, movieInfo.playUrl, movieInfo.name, movieInfo.episode);

                try {
                    boolean canWriteSettings = PermissionUtil.checkWriteSettingsPermission(getActivity());
                    if(canWriteSettings){
                        GoogleUnityActivityHelper.startCardboard(getActivity());
                    }else{
                        PermissionUtil.permitWriteSettings(getActivity());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String report = HttpReporterBuilder.buildTemporaryReport(HttpReporterBuilder.play, String.format("%s+%s",HttpReporterBuilder.recommendPlay, movieInfo.code));
                HttpReporterImpl.getInstance().reportSth(report);
            }
        });
    }

    public void bindData(MovieInfo vo) {
        movieInfo = vo;
    }

    private void initUI(View view){
        try{
            playButton = (ImageButton)view.findViewById(R.id.play);

            durationView = (TextView)view.findViewById(R.id.duration);
            durationView.setText(movieInfo.duration+"分钟");

            ratingView = (TextView)view.findViewById(R.id.rating);
            ratingView.setText(movieInfo.rating+"分");

            titleView = (TextView)view.findViewById(R.id.title);
            titleView.setText(movieInfo.name);

            directorView = (TextView)view.findViewById(R.id.director);
            directorView.setText("导演: "+movieInfo.director);

            actorView = (TextView)view.findViewById(R.id.actor);
            actorView.setText("演员: "+movieInfo.actor);

            descriptionView = (TextView)view.findViewById(R.id.description);
            if(!TextUtils.isEmpty(movieInfo.description)){
                if(movieInfo.description.length() < 100){
                    descriptionView.setText("剧情简介: "+movieInfo.description);
                }else{
                    descriptionView.setText("剧情简介: "+movieInfo.description.substring(0, 100));
                }
            }

            infoArea = (RelativeLayout)view.findViewById(R.id.info_area);
            if(index == 1 && firstDisappear){
                firstDisappear = false;
                disappearMovieInfo();
            }

            imageView = (ImageView)view.findViewById(R.id.posters);
            if(!TextUtils.isEmpty(movieInfo.horizontalPic)){
                ImageLoader.getInstance().displayImage(movieInfo.horizontalPic, imageView);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setIndex(int index){
        this.index = index;
    }

    public void appearMovieInfo(){
        if(infoArea != null){
            infoArea.setVisibility(View.VISIBLE);
        }
    }

    public void disappearMovieInfo(){
        if (infoArea != null){
            infoArea.setVisibility(View.GONE);
        }
    }
}
