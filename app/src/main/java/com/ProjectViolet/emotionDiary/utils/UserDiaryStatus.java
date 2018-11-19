package com.ProjectViolet.emotionDiary.utils;

import java.util.ArrayList;

public class UserDiaryStatus {
    private String UserID;
    private ArrayList<String> UserDiaryStatus;

    public UserDiaryStatus(String UserID, ArrayList<String> userDiaryStatus) {
        this.UserID = UserID;
        this.UserDiaryStatus = userDiaryStatus;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public void setUserDiaryStatus(ArrayList<String> userDiaryStatus) {
        UserDiaryStatus = userDiaryStatus;
    }

    public ArrayList<String> getUserDiaryStatus() {
        return UserDiaryStatus;
    }

    public String getUserID() {
        return UserID;
    }
}
