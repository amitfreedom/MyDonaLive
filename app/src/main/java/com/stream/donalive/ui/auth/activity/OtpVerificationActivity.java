package com.stream.donalive.ui.auth.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;
import com.stream.donalive.R;
import com.stream.donalive.databinding.ActivityOtpVerificationBinding;
import com.stream.donalive.global.AppConstants;
import com.stream.donalive.global.ApplicationClass;
import com.stream.donalive.ui.common.GenerateUserId;
import com.stream.donalive.ui.home.HomeActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OtpVerificationActivity extends AppCompatActivity {

    private static final String TAG = "OtpVerificationActivity";
    private ActivityOtpVerificationBinding binding;
    private String phone;
    private String mVerificationId;
    private String token;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    String countryName = "";
    String countryCode="";
    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        countryName = binding.ccp.getSelectedCountryName();
        countryCode = binding.ccp.getSelectedCountryCode();


        // Assign click listeners
        binding.buttonStartVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatePhoneNumber()) {
                    return;
                }

                startPhoneNumberVerification("+"+countryCode+binding.fieldPhoneNumber.getText().toString());
            }
        });

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = binding.txtOtp.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    binding.txtOtp.setError("Cannot be empty.");
                    return;
                }
                verifyPhoneNumberWithCode(mVerificationId, code);
            }
        });

        binding.btnResend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                binding.txtOtp.setText("");
                phone ="+"+countryCode+binding.fieldPhoneNumber.getText().toString();
                if (phone==null) {
                    return;
                }
                resendVerificationCode(phone, mResendToken);
            }
        });

        // Initialize phone auth callbacks
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(OtpVerificationActivity.this, "Invalid phone number.", Toast.LENGTH_SHORT).show();
//                    binding.otpView.setVisibility(View.GONE);
//                    binding.fieldPhoneNumber.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(OtpVerificationActivity.this, "Quota exceeded", Toast.LENGTH_SHORT).show();
//                    binding.otpView.setVisibility(View.GONE);
                }
                progressDialog.dismiss();
                Toast.makeText(OtpVerificationActivity.this, "Verification failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);

                mVerificationId = verificationId;
                mResendToken = token;
                binding.phoneView.setVisibility(View.GONE);
                binding.otpView.setVisibility(View.VISIBLE);

                progressDialog.dismiss();
                Toast.makeText(OtpVerificationActivity.this, "Code sent", Toast.LENGTH_SHORT).show();
            }
        };

    }

    private boolean validatePhoneNumber() {
        String phoneNumber = binding.fieldPhoneNumber.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            binding.fieldPhoneNumber.setError("Invalid phone number.");
            return false;
        }

        return true;
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber) // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(OtpVerificationActivity.this) // Activity (for callback binding)
                        .setCallbacks(mCallbacks) // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

        progressDialog.show();
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        progressDialog.show();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        progressDialog.show();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(OtpVerificationActivity.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OtpVerificationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            checkUserExistenceInFirestore(user);
//                            updateUI(user);
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                binding.txtOtp.setError("Invalid code.");
                            }
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    private void checkUserExistenceInFirestore(FirebaseUser user) {
        FirebaseFirestore.getInstance().collection("login_details")
                .whereEqualTo("phone", user.getPhoneNumber())
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
                            moveNextPage(user);
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
        loginDetails.put("username", "user_"+String.valueOf(nextUserId));
        loginDetails.put("email", "");
        loginDetails.put("phone", user.getPhoneNumber());
        loginDetails.put("country_code", "+"+countryCode);
        loginDetails.put("country_name", countryName);
        loginDetails.put("login_type", "phone");
        loginDetails.put("image", user.getPhotoUrl()!=null?user.getPhotoUrl():"");
        loginDetails.put("reg_id", "");
        loginDetails.put("device_id", "");
        loginDetails.put("beans", "0");
        loginDetails.put("coins", "0");
        loginDetails.put("level", "1");
        loginDetails.put("diamond", "0");
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
//                    Toast.makeText(OtpVerificationActivity.this, "I have logged in successfully",
//                            Toast.LENGTH_SHORT).show();
                    moveNextPage(user);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OtpVerificationActivity.this, "Error adding login details"+e,Toast.LENGTH_SHORT).show();
                    // Handle failure
                    Log.e("MainActivity", "Error adding login details"+ e);
                });


    }

    private void moveNextPage(FirebaseUser user) {
        ApplicationClass.getSharedpref().saveString(AppConstants.USER_ID, user.getUid());
        Intent mainIntent = new Intent(OtpVerificationActivity.this, HomeActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
        Toast.makeText(OtpVerificationActivity.this, "You have logged in successfully",
                            Toast.LENGTH_SHORT).show();
    }

//    private void updateUI(FirebaseUser user) {
//
//        Log.i("updateUI123", "updateUI: "+user.getUid());
//        Log.i("updateUI123", "updateUI: "+user.getPhoneNumber());
//        Log.i("updateUI123", "updateUI: "+user.getEmail());
//        Log.i("updateUI123", "updateUI: "+user.getDisplayName());
//        Log.i("updateUI123", "updateUI: "+user.getPhotoUrl());
//
//    }


    @Override
    protected void onResume() {
        super.onResume();
        binding.ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryName = binding.ccp.getSelectedCountryName();
                countryCode = binding.ccp.getSelectedCountryCode();
                Log.i("PhoneActivity", "countryName : " + countryName);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding= null;
    }
}