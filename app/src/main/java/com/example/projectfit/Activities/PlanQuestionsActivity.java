package com.example.projectfit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectfit.Models.Question;
import com.example.projectfit.R;

public class PlanQuestionsActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button continueButton;
    private ImageButton backButton;  // New back button
    private LinearLayout answer1, answer2, answer3, answer4;
    private int currentQuestionIndex = 0;
    private final int progressStep = 14; // Rounded percentage step for progress bar (14.2857142857)
    private final int totalQuestions = 7;
    private Question[] questions;
    private int selectedAnswerIndex = 0; // To keep track of which answer is selected


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plan_questions);

        // Handling system window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Views
        progressBar = findViewById(R.id.progressBar);
        continueButton = findViewById(R.id.r9i96idg3y5l);
        backButton = findViewById(R.id.back_button);  // Initialize back button
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);

        // Setup Questions123
        setupQuestions();

        // Set first question
        loadQuestion(currentQuestionIndex);

        // Set click listeners for answers
        answer1.setOnClickListener(view -> onAnswerClicked(view, 1));
        answer2.setOnClickListener(view -> onAnswerClicked(view, 2));
        answer3.setOnClickListener(view -> onAnswerClicked(view, 3));
        answer4.setOnClickListener(view -> onAnswerClicked(view, 4));

        // Initially, the continue button is disabled
        continueButton.setEnabled(false);
    }

    private void setupQuestions() {
        questions = new Question[]{
                new Question("What is your goal?", "Build muscle", "Lose weight", "Improve endurance", "Increase flexibility", 0),
                new Question("What is your current fitness level?", "Beginner (0-6 months of regular exercise)", "Intermediate (6-24 months of regular exercise)", "Advanced (More than 24 months of regular exercise)", null, 0),
                new Question("How many days per week can you commit to working out?", "1-2 days", "3-4 days", "5-6 days", "7 days", 0),
                new Question("What type of workouts do you prefer?", "Weightlifting", "Bodyweight exercises", "Mobility", "Mixed (combination of all types)", 0),
                new Question("Do you have any specific muscle groups you want to focus on?", "Upper body (arms, chest, back)", "Lower body (legs, glutes)", "Core (abs, lower back)", "Full body", 0),
                new Question("Do you have any injuries or limitations that should be considered?", "No injuries or limitations", "Upper body injuries/limitations (shoulder, elbow, wrist)", "Lower body injuries/limitations (knee, ankle, hip)", "Back or neck injuries/limitations", 0),
                new Question("How much time do you want to spend on each workout session?", "15-30 minutes", "30-45 minutes", "45-60 minutes", "More than 60 minutes", 0)
        };
    }

    private void loadQuestion(int index) {
        if (index < totalQuestions) {
            Question currentQuestion = questions[index];

            // Set question text
            TextView questionText = findViewById(R.id.r5xj3hlxt1bn);
            questionText.setText(currentQuestion.getQuestionText());

            // Set answer texts and visibility
            setAnswerTextAndVisibility(answer1, currentQuestion.getAnswerNo1());
            setAnswerTextAndVisibility(answer2, currentQuestion.getAnswerNo2());
            setAnswerTextAndVisibility(answer3, currentQuestion.getAnswerNo3());
            setAnswerTextAndVisibility(answer4, currentQuestion.getAnswerNo4());

            // Reset selected answer and disable continue button for new question
            selectedAnswerIndex = currentQuestion.getChoseAnswer();  // Load previous answer
            updateSelectedAnswerUI();  // Update UI based on previously selected answer
            continueButton.setEnabled(selectedAnswerIndex != 0);  // Enable if an answer was already selected
            if(selectedAnswerIndex == 0)
            {
                continueButton.setText("");
                continueButton.setHint("Continue");
            }
        }
    }

    private void setAnswerTextAndVisibility(LinearLayout answerLayout, String answerText) {
        if (answerText != null) {
            answerLayout.setVisibility(View.VISIBLE);
            ((TextView) answerLayout.getChildAt(0)).setText(answerText);
        } else {
            answerLayout.setVisibility(View.INVISIBLE); // Hide unused answer layout
        }
    }

    public void onAnswerClicked(View view, int answerIndex) {
        // Reset all answer backgrounds to default
        resetAnswerBackgrounds();

        // Set clicked answer to orange background
        view.setBackgroundResource(R.drawable.cr36bff9a01);

        // Enable continue button
        continueButton.setEnabled(true);

        // Set the selected answer index
        selectedAnswerIndex = answerIndex;
        questions[currentQuestionIndex].setChoseAnswer(selectedAnswerIndex);
        continueButton.setText("Continue");
        if(currentQuestionIndex == totalQuestions - 1)
            continueButton.setText("Done");


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
        if (currentQuestionIndex < totalQuestions - 1) { // Not the last question
            // Save selected answer for the current question


            // Move to the next question
            currentQuestionIndex++;
            progressBar.setProgress(currentQuestionIndex * progressStep);



            loadQuestion(currentQuestionIndex);
            if (currentQuestionIndex == totalQuestions - 1) {
                if(questions[currentQuestionIndex].getChoseAnswer() == 0) {
                    continueButton.setText(""); // Change button text to "Done" on the last question
                    continueButton.setHint("Done");
                }
                else
                    continueButton.setText("Done");
            }

        } else {
            // Save selected answer for the last question
            questions[currentQuestionIndex].setChoseAnswer(selectedAnswerIndex);

            // Last question answered, go to MyPlanActivity
            Intent intent = new Intent(PlanQuestionsActivity.this, MyPlanActivity.class);
            startActivity(intent);
        }
    }

    public void onBackClicked(View view) {
        if (currentQuestionIndex > 0) {  // Check if not already on the first question
            currentQuestionIndex--;  // Go back to the previous question
            loadQuestion(currentQuestionIndex);  // Load previous question
            continueButton.setText("Continue");
            progressBar.setProgress(currentQuestionIndex * progressStep);  // Update progress bar
        }
        else
            finish();
    }
}
