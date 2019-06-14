package com.dev.fb.client.model;

import java.util.List;

/**
 * Created by deb on 16-05-2019.
 */
public class PostData {

    private String postName;
    private String postDate;
    private List<String> comments;
    private List<String> likes;

    public PostData(String postName, String postDate, List<String> comments, List<String> likes) {

        this.postName = postName;
        this.postDate = postDate;
        this.comments = comments;
        this.likes = likes;
    }

    public String getPostName() {

        return postName;
    }

    public String getPostDate() {
        return postDate;
    }

    public List<String> getComments() {
        return comments;
    }

    public List<String> getLikes() {
        return likes;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PostData postData = (PostData) o;

        if (postName != null ? !postName.equals(postData.postName) : postData.postName != null) return false;
        if (postDate != null ? !postDate.equals(postData.postDate) : postData.postDate != null) return false;
        if (comments != null ? !comments.equals(postData.comments) : postData.comments != null) return false;
        return !(likes != null ? !likes.equals(postData.likes) : postData.likes != null);

    }

    @Override
    public int hashCode() {

        int result = postName != null ? postName.hashCode() : 0;
        result = 31 * result + (postDate != null ? postDate.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (likes != null ? likes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PostData{" +
                "postName='" + postName + '\'' +
                ", postDate='" + postDate + '\'' +
                ", comments=" + comments +
                ", likes=" + likes +
                '}';
    }
}
