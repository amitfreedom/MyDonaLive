package com.stream.donalive.streaming.internal.sdk.components.effect;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.AbsoluteCornerSize;
import com.google.android.material.shape.RelativeCornerSize;
import com.google.android.material.shape.RoundedCornerTreatment;
import com.stream.donalive.R;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyAbility;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyGroup;
import com.stream.donalive.streaming.internal.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class BeautyAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<BeautyItem> beautyItems = new ArrayList<>();
    private int selectedItem = 0;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.beauty_item_ability, null, false);
        DisplayMetrics displayMetrics = parent.getContext().getResources().getDisplayMetrics();
        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, displayMetrics);
        LayoutParams layoutParams = new LayoutParams((int) width, LayoutParams.WRAP_CONTENT);
        inflate.setLayoutParams(layoutParams);
        return new ViewHolder(inflate) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShapeableImageView icon = holder.itemView.findViewById(R.id.item_beauty_icon);
        TextView name = holder.itemView.findViewById(R.id.item_beauty_name);

        BeautyItem beautyItem = beautyItems.get(position);
        name.setText(beautyItem.name);
        icon.setImageDrawable(beautyItem.drawable);

        icon.setClipToOutline(true);
        if (position != 0) {
            boolean selected = selectedItem == position;
            name.setSelected(selected);
            BeautyAbility beautyAbility = beautyItem.beautyAbility;

            if (beautyAbility.getBeautyGroup() == BeautyGroup.FILTERS
                || beautyAbility.getBeautyGroup() == BeautyGroup.STYLE_MAKEUP
                || beautyAbility.getBeautyGroup() == BeautyGroup.STICKERS
                || beautyAbility.getBeautyGroup() == BeautyGroup.BACKGROUND) {
                int cornerSize = Utils.dp2px(4, icon.getResources().getDisplayMetrics());
                icon.setShapeAppearanceModel(
                    icon.getShapeAppearanceModel().toBuilder().setAllCornerSizes(new AbsoluteCornerSize(cornerSize))
                        .setAllCorners(new RoundedCornerTreatment()).build());
            } else {
                icon.setShapeAppearanceModel(
                    icon.getShapeAppearanceModel().toBuilder().setAllCornerSizes(new RelativeCornerSize(0.5f))
                        .setAllCorners(new RoundedCornerTreatment()).build());
            }
            if (selected) {
                int strokeWidth = Utils.dp2px(4, icon.getResources().getDisplayMetrics());
                icon.setStrokeWidth(strokeWidth);
            } else {
                icon.setStrokeWidth(0);
            }

        } else {
            name.setSelected(false);
            icon.setStrokeWidth(0);
        }
    }

    @Override
    public int getItemCount() {
        return beautyItems.size();
    }

    public BeautyItem getBeautyItem(int position) {
        if (position < 0 || position >= beautyItems.size()) {
            return null;
        }
        return beautyItems.get(position);
    }

    public void setBeautyItems(List<BeautyItem> items) {
        beautyItems.clear();
        beautyItems.addAll(items);
        notifyDataSetChanged();
    }

    public int getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
        notifyDataSetChanged();
    }
}
