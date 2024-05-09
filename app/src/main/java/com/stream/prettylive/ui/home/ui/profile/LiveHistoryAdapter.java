package com.stream.prettylive.ui.home.ui.profile;

import static com.stream.prettylive.streaming.functions.Duration.calculateDuration;
import static com.stream.prettylive.streaming.functions.Duration.calculateDurationInMinutes;
import static com.stream.prettylive.streaming.functions.Duration.convertTimestampToDate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.stream.prettylive.databinding.ItemActiveUserBinding;
import com.stream.prettylive.databinding.ListLiveGiftHistoryBinding;
import com.stream.prettylive.ui.home.ui.home.adapter.ActiveUserAdapter;
import com.stream.prettylive.ui.home.ui.home.adapter.FirestoreAdapter;
import com.stream.prettylive.ui.home.ui.home.models.LiveUser;
import com.stream.prettylive.ui.utill.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class LiveHistoryAdapter extends FirestoreAdapter<LiveHistoryAdapter.ViewHolder> {
    static long totalDurationMillis = 0;

    private static String formatDuration(long durationMillis) {
        // Convert durationMillis to a human-readable format
        // For example, convert milliseconds to hours:minutes:seconds format
        // You can implement this based on your requirements
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(durationMillis),
                TimeUnit.MILLISECONDS.toMinutes(durationMillis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(durationMillis) % TimeUnit.MINUTES.toSeconds(1));
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
            String formattedDate = convertTimestampToDate(liveUser.getStartTime());

            binding.tvStartDateTime.setText(convertTimestampToDate(liveUser.getStartTime()));
            binding.tvEndDateTime.setText(convertTimestampToDate(liveUser.getEndTime()));
//            binding.tvDuration.setText((calculateDuration(liveUser.getStartTime(),liveUser.getEndTime())));
            long durationMillis = calculateDuration(liveUser.getStartTime(), liveUser.getEndTime());
            totalDurationMillis += durationMillis; // Add to total duration

            binding.tvDuration.setText(formatDuration(durationMillis));

//            Log.i("test234567", "bind: ==123=>"+formatDuration(durationMillis));
            Log.i("test234567", "bind: ===>"+formatDuration(totalDurationMillis));
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
