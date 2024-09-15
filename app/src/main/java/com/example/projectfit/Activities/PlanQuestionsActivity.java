package com.example.projectfit.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.tensorflow.lite.Interpreter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.io.FileInputStream;
import android.content.res.AssetFileDescriptor;
import java.io.IOException;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectfit.Models.Question;
import com.example.projectfit.Models.User;
import com.example.projectfit.R;
import com.example.projectfit.Room.Repositories.QuestionRoomRepository;
import com.example.projectfit.Room.Repositories.UserRoomRepository;
import com.example.projectfit.Server.Repositories.QuestionServerRepository;
import com.example.projectfit.Server.Repositories.UserServerRepository;
import com.example.projectfit.Utils.GsonProvider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class PlanQuestionsActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button continueButton;
    private ImageButton backButton;
    private LinearLayout answer1, answer2, answer3, answer4;
    private int currentQuestionIndex = 0;
    private final int progressStep = 14;
    private final int totalQuestions = 8;
    private List<Question> questions;
    private int selectedAnswerIndex = 0;
    private QuestionRoomRepository questionRoomRepository;
    private QuestionServerRepository questionServerRepository;
    private UserRoomRepository userRoomRepository;
    private UserServerRepository userServerRepository;
    private SharedPreferences sharedPreferences;
    private User user;
    private Interpreter tflite;
    private int[] userAnswers = new int[totalQuestions-1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plan_questions);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        user = getUserFromSharedPreferences();

        initializeViews();
        initializeRepositories();
        initializeModel();
        setupQuestions();
        loadQuestion(currentQuestionIndex);
        setAnswerClickListeners();
        continueButton.setEnabled(false);
    }

    private User getUserFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("logged_in_user", null);

        if (userJson != null) {
            Gson gson = GsonProvider.getGson();
            Type userType = new TypeToken<User>() {}.getType();
            return gson.fromJson(userJson, userType);
        }
        return null;
    }

    private void initializeViews() {
        progressBar = findViewById(R.id.progressBar);
        continueButton = findViewById(R.id.r9i96idg3y5l);
        backButton = findViewById(R.id.back_button);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);
    }

    private void initializeRepositories() {
        questionRoomRepository = new QuestionRoomRepository(this);
        questionServerRepository = new QuestionServerRepository();
        userRoomRepository = new UserRoomRepository(this);
        userServerRepository = new UserServerRepository();
    }

    private void setAnswerClickListeners() {
        answer1.setOnClickListener(view -> onAnswerClicked(view, 1));
        answer2.setOnClickListener(view -> onAnswerClicked(view, 2));
        answer3.setOnClickListener(view -> onAnswerClicked(view, 3));
        answer4.setOnClickListener(view -> onAnswerClicked(view, 4));
    }

    private void setupQuestions() {
        questions = new ArrayList<>();
        questions.add(new Question("Do you want to build the plan manually or automatically?",
                "Manually", "Automatically", null, null, 0));
        questions.add(new Question("What is your goal?", "Build muscle", "Lose weight", "Improve endurance", "Increase flexibility", 0));
        questions.add(new Question("What is your current fitness level?", "Beginner (0-6 months of regular exercise)", "Intermediate (6-24 months of regular exercise)", "Advanced (More than 24 months of regular exercise)", null, 0));
        questions.add(new Question("How many days per week can you commit to working out?", "1-2 days", "3-4 days", "5-6 days", "7 days", 0));
        questions.add(new Question("What type of workouts do you prefer?", "Weightlifting", "Bodyweight exercises", "Mobility", "Mixed (combination of all types)", 0));
        questions.add(new Question("Do you have any specific muscle groups you want to focus on?", "Upper body (arms, chest, back)", "Lower body (legs, glutes)", "Core (abs, lower back)", "Full body", 0));
        questions.add(new Question("Do you have any injuries or limitations that should be considered?", "No injuries or limitations", "Upper body injuries/limitations (shoulder, elbow, wrist)", "Lower body injuries/limitations (knee, ankle, hip)", "Back or neck injuries/limitations", 0));
        questions.add(new Question("How much time do you want to spend on each workout session?", "15-30 minutes", "30-45 minutes", "45-60 minutes", "More than 60 minutes", 0));
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("workout_plan_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private void initializeModel() {
        try {
            tflite = new Interpreter(loadModelFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runModelAndSaveRecommendation() {
        float[][] input = new float[1][10];

        for (int i = 0; i < 7; i++) {
            input[0][i] = userAnswers[i];
        }

        input[0][7] = calculateAge(user.getBirthday());
        input[0][8] = (float) user.getWeight();
        input[0][9] = (float) user.getHeight();

        float[][] output = new float[1][3];
        tflite.run(input, output);
        int recommendedPlanIndex = argmax(output[0]);
        String recommendedPlan = getWorkoutPlanFromIndex(recommendedPlanIndex);
        System.out.println(recommendedPlanIndex);
        System.out.println(recommendedPlan);
        saveRecommendation(recommendedPlan);
    }


    private int argmax(float[] array) {
        int maxIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private String getWorkoutPlanFromIndex(int index) {
        switch (index) {
            case 0: return "planA";
            case 1: return "planB";
            case 2: return "planC";
            default: return "Unknown Plan";
        }
    }

    private int calculateAge(LocalDate birthDate) {
        if (birthDate == null) {
            return 0;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Period.between(birthDate, LocalDate.now()).getYears();
        }
        return 0;
    }

    private void saveRecommendation(String plan) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("recommended_plan", plan);
        editor.apply();
        user.setPlan(plan);
        System.out.println(user.getPlan());
        userRoomRepository.updatePlanForUser(user, plan);
        userServerRepository.updateUserPlanInServer(user.getId(),plan, new UserServerRepository.OnUserUpdateCallback() {
            @Override
            public void onSuccess() {
                System.out.println("User updated successfully on the server.");
            }
            @Override
            public void onFailure(String errorMessage) {
                System.out.println("Failed to update user on the server: " + errorMessage);
            }
        });
    }


    private void loadQuestion(int index) {
        if (index < totalQuestions) {
            Question currentQuestion = questions.get(index);
            TextView questionText = findViewById(R.id.r5xj3hlxt1bn);
            questionText.setText(currentQuestion.getQuestionText());

            setAnswerTextAndVisibility(answer1, currentQuestion.getAnswerNo1());
            setAnswerTextAndVisibility(answer2, currentQuestion.getAnswerNo2());
            setAnswerTextAndVisibility(answer3, currentQuestion.getAnswerNo3());
            setAnswerTextAndVisibility(answer4, currentQuestion.getAnswerNo4());

            selectedAnswerIndex = currentQuestion.getChoseAnswer();
            updateSelectedAnswerUI();
            continueButton.setEnabled(selectedAnswerIndex != 0);
        }
    }

    private void setAnswerTextAndVisibility(LinearLayout answerLayout, String answerText) {
        if (answerText != null) {
            answerLayout.setVisibility(View.VISIBLE);
            ((TextView) answerLayout.getChildAt(0)).setText(answerText);
        } else {
            answerLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void onAnswerClicked(View view, int answerIndex) {
        resetAnswerBackgrounds();
        view.setBackgroundResource(R.drawable.cr36bff9a01);
        continueButton.setEnabled(true);
        selectedAnswerIndex = answerIndex;
        questions.get(currentQuestionIndex).setChoseAnswer(selectedAnswerIndex);
        if (currentQuestionIndex > 0) {
            userAnswers[currentQuestionIndex - 1] = selectedAnswerIndex;
        }
        continueButton.setText(currentQuestionIndex == totalQuestions - 1 ? "Done" : "Continue");
    }

    private void resetAnswerBackgrounds() {
        answer1.setBackgroundResource(R.drawable.cr36bffffff);
        answer2.setBackgroundResource(R.drawable.cr36bffffff);
        answer3.setBackgroundResource(R.drawable.cr36bffffff);
        answer4.setBackgroundResource(R.drawable.cr36bffffff);
    }

    private void updateSelectedAnswerUI() {
        resetAnswerBackgrounds();
        if (selectedAnswerIndex != 0) {
            switch (selectedAnswerIndex) {
                case 1:
                    answer1.setBackgroundResource(R.drawable.cr36bff9a01);
                    break;
                case 2:
                    answer2.setBackgroundResource(R.drawable.cr36bff9a01);
                    break;
                case 3:
                    answer3.setBackgroundResource(R.drawable.cr36bff9a01);
                    break;
                case 4:
                    answer4.setBackgroundResource(R.drawable.cr36bff9a01);
                    break;
            }
        }
    }

    public void onContinueClicked(View view) {
        if (currentQuestionIndex == 0) {
            if (selectedAnswerIndex == 1) {
                user.setBuildPlan(false);
                updateBuildPlanInDatabase();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("plan_type", "manual");
                editor.apply();

                Intent intent = new Intent(PlanQuestionsActivity.this, BottomNavigate.class);
                intent.putExtra("navigateTo", "plan");
                intent.putExtra("plan", "manual");
                startActivity(intent);
                finish();
            } else {
                currentQuestionIndex++;
                progressBar.setProgress(currentQuestionIndex * progressStep);
                loadQuestion(currentQuestionIndex);
            }
        } else if (currentQuestionIndex < totalQuestions - 1) {
            currentQuestionIndex++;
            progressBar.setProgress(currentQuestionIndex * progressStep);
            loadQuestion(currentQuestionIndex);
        } else {
            saveUserAnswers();
            user.setBuildPlan(false);
            updateBuildPlanInDatabase();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("plan_type", "Auto");
            editor.apply();

            runModelAndSaveRecommendation();
            Intent intent = new Intent(PlanQuestionsActivity.this, BottomNavigate.class);
            intent.putExtra("navigateTo", "plan");
            intent.putExtra("plan", "Auto");
            startActivity(intent);
            finish();
        }
    }


    private void saveUserAnswers() {
        List<String> answerList = new ArrayList<>();
        for (int answer : userAnswers) {
            answerList.add(String.valueOf(answer));
        }
        user.setAnswers(answerList);
        userRoomRepository.updateUserLocally(user);
        saveUserToSharedPreferences();
    }

    private void updateBuildPlanInDatabase() {
        user.setBuildPlan(false);
        userRoomRepository.updateUserLocally(user);
        saveUserToSharedPreferences();
    }


    private void saveUserToSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = GsonProvider.getGson();
        String userJson = gson.toJson(user);
        editor.putString("logged_in_user", userJson);
        editor.apply();
    }

    public void onBackClicked(View view) {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            loadQuestion(currentQuestionIndex);
            continueButton.setText("Continue");
            progressBar.setProgress(currentQuestionIndex * progressStep);
        } else {
            finish();
        }
    }
}
