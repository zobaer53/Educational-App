package com.educational_app.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.educational_app.R;
import com.educational_app.Utility.NetworkChangeListener;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Profile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    ImageView profile;
    TextView nameTextView, nameTopTextView, phoneTextView, emailTextView, addressTextView, dobTextView, genderTextView;
    LinearLayout editName, editTextVisibility, editDOB, editGender;
    CardView cardView;
    EditText editText, DOBEditText;
    Button updateButton, logoutButton, cancelButton;


    DatabaseReference dbreference;
    StorageReference storageReference;

    Uri filepath;
    Bitmap bitmap;
    String phone = "", updateText, homeID;

    Spinner spin;
    String course;
    String courseID;
    List<String> courses;
    Calendar myCalendar;

    private RadioGroup radioGroup;
    private RadioButton radioButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back_icon);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnBackPress();
            }
        });


        profile = findViewById(R.id.profileImageView);
        nameTextView = findViewById(R.id.name);
        nameTopTextView = findViewById(R.id.name_top);
        phoneTextView = findViewById(R.id.phone);
        emailTextView = findViewById(R.id.email);
        addressTextView = findViewById(R.id.address);
        dobTextView = findViewById(R.id.dOB);
        genderTextView = findViewById(R.id.gender);
        editName = findViewById(R.id.editName);
        cardView = findViewById(R.id.cardView);
        editText = findViewById(R.id.editText);
        DOBEditText = findViewById(R.id.DOBEditText);
        editTextVisibility = findViewById(R.id.editTextVisibility);
        updateButton = findViewById(R.id.updateButton);
        cancelButton = findViewById(R.id.cancelButton);
        editDOB = findViewById(R.id.editDOB);
        dobTextView = findViewById(R.id.dOB);
        editGender = findViewById(R.id.editGender);
        logoutButton = findViewById(R.id.logoutButton);
        radioGroup = (RadioGroup) findViewById(R.id.radio);


        editTextVisibility.setVisibility(View.INVISIBLE);
        radioGroup.setVisibility(View.GONE);
        DOBEditText.setVisibility(View.GONE);
        cardView.setVisibility(View.VISIBLE);


        homeID = getIntent().getStringExtra("homeID");
        phone = getIntent().getStringExtra("phone");
        Log.i("Tag", "phone from profile= " + phone);


        dbreference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        spin = findViewById(R.id.coursesspinner);
        courses = new ArrayList<>();
        courses.add("Select course");

        loadSpinnerFirebase();

        spin.setOnItemSelectedListener(this);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardView.setVisibility(View.VISIBLE);
                editTextVisibility.setVisibility(View.GONE);
                radioGroup.setVisibility(View.GONE);
                DOBEditText.setVisibility(View.GONE);
                editText.setFocusable(true);
                editText.setVisibility(View.VISIBLE);
                updateText = editText.getText().toString();
                editText.setText("");

            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Profile.this, OnBoardingScreenActivity.class));
                finish();
            }
        });


        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardView.setVisibility(View.INVISIBLE);
                editTextVisibility.setVisibility(View.VISIBLE);
                editText.isClickable();
                editText.setHint("Update Name");

                updateProfileName();

            }
        });
        editDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardView.setVisibility(View.INVISIBLE);
                editTextVisibility.setVisibility(View.VISIBLE);
                editText.setVisibility(View.GONE);
                DOBEditText.setVisibility(View.VISIBLE);


                DOBEditText.setHint("Update Date OF Birth");
                updateProfileDOB();

                // Log.i("Tag","new name= "+editText.getText().toString());

            }
        });
        editGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardView.setVisibility(View.INVISIBLE);
                editTextVisibility.setVisibility(View.VISIBLE);
                editText.setVisibility(View.INVISIBLE);
                editText.isClickable();
                editText.setHint("Update Gender");
                addListenerOnButton();


                // Log.i("Tag","new name= "+editText.getText().toString());

            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Please Select File"), 101);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }

                        }).check();

            }

        });
    }


    public void addListenerOnButton() {


        radioGroup.setVisibility(View.VISIBLE);


        updateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioButton = (RadioButton) findViewById(selectedId);

                cardView.setVisibility(View.VISIBLE);
                editTextVisibility.setVisibility(View.GONE);
                editText.setVisibility(View.VISIBLE);
                radioGroup.setVisibility(View.GONE);
                updateText = (String) radioButton.getText();
                Log.i("Tag", "new name= " + updateText);

                final Map<String, Object> map = new HashMap<>();
                map.put("gender", updateText);

                dbreference.child("Users").child(phone).updateChildren(map);

                editText.setText("");

                Toast.makeText(Profile.this,
                        radioButton.getText(), Toast.LENGTH_SHORT).show();


            }

        });

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        DOBEditText.setText(sdf.format(myCalendar.getTime()));
    }

    private void handleOnBackPress() {
        //super.onBackPressed();
        Intent intent = new Intent(Profile.this, ActivityHome.class);
        intent.putExtra("phone", phone);
        intent.putExtra("courseID", courseID);
        startActivity(intent);
        finish();
    }

    private void loadSpinnerFirebase() {

        dbreference.child("courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot contentSnapShot : dataSnapshot.getChildren()) {
                    if (contentSnapShot.getKey().toString().equals(homeID)) {
                        courseID = contentSnapShot.getKey().toString();


                    }

                    String courseName = (String) contentSnapShot.child("courseName").getValue();
                    courses.add(courseName);
                    // Log.i("Tag","course= "+courses+" courseID firebase= "+courseID);
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, courses);

                arrayAdapter.setDropDownViewResource(
                        android.R.layout
                                .simple_spinner_dropdown_item);
                spin.setSelection(2);
                spin.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private void updateProfileDOB() {


        myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        DOBEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Profile.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cardView.setVisibility(View.VISIBLE);
                editTextVisibility.setVisibility(View.GONE);
                DOBEditText.setVisibility(View.GONE);
                editText.setVisibility(View.VISIBLE);
                updateText = DOBEditText.getText().toString();
                Log.i("Tag", "new name= " + updateText);

                final Map<String, Object> map = new HashMap<>();
                map.put("DOB", updateText);

                dbreference.child("Users").child(phone).updateChildren(map);
                DOBEditText.setText("");


            }
        });

    }

    private void updateProfileName() {


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cardView.setVisibility(View.VISIBLE);
                editTextVisibility.setVisibility(View.GONE);
                updateText = editText.getText().toString();
                Log.i("Tag", "new name= " + updateText);

                final Map<String, Object> map = new HashMap<>();
                map.put("name", updateText);

                dbreference.child("Users").child(phone).updateChildren(map);
                editText.setText("");

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        phone = getIntent().getStringExtra("phone");
        dbreference.child("Users").child(phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    nameTextView.setText(snapshot.child("name").getValue().toString());
                    nameTopTextView.setText(snapshot.child("name").getValue().toString());
                    emailTextView.setText(snapshot.child("email").getValue().toString());
                    phoneTextView.setText(phone);
                    dobTextView.setText(snapshot.child("DOB").getValue().toString());
                    genderTextView.setText(snapshot.child("gender").getValue().toString());
                    if (snapshot.child("courseID").getValue().toString().equals("01")) {

                        spin.setSelection(1);
                    } else if (snapshot.child("courseID").getValue().toString().equals("02")) {

                        spin.setSelection(2);
                    } else if (snapshot.child("courseID").getValue().toString().equals("03")) {

                        spin.setSelection(3);
                    }

                    addressTextView.setText(snapshot.child("address").getValue().toString());
                    if (snapshot.child("uimage").exists()) {

                        Glide.with(getApplicationContext()).load(snapshot.child("uimage").getValue().toString()).into(profile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updatetofirebase() {


        final StorageReference uploader = storageReference.child("profileimages/" + "img" + System.currentTimeMillis());
        uploader.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final Map<String, Object> map = new HashMap<>();
                                map.put("uimage", uri.toString());
                                //  map.put("uname",uname.getText().toString());

                                dbreference.child("Users").child(phone).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists())
                                            dbreference.child("Users").child(phone).updateChildren(map);
                                        Log.i("Tag", "updated image");

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });


                                Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                profile.setImageBitmap(bitmap);
                updatetofirebase();
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Profile.this, ActivityHome.class);
        intent.putExtra("phone", phone);
        intent.putExtra("courseID", courseID);
        startActivity(intent);
        finish();

        //super.onBackPressed();


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        dbreference.child("courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot contentSnapShot : dataSnapshot.getChildren()) {
                    if (courses.get(i).equals(contentSnapShot.child("courseName").getValue().toString())) {
                        courseID = contentSnapShot.getKey().toString();

                        Log.i("Tag", " Selected courseID= " + courseID);
                        final Map<String, Object> map = new HashMap<>();
                        map.put("courseID", courseID);
                        map.put("course", courses.get(i));
                        //  map.put("course",contentSnapShot.child("course").getValue().toString());

                        dbreference.child("Users").child(phone).updateChildren(map);

                        break;

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        course = courses.get(i);

        Log.i("Tag", "courseSelected= " + course);
        Toast.makeText(getApplicationContext(),
                courses.get(i),
                Toast.LENGTH_SHORT)
                .show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}