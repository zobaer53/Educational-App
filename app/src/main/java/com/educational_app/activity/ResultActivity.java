package com.educational_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.educational_app.R;

public class ResultActivity extends AppCompatActivity {

    TextView scoreTextView;
    Button restartButton, shareButton;
    String courseID, topicName;
    int topicPosition;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_result);
        scoreTextView = findViewById(R.id.score);
        restartButton = findViewById(R.id.restartBtn);
        shareButton = findViewById(R.id.shareBtn);
        courseID = getIntent().getStringExtra("courseID");
        topicName = getIntent().getStringExtra("topicName");
        topicPosition = getIntent().getIntExtra("coursePosition", 0);


        int correctAnswers = getIntent().getIntExtra("correct", 0);
        int totalQuestions = getIntent().getIntExtra("total", 0);

        scoreTextView.setText(String.format("%d/%d", correctAnswers, totalQuestions));

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, ChapterActivity.class);
                intent.putExtra("coursePosition", topicPosition);
                intent.putExtra("courseID", courseID);
                intent.putExtra("topicName", topicName);
                startActivity(intent);
                finish();
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = topicName + " Quiz score  " + correctAnswers;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, correctAnswers);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });


    }


}