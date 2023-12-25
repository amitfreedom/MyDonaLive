package com.stream.donalive.ui.vip;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.stream.donalive.R;
import com.stream.donalive.databinding.ActivityVipactivityBinding;
import com.stream.donalive.global.AppConstants;
import com.stream.donalive.ui.utill.Constant;
import com.stream.donalive.ui.vip.adapter.VipGiftAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class VIPActivity extends AppCompatActivity implements VipGiftAdapter.OnVipSelectedListener {

    private ActivityVipactivityBinding binding;
    private VipGiftAdapter mAdapter;
    private Query mQuery;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVipactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        buttonSelect();

        setAdapter();
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
                .limit(30);
        mAdapter = new VipGiftAdapter(mQuery, this) {
            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
//                    binding.recyclerRestaurants.setVisibility(View.GONE);
//                    binding.viewEmpty.setVisibility(View.VISIBLE);
                } else {
//                    binding.recyclerRestaurants.setVisibility(View.VISIBLE);
//                    binding.viewEmpty.setVisibility(View.GONE);
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
        AddVip();
    }

    private void AddVip() {
        long timestamp = System.currentTimeMillis();
        Map<String, Object> vipMap = new HashMap<>();
        vipMap.put("vipId", "");
        vipMap.put("title", "");
        vipMap.put("image", "");
        vipMap.put("beans", "");

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
        showBottomSheetDialog(image,title,price);
    }

    private void showBottomSheetDialog(String image, String title, String price) {

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


        topUp.setOnClickListener(view -> {
            startActivity(new Intent(VIPActivity.this, TopUpActivity.class));
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
        txt_price.setText(price);



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
                        txt_price.setText(price);
                    }else  if (title.equals("1 month")){
                        try {
                            try {
                                // Attempt to parse the string to an integer
                                int i = Integer.parseInt(price);
                                int finalPrice = i * 3;
                                txt_price.setText(String.valueOf(finalPrice));
                            } catch (NumberFormatException e) {
                                e.printStackTrace(); // Log the exception or handle it in an appropriate way
                            }
                        }catch (Exception e){

                        }
                    }
                    else  if (title.equals("6 months")){
                        try {
                            // Attempt to parse the string to an integer
                            int i = Integer.parseInt(price);

                            // Multiply the integer value by 3
                            int finalPrice = i * 12;

                            // Convert the final price to a string and set it in the txt_price view
                            txt_price.setText(String.valueOf(finalPrice));
                        } catch (NumberFormatException e) {
                            // Handle the case where the string cannot be parsed as an integer
                            e.printStackTrace(); // Log the exception or handle it in an appropriate way
                        }

                    }

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

//        LinearLayout copy = bottomSheetDialog.findViewById(R.id.copyLinearLayout);
//        LinearLayout share = bottomSheetDialog.findViewById(R.id.shareLinearLayout);
//        LinearLayout upload = bottomSheetDialog.findViewById(R.id.uploadLinearLayout);
//        LinearLayout download = bottomSheetDialog.findViewById(R.id.download);
//        LinearLayout delete = bottomSheetDialog.findViewById(R.id.delete);

        bottomSheetDialog.show();
    }
}