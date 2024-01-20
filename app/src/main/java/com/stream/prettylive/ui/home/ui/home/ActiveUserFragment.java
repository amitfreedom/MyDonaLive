package com.stream.prettylive.ui.home.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.stream.prettylive.R;
import com.stream.prettylive.databinding.FragmentActiveUserBinding;
import com.stream.prettylive.databinding.FragmentMainBinding;
import com.stream.prettylive.global.AppConstants;
import com.stream.prettylive.global.ApplicationClass;
import com.stream.prettylive.streaming.activity.LiveAudioRoomActivity;
import com.stream.prettylive.streaming.activity.LiveStreamingActivity;
import com.stream.prettylive.streaming.functions.KickOutInfo;
import com.stream.prettylive.streaming.internal.ZEGOCallInvitationManager;
import com.stream.prettylive.streaming.internal.ZEGOLiveStreamingManager;
import com.stream.prettylive.ui.activity.MainActivity;
import com.stream.prettylive.ui.home.ui.home.adapter.ActiveUserAdapter;
import com.stream.prettylive.ui.home.ui.home.adapter.ImageSliderAdapter;
import com.stream.prettylive.ui.home.ui.home.models.LiveUser;
import com.stream.prettylive.ui.utill.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ActiveUserFragment extends Fragment implements ActiveUserAdapter.OnActiveUserSelectedListener {
    private FragmentActiveUserBinding binding;
    private static final int LIMIT = 50;
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private ActiveUserAdapter mAdapter;
    private String[] images = {"https://restream.io/blog/content/images/size/w2000/2023/06/how-to-stream-live-video-on-your-website.JPG","https://kingscourtbrampton.org/wp-content/uploads/2022/08/istockphoto-1306922705-612x612-1.jpg","https://wave.video/blog/wp-content/uploads/2021/10/Instagram-Live-Streaming-for-Business-How-to-Get-Started-1.jpg"}; // Replace with your image resource IDs
    private ImageSliderAdapter imageSliderAdapter;
//    private LiveUserAdapter liveUserAdapter;
    private List<LiveUser> itemList;
    //    private NestedScrollView scrollView;
    private boolean isLoading = false;
    private int currentPage = 1; // Keeps track of the current page
    private int totalPages = 10; // Replace this with the total number of pages
    Animation topAnimantion,bottomAnimation,middleAnimation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentActiveUserBinding.inflate(inflater, container, false);
//        bottomAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.top_animantion);
//        binding.recyclerRestaurants.setAnimation(bottomAnimation);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFirestore = FirebaseFirestore.getInstance();

        mQuery = mFirestore.collection(Constant.LIVE_DETAILS)
                .orderBy("startTime", Query.Direction.DESCENDING)
                .whereEqualTo("liveStatus","online")

//                .whereNotEqualTo("userId",ApplicationClass.getSharedpref().getString(AppConstants.USER_ID))
                .limit(LIMIT);

        imageSliderAdapter = new ImageSliderAdapter(getActivity(), images);
        binding.viewPager.setAdapter(imageSliderAdapter);

        addDotsIndicator(0);

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                addDotsIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });



        // RecyclerView
        mAdapter = new ActiveUserAdapter(mQuery, this) {
            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    binding.recyclerRestaurants.setVisibility(View.GONE);
                    binding.viewEmpty.setVisibility(View.VISIBLE);
                } else {
                    binding.recyclerRestaurants.setVisibility(View.VISIBLE);
                    binding.viewEmpty.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Log.e("FirebaseFirestoreException", "onError: "+e );
                // Show a snackbar on errors
//                Snackbar.make(binding.getRoot(),
//                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }


        };
