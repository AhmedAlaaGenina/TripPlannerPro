package com.ahmedg.tripplannerpro.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ahmedg.tripplannerpro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordDailogFragment extends DialogFragment {
    private static final String TAG = "DialogForgetPassword";
    private EditText mEmail;
    private Button mResetBtn;
    private Context mCtx;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forget_password_dailog, container, false);
        mCtx = getActivity();
        mEmail = view.findViewById(R.id.email_dialog_fragment_et);
        mResetBtn = view.findViewById(R.id.reset_password_btn);
        mResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mEmail.getText().toString().isEmpty()) {
                    sendPasswordResetEmail(mEmail.getText().toString());
                }else {
                    Toast.makeText(mCtx, "Fill out all fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void sendPasswordResetEmail(String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "Email sent.");
                    Toast.makeText(mCtx, "Check inbox.", Toast.LENGTH_SHORT).show();
                    if (getDialog()!=null)getDialog().dismiss();
                }else{
                    Log.d(TAG, "Failure Email sent.");
                    Toast.makeText(mCtx, "Can not sent email", Toast.LENGTH_SHORT).show();
                    if (getDialog()!=null)getDialog().dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG,e.toString());
            }
        });
    }
}