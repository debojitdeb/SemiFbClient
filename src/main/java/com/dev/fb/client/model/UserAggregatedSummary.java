package com.dev.fb.client.model;

import java.util.List;

/**
 * Created by Debojit Deb on 11-02-2018.
 */

public class UserAggregatedSummary implements Comparable<UserAggregatedSummary> {

    private String name;
    private String link;
    private int numberOfComments;
    private int numberOfLikes;
    private List<String> commentDates;
    private List<String> likeDates;

    public UserAggregatedSummary(String name, String link, int numberOfComments,  int numberOfLikes,
                                 List<String> commentDates, List<String> likeDates) {

        this.name = name;
        this.link = link;
        this.numberOfComments =  numberOfComments;
        this.numberOfLikes = numberOfLikes;
        this.commentDates = commentDates;
        this.likeDates = likeDates;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public void setNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public List<String> getCommentDates() {
        return commentDates;
    }

    public List<String> getLikeDates() {
        return likeDates;
    }

    public void setLikeDates(List<String> likeDates) {
        this.likeDates = likeDates;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAggregatedSummary that = (UserAggregatedSummary) o;

        if (numberOfComments != that.numberOfComments) return false;
        if (numberOfLikes != that.numberOfLikes) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (link != null ? !link.equals(that.link) : that.link != null) return false;
        if (commentDates != null ? !commentDates.equals(that.commentDates) : that.commentDates != null) return false;
        return !(likeDates != null ? !likeDates.equals(that.likeDates) : that.likeDates != null);

    }

    @Override
    public int hashCode() {

        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + numberOfComments;
        result = 31 * result + numberOfLikes;
        result = 31 * result + (commentDates != null ? commentDates.hashCode() : 0);
        result = 31 * result + (likeDates != null ? likeDates.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {

        return "UserAggregatedSummary{" +
                "name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", numberOfComments=" + numberOfComments +
                ", numberOfLikes=" + numberOfLikes +
                ", commentDates=" + commentDates +
                ", likeDates=" + likeDates +
                '}';
    }

    //@Override
    public int compareTo(UserAggregatedSummary o) {

        int otherTotalNumberOfLikesAndComments = o.getNumberOfComments() + o.getNumberOfLikes();
        int thisTotalNumberOfLikesAndComments = getNumberOfComments() + getNumberOfLikes();
        int difference = otherTotalNumberOfLikesAndComments - thisTotalNumberOfLikesAndComments;
        if(difference != 0) {
            return difference;
        }

        return name.compareTo(o.name);
    }

}