//        binding.recyclerRestaurants.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerRestaurants.setAdapter(mAdapter);

        // if LiveStreaming,init after user login,may receive pk request.
        ZEGOLiveStreamingManager.getInstance().init();
        // if Call invitation,init after user login,may receive call request.
        ZEGOCallInvitationManager.getInstance().init();

        binding.extendedFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), MainActivity.class);
                startActivity(intent);
            }
        });

        updateLiveStatus(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));

    }

    private void checkKickOut(String liveId, String userId,DocumentSnapshot user) {
        new KickOutInfo(new KickOutInfo.Select() {
            @Override
            public void KickOutStatus(int kickValue) {
                if (kickValue==1){
                    Toast.makeText(getActivity(), "The host has kicked out you for this live. You can not enter this live, ", Toast.LENGTH_SHORT).show();
                }else {

                    goToLive(user);

                }

            }
        }).checkKickOut(liveId,userId);

    }

    private void goToLive(DocumentSnapshot user) {
        String liveType= (String) user.get("liveType");
        String liveID= (String) user.get("liveID");
        String userId= (String) user.get("userId");
        String username= (String) user.get("username");
        long uid= (long) user.get("uid");
        if (TextUtils.isEmpty(liveID)) {
            return;
        }
        List<String> permissions = Arrays.asList(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO);
        requestPermissionIfNeeded(permissions, new RequestCallback() {
            @Override
            public void onResult(boolean allGranted, @NonNull List<String> grantedList,
                                 @NonNull List<String> deniedList) {
                if (allGranted) {
                    Intent intent;
                    if (Objects.equals(liveType, "0")){
                        intent = new Intent(getActivity().getApplication(), LiveStreamingActivity.class);
                        intent.putExtra("host", false);
                        intent.putExtra("liveID", liveID);
                        intent.putExtra("userId", userId);
                        intent.putExtra("audienceId", ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));
                        intent.putExtra("username", username);
                        intent.putExtra("uid", uid);
                        intent.putExtra("country_name", "");
                        startActivity(intent);
                    }else {
                        intent = new Intent(getActivity().getApplication(), LiveAudioRoomActivity.class);
                        intent.putExtra("host", false);
                        intent.putExtra("liveID", liveID);
                        intent.putExtra("userId", userId);
                        intent.putExtra("username", username);
                        intent.putExtra("audienceId", ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));
                        intent.putExtra("uid", uid);
                        intent.putExtra("country_name", "");
                        startActivity(intent);

                    }
                }
            }
        });

    }

    private void updateLiveStatus(String userId) {
        // Reference to the Firestore collection
        CollectionReference liveDetailsRef = FirebaseFirestore.getInstance().collection(Constant.LIVE_DETAILS);

        // Create a query to find the document with the given userId
        Query query = liveDetailsRef.whereEqualTo("userId", userId);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get the document ID for the matched document
                    String documentId = document.getId();
                    String liveStatus = document.getString("liveStatus");

                    long timestamp = System.currentTimeMillis();
                    Map<String, Object> updateDetails = new HashMap<>();
                    updateDetails.put("liveStatus", "offline");
                    updateDetails.put("endTime", timestamp);

                    // Update the liveType field from 0 to 1
                    liveDetailsRef.document(documentId)
                            .update(updateDetails)
                            .addOnSuccessListener(aVoid -> {
                                Log.i("UpdateLiveType", "liveType updated successfully for user with ID: " + userId);
                            })
                            .addOnFailureListener(e -> {
                                Log.e("UpdateLiveType", "Error updating liveType for user with ID: " + userId, e);
                            });
                }
            } else {
                Log.e("UpdateLiveType", "Error getting documents: ", task.getException());
            }
        });
    }

    private void addDotsIndicator(int position) {
        ImageView[] dots = new ImageView[images.length];
        binding.dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(getResources().getDrawable(
                    i == position ? R.drawable.dot_selected : R.drawable.dot_unselected
            ));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(8, 0, 8, 0);
            binding.dotsLayout.addView(dots[i], params);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Start listening for Firestore updates
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
    public void onDestroyView() {
        super.onDestroyView();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
        binding=null;
    }


    @Override
    public void onActiveUserSelected(DocumentSnapshot user) {
        Log.i("test12345", "onActiveUserSelected: "+user.get("userId"));
        String liveType= (String) user.get("liveType");
        String liveID= (String) user.get("liveID");
        String userId= (String) user.get("userId");
        String username= (String) user.get("username");
        long uid= (long) user.get("uid");
        if (TextUtils.isEmpty(liveID)) {
            return;
        }

        checkKickOut(liveID,ApplicationClass.getSharedpref().getString(AppConstants.USER_ID),user);

//        List<String> permissions = Arrays.asList(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO);
//        requestPermissionIfNeeded(permissions, new RequestCallback() {
//            @Override
//            public void onResult(boolean allGranted, @NonNull List<String> grantedList,
//                                 @NonNull List<String> deniedList) {
//                if (allGranted) {
//                   Intent intent;
//                    if (Objects.equals(liveType, "0")){
//                        intent = new Intent(getActivity().getApplication(), LiveStreamingActivity.class);
//                        intent.putExtra("host", false);
//                        intent.putExtra("liveID", liveID);
//                        intent.putExtra("userId", userId);
//                        intent.putExtra("audienceId", ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));
//                        intent.putExtra("username", username);
//                        intent.putExtra("uid", uid);
//                        intent.putExtra("country_name", "");
//                        startActivity(intent);
//                    }else {
//                        intent = new Intent(getActivity().getApplication(), LiveAudioRoomActivity.class);
//                        intent.putExtra("host", false);
//                        intent.putExtra("liveID", liveID);
//                        intent.putExtra("userId", userId);
//                        intent.putExtra("username", username);
//                        intent.putExtra("audienceId", ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));
//                        intent.putExtra("uid", uid);
//                        intent.putExtra("country_name", "");
//                        startActivity(intent);
//
//                    }
//                }
//            }
//        });
    }

    private void requestPermissionIfNeeded(List<String> permissions, RequestCallback requestCallback) {
        boolean allGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
            }
        }
        if (allGranted) {
            requestCallback.onResult(true, permissions, new ArrayList<>());
            return;
        }

        PermissionX.init(this).permissions(permissions).onExplainRequestReason((scope, deniedList) -> {
            String message = "";
            if (permissions.size() == 1) {
                if (deniedList.contains(Manifest.permission.CAMERA)) {
                    message = this.getString(R.string.permission_explain_camera);
                } else if (deniedList.contains(Manifest.permission.RECORD_AUDIO)) {
                    message = this.getString(R.string.permission_explain_mic);
                }
            } else {
                if (deniedList.size() == 1) {
                    if (deniedList.contains(Manifest.permission.CAMERA)) {
                        message = this.getString(R.string.permission_explain_camera);
                    } else if (deniedList.contains(Manifest.permission.RECORD_AUDIO)) {
                        message = this.getString(R.string.permission_explain_mic);
                    }
                } else {
                    message = this.getString(R.string.permission_explain_camera_mic);
                }
            }
            scope.showRequestReasonDialog(deniedList, message, getString(R.string.ok));
        }).onForwardToSettings((scope, deniedList) -> {
            String message = "";
            if (permissions.size() == 1) {
                if (deniedList.contains(Manifest.permission.CAMERA)) {
                    message = this.getString(R.string.settings_camera);
                } else if (deniedList.contains(Manifest.permission.RECORD_AUDIO)) {
                    message = this.getString(R.string.settings_mic);
                }
            } else {
                if (deniedList.size() == 1) {
                    if (deniedList.contains(Manifest.permission.CAMERA)) {
                        message = this.getString(R.string.settings_camera);
                    } else if (deniedList.contains(Manifest.permission.RECORD_AUDIO)) {
                        message = this.getString(R.string.settings_mic);
                    }
                } else {
                    message = this.getString(R.string.settings_camera_mic);
                }
            }
            scope.showForwardToSettingsDialog(deniedList, message, getString(R.string.settings),
                    getString(R.string.cancel));
        }).request(new RequestCallback() {
            @Override
            public void onResult(boolean allGranted, @NonNull List<String> grantedList,
                                 @NonNull List<String> deniedList) {
                if (requestCallback != null) {
                    requestCallback.onResult(allGranted, grantedList, deniedList);
                }
            }
        });
    }
}