package com.stream.donalive.ui.home.ui.live;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stream.donalive.R;
import com.stream.donalive.databinding.FragmentHomeBinding;
import com.stream.donalive.databinding.FragmentLiveBinding;


public class LiveFragment extends Fragment {

    private FragmentLiveBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLiveBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.mainHostVideo.startPreviewOnly();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }
}