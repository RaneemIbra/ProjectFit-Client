package com.example.projectfit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface QuestionAPI {
    @GET("question")
    Call<List<Question>> getAllQuestions();
}
