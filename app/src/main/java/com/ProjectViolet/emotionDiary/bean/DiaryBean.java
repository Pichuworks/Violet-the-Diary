package com.ProjectViolet.emotionDiary.bean;

/**
 * Created by 李 on 2017/1/26.
 */
public class DiaryBean {
    private String date;
    private String title;
    private String content;
    private String tag;
    private String id;
    private String analysedResult;

    public DiaryBean(String date, String title, String content, String tag, String id, String analysedResult) {
        this.date = date;
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.id=id;
        this.analysedResult = analysedResult;
    }
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAnalysedResult(String analysedResult) {
        this.analysedResult = analysedResult;
    }

    public String getAnalysedResult() {
        return analysedResult;
    }
}
