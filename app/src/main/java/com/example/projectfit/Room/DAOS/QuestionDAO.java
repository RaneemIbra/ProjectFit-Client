package com.example.projectfit.Room.DAOS;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projectfit.Room.Entities.Question;

import java.util.List;

@Dao
public interface QuestionDAO {
    @Insert
    void insertQuestion(Question question);

    @Update
    void updateQuestion(Question question);

    @Query("SELECT * FROM questions")
    LiveData<List<Question>> getAllQuestions();
}
