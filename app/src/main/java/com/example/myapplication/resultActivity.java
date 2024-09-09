package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class resultActivity extends AppCompatActivity {

    private TextView resultTextView;
    private int score, totalQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultTextView = findViewById(R.id.result_text);
        Button b1 = findViewById(R.id.restart_button);

        // Get the score and totalQuestions from the intent that started this activity
        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        totalQuestions = intent.getIntExtra("totalQuestions", 0);

        // Display the final score
        String resultText = "You scored " + score + " out of " + (totalQuestions * 5) +
                "\nPercentage: " + String.format("%.2f", (double) score / (double) (totalQuestions * 5) * 100) + "%";
        // Assuming each question has 5 points max
        resultTextView.setText(resultText);

        // Set up the Restart Quiz button
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new intent to start MainActivity again
                Intent restartIntent = new Intent(resultActivity.this, MainActivity.class);

                // Clear all activities on the stack (to fully reset the app) and start a new task
                restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                // Start MainActivity
                startActivity(restartIntent);

                // Finish the current activity (resultActivity) so that it's removed from the stack
                finish();
            }
        });
    }
}
