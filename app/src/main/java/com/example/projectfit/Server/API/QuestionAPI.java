package com.example.projectfit.Server.API;

import com.example.projectfit.Models.Question;
import com.example.projectfit.Models.Workout;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface QuestionAPI {
    @GET("questions")
    Call<List<Question>> getAllQuestions();

    @POST("questions")
    Call<Question> createQuestion(@Body Question question);
}
