package com.stream.donalive.ui.home.ui.reels;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.stream.donalive.databinding.FragmentSortVideoBinding;

import java.util.ArrayList;
import java.util.List;

public class SortVideoFragment extends Fragment {

 private FragmentSortVideoBinding binding;
//    private VideoPagerAdapter videoPagerAdapter;

    private List<String>videoUrls = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSortVideoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
// Add some sample video URLs for demonstration
//        videoUrls.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
//        videoUrls.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
//        videoPagerAdapter = new VideoPagerAdapter(getActivity(), videoUrls);
//        binding.viewPager.setAdapter(videoPagerAdapter);


//        videoPagerAdapter = new VideoPagerAdapter(getActivity());
//
//        binding.viewPager.setAdapter(videoPagerAdapter);

        return root;
    }
}