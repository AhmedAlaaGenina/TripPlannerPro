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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.text.TextUtils.isEmpty;


public class ResendVerificationEmailDailogFragment extends DialogFragment {
    private static final String TAG = "ResendVerifactionEmail";
    private EditText mConfirmPassword, mConfirmEmail;
    private Button mConfirmBtn, mCancelBtn;
    private Context mCtx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resend_verification_email_dailog, container, false);
        mConfirmEmail = view.findViewById(R.id.email_dialog_fragment_et);
        mConfirmPassword = view.findViewById(R.id.password_dialog_fragment_et);
        mConfirmBtn = view.findViewById(R.id.confirm_btn);
        mCancelBtn = view.findViewById(R.id.cancel_btn);
        mCtx = getActivity();
        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to resend verification email.");
                if (!isEmpty(mConfirmEmail.getText().toString()) && !isEmpty(mConfirmPassword.getText()
                        .toString())) {
                    //temporarily authenticate and resend verification email
                    authenticateAndResendEmail(mConfirmEmail.getText().toString(),
                            mConfirmPassword.getText().toString());
                } else {
                    Toast.makeText(mCtx, "Fill out all fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return view;
    }

    private void authenticateAndResendEmail(String email, String password) {
        //Step 1:  reauthenricate(credential) email and password
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: reauthenticate success.");
                    //Step 2: resend email
                    resendVerificationEmail();
                    FirebaseAuth.getInstance().signOut();
                    getDialog().dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mCtx, "Invalid Credentials. \nReset your password and try again", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        });

    }

    private void resendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: resendVerificationEmail success.");
                        Toast.makeText(mCtx, "Sent Verification Email ", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    } else {
                        Toast.makeText(mCtx, "couldn't send email", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}