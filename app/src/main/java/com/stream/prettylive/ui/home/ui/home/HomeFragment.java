package com.stream.prettylive.ui.home.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stream.prettylive.databinding.FragmentHomeBinding;
import com.stream.prettylive.ui.home.ui.home.adapter.MyPagerAdapter;
import com.stream.prettylive.ui.search.activity.SearchUserActivity;
import com.stream.prettylive.ui.toplist.TopListActivity;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseFirestore firestore;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        firestore = FirebaseFirestore.getInstance();

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
//                    deleteUserFromViewersCollection("20k7E32T6T9iE606dA0i_1000001_main_host", "1233");
                }
            });
            binding.leftIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity().getApplication(), TopListActivity.class);
                    startActivity(intent);
                }
            });



        return root;
    }

    private void deleteUserFromViewersCollection(String streamId, String userId) {
        firestore.collection("room_users")
                .document(streamId)
                .collection("viewers")
                .document(userId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i("delete_user", "User deleted from viewers collection successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("delete_user", "Failed to delete user from viewers collection: " + e.getMessage());
                    }
                });
    }

    private void setupViewPager(ViewPager viewPager) {
        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
//        adapter.addFragment(new MainFragment(), "Fresher");
        adapter.addFragment(new ActiveUserFragment(), "Popular");
        adapter.addFragment(new ActiveUserFragment(), "Live");
        adapter.addFragment(new ActiveUserFragment(), "Audio live");
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