package com.stream.donalive.ui.home.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.core.Query;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stream.donalive.R;
import com.stream.donalive.databinding.FragmentPopulerBinding;
import com.stream.donalive.ui.home.ui.home.adapter.ImageSliderAdapter;

public class PopulerFragment extends Fragment {

    private FragmentPopulerBinding binding;
    private FirebaseFirestore db;

    private String[] images = {"https://restream.io/blog/content/images/size/w2000/2023/06/how-to-stream-live-video-on-your-website.JPG","https://kingscourtbrampton.org/wp-content/uploads/2022/08/istockphoto-1306922705-612x612-1.jpg","https://wave.video/blog/wp-content/uploads/2021/10/Instagram-Live-Streaming-for-Business-How-to-Get-Started-1.jpg"}; // Replace with your image resource IDs
    private ImageSliderAdapter imageSliderAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPopulerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
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

        binding.icFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();
            }
        });

        return root;
    }

    private void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);

        LinearLayout copy = bottomSheetDialog.findViewById(R.id.copyLinearLayout);
        LinearLayout share = bottomSheetDialog.findViewById(R.id.shareLinearLayout);
        LinearLayout download = bottomSheetDialog.findViewById(R.id.download);
        LinearLayout delete = bottomSheetDialog.findViewById(R.id.delete);

        bottomSheetDialog.show();
//        bottomSheetDialog.dismiss();
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
}