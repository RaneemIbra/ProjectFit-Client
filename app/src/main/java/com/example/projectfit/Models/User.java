package com.example.projectfit.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//import com.example.projectfit.Models.Workout;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Entity(tableName = "users")
public class User implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    String fullName;
    Long phoneNum;
    String emailAddress;
    String password;
    LocalDate birthday;
    double height;
    double weight;
    boolean gender;
    String securityQuestion;
    String answer;
    private String profilePicture;
    List<Boolean> achievements;
    private Map<Integer, Workout> plan;
    private Map<Integer, Workout> workoutHistory;
    private Map<LocalDate, Integer> stepsHistory;
    private Map<LocalDate, Integer> waterHistory;

    public User(Long id, String fullName, Long phoneNum, String emailAddress, String password, LocalDate birthday,
                double height, double weight, boolean gender, String securityQuestion, String answer,
                String profilePicture, List<Boolean> achievements, Map<Integer, Workout> plan,
                Map<Integer, Workout> workoutHistory, Map<LocalDate, Integer> stepsHistory,
                Map<LocalDate, Integer> waterHistory) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNum = phoneNum;
        this.emailAddress = emailAddress;
        this.password = password;
        this.birthday = birthday;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.securityQuestion = securityQuestion;
        this.answer = answer;
        this.profilePicture = profilePicture;
        this.achievements = achievements;
        this.plan = plan;
        this.workoutHistory = workoutHistory;
        this.stepsHistory = stepsHistory;
        this.waterHistory = waterHistory;
    }

    protected User(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        fullName = in.readString();
        if (in.readByte() == 0) {
            phoneNum = null;
        } else {
            phoneNum = in.readLong();
        }
        emailAddress = in.readString();
        password = in.readString();
        height = in.readDouble();
        weight = in.readDouble();
        gender = in.readByte() != 0;
        securityQuestion = in.readString();
        answer = in.readString();
        profilePicture = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(fullName);
        if (phoneNum == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(phoneNum);
        }
        dest.writeString(emailAddress);
        dest.writeString(password);
        dest.writeDouble(height);
        dest.writeDouble(weight);
        dest.writeByte((byte) (gender ? 1 : 0));
        dest.writeString(securityQuestion);
        dest.writeString(answer);
        dest.writeString(profilePicture);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(Long phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<Boolean> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Boolean> achievements) {
        this.achievements = achievements;
    }

    public Map<Integer, Workout> getPlan() {
        return plan;
    }

    public void setPlan(Map<Integer, Workout> plan) {
        this.plan = plan;
    }

    public Map<Integer, Workout> getWorkoutHistory() {
        return workoutHistory;
    }

    public void setWorkoutHistory(Map<Integer, Workout> workoutHistory) {
        this.workoutHistory = workoutHistory;
    }

    public Map<LocalDate, Integer> getStepsHistory() {
        return stepsHistory;
    }

    public void setStepsHistory(Map<LocalDate, Integer> stepsHistory) {
        this.stepsHistory = stepsHistory;
    }

    public Map<LocalDate, Integer> getWaterHistory() {
        return waterHistory;
    }

    public void setWaterHistory(Map<LocalDate, Integer> waterHistory) {
        this.waterHistory = waterHistory;
    }
}
