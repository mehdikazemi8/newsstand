package com.nebeek.newsstand.data.models;

//@AutoValue
//public abstract class Comment {
public class Comment {

    private String username;
    private String comment;
    private String likeCount;
    private String dislikeCount;

    public Comment() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(String dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    /*
    abstract String username();

    abstract String comment();

    abstract Integer likeCount();

    abstract Integer dislikeCount();

    static Builder builder() {
        return new AutoValue_Comment.Builder();
    }

    @AutoValue.Builder
    abstract static class Builder {
        abstract Builder username(String username);

        abstract Builder comment(String comment);

        abstract Builder likeCount(Integer likeCount);

        abstract Builder dislikeCount(Integer dislikeCount);

        abstract Comment build();
    }

    public static TypeAdapter<AutoValue_Comment> typeAdapter(Gson gson) {
        return new AutoValue_Comment.GsonTypeAdapter(gson);
    }
    */
}