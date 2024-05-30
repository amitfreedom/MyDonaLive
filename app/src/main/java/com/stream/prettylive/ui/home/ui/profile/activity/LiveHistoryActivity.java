package com.stream.prettylive.ui.home.ui.profile.activity;

import static com.stream.prettylive.streaming.functions.Duration1.calculateDuration;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.stream.prettylive.databinding.ActivityLiveHistoryBinding;
import com.stream.prettylive.global.AppConstants;
import com.stream.prettylive.global.ApplicationClass;
import com.stream.prettylive.ui.activity.MainActivity;
import com.stream.prettylive.ui.home.ui.home.models.LiveUser;
import com.stream.prettylive.ui.home.ui.profile.LiveHistoryAdapter;
import com.stream.prettylive.ui.utill.Constant;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LiveHistoryActivity extends AppCompatActivity implements LiveHistoryAdapter.OnActiveUserSelectedListener {

    private ActivityLiveHistoryBinding binding;
    private static final int LIMIT = 50;
    private FirebaseFirestore mFirestore;
    private LiveHistoryAdapter liveHistoryAdapter;
    private Query mQuery;
    private String totalTime ="";
    private String totalDays ="";

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
//        getAllData(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));

        // Get current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateTime = currentDateTime.format(formatter);

        fetchUsersByDate("2024-05-15 10:10:10");
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

    private void fetchUsersByDate(String dateFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        String currentDate = LocalDateTime.now().format(formatter);

        Log.i("jhgsjfhgjkhsdf", "checkdate: "+currentDate);

        mFirestore.collection(Constant.LIVE_DETAILS)
                .orderBy("startDate", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(LiveHistoryActivity.this, "Error fetching users: " + e, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        List<LiveUser> userList = new ArrayList<>();
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            LiveUser user = document.toObject(LiveUser.class);
                            if (user != null) {
                                // Extract the date part from startDate and compare with currentDate
                                String userDate = LocalDateTime.parse(user.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).format(formatter);

                                Log.i("jhgsjfhgjkhsdf", "userDate: "+userDate+"   =>"+currentDate + user.getStartDate());
                                if (userDate.equals(currentDate)) {
                                    userList.add(user);
                                }
                            }
                        }

                        // Handle the user list (e.g., display in UI)
                        displayUserList(userList);
                    }
                });
    }


    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void displayUserList(List<LiveUser> userList) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            long totalSeconds = 0;
            long totalHours = 0;
            long totalMinutes = 0;

            for (LiveUser user : userList) {
                LocalDateTime startDateTime = LocalDateTime.parse(user.getStartDate(), formatter);
                LocalDateTime endDateTime = LocalDateTime.parse(user.getEndDate(), formatter);
                Duration duration = Duration.between(startDateTime, endDateTime);
                long userSeconds = duration.getSeconds();

                totalSeconds += userSeconds;
                totalMinutes += userSeconds / 60;
                totalHours += userSeconds / 3600;
            }

            // Calculate remaining hours, minutes, and seconds
            long days = totalSeconds / (24 * 3600);
            long hours = totalSeconds % (24 * 3600) / 3600;
            long minutes = (totalSeconds % 3600) / 60;
            long seconds = totalSeconds % 60;

            totalMinutes += minutes;
            totalHours += hours;

            totalTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            totalDays = String.format("%d days", days);
            binding.tvTotalDays.setText(totalDays);
            binding.tvTotalMin.setText("Total time : "+totalTime);

            Log.i("TotalTime", String.format("Total Hours: %d, Total Minutes: %d, Total Seconds: %d", totalHours, totalMinutes, totalSeconds));

            @SuppressLint("DefaultLocale")
            String totalTimeDifference = String.format("Total Difference: %d days, %02d hours, %02d minutes, %02d seconds", days, hours, minutes, seconds);
            Log.i("TotalTimeDifference", totalTimeDifference);

            for (LiveUser user : userList) {
                if (user.getStartDate() != null && user.getEndDate() != null) {
                    String userTimeDifference = calculateDateDifference(user.getStartDate(), user.getEndDate());

                }
            }
        }catch (Exception e){

        }
    }

    @SuppressLint("DefaultLocale")
    private String calculateDateDifference(String startDate, String endDate) {
       try {
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
           LocalDateTime startDateTime = LocalDateTime.parse(startDate, formatter);
           LocalDateTime endDateTime = LocalDateTime.parse(endDate, formatter);

           Duration duration = Duration.between(startDateTime, endDateTime);
           long totalSeconds = duration.getSeconds();

           long hours = totalSeconds / 3600;
           long minutes = (totalSeconds % 3600) / 60;
           long seconds = totalSeconds % 60;

           return String.format("%02d:%02d:%02d", hours, minutes, seconds);
       }catch (Exception e){
           return "";
       }
    }

    }