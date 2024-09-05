package com.example.projectfit.Room.Repositories;

import android.content.Context;

import com.example.projectfit.Models.Question;
import com.example.projectfit.Room.Databases.QuestionDatabase;

import java.util.concurrent.Executors;

public class QuestionRoomRepository {
    private final QuestionDatabase questionDatabase;
    public QuestionRoomRepository(Context context) {
        questionDatabase = QuestionDatabase.getInstance(context.getApplicationContext());
    }
    public void addQuestionLocally(Question question) {
        Executors.newSingleThreadExecutor().execute(() -> {
            questionDatabase.questionDAO().insertQuestion(question);
            System.out.println("is it working?");
        });
    }
}
