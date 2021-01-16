package com.ahmedg.tripplannerpro.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ahmedg.tripplannerpro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private Button mSignUpBtn, mSignInBtn;
    private EditText mEmailSignUp, mPasswordSignUp, mConfirmPasswordSignUp;
    private FirebaseAuth mAuth;
    private TextInputLayout mEmailTextInputLayoutSignUp, mPasswordTextInputLayoutSignUp, mConfirmPasswordTextInputLayoutSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        setUpHideErorrs();
        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validate = validate(mEmailSignUp.getText().toString(),
                        mPasswordSignUp.getText().toString(), mConfirmPasswordSignUp.getText().toString());
                if (validate) {
                    Log.v(TAG, validate + "");
                    createUserWithEmailAndPassword(mEmailSignUp.getText().toString(),
                            mPasswordSignUp.getText().toString());
                } else {
                    Log.v(TAG, validate + "");
                }

            }
        });
        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    private void init() {
        mSignUpBtn = findViewById(R.id.signup_button);
        mSignInBtn = findViewById(R.id.signInBtn);
        mEmailSignUp = findViewById(R.id.email_et);
        mPasswordSignUp = findViewById(R.id.password_et);
        mConfirmPasswordSignUp = findViewById(R.id.password_repeat_et);
        mAuth = FirebaseAuth.getInstance();
        mEmailTextInputLayoutSignUp = findViewById(R.id.emailTextInputLayoutSignUp);
        mPasswordTextInputLayoutSignUp = findViewById(R.id.passwordTextInputLayoutSignUp);
        mConfirmPasswordTextInputLayoutSignUp = findViewById(R.id.confirmPasswordTextInputLayoutSignUp);
    }

    private boolean validate(String email, String password, String confirmPassword) {
        boolean validate = false;
        mEmailTextInputLayoutSignUp.setErrorEnabled(false);
        mPasswordTextInputLayoutSignUp.setErrorEnabled(false);
        mConfirmPasswordTextInputLayoutSignUp.setErrorEnabled(false);
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(confirmPassword)) {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if ((password).equals(confirmPassword)) {
                    // createUserWithEmailAndPassword(mEmailSignUp.getText().toString(), mPasswordSignUp.getText().toString());
                    validate = true;
                } else {
                    setUpTextInputLayout(mConfirmPasswordTextInputLayoutSignUp,R.string.passwordsNotMatch);
                    mConfirmPasswordTextInputLayoutSignUp.setErrorEnabled(true);
                }
            } else {
                setUpTextInputLayout(mEmailTextInputLayoutSignUp,R.string.warnOfDomainInvaild);
                mEmailTextInputLayoutSignUp.setErrorEnabled(true);
            }
        } else {
            if (TextUtils.isEmpty(email)) {
                setUpTextInputLayout(mEmailTextInputLayoutSignUp,R.string.warnOfEmptyEmail);
                mEmailTextInputLayoutSignUp.setErrorEnabled(true);
            }
            if (TextUtils.isEmpty(password)) {
                setUpTextInputLayout(mPasswordTextInputLayoutSignUp,R.string.warnOfEmptyPassword);
                mPasswordTextInputLayoutSignUp.setErrorEnabled(true);
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                setUpTextInputLayout(mConfirmPasswordTextInputLayoutSignUp,R.string.warnOfEmptyConfirmPassword);
                mConfirmPasswordTextInputLayoutSignUp.setErrorEnabled(true);
            }
        }


        return validate;
    }

    private void setUpHideErorrs() {
        mEmailSignUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mEmailSignUp.getText().toString().trim().length() > 0) {
                    mEmailTextInputLayoutSignUp.setErrorEnabled(false);
                }else{
                    mEmailTextInputLayoutSignUp.setError(getString(R.string.warnOfEmptyEmail));
                    mEmailTextInputLayoutSignUp.setErrorIconDrawable(R.drawable.error);
                    mEmailTextInputLayoutSignUp.setErrorEnabled(true);
                }
            }
        });
        mPasswordSignUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mPasswordSignUp.getText().toString().trim().length() > 0) {
                    mPasswordTextInputLayoutSignUp.setErrorEnabled(false);
                }else{
                    mPasswordTextInputLayoutSignUp.setError(getString(R.string.warnOfEmptyPassword));
                    mPasswordTextInputLayoutSignUp.setErrorIconDrawable(R.drawable.error);
                    mPasswordTextInputLayoutSignUp.setErrorEnabled(true);
                }
            }
        });
        mConfirmPasswordSignUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mConfirmPasswordSignUp.getText().toString().trim().length() > 0) {
                    mConfirmPasswordTextInputLayoutSignUp.setErrorEnabled(false);
                }else{
                    mConfirmPasswordTextInputLayoutSignUp.setError(getString(R.string.warnOfEmptyConfirmPassword));
                    mConfirmPasswordTextInputLayoutSignUp.setErrorIconDrawable(R.drawable.error);
                    mConfirmPasswordTextInputLayoutSignUp.setErrorEnabled(true);
                }
            }
        });

    }
    private void createUserWithEmailAndPassword(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "createUserWithEmail:success");
                    sendVerificationEmail();
                } else {
                    setUpException(task);

                }

            }
        });
    }
    private void sendVerificationEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "sendEmailVerification:success");
                        Toast.makeText(getApplicationContext(), "Send Verification Email", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, e.toString());
                    Toast.makeText(getApplicationContext(), "Could not Send Verification Email", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    private void setUpException(Task<AuthResult> task){
        try {
            if (task.getException() != null)
                throw task.getException();
        } catch (FirebaseAuthWeakPasswordException e) {
            setUpTextInputLayout(mPasswordTextInputLayoutSignUp,R.string.weakPassword);
            mPasswordTextInputLayoutSignUp.requestFocus();
        } catch (FirebaseAuthInvalidCredentialsException e) {
            setUpTextInputLayout(mPasswordTextInputLayoutSignUp,R.string.invaildEmail);
            mPasswordTextInputLayoutSignUp.requestFocus();
        } catch (FirebaseAuthUserCollisionException e) {
            setUpTextInputLayout(mEmailTextInputLayoutSignUp,R.string.userExist);
            mEmailTextInputLayoutSignUp.requestFocus();
        } catch (FirebaseAuthException | FirebaseNetworkException e) {
            Toast.makeText(SignUpActivity.this, "Check your internet!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.w(TAG, "createUserWithEmail:failure", e);
            Toast.makeText(getApplicationContext(), "Sign up failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void setUpTextInputLayout(TextInputLayout textInputLayout, int s){
        textInputLayout.setError(getString(s));
        textInputLayout.setErrorIconDrawable(R.drawable.error);
    }
}