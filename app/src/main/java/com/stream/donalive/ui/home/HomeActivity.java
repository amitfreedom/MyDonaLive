package com.stream.donalive.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stream.donalive.R;
import com.stream.donalive.databinding.ActivityHomeBinding;
import com.stream.donalive.global.AppConstants;
import com.stream.donalive.global.ApplicationClass;
import com.stream.donalive.streaming.ZEGOSDKKeyCenter;
import com.stream.donalive.streaming.components.call.CallBackgroundService;
import com.stream.donalive.streaming.internal.ZEGOCallInvitationManager;
import com.stream.donalive.streaming.internal.ZEGOLiveStreamingManager;
import com.stream.donalive.streaming.internal.sdk.ZEGOSDKManager;
import com.stream.donalive.streaming.internal.sdk.basic.ZEGOSDKCallBack;
import com.stream.donalive.ui.home.ui.profile.models.UserDetailsModel;
import com.stream.donalive.ui.utill.Constant;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    public static String STATUS="";
    private FirebaseFirestore db;
    private CollectionReference usersRef;
    private FirebaseAuth mAuth;
    private UserDetailsModel userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_explore, R.id.sortVideoFragment,R.id.navigation_chat,R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(Constant.LOGIN_DETAILS);

//        initZEGOSDK();
    }

    private void fetchUserDetails(String userId) {

        usersRef.whereEqualTo("userId", userId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Handle error
                        Log.e("FirestoreListener", "Listen failed: " + error.getMessage());
                        return;
                    }

                    for (DocumentSnapshot document : value) {
                        userDetails = document.toObject(UserDetailsModel.class);
                        updateUI(userDetails);
                    }
                });

//
    }

    private void updateUI(UserDetailsModel userDetails) {
        signInZEGOSDK(String.valueOf(userDetails.getUid()), userDetails.getUsername(), (errorCode, message) -> {
            if (errorCode == 0) {
//                Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signInZEGOSDK(String userID, String userName, ZEGOSDKCallBack callback) {
        ZEGOSDKManager.getInstance().connectUser(userID, userName, callback);
    }

    private void initZEGOSDK() {

        ZEGOSDKManager.getInstance().initSDK(getApplication(), ZEGOSDKKeyCenter.appID, ZEGOSDKKeyCenter.appSign);
        ZEGOSDKManager.getInstance().enableZEGOEffects(true);
    }


    @Override
    protected void onStart() {
        super.onStart();

        fetchUserDetails(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));
    }

    @Override
    protected void onPause() {
        super.onPause();

//        if (isFinishing()){
//            ZEGOSDKManager.getInstance().disconnectUser();
//            ZEGOLiveStreamingManager.getInstance().removeUserData();
//            ZEGOLiveStreamingManager.getInstance().removeUserListeners();
//            // if Call invitation,init after user login,may receive call request.
//            ZEGOCallInvitationManager.getInstance().removeUserData();
//            ZEGOCallInvitationManager.getInstance().removeUserListeners();
//            Intent intent = new Intent(this, CallBackgroundService.class);
//            stopService(intent);
//        }

    }

    @Override
    protected void onDestroy() {
//        saveData();
        super.onDestroy();
        Log.i("checkmethod", "onDestroy:======== ");

        // Create a new user data map


    }

    private void saveData() {
        Log.i("checkmethod", "onDestroy:======123== ");
        // Create a new user data map
        Map<String, Object> user = new HashMap<>();
        user.put("name", "Dave");
        user.put("email", "dave@example.com");

// Add a new document with a generated ID
        db.collection("test123")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    Log.i("checkmethod", "DocumentSnapshot added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.i("checkmethod", "Error adding document", e);
                });

        Log.i("checkmethod", "onDestroy:======1234== ");

    }
}