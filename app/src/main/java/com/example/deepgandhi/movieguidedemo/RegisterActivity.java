package com.example.deepgandhi.movieguidedemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

public class RegisterActivity extends AppCompatActivity {
    EditText reg_email,reg_username,reg_password,reg_confirmpassword;
    Button btn_signup;
    SignInButton btn_gsignup;
    TextView tv_reg_pwdcheck,tv_reg_usernamecherck,tv_reg_pwdchk,tv_reg_emailcheck;
    private static int REQ_CODE = 9001;
    GoogleSignInClient mGoogleSignInClient;
    Button join_club,know_more;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        reg_confirmpassword=findViewById(R.id.reg_confirmpassword);
        reg_email=findViewById(R.id.reg_email);
        reg_password=findViewById(R.id.reg_password);
        reg_username=findViewById(R.id.reg_username);
        btn_signup=findViewById(R.id.reg_signup);
        btn_gsignup=findViewById(R.id.reg_googlesignup);
        tv_reg_pwdcheck=findViewById(R.id.reg_pwdcheck);
        tv_reg_usernamecherck=findViewById(R.id.reg_usernamecheck);
        tv_reg_pwdchk=findViewById(R.id.reg_pwdchk);
        tv_reg_emailcheck=findViewById(R.id.reg_emailcheck);
        know_more=findViewById(R.id.register_leran_more);
        join_club=findViewById(R.id.register_join_club);


        know_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://github.com/deepgandhi00/MovieGuideDemo";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        join_club.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://github.com/deepgandhi00/MovieGuideDemo";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        for (int i = 0; i < btn_gsignup.getChildCount(); i++) {
            View v = btn_gsignup.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText("SIGN UP");
                return;
            }
        }


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        btn_gsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, REQ_CODE);
            }
        });

        reg_confirmpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Toast.makeText(RegisterActivity.this, charSequence, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Toast.makeText(RegisterActivity.this, editable.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ_CODE){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String email=account.getEmail();
                String password="default";
                String username=account.getDisplayName();
                Signup signup=new Signup();
                signup.execute(email,username,password);
            }
            catch (Exception e){

            }
        }
    }

    public void signupclick(View view) {
        if(validateInputs()){
            Signup signup=new Signup();
            signup.execute(reg_email.getText().toString().trim(),reg_username.getText().toString().trim(),reg_password.getText().toString().trim());
        }
    }

    private boolean validateInputs() {
        tv_reg_emailcheck.setVisibility(View.GONE);
        tv_reg_usernamecherck.setVisibility(View.GONE);
        tv_reg_pwdcheck.setVisibility(View.GONE);
        if(validatEmail()){
            if(validateUser()){
                //tv_reg_usernamecherck.setVisibility(View.GONE);
                if(validatePassword()){
                    //tv_reg_pwdcheck.setVisibility(View.GONE);
                    return true;
                }
                else {
                    tv_reg_pwdcheck.setText("Password and confirm passwod do not match");
                    tv_reg_pwdcheck.setVisibility(View.VISIBLE);
                    return false;
                }
            }
            else{
                tv_reg_usernamecherck.setText("Username cannot be empty");
                tv_reg_usernamecherck.setVisibility(View.VISIBLE);
                return false;
            }
        }
        else{
            tv_reg_emailcheck.setText("Email doesn't match the appropriate email format");
            tv_reg_emailcheck.setVisibility(View.VISIBLE);
            return false;
        }
    }
    private boolean validatEmail(){
        String email=reg_email.getText().toString().trim();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
    private boolean validateUser(){
        return (!TextUtils.isEmpty(reg_username.getText().toString().trim()));
    }
    private boolean validatePassword(){
        if(reg_password.getText().toString().trim().equals(reg_confirmpassword.getText().toString().trim())){
            return true;
        }
        else{
            return false;
        }
    }

    class Signup extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder response=new StringBuilder();
            try{
                URL url=new URL(getResources().getString(R.string.register_url)+strings[0]+"&username="+strings[1]+"&password="+strings[2]);
                URLConnection ucon=url.openConnection();
                BufferedInputStream bis=new BufferedInputStream(ucon.getInputStream());
                int data=0;
                while((data=bis.read())!=-1){
                    response.append((char)data);
                }

                bis.close();
            }
            catch (Exception e){}
            return response.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                if((jsonObject.getString("email")!=null || jsonObject.getString("email")!="")
                        && jsonObject.getString("username")!=null || jsonObject.getString("username")!=""){
                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            catch (Exception e){}
        }
    }

}
