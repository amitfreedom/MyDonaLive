package com.stream.donalive.ui.home.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.stream.donalive.databinding.FragmentHomeBinding;
import com.stream.donalive.streaming.activity.LiveAudioRoomActivity;
import com.stream.donalive.streaming.activity.LiveStreamingActivity;
import com.stream.donalive.ui.activity.MainActivity;
import com.stream.donalive.ui.home.ui.home.adapter.MyPagerAdapter;
import com.stream.donalive.ui.search.activity.SearchUserActivity;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

            setupViewPager(binding.viewPager);

            binding.tabLayout.setupWithViewPager(binding.viewPager);

            binding.searchView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;

                    intent = new Intent(getActivity().getApplication(), SearchUserActivity.class);
                    startActivity(intent);
                }
            });
            binding.rightIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent;
//
//                    intent = new Intent(getActivity().getApplication(), MainActivity.class);
//                    startActivity(intent);
                }
            });


        return root;
    }

    private void setupViewPager(ViewPager viewPager) {
        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
//        adapter.addFragment(new MainFragment(), "Fresher");
        adapter.addFragment(new ActiveUserFragment(), "Popular");
//        adapter.addFragment(new PopulerFragment(), "Live");
//        adapter.addFragment(new PopulerFragment(), "Audio live");
//        adapter.addFragment(new PopulerFragment(), "Pk battle");
        // Add more fragments as needed
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }
}