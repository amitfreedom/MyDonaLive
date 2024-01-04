package com.stream.prettylive.ui.home.ui.explore.adapter;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.bumptech.glide.Glide;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.Query;
        import com.stream.prettylive.databinding.ItemActiveUserBinding;
        import com.stream.prettylive.databinding.ItemRestaurantBinding;
        import com.stream.prettylive.ui.home.ui.home.adapter.FirestoreAdapter;
        import com.stream.prettylive.ui.home.ui.home.models.LiveUser;
        import com.stream.prettylive.ui.utill.Constant;

        import java.util.Objects;

public class ExploreAdapter extends FirestoreAdapter<ExploreAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public interface OnActiveUserSelectedListener {

        void onActiveUserSelected(DocumentSnapshot user);

    }

    private ExploreAdapter.OnActiveUserSelectedListener mListener;


    public ExploreAdapter(Query query, ExploreAdapter.OnActiveUserSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ExploreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExploreAdapter.ViewHolder(ItemActiveUserBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(ExploreAdapter.ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemActiveUserBinding binding;

        public ViewHolder(ItemActiveUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final ExploreAdapter.OnActiveUserSelectedListener listener) {

            LiveUser restaurant = snapshot.toObject(LiveUser.class);

            if (Objects.equals(restaurant.getLiveType(), "0")){
                binding.liveType.setText("Video Live");
            }

            if (Objects.equals(restaurant.getLiveType(), "1")){
                binding.liveType.setText("Audio Party");
            }

            if (Objects.equals(restaurant.getPhoto(), "")){
                // Load image
                Glide.with(binding.restaurantItemImage.getContext())
                        .load(Constant.USER_PLACEHOLDER_PATH)
                        .into(binding.restaurantItemImage);
            }else {
                // Load image
                Glide.with(binding.restaurantItemImage.getContext())
                        .load(restaurant.getPhoto())
                        .into(binding.restaurantItemImage);
            }

            binding.restaurantItemName.setText(restaurant.getUsername());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onActiveUserSelected(snapshot);
                    }
                }
            });
        }

    }
}

