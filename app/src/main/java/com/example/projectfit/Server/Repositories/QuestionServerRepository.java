package com.example.projectfit.Server.Repositories;

import com.example.projectfit.Models.Question;
import com.example.projectfit.Models.Workout;
import com.example.projectfit.Server.API.AppAPI;
import com.example.projectfit.Server.API.QuestionAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionServerRepository {
    private QuestionAPI questionAPI;
    public QuestionServerRepository() {
        questionAPI = AppAPI.getClient().create(QuestionAPI.class);
    }
    public void addQuestionInServer(Question question){
        Call<Question> call = questionAPI.createQuestion(question);
        call.enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                if (response.isSuccessful()) {
                    System.out.println("Question submitted successfully");
                } else {
                    System.out.println("Question wasn't submitted successfully");
                }
            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());
            }
        });
    }
}
