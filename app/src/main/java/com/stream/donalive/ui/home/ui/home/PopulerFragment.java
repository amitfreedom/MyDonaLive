package com.stream.donalive.ui.home.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.core.Query;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stream.donalive.databinding.FragmentPopulerBinding;

public class PopulerFragment extends Fragment {

    private FragmentPopulerBinding binding;
    private FirebaseFirestore db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPopulerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        binding.getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        return root;
    }
}