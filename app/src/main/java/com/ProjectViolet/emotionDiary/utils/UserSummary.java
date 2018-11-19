package com.ProjectViolet.emotionDiary.utils;

public class UserSummary {
    private String UserID;
    private String Username;
    private String UserDiaryStatus;

    public UserSummary(String UserID, String Username, String UserDiaryStatus) {
        this.UserID = UserID;
        this.Username = Username;
        this.UserDiaryStatus = UserDiaryStatus;
    }

    public String getUserID() {
        return UserID;
    }

    public String getUsername() {
        return Username;
    }

    public String getUserDiaryStatus() {
        return UserDiaryStatus;
    }

    public void setUserDiaryStatus(String userDiaryStatus) {
        UserDiaryStatus = userDiaryStatus;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public void setUsername(String username) {
        Username = username;
    }

}
