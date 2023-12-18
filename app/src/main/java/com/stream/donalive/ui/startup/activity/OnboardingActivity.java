package com.stream.donalive.ui.startup.activity;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stream.donalive.R;
import com.stream.donalive.databinding.ActivityOnboardingBinding;
import com.stream.donalive.global.AppConstants;
import com.stream.donalive.global.ApplicationClass;
import com.stream.donalive.streaming.internal.sdk.ZEGOSDKManager;
import com.stream.donalive.streaming.internal.sdk.basic.ZEGOSDKCallBack;
import com.stream.donalive.ui.auth.activity.LoginActivity;
//import com.stream.donalive.ui.auth.activity.PhoneActivity;
import com.stream.donalive.ui.auth.activity.OtpVerificationActivity;
import com.stream.donalive.ui.auth.activity.PhoneActivity;
import com.stream.donalive.ui.common.GenerateUserId;
import com.stream.donalive.ui.home.HomeActivity;

import java.util.HashMap;
import java.util.Map;

public class OnboardingActivity extends AppCompatActivity {
    private ActivityOnboardingBinding binding;
    Animation bottomAnimation,middleAnimation;
    private SignInClient signInClient;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    private final ActivityResultLauncher<IntentSenderRequest> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartIntentSenderForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    handleSignInResult(result.getData());
                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animantion);
        // Configure Google Sign In
        signInClient = Identity.getSignInClient(OnboardingActivity.this);
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        spannableString();
        binding.btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnboardingActivity.this, LoginActivity.class));
            }
        });

        binding.btnGoogle.setAnimation(bottomAnimation);
        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });

        binding.btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnboardingActivity.this, OtpVerificationActivity.class));

            }
        });



    }

    private void googleSignIn() {
        GetSignInIntentRequest signInRequest = GetSignInIntentRequest.builder()
                .setServerClientId(getString(R.string.default_web_client_id))
                .build();

        signInClient.getSignInIntent(signInRequest)
                .addOnSuccessListener(new OnSuccessListener<PendingIntent>() {
                    @Override
                    public void onSuccess(PendingIntent pendingIntent) {
                        launchSignIn(pendingIntent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Google Sign-in failed", e);
                    }
                });
    }

    private void launchSignIn(PendingIntent pendingIntent) {
        try {
            IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(pendingIntent)
                    .build();
            signInLauncher.launch(intentSenderRequest);
        } catch (Exception e) {
            Log.e(TAG, "Couldn't start Sign In: " + e.getLocalizedMessage());
        }
    }
    private void handleSignInResult(Intent data) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            SignInCredential credential = signInClient.getSignInCredentialFromIntent(data);
            String idToken = credential.getGoogleIdToken();
            Log.d(TAG, "firebaseAuthWithGoogle:" + credential.getId());
            firebaseAuthWithGoogle(idToken);
        } catch (ApiException e) {
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e);
//            updateUI(null);
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OnboardingActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            checkUserExistenceInFirestore(user);
//                            checkLastId(user);
//                            updateUI(user);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(OnboardingActivity.this, "Authentication Failed,try again...", Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();
                    }
                });
    }

    private void oneTapSignIn() {
        // Configure One Tap UI
        BeginSignInRequest oneTapRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true)
                                .setServerClientId(getString(R.string.default_web_client_id))
                                .setFilterByAuthorizedAccounts(true)
                                .build()
                )
                .build();

        // Display the One Tap UI
        signInClient.beginSignIn(oneTapRequest)
                .addOnSuccessListener(new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult beginSignInResult) {
                        launchSignIn(beginSignInResult.getPendingIntent());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // No saved credentials found. Launch the One Tap sign-up flow, or
                        // do nothing and continue presenting the signed-out UI.
                    }
                });
    }

    private void spannableString() {
        String fullText = "Login means you agree to our Terms & Privacy Policy";

        // Create a SpannableString to underline the specific text
        SpannableString spannableString = new SpannableString(fullText);

        // Find the starting and ending indices of the text to be underlined
        int startUnderline = fullText.indexOf("Terms");
        int endUnderline = fullText.indexOf("Policy") + "Policy".length();

        // Apply underline to the specified portion of text
        if (startUnderline != -1 && endUnderline != -1) {
            spannableString.setSpan(new UnderlineSpan(), startUnderline, endUnderline, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            // You can also change the color of the underlined text if needed
            spannableString.setSpan(new ForegroundColorSpan(Color.GREEN), startUnderline, endUnderline, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        binding.txtTerms.setText(spannableString);
    }

    // Check if a user exists in Firestore based on email
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
                            ApplicationClass.getSharedpref().saveString(AppConstants.USER_ID, user.getUid());
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
        ApplicationClass.getSharedpref().saveString(AppConstants.USER_ID, user.getUid());
            long timestamp = System.currentTimeMillis();
            Map<String, Object> loginDetails = new HashMap<>();
            loginDetails.put("userId", user.getUid());
            loginDetails.put("uid", nextUserId);
            loginDetails.put("username", user.getDisplayName()!=null?user.getDisplayName():String.valueOf(nextUserId));
            loginDetails.put("email", user.getEmail());
            loginDetails.put("phone", "");
            loginDetails.put("country_code", "");
            loginDetails.put("country_name", "");
            loginDetails.put("login_type", "google");
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
                        Toast.makeText(OnboardingActivity.this, "You have logged in successfully",
                                Toast.LENGTH_SHORT).show();
                        moveNextPage();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(OnboardingActivity.this, "Error adding login details"+e,Toast.LENGTH_SHORT).show();
                        // Handle failure
                        Log.e("MainActivity", "Error adding login details", e);
                    });


    }

    private void moveNextPage() {
        Intent mainIntent = new Intent(OnboardingActivity.this, HomeActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Display One-Tap Sign In if user isn't logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.i("currentUser123", "onCreate: "+currentUser);
        if (currentUser == null) {
//            oneTapSignIn();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        signInZEGOSDK("amit123", "testuser123", (errorCode, message) -> {
//            if (errorCode == 0) {
//
//            }
//            System.out.println(errorCode+"<=code123");
//            System.out.println(message.toString()+"<=message123");
//
//        });

    }

    private void signInZEGOSDK(String userID, String userName, ZEGOSDKCallBack callback) {
        ZEGOSDKManager.getInstance().connectUser(userID, userName, callback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}