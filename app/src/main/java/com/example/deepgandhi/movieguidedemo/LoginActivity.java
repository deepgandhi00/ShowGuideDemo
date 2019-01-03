package com.example.deepgandhi.movieguidedemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class LoginActivity extends AppCompatActivity{
    SignInButton signIn;
    private static int REQ_CODE = 9000;
    EditText ed_pwd,ed_email;
    Button btn_signin;
    TextView txt_reg;
    GoogleSignInClient mGoogleSignInClient;
    Button join_club,know_more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signIn= findViewById(R.id.signinGoogleSignIn);
        ed_pwd=findViewById(R.id.editTextPassword);
        ed_email=findViewById(R.id.editTextEmail);
        btn_signin=findViewById(R.id.signinBtnSignIn);
        txt_reg=findViewById(R.id.signinNewRegister);
        join_club=findViewById(R.id.login_join_club);
        know_more=findViewById(R.id.login_learn_more);


        join_club.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://github.com/deepgandhi00/MovieGuideDemo";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        know_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://github.com/deepgandhi00/MovieGuideDemo";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, REQ_CODE);
            }
        });
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("clicked","yes");
                //SignIn signIn=new SignIn(true);
                //signIn.execute(ed_email.getText().toString().trim(),ed_pwd.getText().toString().trim());
            }
        });
        txt_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
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
                SignIn signIn=new SignIn(false);
                signIn.execute(email);
            }
            catch (Exception e){

            }
        }
    }
    class SignIn extends AsyncTask<String,String,String>{
        boolean flag;
        String pwd=null;

        public SignIn(boolean flag) {
            this.flag = flag;
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.i("classcall",strings[0]);
            String response="";
            try {
                if(flag){
                    pwd=strings[1];
                }
                URL url = new URL(getResources().getString(R.string.login_url)+"?email="+strings[0]);
                Log.i("urlconnect",url.toString());
                URLConnection ucon=url.openConnection();
                InputStream ins=ucon.getInputStream();
                BufferedInputStream bis=new BufferedInputStream(ins);
                Log.i("inputstream","bis");
                int data=0;
                while ((data=bis.read())!=-1){
                    response=response+(char)data;
                }
                Log.i("responsebackground",response);
            }
            catch(Exception e){
                Log.i("excuteexception",e.getMessage());
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                Log.i("postexecute",s);
                JSONArray array = new JSONArray(s);
                JSONObject jsonObject=array.getJSONObject(0);
                if(array.length()==0){
                    //Toast.makeText(LoginActivity.this, "Login Failed try again", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (flag) {
                        JSONObject object=array.getJSONObject(0);
                        if(pwd.equals(object.getString("password"))){
                            SharedPreferences preferences=getSharedPreferences("data",MODE_PRIVATE);
                            SharedPreferences.Editor editor=preferences.edit();
                            editor.putString("email",jsonObject.getString("email"));
                            editor.putString("id",jsonObject.getString("id"));
                            editor.putString("username",jsonObject.getString("name"));
                            editor.commit();
                            //Toast.makeText(LoginActivity.this, "Sign in success", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(LoginActivity.this,HomeScreenActvity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else{
                        SharedPreferences preferences=getSharedPreferences("data",MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("email",jsonObject.getString("email"));
                        editor.putString("id",jsonObject.getString("id"));
                        editor.putString("username",jsonObject.getString("name"));
                        editor.commit();
                       // Toast.makeText(LoginActivity.this, "Google sign in success", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(LoginActivity.this,HomeScreenActvity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
            catch (Exception e){
                Log.i("exceptioninpost",e.getMessage()+"");
            }
        }
    }
}
