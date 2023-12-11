package com.stream.donalive.ui.home.ui.live;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.stream.donalive.databinding.FragmentLiveBinding;
import com.stream.donalive.global.ApplicationClass;


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

        ApplicationClass.getSharedpref().saveString("name123","amitkumar");

    }

    @Override
    public void onResume() {
        super.onResume();
//        ApplicationClass.getSharedpref().getString("name123");
//        Toast.makeText(getActivity(), ApplicationClass.getSharedpref().getString("name123"), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }
}