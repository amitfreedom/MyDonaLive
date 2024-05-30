package com.stream.prettylive.ui.home.ui.profile;

import static com.stream.prettylive.streaming.functions.Duration1.calculateDuration;
import static com.stream.prettylive.streaming.functions.Duration1.convertTimestampToDate;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.stream.prettylive.databinding.ListLiveGiftHistoryBinding;
import com.stream.prettylive.streaming.functions.Duration1;
import com.stream.prettylive.ui.home.ui.home.adapter.FirestoreAdapter;
import com.stream.prettylive.ui.home.ui.home.models.LiveUser;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class LiveHistoryAdapter extends FirestoreAdapter<LiveHistoryAdapter.ViewHolder> {
    @SuppressLint("DefaultLocale")
    private static String calculateDateDifference(String startDate, String endDate) {
        try{
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
            Log.i("kjghf", "calculateDateDifference: ");
            return "";
        }
    }

public interface OnActiveUserSelectedListener {

    void onActiveUserSelected(DocumentSnapshot user);

}

    private LiveHistoryAdapter.OnActiveUserSelectedListener mListener;


    public LiveHistoryAdapter(Query query, LiveHistoryAdapter.OnActiveUserSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public LiveHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LiveHistoryAdapter.ViewHolder(ListLiveGiftHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(LiveHistoryAdapter.ViewHolder holder, int position) {

        holder.bind(getSnapshot(position), mListener);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private ListLiveGiftHistoryBinding binding;

        public ViewHolder(ListLiveGiftHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final LiveHistoryAdapter.OnActiveUserSelectedListener listener) {

            LiveUser liveUser = snapshot.toObject(LiveUser.class);
            assert liveUser != null;

            binding.tvStartDateTime.setText(convertTimestampToDate(liveUser.getStartTime()));
            binding.tvEndDateTime.setText(convertTimestampToDate(liveUser.getEndTime()));
            if (liveUser.getStartDate() != null && liveUser.getEndDate() != null) {
                binding.tvDuration.setText(calculateDateDifference(liveUser.getStartDate(), liveUser.getEndDate()));
            }
            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View view) {
                    if (listener != null) {
//                        listener.onActiveUserSelected(snapshot);
                    }
                }
            });
        }

    }
}
