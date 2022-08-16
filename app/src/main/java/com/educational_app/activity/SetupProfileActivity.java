package com.educational_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.educational_app.R;
import com.educational_app.Utility.NetworkChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SetupProfileActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    private FirebaseAuth mAuth;
    private EditText nameEditText,emailEditText,addressEditText;
    String PhoneNumber;
    DatabaseReference dbr;
    HashMap<String, Object> userMap;
    Spinner spin;
    String course ;
    String courseID;
     List<String> courses;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

         PhoneNumber= getIntent().getStringExtra("phoneNumber");
        Log.i("Tag","Setup phone = "+PhoneNumber);

         dbr = FirebaseDatabase.getInstance().getReference().child("Users").child(PhoneNumber);
         userMap = new HashMap<>();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        mAuth = FirebaseAuth.getInstance();
        nameEditText = findViewById(R.id.nameBox);
        emailEditText = findViewById(R.id.emailBox);
        addressEditText = findViewById(R.id.addressBox);
        Button setupProfileButton = findViewById(R.id.setupProfile);

         spin = findViewById(R.id.coursesspinner);

         loadSpinnerFirebase();

       spin.setOnItemSelectedListener(this);
        setupProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String UserName = nameEditText.getText().toString().toLowerCase(Locale.ROOT).trim();
                String UserEmail = emailEditText.getText().toString().toLowerCase(Locale.ROOT).trim();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if(UserName.isEmpty()  ) {
                    nameEditText.setError("Please type a name");
                }
                else if(UserEmail.isEmpty() || !UserEmail.matches(emailPattern)) {
                    emailEditText.setError("invalid email");
                }
                else {
                updateFirebase();}
            }
        });
    }
    private void loadSpinnerFirebase() {

        mDatabaseReference.child("courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              courses = new ArrayList<>();
                for (DataSnapshot contentSnapShot : dataSnapshot.getChildren()) {
                    courseID= contentSnapShot.getKey().toString();
                   String courseName = (String) contentSnapShot.child("courseName").getValue();
                   courses.add(courseName);
                   // Log.i("Tag","course= "+courses+" courseID= "+courseID);
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter( getApplicationContext(), android.R.layout.simple_spinner_item,courses);

                arrayAdapter.setDropDownViewResource(
                        android.R.layout
                                .simple_spinner_dropdown_item);
                spin.setAdapter(arrayAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void updateFirebase() {

        String UserName = nameEditText.getText().toString().trim();
        String UserEmail = emailEditText.getText().toString().trim();
        String UserAddress = addressEditText.getText().toString().trim();
        if(addressEditText.getText().toString().toLowerCase(Locale.ROOT).trim().isEmpty()){
            UserAddress = "Address";
        }
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();

        userMap.put("uid", uid);
        userMap.put("name", UserName);
        userMap.put("email", UserEmail);
        userMap.put("address", UserAddress);
        userMap.put("course", course);
        userMap.put("courseID", courseID);
        userMap.put("DOB", "Date of birth");
        userMap.put("gender", "Gender");
        userMap.put("uimage", "https://cdn-icons-png.flaticon.com/512/1946/1946429.png");


        Log.i("Tag","courseFinal= "+course);

            dbr.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                        Intent openHome = new Intent(SetupProfileActivity.this,ActivityHome.class);
                        openHome.putExtra("courseID",courseID);
                        openHome.putExtra("phone",PhoneNumber);
                        startActivity(openHome);
                        finish();

                    }
                    else{
                        Toast.makeText(SetupProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        mDatabaseReference.child("courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot contentSnapShot : dataSnapshot.getChildren()) {
                    if(courses.get(i).equals(contentSnapShot.child("courseName").getValue().toString())){
                        courseID= contentSnapShot.getKey().toString();
                        Log.i("Tag"," Selected courseID= "+courseID);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        course = courses.get(i);
        Log.i("Tag","courseSelected= "+course);
        Toast.makeText(getApplicationContext(),
                courses.get(i),
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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