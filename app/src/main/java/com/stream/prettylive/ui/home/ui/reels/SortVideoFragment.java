package com.stream.prettylive.ui.home.ui.reels;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.stream.prettylive.R;
import com.stream.prettylive.databinding.FragmentSortVideoBinding;
import com.stream.prettylive.ui.home.ui.reels.adapter.VideoAdapter;
import com.stream.prettylive.ui.home.ui.reels.models.VideoModel;

import java.util.ArrayList;
import java.util.List;

public class SortVideoFragment extends Fragment {

 private FragmentSortVideoBinding binding;
    private List<VideoModel> videoList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSortVideoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        VideoModel obj = new VideoModel("https://sample-videos.com/video321/mp4/720/big_buck_bunny_720p_1mb.mp4", "Title1", "This is basic desc");
        videoList.add(obj);

        VideoModel obj1 = new VideoModel("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4", "Title2", "This is basic desc");
        videoList.add(obj1);

        VideoModel obj11 = new VideoModel("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4", "Title3", "This is basic desc");
        videoList.add(obj11);

        VideoModel obj2 = new VideoModel("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4", "Title4", "This is basic desc");
        videoList.add(obj2);

        VideoModel obj3 = new VideoModel("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4", "Title5", "This is basic desc");
        videoList.add(obj3);
        VideoModel obj33 = new VideoModel("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4", "Title5", "This is basic desc");
        videoList.add(obj33);


        binding.viewPager.setAdapter(new VideoAdapter(requireActivity(),videoList));
    }
}