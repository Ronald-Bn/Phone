package com.example.phone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private TextView usernumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button button = findViewById(R.id.logoutBtn);
        usernumber = findViewById(R.id.phoneTv);


        button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    });
}

    private void checkUserStatus() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            String UserId = firebaseUser.getUid();
            usernumber.setText(UserId);
        }else{
            finish();
        }
    }
}