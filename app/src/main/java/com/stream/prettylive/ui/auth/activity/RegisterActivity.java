package com.stream.prettylive.ui.auth.activity;

import androidx.annotation.NonNull;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;
import com.stream.prettylive.R;
import com.stream.prettylive.databinding.ActivityRegisterBinding;
import com.stream.prettylive.global.AppConstants;
import com.stream.prettylive.global.ApplicationClass;
import com.stream.prettylive.ui.common.BaseActivity;
import com.stream.prettylive.ui.common.GenerateUserId;
import com.stream.prettylive.ui.home.HomeActivity;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity";
    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private ProgressDialog progressDialog;
    String countryName = "";
    String countryCode="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        init();

    }

    private void init(){

        countryName = binding.ccp.getSelectedCountryName();
        countryCode = binding.ccp.getSelectedCountryCode();
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
//                    Toast.makeText(RegisterActivity.this, "I have logged in successfully",
//                            Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    ApplicationClass.getSharedpref().saveString(AppConstants.USER_ID, user.getUid());
                    checkUserExistenceInFirestore(user);
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

    private void checkUserExistenceInFirestore(FirebaseUser user) {
        FirebaseFirestore.getInstance().collection("login_details")
                .whereEqualTo("email", user.getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            Log.i("Onboard", "checkUserExistenceInFirestore: if"+task.getResult());
                            // User doesn't exist in Firestore
                            // You might perform actions like creating a new user document
                            checkLastId(user);
                        } else {
                            Log.i("Onboard", "checkUserExistenceInFirestore:  else"+task.getResult());
                            // User exists in Firestore
                            // You can retrieve the user's information if needed
                            moveNextPage();
                        }
                    } else {
                        // Handle Firestore query failure
                    }
                });
//        checkLastId(user);
    }

    private void checkLastId(FirebaseUser user){
        GenerateUserId.getLastUserId(firestore, "login_details", new GenerateUserId.UserIdCallback() {
            @Override
            public void onUserIdReceived(int userId) {
                Log.i("MainActivity", "Last user ID: " + userId);

                // Example: Get the next user ID (increment by 1)
                int nextUserId = userId + 1;
                Log.i("MainActivity", "Next user ID: " + nextUserId);
                updateUI(user,nextUserId);

            }

            @Override
            public void onFailure(Exception e) {
                Log.i("MainActivity", "Exception: " + e);
                if (e==null){
                    updateUI(user,1000000);
                }
            }
        });
    }

    private void updateUI(FirebaseUser user, int nextUserId) {
        long timestamp = System.currentTimeMillis();
        Map<String, Object> loginDetails = new HashMap<>();
        loginDetails.put("userId", user.getUid());
        loginDetails.put("uid", nextUserId);
        loginDetails.put("username", binding.txtUsername.getText().toString().trim());
        loginDetails.put("email", user.getEmail());
        loginDetails.put("phone", binding.txtPhone.getText().toString().trim());
        loginDetails.put("country_code", "+"+countryCode);
        loginDetails.put("country_name", countryName);
        loginDetails.put("login_type", "email");
        loginDetails.put("image", user.getPhotoUrl()!=null?user.getPhotoUrl():"");
        loginDetails.put("reg_id", "");
        loginDetails.put("device_id", "");
        loginDetails.put("beans", "0");
        loginDetails.put("coins", "0");
        loginDetails.put("level", "1");
        loginDetails.put("diamond", "0");
        loginDetails.put("docId", "");
        loginDetails.put("latitude", "0");
        loginDetails.put("longitude", "0");
        loginDetails.put("friends", "0");
        loginDetails.put("followers", "0");
        loginDetails.put("following", "0");
        loginDetails.put("loginTime", timestamp);

        // Add the login details to Firestore
        firestore.collection("login_details")
                .add(loginDetails)
                .addOnSuccessListener(documentReference -> {
                    // Login details added successfully
                    Toast.makeText(RegisterActivity.this, "I have logged in successfully",
                            Toast.LENGTH_SHORT).show();
                    moveNextPage();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this, "Error adding login details"+e,Toast.LENGTH_SHORT).show();
                    // Handle failure
                    Log.e("MainActivity", "Error adding login details", e);
                });


    }

    private void moveNextPage() {
        Intent mainIntent = new Intent(RegisterActivity.this, HomeActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryName = binding.ccp.getSelectedCountryName();
                countryCode = binding.ccp.getSelectedCountryCode();
                Log.i("MainActivity", "countryName : " + countryName);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}