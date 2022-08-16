package com.educational_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.educational_app.R;
import com.educational_app.Utility.NetworkChangeListener;
import com.hbb20.CountryCodePicker;

public class SignIn extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    EditText phone_no;
    Button button;
    CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        phone_no = findViewById(R.id.phone_no);
        button = findViewById(R.id.send_otp);
        ccp = findViewById(R.id.ccpId);
        ccp.registerCarrierNumberEditText(phone_no);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (phone_no.getText().toString().isEmpty()) {
                    phone_no.setError("Please type a Number");
                } else {


                    Intent intent = new Intent(getApplicationContext(), OtpVerification.class);
                    intent.putExtra("phoneNumber", ccp.getFullNumberWithPlus());
                    //  Log.i("Tag",ccp.getFullNumberWithPlus());
                    startActivity(intent);
                    finish();

                }

            }
        });
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
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