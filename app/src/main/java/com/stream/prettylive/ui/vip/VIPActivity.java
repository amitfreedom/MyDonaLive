package com.stream.prettylive.ui.vip;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import com.stream.prettylive.R;
import com.stream.prettylive.databinding.ActivityVipactivityBinding;
import com.stream.prettylive.global.AppConstants;
import com.stream.prettylive.global.ApplicationClass;
import com.stream.prettylive.ui.home.ui.profile.models.UserDetailsModel;
import com.stream.prettylive.ui.utill.Constant;
import com.stream.prettylive.ui.utill.Convert;
import com.stream.prettylive.ui.vip.adapter.VipGiftAdapter;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VIPActivity extends AppCompatActivity implements VipGiftAdapter.OnVipSelectedListener {

    private ActivityVipactivityBinding binding;
    private VipGiftAdapter mAdapter;
    private Query mQuery;
    private FirebaseFirestore db;
    private CollectionReference giftsCollection,usersRef;
    private UserDetailsModel userDetailsModel;
    private String totalCoins="0";
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVipactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        giftsCollection = db.collection("purchaseGift");
        usersRef = db.collection(Constant.LOGIN_DETAILS);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...!");

        buttonSelect();

        getUserCoins(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));

        setAdapter();
    }

    private void getUserCoins(String userId) {
        Log.i("Coins123", "userId =: " + userId);
        usersRef.whereEqualTo("userId", userId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Handle error
                        Log.e("FirestoreListener", "Listen failed: " + error.getMessage());
                        return;
                    }

                    for (DocumentSnapshot document : value) {
                        if (document.exists()) {
                            userDetailsModel = document.toObject(UserDetailsModel.class);
                            // Get the "coins" field from the document
                            String coins = document.getString("coins");
                            Long uid = document.getLong("uid");

                            if (coins != null) {
                                totalCoins=coins;
                            }

                        }
                    }
                });
    }


    private void updateDocId(String userId, String docId, BottomSheetDialog bottomSheetDialog) {
        // Reference to the Firestore collection
        CollectionReference liveDetailsRef = db.collection(Constant.LOGIN_DETAILS);
        Query query = liveDetailsRef.whereEqualTo("userId", userId);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get the document ID for the matched document
                    String documentId = document.getId();
                    Map<String, Object> updateDetails = new HashMap<>();
                    updateDetails.put("docId", docId);

                    // Update the liveType field from 0 to 1
                    liveDetailsRef.document(documentId)
                            .update(updateDetails)
                            .addOnSuccessListener(aVoid -> {
                                progressDialog.dismiss();
                                bottomSheetDialog.dismiss();
                                Toast.makeText(this, "Congratulation , you have successfully purchase vip", Toast.LENGTH_SHORT).show();

                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                bottomSheetDialog.dismiss();
                                Toast.makeText(this, "Somethings went wrong , try again", Toast.LENGTH_SHORT).show();

                                Log.e("8963457834534", "Error updating liveType for user with ID: " + userId, e);
                            });
                }
            } else {
                Log.e("8963457834534", "Error getting documents: ", task.getException());
            }
        });
    }
    private void purchaseGiftForSixWeeks(String userId, String vipId, String fileName, String price, String title, BottomSheetDialog bottomSheetDialog) {
        // Calculate end date for 6 weeks from now
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        Date endDate = calendar.getTime();

        // 1 for y 0 for No
        // Create a Map with purchase details
        Map<String, Object> purchaseDetails = new HashMap<>();
        purchaseDetails.put("userId", userId); // Replace with the user's ID
        purchaseDetails.put("giftId", vipId);
        purchaseDetails.put("fileName", fileName);
        purchaseDetails.put("title", title);
        purchaseDetails.put("giftPrice", price);
        purchaseDetails.put("show", "1");
        purchaseDetails.put("purchaseDate", new Date());
        purchaseDetails.put("endDate", endDate);

        // Add this purchase to Firestore
        giftsCollection.add(purchaseDetails)
                .addOnSuccessListener(documentReference -> {
                    String docId = documentReference.getId();
                    if (docId!=null){
                        updateDocId(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID),docId,bottomSheetDialog);
                    }

                })
                .addOnFailureListener(e -> {
                    // Handle failure (e.g., show an error message)
                });
    }

    private void updateUserCoins(String senderId, String totalCoins, String currentPrice) {
        // Reference to the Firestore collection
        CollectionReference detailsRef = db.collection(Constant.LOGIN_DETAILS);

        // Create a query to find the document with the given userId
        Query query = detailsRef.whereEqualTo("userId", senderId);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get the document ID for the matched document
                    String documentId = document.getId();
                    Long totalCoin = Long.parseLong(totalCoins) - Long.parseLong(currentPrice);
                    Map<String, Object> updateDetails = new HashMap<>();
                    updateDetails.put("coins", String.valueOf(totalCoin));
                    // Update the liveType field from 0 to 1
                    detailsRef.document(documentId)
                            .update(updateDetails)
                            .addOnSuccessListener(aVoid -> {
                                Log.i("Coinsup", "Coins updated successfully for user with ID: " + senderId);
                            })
                            .addOnFailureListener(e -> {
                                Log.e("UpdateLiveType", "Error updating liveType for user with ID: "+e);
                            });
                }
            } else {
                Log.e("UpdateLiveType", "Error getting documents: ", task.getException());
            }
        });

    }

    private void buttonSelect() {
        binding.toggleGroup.check(binding.buttonPurchasable.getId());

// Change the background color of the selected button
        binding.buttonPurchasable.setBackgroundColor(getResources().getColor(R.color.pink_top));
        binding.buttonPurchasable.setTextColor(getResources().getColor(R.color.white));
        binding.buttonPurchasable.setStrokeColorResource(R.color.pink_top);
//        binding.buttonPurchasable.setTextSize(R.dimen._18sp);
        binding.toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    MaterialButton checkedButton = group.findViewById(checkedId);
                    // Change the background color of the selected button
                    checkedButton.setBackgroundColor(getResources().getColor(R.color.pink_top));
                    checkedButton.setTextColor(getResources().getColor(R.color.white));
                    checkedButton.setStrokeColorResource(R.color.pink_top);
//                    checkedButton.setTextSize(R.dimen._18sp);
                }
                else {
                    MaterialButton checkedButton = group.findViewById(checkedId);
                    // Change the background color of the selected button
                    checkedButton.setBackgroundColor(getResources().getColor(R.color.white));
                    checkedButton.setTextColor(getResources().getColor(R.color.gray));
//                    checkedButton.setTextSize(R.dimen._14sp);

                }
            }
        });

    }

    private void setAdapter() {
        mQuery = db.collection(Constant.VIP)
//                .orderBy("startTime", Query.Direction.DESCENDING)
//                .whereEqualTo("liveStatus","online")
                .limit(50);
        mAdapter = new VipGiftAdapter(mQuery, this) {
            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    binding.progressCircular.setVisibility(View.VISIBLE);
                    binding.rvVipGift.setVisibility(View.GONE);
                } else {
                    binding.rvVipGift.setVisibility(View.VISIBLE);
                    binding.progressCircular.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Log.e("FirebaseFirestoreException", "onError: "+e );
            }


        };
        binding.rvVipGift.setAdapter(mAdapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    public void addList(View view) {
//        AddVip();
    }

    private void AddVip1() {
        long timestamp = System.currentTimeMillis();
        Map<String, Object> vipMap = new HashMap<>();
        vipMap.put("vipId", "09");
        vipMap.put("title", "Yacht");
        vipMap.put("image", "https://firebasestorage.googleapis.com/v0/b/prettydemo-48691.appspot.com/o/images%2Fyacht.png?alt=media&token=6f3212c9-ce9a-40e5-a39a-f6d184c8be36");
        vipMap.put("beans", "310000");
        vipMap.put("fileName", "yacht.svga");

        db.collection("vip")
                .add(vipMap)
                .addOnSuccessListener(documentReference -> {
                    // Login details added successfully
                    Toast.makeText(VIPActivity.this, "added",
                            Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(VIPActivity.this, "Error failed"+e,Toast.LENGTH_SHORT).show();
                    // Handle failure
                    Log.e("MainActivity", "Error adding failed", e);
                });


    }
    private void AddVip() {
        long timestamp = System.currentTimeMillis();
        Map<String, Object> vipMap = new HashMap<>();

        vipMap.put("giftId", "1000");
        vipMap.put("giftName", "Race car");
        vipMap.put("gift_type", "1000");
        vipMap.put("image", "https://firebasestorage.googleapis.com/v0/b/prettydemo-48691.appspot.com/o/images%2Fracecar.png?alt=media&token=d6edbfae-590a-4d1a-9b47-40566f9d566b");
        vipMap.put("price", "5800000");
        vipMap.put("fileName", "racecar.svga");
        vipMap.put("timestamp", 1);

        db.collection("gifts")
                .add(vipMap)
                .addOnSuccessListener(documentReference -> {
                    // Login details added successfully
                    Toast.makeText(VIPActivity.this, "added",
                            Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(VIPActivity.this, "Error failed"+e,Toast.LENGTH_SHORT).show();
                    // Handle failure
                    Log.e("MainActivity", "Error adding failed", e);
                });


    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    @Override
    public void OnVipSelected(DocumentSnapshot user) {
        String image = user.getString("image");
        String title = user.getString("title");
        String price = user.getString("beans");
        String fileName = user.getString("fileName");
        String vipId = user.getString("vipId");
        showBottomSheetDialog(image,title,price,fileName,vipId);
    }

    private void showBottomSheetDialog(String image, String title, String price, String fileName, String vipId) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_vip);

        MaterialButton button_one_week = bottomSheetDialog.findViewById(R.id.button_one_week);
        MaterialButton button_one_month = bottomSheetDialog.findViewById(R.id.button_one_month);
        MaterialButton button_six_month = bottomSheetDialog.findViewById(R.id.button_six_month);

        MaterialButtonToggleGroup toggleGroup = bottomSheetDialog.findViewById(R.id.toggleGroup);
        ImageView giftImage = bottomSheetDialog.findViewById(R.id.iv_gift_image);
        TextView giftName = bottomSheetDialog.findViewById(R.id.txt_gift_name);
        TextView uid = bottomSheetDialog.findViewById(R.id.txt_UID);
        TextView txt_price = bottomSheetDialog.findViewById(R.id.txt_price);
        MaterialButton topUp = bottomSheetDialog.findViewById(R.id.btn_topup);
        Long totalCoin = Long.parseLong(totalCoins);
        Long giftPrice = Long.parseLong(price);

        if (totalCoin>=giftPrice){
            topUp.setText("Continue");
        }else {
            topUp.setText("Insufficient balance. Top-up now!");
        }

        topUp.setOnClickListener(view -> {

            if (totalCoin>=giftPrice){
                progressDialog.show();
                purchaseGiftForSixWeeks(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID),vipId,fileName,price,title,bottomSheetDialog);
                updateUserCoins(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID),totalCoins,price);
            }else {
                startActivity(new Intent(VIPActivity.this, TopUpActivity.class));

            }
        });
        if(!Objects.equals(image, "")){
            assert giftImage != null;
            Glide.with(this).load(image).into(giftImage);
        }
        else {
            assert giftImage != null;
            Glide.with(this).load(Constant.USER_PLACEHOLDER_PATH).into(giftImage);
        }

        assert giftName != null;
        giftName.setText(title);
        assert uid != null;
        uid.setText(String.valueOf(userDetailsModel.getUid()));
        txt_price.setText(new Convert().prettyCount(Integer.parseInt(price)));



        assert toggleGroup != null;
        assert button_one_week != null;
        toggleGroup.check(button_one_week.getId());
        button_one_week.setBackgroundColor(getResources().getColor(R.color.pink_top));
        button_one_week.setTextColor(getResources().getColor(R.color.white));
        button_one_week.setStrokeColorResource(R.color.pink_top);
        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    MaterialButton checkedButton = group.findViewById(checkedId);
                    String title = checkedButton.getText().toString();
                    checkedButton.setBackgroundColor(getResources().getColor(R.color.pink_top));
                    checkedButton.setTextColor(getResources().getColor(R.color.white));
                    checkedButton.setStrokeColorResource(R.color.pink_top);

                    if (title.equals("1 week")){
                        txt_price.setText(new Convert().prettyCount(Integer.parseInt(price)));
                    }
//                    else  if (title.equals("1 month")){
//                        try {
//                            try {
//                                // Attempt to parse the string to an integer
//                                int i = Integer.parseInt(price);
//                                int finalPrice = i * 3;
//                                txt_price.setText(String.valueOf(finalPrice));
//                            } catch (NumberFormatException e) {
//                                e.printStackTrace(); // Log the exception or handle it in an appropriate way
//                            }
//                        }catch (Exception e){
//
//                        }
//                    }
//                    else  if (title.equals("6 months")){
//                        try {
//                            // Attempt to parse the string to an integer
//                            int i = Integer.parseInt(price);
//
//                            // Multiply the integer value by 3
//                            int finalPrice = i * 12;
//
//                            // Convert the final price to a string and set it in the txt_price view
//                            txt_price.setText(String.valueOf(finalPrice));
//                        } catch (NumberFormatException e) {
//                            // Handle the case where the string cannot be parsed as an integer
//                            e.printStackTrace(); // Log the exception or handle it in an appropriate way
//                        }
//
//                    }

                }
                else {
                    MaterialButton checkedButton = group.findViewById(checkedId);
//                    Log.i("idddddddd", "onButtonChecked: else "+checkedId);
                    // Change the background color of the selected button
                    checkedButton.setBackgroundColor(getResources().getColor(R.color.white));
                    checkedButton.setTextColor(getResources().getColor(R.color.gray));
//                    checkedButton.setTextSize(R.dimen._14sp);

                }
            }
        });

        bottomSheetDialog.show();
    }
}