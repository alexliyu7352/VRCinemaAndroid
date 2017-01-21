package com.bestv.vrcinema.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xujunyang on 17/1/3.
 */

public class MovieInfo implements Parcelable {
    // 共15个字段
    private String name;
    private String code;
    private String type;  //影片播放类型
    private String playUrl;
    private String horizontalPic;
    private String verticalPic;
    private int    duration;
    private Double rating;
    private String showDetail;
    private String year;
    private int episode;  //影片剧集数量
    private String genre;
    private String actor;
    private String director;
    private String description;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getHorizontalPic() {
        return horizontalPic;
    }

    public void setHorizontalPic(String horizontalPic) {
        this.horizontalPic = horizontalPic;
    }

    public String getVerticalPic() {
        return verticalPic;
    }

    public void setVerticalPic(String verticalPic) {
        this.verticalPic = verticalPic;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getShowDetail() {
        return showDetail;
    }

    public void setShowDetail(String showDetail) {
        this.showDetail = showDetail;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getEpisode() {
        return episode;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static final Parcelable.Creator<MovieInfo> CREATOR = new Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel source) {
            MovieInfo movieInfo = new MovieInfo();
            movieInfo.name = source.readString();
            movieInfo.code = source.readString();
            movieInfo.type = source.readString();
            movieInfo.playUrl = source.readString();
            movieInfo.horizontalPic = source.readString();
            movieInfo.verticalPic = source.readString();
            movieInfo.duration = source.readInt();
            movieInfo.rating = source.readDouble();
            movieInfo.showDetail = source.readString();
            movieInfo.year = source.readString();
            movieInfo.episode = source.readInt();
            movieInfo.genre = source.readString();
            movieInfo.actor = source.readString();
            movieInfo.director = source.readString();
            movieInfo.description = source.readString();
            return movieInfo;
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(code);
        out.writeString(type);
        out.writeString(playUrl);
        out.writeString(horizontalPic);
        out.writeString(verticalPic);
        out.writeInt(duration);
        out.writeDouble(rating);
        out.writeString(showDetail);
        out.writeString(year);
        out.writeInt(episode);
        out.writeString(genre);
        out.writeString(actor);
        out.writeString(director);
        out.writeString(description);
    }
}
