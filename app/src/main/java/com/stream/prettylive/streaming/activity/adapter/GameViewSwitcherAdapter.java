package com.stream.prettylive.streaming.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.stream.prettylive.R;
import com.stream.prettylive.streaming.components.message.barrage.RoundBackgroundColorSpan;
import com.stream.prettylive.streaming.internal.utils.Utils;
import com.stream.prettylive.ui.home.ui.profile.models.UserDetailsModel;

import java.util.ArrayList;
import java.util.List;
public class GameViewSwitcherAdapter extends PagerAdapter {
    private Context mContext;
    private List<UserDetailsModel> mImageUrls;
//    private List<UserDetailsModel>topGamer= new ArrayList();

    public GameViewSwitcherAdapter(Context context, List<UserDetailsModel> imageUrls) {
        mContext = context;
        mImageUrls = imageUrls;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_game_swicher_view, container, false);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ImageView imageView = view.findViewById(R.id.imageGameView);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView txtMessageView = view.findViewById(R.id.txtMessageView);


        try{
            String lv = "Lv"+mImageUrls.get(position).getLevel();
            DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
            RoundBackgroundColorSpan backgroundColorSpan = new RoundBackgroundColorSpan(mContext,
                    ContextCompat.getColor(mContext, R.color.pink_top),
                    ContextCompat.getColor(mContext, android.R.color.white));
            // Create a SpannableStringBuilder
            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append(lv);
            builder.append(" ");


            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(Utils.sp2px(10, displayMetrics));
            builder.setSpan(absoluteSizeSpan, 0, lv.length(),
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            builder.setSpan(backgroundColorSpan, 0, lv.length(),
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

            // Append the name with red color
            int nameStart = builder.length();
            builder.append(mImageUrls.get(position).getUsername());
            int nameEnd = builder.length();
            builder.setSpan(new ForegroundColorSpan(Color.parseColor("#fb4f90")), nameStart, nameEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// Append a space
            builder.append(" ");

// Append the message with white color
            int messageStart = builder.length();
            builder.append("win 500k coins");
            int messageEnd = builder.length();
            builder.setSpan(new ForegroundColorSpan(Color.WHITE), messageStart, messageEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


            txtMessageView.setText(builder);
            Glide.with(mContext).load("https://mir-s3-cdn-cf.behance.net/projects/404/53cf9a170521769.Y3JvcCwxMDI0LDgwMCwwLDE2Mg.png").into(imageView);
        }catch (Exception e){
            Log.i("kjkdg", "instantiateItem: ");
        }

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mImageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    // Call this method whenever the dataset is updated
    public void updateData(List<UserDetailsModel> newImageUrls) {
        mImageUrls = newImageUrls;
        notifyDataSetChanged();
    }
}
