package com.example.phone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private EditText etfullname , etphonenum, etemail, etpassword, etconfirmpassword, etaddress, etzipcode;
    private Button btn_register;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etfullname = findViewById(R.id.EditTextFullName);
        etphonenum = findViewById(R.id.EditTextPhoneNum);
        etemail = findViewById(R.id.EditTextRegisterEmail);
        etpassword = findViewById(R.id.EditTextPassword);
        etconfirmpassword = findViewById(R.id.EditTextConfirmPassword);
        etaddress = findViewById(R.id.EditTextAddress);
        etzipcode = findViewById(R.id.EditTextZipCode);
        btn_register = findViewById(R.id.Btn_Register);
        checkBox = findViewById(R.id.checkBox);



       btn_register.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               /*if (TextUtils.isEmpty(etfullname.getText())) {
                   Toast.makeText(RegisterActivity.this, "Please Enter Your Full Name", Toast.LENGTH_SHORT).show();
               } else if (TextUtils.isEmpty(etphonenum.getText()) || etphonenum.getText().length() != 11) {
                   Toast.makeText(RegisterActivity.this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();
               } else if (TextUtils.isEmpty(etemail.getText())) {
                   Toast.makeText(RegisterActivity.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
               } else if (TextUtils.isEmpty(etpassword.getText())) {
                   Toast.makeText(RegisterActivity.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
               } else if (TextUtils.isEmpty(etconfirmpassword.getText())) {
                   Toast.makeText(RegisterActivity.this, "Please Enter Your Confirm Password", Toast.LENGTH_SHORT).show();
               } else if (TextUtils.isEmpty(etaddress.getText())) {
                   Toast.makeText(RegisterActivity.this, "Please Enter Your Address", Toast.LENGTH_SHORT).show();
               } else if (TextUtils.isEmpty(etzipcode.getText())) {
                   Toast.makeText(RegisterActivity.this, "Please Enter Your Zip Code", Toast.LENGTH_SHORT).show();
               }else if (!etpassword.getText().toString().trim().equals(etconfirmpassword.getText().toString().trim())) {
                   Toast.makeText(RegisterActivity.this, "The Password is Not Match", Toast.LENGTH_SHORT).show();
               }else {
               }*/
               if(!validateEmail()){
                   return;
               }else {
                   Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                   intent.putExtra("fullname", etfullname.getText().toString());
                   intent.putExtra("phonenum", etphonenum.getText().toString());
                   intent.putExtra("email", etemail.getText().toString());
                   intent.putExtra("password", etpassword.getText().toString());
                   intent.putExtra("address", etaddress.getText().toString());
                   intent.putExtra("zipcode", etzipcode.getText().toString());
                   startActivity(intent);
               }
           }
       });
    }

    private boolean validateFullName(){
        String fullNameInput = etfullname.getText().toString();
        if (fullNameInput.isEmpty()) {
            etemail.setError("Field can't be empty");
            return false;
        }
        return true;
    }
    private boolean validateEmail(){
        String emailInput = etemail.getText().toString().trim();

        if (emailInput.isEmpty()) {
            etemail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            etemail.setError("Please enter a valid email address");
            return false;
        } else {
            etemail.setError(null);
            return true;
        }
    }

}