package com.example.deepgandhi.movieguidedemo;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

public class ProfileActivity extends AppCompatActivity implements PasswordDialog.PasswordDialogListener {
    TextView tv_username,tv_email,tv_password;
    ImageView edit_username,edit_email,edit_password;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String user_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tv_email=findViewById(R.id.profile_email);
        tv_password=findViewById(R.id.profile_password);
        tv_username=findViewById(R.id.profile_username);
        edit_email=findViewById(R.id.edit_email);
        edit_password=findViewById(R.id.edit_password);
        edit_username=findViewById(R.id.edit_username);

        preferences=getSharedPreferences("data",MODE_PRIVATE);
        editor=preferences.edit();
        user_id=preferences.getString("id","");

        tv_email.setText(preferences.getString("email",""));
        tv_username.setText(preferences.getString("username",""));
        tv_password.setText("***********");

        edit_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProfileActivity.this);
                LayoutInflater inflater = ProfileActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.edittext, null);
                dialogBuilder.setView(dialogView);

                final EditText editText = (EditText) dialogView.findViewById(R.id.alert_edit_change);
                dialogBuilder.setTitle("EMAIL");
                editText.setHint("email");
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                dialogBuilder.setPositiveButton("change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ChangeEmail changeEmail=new ChangeEmail();
                        changeEmail.execute(editText.getText().toString().trim(),user_id);
                    }
                });
                dialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });


        edit_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProfileActivity.this);
                LayoutInflater inflater = ProfileActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.edittext, null);
                dialogBuilder.setView(dialogView);

                final EditText editText = (EditText) dialogView.findViewById(R.id.alert_edit_change);
                dialogBuilder.setTitle("USERNAME");
                editText.setHint("username");
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                dialogBuilder.setPositiveButton("change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ChangeUsername changeUsername=new ChangeUsername();
                        changeUsername.execute(editText.getText().toString().trim(),user_id);
                    }
                });
                dialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });

        edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasswordDialog passwordDialog=new PasswordDialog();
                passwordDialog.show(getSupportFragmentManager(),"PASSWORD");
            }
        });
    }

    @Override
    public void getTrexts(String password, String confirmpassword) {
        if(password.equals(confirmpassword)){
            ChangePassword changePassword=new ChangePassword();
            changePassword.execute(password,user_id);
        }
        else{
            Toast.makeText(this, "password and confirm password do not match", Toast.LENGTH_SHORT).show();
        }
    }

    class ChangePassword extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            String response="";
            try {
                URL url = new URL(getResources().getString(R.string.change_url) + "3&password=" + strings[0] + "&user_id=" + strings[1]);
                URLConnection ucon=url.openConnection();
                BufferedInputStream bis=new BufferedInputStream(ucon.getInputStream());
                int data=0;
                while ((data=bis.read())!=-1){
                    response=response+(char)data;
                }
            }
            catch (Exception e){}
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            s=s.trim();
            if(s.charAt(0)=='1'){
                Toast.makeText(ProfileActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
            }
        }
    }
    class ChangeUsername extends AsyncTask<String,String,String>{
        String response="";
        String username="";
        @Override
        protected String doInBackground(String... strings) {
            try {
                username=strings[0];
                URL url = new URL(getResources().getString(R.string.change_url) + "2&username=" + strings[0] + "&user_id=" + strings[1]);
                URLConnection ucon = url.openConnection();
                BufferedInputStream bis = new BufferedInputStream(ucon.getInputStream());
                int data = 0;
                while ((data = bis.read()) != -1) {
                    response = response + (char) data;
                }
            }
            catch (Exception e){}
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            s=s.trim();
            if(s.charAt(0)=='1'){
                Toast.makeText(ProfileActivity.this, "Username Updated", Toast.LENGTH_SHORT).show();
            }
            editor.remove("username");
            editor.commit();
            editor.putString("username",username);
            editor.commit();
            tv_username.setText(username);
        }
    }
    class ChangeEmail extends AsyncTask<String,String,String>{
        String email="";
        String response="";
        @Override
        protected String doInBackground(String... strings) {
            email=strings[0];
            try {
                URL url = new URL(getResources().getString(R.string.change_url) + "1&email=" + strings[0] + "&user_id=" + strings[1]);
                URLConnection ucon = url.openConnection();
                BufferedInputStream bis = new BufferedInputStream(ucon.getInputStream());
                int data = 0;
                while ((data = bis.read()) != -1) {
                    response = response + (char) data;
                }
            }
            catch (Exception e){}
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            s=s.trim();
            if(s.charAt(0)=='1'){
                Toast.makeText(ProfileActivity.this, "Email Updated", Toast.LENGTH_SHORT).show();
            }
            editor.remove("email");
            editor.commit();
            editor.putString("email",email);
            editor.commit();
            tv_email.setText(email);
        }
    }
}
