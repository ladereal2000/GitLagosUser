package com.practice.gitlagosuser.models;

/**
 * Created by David on 8/11/2017.
 */

public class GitUsers {

    private int id;
    private String username;
    private String score;
    private String avatarUrl;
    private String htmlUrl;
    private String profileUrl;


    /**
     *
     * @param id
     * @param username
     * @param score
     * @param avatarUrl
     * @param htmlUrl
     * @param profileUrl
     */
    public GitUsers(int id, String username, String score, String avatarUrl, String htmlUrl, String profileUrl) {
        this.id = id;
        this.username = username;
        this.score = score;
        this.avatarUrl = avatarUrl;
        this.htmlUrl = htmlUrl;
        this.profileUrl = profileUrl;
    }


    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }


    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
