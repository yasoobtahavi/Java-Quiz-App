package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private String[] questions;
    private String[][] options;
    private int[] correctAnswers = {0, 0, 0};  // Indices of correct answers (adjust accordingly)
    private int score = 0;
    private int currentQuestionIndex = 0;
    private boolean isAnswerChecked = false;
////////
private boolean[] answerChecked;
    private TextView questionTextView, scoreTextView, timerTextView;
    private RadioGroup optionsRadioGroup;
    private RadioButton option1, option2, option3, option4;
    private Button nextButton, prevButton, endExamButton, showAnswerButton;

    private CountDownTimer timer;
    private long timeLeftInMillis = 600000; // 10 minutes (600,000 milliseconds)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load questions and options from strings.xml
        questions = getResources().getStringArray(R.array.questions);

        options = new String[questions.length][4]; // Assuming each question has 4 options
        options[0] = getResources().getStringArray(R.array.options_0);
        options[1] = getResources().getStringArray(R.array.options_1);
        options[2] = getResources().getStringArray(R.array.options_2);
        // Add more options arrays for each question in strings.xml
/////////////////
        answerChecked = new boolean[questions.length];
        for (int i = 0; i < answerChecked.length; i++) {
            answerChecked[i] = false;
        }

        /////////////////////////
        // Initialize UI components
        questionTextView = findViewById(R.id.question_text);
        scoreTextView = findViewById(R.id.score_text);
        timerTextView = findViewById(R.id.timer_text);

        optionsRadioGroup = findViewById(R.id.options_radio_group);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        endExamButton = findViewById(R.id.end_exam_button);
        showAnswerButton = findViewById(R.id.show_answer_button);

        // Set up timer
        startTimer();

        // Display the first question
        displayQuestion();

        // Set up button listeners
        nextButton.setOnClickListener(v -> {
            if (currentQuestionIndex < questions.length - 1) {
                // Clear previous selection before displaying next question
                optionsRadioGroup.clearCheck();
                currentQuestionIndex++;
                displayQuestion();

                // Reset checked status
                isAnswerChecked = false;
            } else {
                endQuiz();
            }

        });
        prevButton.setOnClickListener(v -> {
            if (currentQuestionIndex > 0) {
                // Clear previous selection before displaying previous question
                optionsRadioGroup.clearCheck();
                currentQuestionIndex--;
                displayQuestion();

                isAnswerChecked = false;
            }
        });

        endExamButton.setOnClickListener(v -> endQuiz());

        showAnswerButton.setOnClickListener(v -> {
            // Functionality remains the same (penalize for showing answer)
            if(!(answerChecked[currentQuestionIndex])) score -= 1;
            isAnswerChecked = true;
            answerChecked[currentQuestionIndex] = true;
            Toast.makeText(MainActivity.this, "Correct answer: " + options[currentQuestionIndex][correctAnswers[currentQuestionIndex]], Toast.LENGTH_SHORT).show();
            scoreTextView.setText("Score: " + score);
        });

        // Check the answer and lock options when the user selects an option
        optionsRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId != -1) {
                if (!isAnswerChecked && !(answerChecked[currentQuestionIndex])) {
                    checkAnswer();
                    isAnswerChecked = true; // Prevent multiple checks for the same question
                    answerChecked[currentQuestionIndex] = true;

                    // Disable all RadioButtons to lock the answer
                    for (int i = 0; i < optionsRadioGroup.getChildCount(); i++) {
                        optionsRadioGroup.getChildAt(i).setEnabled(false);
                    }
                }
            }
            if(answerChecked[currentQuestionIndex]) {
                // Disable all RadioButtons to lock the answer
                for (int i = 0; i < optionsRadioGroup.getChildCount(); i++) {
                    optionsRadioGroup.getChildAt(i).setEnabled(false);
                }
            }
        });
    }

    private void displayQuestion() {
        // Reset checked status
        //isAnswerChecked = false;

        // Display the current question and options
        questionTextView.setText(questions[currentQuestionIndex]);
        option1.setText(options[currentQuestionIndex][0]);
        option2.setText(options[currentQuestionIndex][1]);
        option3.setText(options[currentQuestionIndex][2]);
        option4.setText(options[currentQuestionIndex][3]);

        // Clear previous selection and enable all options
        optionsRadioGroup.clearCheck();
        for (int i = 0; i < optionsRadioGroup.getChildCount(); i++) {
            optionsRadioGroup.getChildAt(i).setEnabled(true);
        }
    }

    private void checkAnswer() {
        int selectedOptionId = optionsRadioGroup.getCheckedRadioButtonId();
        if (selectedOptionId != -1) {
            int selectedAnswerIndex = optionsRadioGroup.indexOfChild(findViewById(selectedOptionId));
            if (selectedAnswerIndex == correctAnswers[currentQuestionIndex]) {
                score += 5; // Add points for correct answer
            } else {
                score -= 1; // Deduct points for incorrect answer
            }
            scoreTextView.setText("Score: " + score);
        }
    }

    private void startTimer() {
        timer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                int minutes = (int) (timeLeftInMillis / 1000) / 60;
                int seconds = (int) (timeLeftInMillis / 1000) % 60;
                String timeFormatted = String.format("%02d:%02d", minutes, seconds);
                timerTextView.setText("Time Left: " + timeFormatted);
            }

            @Override
            public void onFinish() {
                endQuiz();
            }
        }.start();
    }

    private void endQuiz() {
        timer.cancel();
        Intent resultIntent = new Intent(MainActivity.this, resultActivity.class);
        resultIntent.putExtra("score", score);
        resultIntent.putExtra("totalQuestions", questions.length);
        startActivity(resultIntent);
        finish();
    }
}