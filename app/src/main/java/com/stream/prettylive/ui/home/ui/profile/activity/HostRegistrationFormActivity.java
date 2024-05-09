package com.stream.prettylive.ui.home.ui.profile.activity;

import static com.stream.prettylive.ui.utill.AlertUtils.showErrorAlert;
import static com.stream.prettylive.ui.utill.DownloadUri.uploadImageReturnUrl;
import static com.stream.prettylive.ui.utill.ImageUtils.uriToBitmap;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.stream.prettylive.databinding.ActivityHostRegistrationFormBinding;
import com.stream.prettylive.global.AppConstants;
import com.stream.prettylive.global.ApplicationClass;
import com.stream.prettylive.ui.home.ui.profile.models.HostModal;
import com.stream.prettylive.ui.home.ui.profile.models.UserDetailsModel;
import com.stream.prettylive.ui.utill.Constant;
import com.stream.prettylive.ui.utill.DownloadUri;
import com.stream.prettylive.ui.utill.FirestoreHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HostRegistrationFormActivity extends AppCompatActivity {

    private static final String TAG = "HostRegistrationFormAct";
    private ActivityHostRegistrationFormBinding binding;

    private String country="";
    private String realName="";
    private String whatsAppNumber="";
    private String email="";
    private String docType="";
    private String idCardNumber="";
    private String idImage="";
    private String agencyCode="";
    private String uid="";
    private String photo="";

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

        binding = ActivityHostRegistrationFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent() != null) {
            uid = getIntent().getStringExtra("uid");
            photo = getIntent().getStringExtra("image");
        }

        Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();

        init();

        intentLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(), fileUri -> {
                    if (fileUri != null) {
                        showProgressBar();
                        Bitmap bitmap = uriToBitmap(this, fileUri);
                        if (bitmap != null) {
                            uploadImageReturnUrl(bitmap, new DownloadUri.UploadImageCallback() {
                                @Override
                                public void onUploadSuccess(Uri imageUrl) {
                                    hideProgressBar();
                                    idImage = imageUrl.toString();
                                    updateUI(idImage);
                                }

                                @Override
                                public void onUploadFailure(String errorMessage) {
                                    // Handle upload failure
                                    idImage ="";
                                    Log.e(TAG, errorMessage);
                                }
                            });
                        } else {
                            Log.w(TAG, "Bitmap is null");
                        }
                    } else {
                        Log.w(TAG, "File URI is null");
                    }
                });

        askNotificationPermission();

//        fetchUserDetails(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));
    }

    private void init (){

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(Constant.LOGIN_DETAILS);

        binding.imgHoldNationalId.setOnClickListener(view -> {
            launchCamera();
        });
        binding.documentsType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedRadioButton = radioGroup.findViewById(checkedId);
                if (selectedRadioButton != null) {
                    docType = selectedRadioButton.getText().toString();
                    // Do something with the selected title
                    Log.d("SelectedTitle", docType);
                    Toast.makeText(HostRegistrationFormActivity.this, ""+docType, Toast.LENGTH_SHORT).show();

                }
            }
        });

        binding.btnApply.setOnClickListener(view -> {
            realName = Objects.requireNonNull(binding.edtName.getText()).toString();
            whatsAppNumber = Objects.requireNonNull(binding.edtNumber.getText()).toString();
            email = Objects.requireNonNull(binding.edtEmail.getText()).toString();
            idCardNumber = Objects.requireNonNull(binding.edtNationalId.getText()).toString();
            agencyCode = Objects.requireNonNull(binding.etAgencyCode.getText()).toString();
            
            if (realName.isEmpty()){
                showErrorAlert(this, "Enter Real name first.");
            }
            else if (whatsAppNumber.isEmpty()){
                showErrorAlert(this, "Enter your whatsApp number");
            } else if (agencyCode.isEmpty()){
                showErrorAlert(this, "Enter agency code number");
            }else if (email.isEmpty()){
                showErrorAlert(this, "Enter your email address");
            }else if (idCardNumber.isEmpty()){
                showErrorAlert(this, "Enter your ID Card number");
            }else if (docType.equals("")){
                showErrorAlert(this, "Please select documents type");
            }else if (idImage.equals("")){
                showErrorAlert(this, "Please upload your documents photo");
            }
            else {
                showProgressBar();
                HostModal hostModal = new HostModal();
                hostModal.setRealName(realName);
                hostModal.setPhoneNumber(whatsAppNumber);
                hostModal.setAgencyCode(agencyCode);
                hostModal.setEmailAddress(email);
                hostModal.setDocType(docType);
                hostModal.setIdCardNumber(idCardNumber);
                hostModal.setIdCardImage(idImage);
                hostModal.setStatus("pending");
                hostModal.setUid(uid);
                hostModal.setPhoto(photo);
                hostModal.setUserId(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));

                db.collection(Constant.HOST_REGISTER).document("pending").collection("host").document(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID)).set(hostModal).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        hideProgressBar();

                        AlertDialog.Builder builder = new AlertDialog.Builder(HostRegistrationFormActivity.this);
                        builder.setTitle("")
                                .setMessage("Your request has been successfully")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FirestoreHelper.fetchAndUpdateHostCount("pending");
                                        dialog.dismiss();
                                        onBackPressed();
                                    }
                                })
                                .show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgressBar();
                        Toast.makeText(HostRegistrationFormActivity.this, "error=>"+e, Toast.LENGTH_SHORT).show();

                    }
                });

            }


        });
    }

    private void launchCamera() {
        Log.d(TAG, "launchCamera");

        // Pick an image from storage
        intentLauncher.launch(new String[]{ "image/*" });
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
//                        updateUI(userDetails);
                    }

                    // Now userDetailsList contains UserDetails objects from Firestore
                    // Use the list as needed (e.g., display in UI, perform operations)
                });
    }

    private void updateUI(String idImage) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (Objects.equals(idImage, "")){
                        binding.rlHoldNationalId.setVisibility(View.VISIBLE);
                        Glide.with(getApplication())
                                .load(Constant.USER_PLACEHOLDER_PATH)
                                .into(binding.imgHoldNationalId);
                    }else {
                        binding.rlHoldNationalId.setVisibility(View.INVISIBLE);
                        Glide.with(getApplication())
                                .load(idImage)
                                .into(binding.imgHoldNationalId);
                    }
                }
            });

        }catch (Exception e){

        }

    }
    public void onBackPress(View view) {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}