package com.example.projectfit.Models;

public class Question {
    private Long id;
    String questionText;
    String answerNo1;
    String answerNo2;
    String answerNo3;
    String answerNo4;
    int choseAnswer;

    public Question(Long id, String questionText, String answerNo1, String answerNo2, String answerNo3, String answerNo4, int choseAnswer) {
        this.id = id;
        this.questionText = questionText;
        this.answerNo1 = answerNo1;
        this.answerNo2 = answerNo2;
        this.answerNo3 = answerNo3;
        this.answerNo4 = answerNo4;
        this.choseAnswer = choseAnswer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAnswerNo1() {
        return answerNo1;
    }

    public void setAnswerNo1(String answerNo1) {
        this.answerNo1 = answerNo1;
    }

    public String getAnswerNo2() {
        return answerNo2;
    }

    public void setAnswerNo2(String answerNo2) {
        this.answerNo2 = answerNo2;
    }

    public String getAnswerNo3() {
        return answerNo3;
    }

    public void setAnswerNo3(String answerNo3) {
        this.answerNo3 = answerNo3;
    }

    public String getAnswerNo4() {
        return answerNo4;
    }

    public void setAnswerNo4(String answerNo4) {
        this.answerNo4 = answerNo4;
    }

    public int getChoseAnswer() {
        return choseAnswer;
    }

    public void setChoseAnswer(int choseAnswer) {
        this.choseAnswer = choseAnswer;
    }
}
