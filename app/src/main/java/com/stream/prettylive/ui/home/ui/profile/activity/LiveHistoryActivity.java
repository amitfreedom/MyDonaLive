package com.stream.prettylive.ui.home.ui.profile.activity;

import static com.stream.prettylive.streaming.functions.Duration.calculateDuration;

import static okhttp3.internal.concurrent.TaskLoggerKt.formatDuration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.stream.prettylive.databinding.ActivityLiveHistoryBinding;
import com.stream.prettylive.global.AppConstants;
import com.stream.prettylive.global.ApplicationClass;
import com.stream.prettylive.ui.home.ui.profile.LiveHistoryAdapter;
import com.stream.prettylive.ui.utill.Constant;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LiveHistoryActivity extends AppCompatActivity implements LiveHistoryAdapter.OnActiveUserSelectedListener {

    private ActivityLiveHistoryBinding binding;
    private static final int LIMIT = 50;
    private FirebaseFirestore mFirestore;
    private LiveHistoryAdapter liveHistoryAdapter;
    private Query mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLiveHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mFirestore = FirebaseFirestore.getInstance();

        binding.imgBack.setOnClickListener(view -> {
            onBackPressed();
        });

        getData(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));
        setCurrentDate();

        getAllData(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));
    }
long totalDurationMillis =0;
    private void getAllData(String userId) {

        // Get the date 15 days ago
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -29);
        Date fifteenDaysAgo = calendar.getTime();

// Get the current date
        Date currentDate = new Date();

// Convert currentDate to timestamp
        long timestamp = currentDate.getTime();

        mFirestore.collection(Constant.LIVE_DETAILS)
                .orderBy("startTime", Query.Direction.DESCENDING)
                .whereEqualTo("liveStatus","offline")
                .whereEqualTo("userId",userId)
                .limit(10)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Iterate through each document
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                // Access document data
//                                Map<String, Object> data = document.getData();
//                                // Process document data as needed
//                                // For example:
//                                String liveStatus = (String) data.get("liveStatus");
//                                String startTime = (String) data.get("startTime");
//                                String endTime = (String) data.get("liveStatus");
////                                String userId = (String) data.get("userId");
//                                // Process other fields...

                                long startTime = document.getLong("startTime");
                                long endTime = document.getLong("endTime");

                                long durationMillis = calculateDuration(startTime, endTime);
                                totalDurationMillis += durationMillis; // Add to total duration

                                Log.i("jhgsejfhgejhfg", "totalDurationMillis =>"+formatDuration(totalDurationMillis));

                            }
                        } else {
                            Log.i("jhgsejfhgejhfg", "onSuccess: ====>else");

                            // Handle case when there are no documents
                        }
                    }
                });
    }

    private void setCurrentDate() {
        binding.tvMonthdate.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
    }


    private static String formatDuration(long durationMillis) {
        // Convert durationMillis to a human-readable format
        // For example, convert milliseconds to hours:minutes:seconds format
        // You can implement this based on your requirements
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(durationMillis),
                TimeUnit.MILLISECONDS.toMinutes(durationMillis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(durationMillis) % TimeUnit.MINUTES.toSeconds(1));
    }
    private void getData(String userId) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date startDate = calendar.getTime();
        calendar.add(Calendar.DATE, -29);
        Date endDate = calendar.getTime();

        Timestamp tenDaysAgoTimestamp = new Timestamp(endDate);


        mFirestore = FirebaseFirestore.getInstance();

        mQuery = mFirestore.collection(Constant.LIVE_DETAILS)
                .orderBy("startTime", Query.Direction.DESCENDING)
                .whereEqualTo("liveStatus","offline")
                .whereEqualTo("userId",userId)
//                .whereEqualTo("startTime",  1713695780)
//                .whereLessThanOrEqualTo("startTime", 1711916228)
                .limit(LIMIT);

        // RecyclerView
        liveHistoryAdapter = new LiveHistoryAdapter(mQuery, this) {
            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    binding.rvLiveHistory.setVisibility(View.GONE);
//                    binding.viewEmpty.setVisibility(View.VISIBLE);
                } else {
                    binding.rvLiveHistory.setVisibility(View.VISIBLE);
//                    binding.viewEmpty.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Log.i("hvsdjhfhjsdfv", "onError: "+e.getMessage());
                Snackbar.make(binding.getRoot(),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }


        };
        binding.rvLiveHistory.setAdapter(liveHistoryAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (liveHistoryAdapter != null) {
            liveHistoryAdapter.stopListening();
        }
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Start listening for Firestore updates
        if (liveHistoryAdapter != null) {
            liveHistoryAdapter.startListening();
        }


    }

    @Override
    public void onStop() {
        super.onStop();
        if (liveHistoryAdapter != null) {
            liveHistoryAdapter.stopListening();
        }
    }


    @Override
    public void onActiveUserSelected(DocumentSnapshot user) {

    }
}