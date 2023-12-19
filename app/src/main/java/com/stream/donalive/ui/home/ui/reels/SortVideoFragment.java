package com.stream.donalive.ui.home.ui.reels;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stream.donalive.R;
import com.stream.donalive.databinding.FragmentHomeBinding;
import com.stream.donalive.databinding.FragmentSortVideoBinding;

public class SortVideoFragment extends Fragment {

 private FragmentSortVideoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSortVideoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }
}