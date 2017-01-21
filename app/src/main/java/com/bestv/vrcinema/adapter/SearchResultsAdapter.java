package com.bestv.vrcinema.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bestv.vrcinema.R;
import com.bestv.vrcinema.httpReport.HttpReporterBuilder;
import com.bestv.vrcinema.httpReport.HttpReporterImpl;
import com.bestv.vrcinema.model.CurrentMovieInfo;
import com.bestv.vrcinema.model.MovieInfo;
import com.bestv.vrcinema.util.GoogleUnityActivityHelper;
import com.bestv.vrcinema.util.NetWorkUtils;
import com.bestv.vrcinema.util.PermissionUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by xujunyang on 17/1/10.
 */

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {
    private Activity activity;
    private List<MovieInfo> movieInfoList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View resultItem;
        FrameLayout frame1;
        ImageView image1;
        TextView text1;
        FrameLayout frame2;
        ImageView image2;
        TextView text2;

        public ViewHolder(View view){
            super(view);
            resultItem = view;
            frame1 = (FrameLayout)view.findViewById(R.id.frame1);
            image1 = (ImageView)view.findViewById(R.id.image1);
            text1 = (TextView)view.findViewById(R.id.text1);
            frame2 = (FrameLayout)view.findViewById(R.id.frame2);
            image2 = (ImageView)view.findViewById(R.id.image2);
            text2 = (TextView)view.findViewById(R.id.text2);
        }
    }

    public SearchResultsAdapter(Activity activity, List<MovieInfo> movieInfoList){
        this.activity = activity;
        this.movieInfoList = movieInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_list_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.image1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!NetWorkUtils.isNetworkConnected(activity)){
                    Toast.makeText(activity, "请检查网络连接后重试", Toast.LENGTH_SHORT).show();
                    return;
                }

                int position = holder.getAdapterPosition();
                MovieInfo movieInfo = movieInfoList.get(position*2);
                CurrentMovieInfo.updateInfo(movieInfo.getCode(), movieInfo.getType(),
                        movieInfo.getPlayUrl(), movieInfo.getName(), movieInfo.getEpisode());

                try {
                    boolean canWriteSettings = PermissionUtil.checkWriteSettingsPermission(activity);
                    if(canWriteSettings){
                        GoogleUnityActivityHelper.startCardboard(activity);
                    }else{
                        PermissionUtil.permitWriteSettings(activity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 2: play search result
                String report = HttpReporterBuilder.buildTemporaryReport(HttpReporterBuilder.play, String.format("%s+%s",
                        HttpReporterBuilder.searchPlay, movieInfo.getCode()));
                HttpReporterImpl.getInstance().reportSth(report);
            }
        });

        holder.image2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!NetWorkUtils.isNetworkConnected(activity)){
                    Toast.makeText(activity, "请检查网络连接后重试", Toast.LENGTH_SHORT).show();
                    return;
                }

                int position = holder.getAdapterPosition();
                MovieInfo movieInfo = movieInfoList.get(position*2+1);
                CurrentMovieInfo.updateInfo(movieInfo.getCode(), movieInfo.getType(), movieInfo.getPlayUrl(),
                        movieInfo.getName(), movieInfo.getEpisode());

                try {
                    boolean canWriteSettings = PermissionUtil.checkWriteSettingsPermission(activity);
                    if(canWriteSettings){
                        GoogleUnityActivityHelper.startCardboard(activity);
                    }else{
                        PermissionUtil.permitWriteSettings(activity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 2: play search result
                String report = HttpReporterBuilder.buildTemporaryReport(HttpReporterBuilder.play, String.format("%s+%s",
                        HttpReporterBuilder.searchPlay, movieInfo.getCode()));
                HttpReporterImpl.getInstance().reportSth(report);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.image1.setImageResource(android.R.color.transparent);
        holder.image2.setImageResource(android.R.color.transparent);

        holder.frame1.setVisibility(View.VISIBLE);
        MovieInfo movieInfo1 = movieInfoList.get(position*2);
        if(!TextUtils.isEmpty(movieInfo1.getHorizontalPic())){
            ImageLoader.getInstance().displayImage(movieInfo1.getHorizontalPic(), holder.image1);
        }
        holder.text1.setText(movieInfo1.getName());

        if(movieInfoList.size() > (position*2+1)){
            holder.frame2.setVisibility(View.VISIBLE);
            MovieInfo movieInfo2 = movieInfoList.get(position*2+1);
            if(!TextUtils.isEmpty(movieInfo2.getHorizontalPic())){
                ImageLoader.getInstance().displayImage(movieInfo2.getHorizontalPic(), holder.image2);
            }
            holder.text2.setText(movieInfo2.getName());
        }else{
            holder.frame2.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount(){
        return (movieInfoList.size()+1)/2;
    }
}