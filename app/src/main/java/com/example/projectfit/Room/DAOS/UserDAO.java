package com.example.projectfit.Room.DAOS;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projectfit.Models.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Query("SELECT * FROM users WHERE emailAddress = :email LIMIT 1")
    User getUserByEmail(String email);

    @Query("SELECT * FROM users WHERE emailAddress = :email AND answer = :answer LIMIT 1")
    User getUserByEmailAndAnswer(String email, String answer);
}
