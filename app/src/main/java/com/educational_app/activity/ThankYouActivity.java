package com.educational_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.educational_app.R;

public class ThankYouActivity extends AppCompatActivity {
    Button browseButton,myCourseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        browseButton = findViewById(R.id.browseButton);
        myCourseButton = findViewById(R.id.myCourseButton);

        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThankYouActivity.this, AllCourseActivity.class);
                startActivity(intent);
                finish();
            }
        });

        myCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThankYouActivity.this, MyCoursesActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}