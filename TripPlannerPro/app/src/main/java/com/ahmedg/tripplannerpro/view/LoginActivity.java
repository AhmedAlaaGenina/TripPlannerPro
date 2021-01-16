package com.ahmedg.tripplannerpro.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.ahmedg.tripplannerpro.R;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import static android.text.TextUtils.isEmpty;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LoginActivity extends AppCompatActivity {
    private Button loginBtn, signUpBtn ;
    private LoginButton mLoginBtn;
    private EditText mEmailSignIn,mPasswordSignIn;
    private TextView mResendVerificationEmail, mForgetPassword;
    private FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private TextInputLayout mEmailTextInputLayout, mPasswordTextInputLayout;
    private CallbackManager mCallbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        setUpFirebaseAuth();
        setUpHideErorrs();
       // printKeyHash();
        setUpLoginButton();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate(mEmailSignIn.getText().toString(),mPasswordSignIn.getText().toString())) {
                    signInWithEmailAndPassword(mEmailSignIn.getText().toString(), mPasswordSignIn.getText().toString());
                }
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
            }
        });
        mResendVerificationEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ResendVerificationEmailDailogFragment dialogResendVerifactionEmail = new ResendVerificationEmailDailogFragment();
                dialogResendVerifactionEmail.show(getSupportFragmentManager(),"dialog_resend_email_verification");
            }
        });
        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetPasswordDailogFragment dialogResetPassword = new ForgetPasswordDailogFragment();
                dialogResetPassword.show(getSupportFragmentManager(),"dialog forget Password");
            }
        });

    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // updateUI(user);
                            if(user != null){
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                        // ...
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void init(){
        loginBtn = findViewById(R.id.login_button);
        signUpBtn = findViewById(R.id.signup_button);
        mAuth = FirebaseAuth.getInstance();
        mEmailSignIn = findViewById(R.id.email_et);
        mPasswordSignIn = findViewById(R.id.password_et);
        mResendVerificationEmail = findViewById(R.id.resend_verification_email_tv);
        mForgetPassword = findViewById(R.id.forget_password_tv);
        mEmailTextInputLayout = findViewById(R.id.emailSignInTextInputLayout);
        mPasswordTextInputLayout = findViewById(R.id.passwordSignInTextInputLayout);
        FacebookSdk.sdkInitialize(LoginActivity.this);
//        AppEventsLogger.activateApp(this);
        mCallbackManager = CallbackManager.Factory.create();
        mLoginBtn = findViewById(R.id.login_fb_btn);
    }
    private void printKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.ahmedg.tripplannerpro", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHash:", e.toString());
        }
    }
    private void setUpFirebaseAuth(){
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){

                    if (user.isEmailVerified()){
                        Log.d(TAG,"Sign in");
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Check your inbox", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.d(TAG,"Sign out");
                }
            }
        };
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
        mAuth.addAuthStateListener(mAuthStateListener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop1");
        if (mAuthStateListener != null) {
            Log.d(TAG,"onStop2");
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
    private boolean validate(String email, String password){
        boolean validate = false;
        mEmailTextInputLayout.setErrorEnabled(false);
        mPasswordTextInputLayout.setErrorEnabled(false);
        if (!isEmpty(email)&&!isEmpty(password)){
            validate = true;

        }else{
            if (isEmpty(email)){
                setUpTextInputLayout(mEmailTextInputLayout,R.string.warnOfEmptyEmail);
            }
            if (isEmpty(password)){
                setUpTextInputLayout(mPasswordTextInputLayout,R.string.warnOfEmptyPassword);
            }
        }
        return validate;
    }

    private void signInWithEmailAndPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "signInWithEmail:success");

                }else{
                    try {
                        if (task.getException() != null)
                            throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e) {
                        setUpTextInputLayout(mEmailTextInputLayout, R.string.warnEmailNotExist);
                        mEmailTextInputLayout.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        setUpTextInputLayout(mPasswordTextInputLayout, R.string.wrongPassword);
                        mPasswordTextInputLayout.requestFocus();
                    } catch (FirebaseAuthException | FirebaseNetworkException e) {
                        Toast.makeText(getApplicationContext(), "Check your internet!", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.w(TAG, "signInWithEmailAndPassword:failure", e);
                        Toast.makeText(getApplicationContext(), "Sign in failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void setUpTextInputLayout(TextInputLayout textInputLayout, int s){
        textInputLayout.setError(getString(s));
        textInputLayout.setErrorIconDrawable(R.drawable.error);
        mPasswordTextInputLayout.setErrorEnabled(true);
    }
    private void setUpHideErorrs() {
        mEmailSignIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mEmailSignIn.getText().toString().trim().length() > 0) {
                    mEmailTextInputLayout.setErrorEnabled(false);
                } else {
                    setUpTextInputLayout(mEmailTextInputLayout,R.string.warnOfEmptyEmail);
                }
            }
        });
        mPasswordSignIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mPasswordSignIn.getText().toString().trim().length() > 0) {
                    mPasswordTextInputLayout.setErrorEnabled(false);
                } else {
                    setUpTextInputLayout(mPasswordTextInputLayout,R.string.warnOfEmptyPassword);
                }
            }
        });
    }
    private void setUpLoginButton(){
        mLoginBtn.setReadPermissions("email", "public_profile");
        mLoginBtn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
    }

}