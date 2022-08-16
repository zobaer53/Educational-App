package com.educational_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.educational_app.R;
import com.educational_app.Utility.NetworkChangeListener;
import com.educational_app.model.Question;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();



    TextView timerTextView,questionCounterTextView,questionTextView,
    option_1TextView,option_2TextView,option_3TextView,option_4textView;
    Button quitButton,nextButton;

    ArrayList<Question> questions;
    int topicPosition;
    Question question;
    CountDownTimer timer;

    int correctAnswers = 0,index=0;
   DatabaseReference db;
   String courseID,topicName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        questions = new ArrayList<>();
        courseID = getIntent().getStringExtra("courseID");
        topicName=getIntent().getStringExtra("topicName");

        topicPosition = getIntent().getIntExtra("coursePosition",0);
        Log.i("Tag","courseId from quiz Activity= "+courseID+"topic position quiz= "+topicPosition);
       db = FirebaseDatabase.getInstance().getReference();

        timerTextView= findViewById(R.id.timer);
        questionCounterTextView = findViewById(R.id.questionCounter);
        questionTextView = findViewById(R.id.question);
        option_1TextView = findViewById(R.id.option_1);
        option_2TextView = findViewById(R.id.option_2);
        option_3TextView = findViewById(R.id.option_3);
        option_4textView = findViewById(R.id.option_4);
        quitButton = findViewById(R.id.quizBtn);
        nextButton = findViewById(R.id.nextBtn);


        db.child("test").child(courseID).child(String.valueOf(topicPosition)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot mSnapshot: snapshot.getChildren()){

                    if(mSnapshot.exists()){

                        question = mSnapshot.getValue(Question.class);
                        questions.add(question);
                        Log.i("Tag","Questions "+question.getQuestion());

                    }
                }
                setNextQuestion();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        resetTimer();

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(QuizActivity.this,ChapterActivity.class);
                intent.putExtra("coursePosition",topicPosition);
                intent.putExtra("courseID",courseID);
                intent.putExtra("topicName",topicName);
                startActivity(intent);
                finish();
            }
        });



    }

    void resetTimer() {

        timer = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                nextButton.setClickable(false);

                timerTextView.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                nextButton();
                option_1TextView.setClickable(false);
                option_2TextView.setClickable(false);
                option_3TextView.setClickable(false);
                option_4textView.setClickable(false);
                showAnswer();
               // textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));

            }
        };
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void showAnswer() {
        if(question.getAnswer().equals(option_1TextView.getText().toString()))
            option_1TextView.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(option_2TextView.getText().toString()))
            option_2TextView.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(option_3TextView.getText().toString()))
            option_3TextView.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(option_4textView.getText().toString()))
            option_4textView.setBackground(getResources().getDrawable(R.drawable.option_right));
    }

    @SuppressLint("DefaultLocale")
    void setNextQuestion() {
        if(timer != null)
            timer.cancel();

        timer.start();
        if(index <= questions.size()-1) {
            questionCounterTextView.setText(String.format("%d/%d", (index+1), questions.size()));
            question = questions.get(index);
            Log.i("Tag","question1 = "+question.getQuestion()+question.getAnswer());
            questionTextView.setText(question.getQuestion());
            option_1TextView.setText(question.getOption1());
           option_2TextView.setText(question.getOption2());
            option_3TextView.setText(question.getOption3());
            option_4textView.setText(question.getOption4());
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void checkAnswer(TextView textView) {
        String selectedAnswer = textView.getText().toString();
        Log.i("Tag","selected answer= "+selectedAnswer+"answer= "+question.getAnswer());
        if(selectedAnswer.equals(question.getAnswer())) {

            correctAnswers++;
            textView.setBackground(getResources().getDrawable(R.drawable.option_right));
            option_1TextView.setClickable(false);
            option_2TextView.setClickable(false);
            option_3TextView.setClickable(false);
            option_4textView.setClickable(false);
            nextButton.setClickable(true);

            nextButton();

        } else {
            showAnswer();
            textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
            nextButton.setClickable(true);
            nextButton();
            option_1TextView.setClickable(false);
            option_2TextView.setClickable(false);
            option_3TextView.setClickable(false);
            option_4textView.setClickable(false);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void reset() {
        option_1TextView.setClickable(true);
        option_2TextView.setClickable(true);
        option_3TextView.setClickable(true);
        option_4textView.setClickable(true);
        option_1TextView.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        option_2TextView.setBackground(getResources().getDrawable(R.drawable.option_unselected));
       option_3TextView.setBackground(getResources().getDrawable(R.drawable.option_unselected));
       option_4textView.setBackground(getResources().getDrawable(R.drawable.option_unselected));
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.option_1:
            case R.id.option_2:
            case R.id.option_3:
            case R.id.option_4:
                if(timer!=null)
                    timer.cancel();
                TextView selected = (TextView) view;
                checkAnswer(selected);

                break;
        }
    }

public void nextButton(){

    nextButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            reset();
            if(index < questions.size()-1) {
                index++;
                setNextQuestion();
                Log.i("Tag","index= "+index);
            } else {
                Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                intent.putExtra("correct", correctAnswers);
                intent.putExtra("total", questions.size());
                intent.putExtra("coursePosition",topicPosition);
                intent.putExtra("courseID",courseID);
                intent.putExtra("topicName",topicName);


                startActivity(intent);
                finish();
                //Toast.makeText(this, "Quiz Finished.", Toast.LENGTH_SHORT).show();
            }

        }
    });



}

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}