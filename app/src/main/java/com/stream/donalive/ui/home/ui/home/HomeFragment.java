package com.stream.donalive.ui.home.ui.home;

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
import com.stream.donalive.ui.home.ui.home.adapter.MyPagerAdapter;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

            setupViewPager(binding.viewPager);

            binding.tabLayout.setupWithViewPager(binding.viewPager);


        return root;
    }

    private void setupViewPager(ViewPager viewPager) {
        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new PopulerFragment(), "Fresher");
        adapter.addFragment(new PopulerFragment(), "Popular");
        adapter.addFragment(new PopulerFragment(), "Live");
        adapter.addFragment(new PopulerFragment(), "Audio live");
        adapter.addFragment(new PopulerFragment(), "Pk battle");
        // Add more fragments as needed
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}