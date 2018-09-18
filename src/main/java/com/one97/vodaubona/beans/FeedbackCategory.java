package com.one97.vodaubona.beans;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class FeedbackCategory {

    private String categoryId;
    private String startSOU;
    private String endSOU;
    private String songSelected;
    private List<String> songsPlayed;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getStartSOU() {
        return startSOU;
    }

    public void setStartSOU(String startSOU) {
        this.startSOU = startSOU;
    }

    public String getEndSOU() {
        return endSOU;
    }

    public void setEndSOU(String endSOU) {
        this.endSOU = endSOU;
    }

    public String getSongSelected() {
        return songSelected;
    }

    public void setSongSelected(String songSelected) {
        this.songSelected = songSelected;
    }

    public List<String> getSongsPlayed() {
        return songsPlayed;
    }

    public void setSongsPlayed(List<String> songsPlayed) {
        this.songsPlayed = songsPlayed;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FeedbackCategory [categoryId=");
        builder.append(categoryId);
        builder.append(", startSOU=");
        builder.append(startSOU);
        builder.append(", endSOU=");
        builder.append(endSOU);
        builder.append(", songSelected=");
        builder.append(songSelected);
        builder.append(", songsPlayed=");
        builder.append(songsPlayed);
        builder.append("]");
        return builder.toString();
    }

}
