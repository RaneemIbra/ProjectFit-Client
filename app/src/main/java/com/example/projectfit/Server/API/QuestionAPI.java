package com.example.projectfit.Server.API;

import com.example.projectfit.Models.Question;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface QuestionAPI {
    @GET("question")
    Call<List<Question>> getAllQuestions();
}
