package com.stream.donalive.ui.home.ui.profile.activity;

import static com.stream.donalive.ui.utill.ImageUtils.uriToBitmap;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.stream.donalive.R;
import com.stream.donalive.databinding.ActivityUpdateUserDetailsBinding;
import com.stream.donalive.global.AppConstants;
import com.stream.donalive.global.ApplicationClass;
import com.stream.donalive.ui.home.ui.profile.models.UserDetailsModel;
import com.stream.donalive.ui.utill.Constant;
import com.stream.donalive.ui.utill.ImageUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UpdateUserDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "UpdateUserDetailsActivi";
    private ActivityUpdateUserDetailsBinding binding;
    private FirebaseFirestore db;
    private CollectionReference usersRef;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private UserDetailsModel userDetails;
    private ActivityResultLauncher<String[]> intentLauncher;
    private ProgressDialog progressDialog;
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(this,
                            "Can't post notifications without POST_NOTIFICATIONS permission",
                            Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateUserDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(Constant.LOGIN_DETAILS);

        binding.buttonCamera.setOnClickListener(this);

        intentLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(), fileUri -> {
                    if (fileUri != null) {
                        showProgressBar();
                        Bitmap bitmap = uriToBitmap(this, fileUri);
                        if (bitmap != null) {
                            new ImageUtils(new ImageUtils.Select() {
                                @Override
                                public void success(int value) {
                                    hideProgressBar();
                                    if(value==1){
                                        Toast.makeText(UpdateUserDetailsActivity.this, "Profile image has been updated successfully", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(UpdateUserDetailsActivity.this, "Somethings went wrong", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }).uploadImageToFirestore(bitmap);
                        } else {
                            Log.w(TAG, "Bitmap is null");
                        }
                    } else {
                        Log.w(TAG, "File URI is null");
                    }
                });

        askNotificationPermission();

        fetchUserDetails(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));

        binding.txtUserName.setInputType(InputType.TYPE_NULL); // Disables keyboard input

        binding.txtUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });

        binding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.dialog_username_update);

        ImageView close = bottomSheetDialog.findViewById(R.id.close);
        EditText etName = bottomSheetDialog.findViewById(R.id.tx_name);
        MaterialCardView save = bottomSheetDialog.findViewById(R.id.buttonSave);
        MaterialCardView cancel = bottomSheetDialog.findViewById(R.id.buttonCancel);

        etName.setText(userDetails.getUsername());

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();
                updateUserName(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID),etName.getText().toString().trim(),bottomSheetDialog);
            }
        });

        bottomSheetDialog.show();
//        bottomSheetDialog.dismiss();
    }

    private void updateUserName(String userId, String name, BottomSheetDialog bottomSheetDialog) {
        // Reference to the Firestore collection
        firestore = FirebaseFirestore.getInstance();
        CollectionReference liveDetailsRef = firestore.collection(Constant.LOGIN_DETAILS);

        // Create a query to find the document with the given userId
        Query query = liveDetailsRef.whereEqualTo("userId", userId);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get the document ID for the matched document
                    String documentId = document.getId();

                    Map<String, Object> updateDetails = new HashMap<>();
                    updateDetails.put("username", name);

                    // Update the liveType field from 0 to 1
                    liveDetailsRef.document(documentId)
                            .update(updateDetails)
                            .addOnSuccessListener(aVoid -> {
                                hideProgressBar();
                                bottomSheetDialog.dismiss();

                                Toast.makeText(this, "Nick Name has been updated successfully", Toast.LENGTH_SHORT).show();
                                Log.i("UpdateLiveType", "image updated successfully for user with ID: " + userId);
                            })
                            .addOnFailureListener(e -> {
                                hideProgressBar();
                                bottomSheetDialog.dismiss();
                                Toast.makeText(this, "Some things went wrong", Toast.LENGTH_SHORT).show();
                                Log.e("UpdateLiveType", "Error updating liveType for user with ID: " + userId, e);
                            });
                }
            } else {
                Log.e("UpdateLiveType", "Error getting documents: ", task.getException());
            }
        });
    }


    private void showProgressBar() {
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }
    private void hideProgressBar() {
        progressDialog.dismiss();
    }

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // Your app can post notifications.
            } else{
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
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

                    // Now userDetailsList contains UserDetails objects from Firestore
                    // Use the list as needed (e.g., display in UI, perform operations)
                });
    }

    private void updateUI(UserDetailsModel userDetails) {
        binding.txtUserName.setText(userDetails.getUsername());
//        binding.txtUid.setText(String.valueOf(userDetails.getUid()));
//        binding.txtCountry.setText(userDetails.getCountryName());
        // Load image
        if (Objects.equals(userDetails.getImage(), "")){
            Glide.with(getApplication())
                    .load(Constant.USER_PLACEHOLDER_PATH)
                    .into(binding.profileImage);
        }else {
            Glide.with(getApplication())
                    .load(userDetails.getImage())
                    .into(binding.profileImage);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.buttonCamera) {
            launchCamera();
        }
    }

    private void launchCamera() {
        Log.d(TAG, "launchCamera");

        // Pick an image from storage
        intentLauncher.launch(new String[]{ "image/*" });
    }
}