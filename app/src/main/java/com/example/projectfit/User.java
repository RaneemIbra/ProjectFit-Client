package com.example.projectfit;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class User {
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
    private byte[] profilePicture;
    List<Boolean> achievements;
    private Map<Integer, Workout> plan;
    private Map<Integer, Workout> workoutHistory;
    private Map<LocalDate, Integer> stepsHistory;
    private Map<LocalDate, Integer> waterHistory;

    public User(Long id, String fullName, Long phoneNum, String emailAddress, String password,
                LocalDate birthday, double height, double weight, boolean gender, String securityQuestion,
                String answer, byte[] profilePicture, List<Boolean> achievements,
                Map<Integer, Workout> plan, Map<Integer, Workout> workoutHistory,
                Map<LocalDate, Integer> stepsHistory, Map<LocalDate, Integer> waterHistory) {
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

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
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
