package com.educational_app.model;

public class ChapterDetailsItem {

    String question,answer;

    public ChapterDetailsItem() {
    }

    public ChapterDetailsItem(String question, String answer) {

        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
