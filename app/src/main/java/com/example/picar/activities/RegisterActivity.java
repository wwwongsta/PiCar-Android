package com.example.picar.activities;

import android.app.LoaderManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.picar.R;
import com.example.picar.database.entity.User;
import com.example.picar.retrofit.http_request.User_http_request;
import com.example.picar.retrofit.model.type_message.Message;
import com.example.picar.retrofit.model.user_type.UserInfo;

import retrofit2.Call;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements User_http_request.UserHttpError
        , User_http_request.UserHttpResponse {
    EditText name,phone,email,password,family_name;
    Button signIn;
    CheckBox visiblePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = (EditText) findViewById(R.id.Name);
        phone = (EditText) findViewById(R.id.Phone);
        email = (EditText) findViewById(R.id.Email);
        password = (EditText) findViewById(R.id.Password);
        family_name = findViewById(R.id.FamilyName);

        visiblePassword = (CheckBox)findViewById(R.id.visiblePassword);
        visiblePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visiblePassword.isChecked()){
                    password.setTransformationMethod(null);
                }else{
                    password.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        signIn = (Button) findViewById(R.id.SignIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirmValues()){


                }
            }
        });
    }


    private boolean confirmValues() {
        boolean result = true;
        String Name, Phone, Email, Password,FamilyName;
        Name = name.getText().toString();
        Phone = phone.getText().toString();
        Email = email.getText().toString();
        Password = password.getText().toString();
        FamilyName = family_name.getText().toString();
7
        String validName = "[A-Z][a-zA-Z]*";
        String validPhone = "^[+]?[0-9]{10,13}$";
        String validEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String validPassword = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$";

        if(!Name.matches(validName)){
            Toast.makeText(this,"Invalid Name",Toast.LENGTH_LONG).show();
            result = false;
        }
        if(!Phone.matches(validPhone)){
            Toast.makeText(this,"Invalid Phone number",Toast.LENGTH_LONG).show();
            result = false;
        }
        if(!Email.matches(validEmail)){
            Toast.makeText(this,"Invalid Email",Toast.LENGTH_LONG).show();
            result = false;
        }
        if(!Password.matches(validPassword)){
            Toast.makeText(this,"Invalid Password, must contain at least 1 upper case letter, lower case and a digit",Toast.LENGTH_LONG).show();
            result = false;
        }
        return result;
    }
}