package com.example.deepgandhi.movieguidedemo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class PasswordDialog extends AppCompatDialogFragment {
    private EditText password;
    private EditText confirmpassword;
    private PasswordDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.passwordedittext,null);
        builder.setView(view)
                .setTitle("PASSWORD")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String ipassword=password.getText().toString().trim();
                        String iconfirmpassword=confirmpassword.getText().toString().trim();
                        listener.getTrexts(ipassword,iconfirmpassword);
                    }
                });
        password=view.findViewById(R.id.layoutedit_password);
        confirmpassword=view.findViewById(R.id.layoutedit_confirmpassword);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (PasswordDialogListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement listener");
        }
    }

    public interface PasswordDialogListener{
        void getTrexts(String password,String confirmpassword);
    }
}
