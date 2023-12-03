package com.stream.donalive.ui.auth.activity;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.stream.donalive.R;
import com.stream.donalive.databinding.ActivityRegisterBinding;
import com.stream.donalive.ui.common.BaseActivity;

public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity";
    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        init();

    }

    private void init(){
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.txtUsername.getText().toString().trim();
                String phone = binding.txtPhone.getText().toString().trim();
                String emailAddress = binding.txtEmail.getText().toString().trim();
                String password = binding.txtPassword.getText().toString().trim();
                String confirm_password = binding.txtConfirmPassword.getText().toString().trim();

                if (username.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please enter username.", Toast.LENGTH_SHORT).show();
                }
                else if (emailAddress.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Email address can't be empty.", Toast.LENGTH_SHORT).show();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                    Toast.makeText(RegisterActivity.this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
                } else if (phone.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please enter phone number", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                }else if (!password.equals(confirm_password)) {
                    Toast.makeText(RegisterActivity.this, "Password is mismatch please check once", Toast.LENGTH_SHORT).show();
                } else {
                    createAccount(emailAddress, password);
                }


            }
        });
    }

    private void createAccount(String email, String password) {

        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    Toast.makeText(RegisterActivity.this, "createUserWithEmail:success",
                            Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                    switch (errorCode) {

                        case "ERROR_INVALID_CUSTOM_TOKEN":
                            Toast.makeText(RegisterActivity.this, "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_CUSTOM_TOKEN_MISMATCH":
                            Toast.makeText(RegisterActivity.this, "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_INVALID_CREDENTIAL":
                            Toast.makeText(RegisterActivity.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_INVALID_EMAIL":
                            Toast.makeText(RegisterActivity.this, "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                            binding.txtEmail.setError("The email address is badly formatted.");
                            binding.txtEmail.requestFocus();
                            break;

                        case "ERROR_WRONG_PASSWORD":
                            Toast.makeText(RegisterActivity.this, "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                            binding.txtPassword.setError("password is incorrect ");
                            binding.txtPassword.requestFocus();
                            binding.txtPassword.setText("");
                            break;

                        case "ERROR_USER_MISMATCH":
                            Toast.makeText(RegisterActivity.this, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_REQUIRES_RECENT_LOGIN":
                            Toast.makeText(RegisterActivity.this, "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                            Toast.makeText(RegisterActivity.this, "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_EMAIL_ALREADY_IN_USE":
                            Toast.makeText(RegisterActivity.this, "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                            binding.txtEmail.setError("The email address is already in use by another account.");
                            binding.txtEmail.requestFocus();
                            break;

                        case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                            Toast.makeText(RegisterActivity.this, "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_USER_DISABLED":
                            Toast.makeText(RegisterActivity.this, "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_USER_TOKEN_EXPIRED":

                        case "ERROR_INVALID_USER_TOKEN":
                            Toast.makeText(RegisterActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_USER_NOT_FOUND":
                            Toast.makeText(RegisterActivity.this, "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_OPERATION_NOT_ALLOWED":
                            Toast.makeText(RegisterActivity.this, "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_WEAK_PASSWORD":
                            Toast.makeText(RegisterActivity.this, "The given password is invalid.", Toast.LENGTH_LONG).show();
                            binding.txtPassword.setError("The password is invalid it must 6 characters at least");
                            binding.txtPassword.requestFocus();
                            break;

                        default:
                            Toast.makeText(RegisterActivity.this, "Somethings went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                progressDialog.dismiss();
            }
        });

    }

    private void updateUI(FirebaseUser user) {
        Log.i(TAG, "updateUI: "+user.toString());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}