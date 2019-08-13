package com.alpha.test.i_fans;

public class TeamReview {
    private Integer id,rating;
    private String review;

    public TeamReview(Integer id, Integer rating, String review) {
        this.id = id;
        this.rating = rating;
        this.review = review;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
