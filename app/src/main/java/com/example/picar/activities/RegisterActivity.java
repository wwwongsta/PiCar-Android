package com.example.picar.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.picar.R;
import com.example.picar.database.entity.User;
import com.example.picar.retrofit.http_request.User_http_request;
import com.example.picar.retrofit.model.type_message.Message;
import com.example.picar.retrofit.model.type_message.MessageUserEmailApproval;
import com.example.picar.retrofit.model.user_type.UserEmail;

import retrofit2.Call;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements User_http_request.UserHttpError
        , User_http_request.UserHttpResponse {
    EditText name,phone,email,password,family_name;
    String Name, Phone, Email, Password,FamilyName;
    Button signIn;
    CheckBox visiblePassword;
    private UserCreateTask mAuthTask = null;
    private UserGetListEmail mGetEmailsTask = null;
    String emailExits;


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



                    mAuthTask = new UserCreateTask();
                    mAuthTask.execute();

                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    Toast.makeText(RegisterActivity.this, "You are register",Toast.LENGTH_LONG);



                }
            }
        });
    }
    public class UserGetListEmail extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            User_http_request request = new User_http_request(RegisterActivity.this);
            UserEmail sendThisEmail = new UserEmail(Email);
            request.checkEmail(sendThisEmail);
            return true;
        }
    }
    @Override
    public void checkEmail(Call<MessageUserEmailApproval> call, Response<MessageUserEmailApproval> response) {
        MessageUserEmailApproval mess = response.body();
        Log.i("User",response.body().toString());
        Log.i("User",mess.getMessage());
        emailExits = mess.getMessage();
       // Log.i("User",mess.getEmailAlreadyExist().toString()); = A null ?
    }

    public class UserCreateTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            User_http_request request = new User_http_request(RegisterActivity.this);
            User user = new User(Email, Password, FamilyName, Name, Phone);
            request.createUser(user);
            return true;
        }
    }



    @Override
    public void errorEmail(Call<MessageUserEmailApproval> call, Throwable t) {
        Log.i("User",call.toString());
    }

    @Override
    public void createUser(Call<Message> call, Response<Message> response) {
        Message mess = response.body();
        Log.i("User",mess.getMessage());
    }

    private boolean confirmValues() {
        boolean result = true;
       // String Name, Phone, Email, Password,FamilyName;
        Name = name.getText().toString();
        Phone = phone.getText().toString();
        Email = email.getText().toString();
        Password = password.getText().toString();
        FamilyName = family_name.getText().toString();



        /*
        *
        * Vérification de l'existance du email dans la base de donnée
        *
        * */
        mGetEmailsTask = new UserGetListEmail();
        mGetEmailsTask.execute();

        String validName = "^[a-zA-Z0-9_]*$";
        String validPhone = "^[+]?[0-9]{10,13}$";
        String validEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String validPassword = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$";

        if(!FamilyName.matches(validName)){
            family_name.setError("Invalid Family Name");
            family_name.requestFocus();
            result = false;

        }

        if(!Name.matches(validName)){
            name.setError("Invalid Name");
            name.requestFocus();
            result = false;

        }
        if(!Phone.matches(validPhone)){
            phone.setError("Invalid Phone");
            phone.requestFocus();
            result = false;

        }
        if(!Email.matches(validEmail)){
            email.setError("Invalid Email");
            email.requestFocus();
            result = false;

        }
        if(!Password.matches(validPassword)){
            password.setError("Invalid Password, must contain at least 1 upper case letter, lower case and a digit");
            password.requestFocus();
            result = false;

        }
      //  if(emailExits.equalsIgnoreCase("Email already exist")){
//            email.setError("Invalid Email, this Email already exist");
//            email.requestFocus();
//            result = false;
           // Toast.makeText(this,"Invalid Email, this Email already exist",Toast.LENGTH_LONG).show();
       // }
        return result;
    }
}