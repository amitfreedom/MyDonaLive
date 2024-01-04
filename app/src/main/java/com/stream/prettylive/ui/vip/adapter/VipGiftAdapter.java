package com.stream.prettylive.ui.vip.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.stream.prettylive.databinding.ItemActiveUserBinding;
import com.stream.prettylive.databinding.SvgaListItemBinding;
import com.stream.prettylive.ui.home.ui.home.adapter.FirestoreAdapter;
import com.stream.prettylive.ui.utill.Constant;
import com.stream.prettylive.ui.utill.Convert;
import com.stream.prettylive.ui.vip.model.GiftModel;

import java.util.Objects;

public class VipGiftAdapter extends FirestoreAdapter<VipGiftAdapter.ViewHolder> {

    public interface OnVipSelectedListener {

        void OnVipSelected(DocumentSnapshot user);

    }

    private VipGiftAdapter.OnVipSelectedListener mListener;


    public VipGiftAdapter(Query query, VipGiftAdapter.OnVipSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public VipGiftAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VipGiftAdapter.ViewHolder(SvgaListItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(VipGiftAdapter.ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private SvgaListItemBinding binding;

        public ViewHolder(SvgaListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final VipGiftAdapter.OnVipSelectedListener listener) {

            GiftModel giftModel = snapshot.toObject(GiftModel.class);

            try{
                if (Objects.equals(giftModel.getImage(), "")){
                    // Load image
                    Glide.with(binding.vipImage.getContext())
                            .load(Constant.USER_PLACEHOLDER_PATH)
                            .into(binding.vipImage);
                }else {
                    // Load image
                    Glide.with(binding.vipImage.getContext())
                            .load(giftModel.getImage())
                            .into(binding.vipImage);
                }


                binding.title.setText(giftModel.getTitle());
                binding.txtBeans.setText(new Convert().prettyCount(Integer.parseInt(giftModel.getBeans())));
            }catch (Exception e){

            }

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.OnVipSelected(snapshot);
                    }
                }
            });
        }

    }
}
