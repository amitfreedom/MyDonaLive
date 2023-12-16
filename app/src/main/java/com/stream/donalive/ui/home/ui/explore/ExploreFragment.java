package com.stream.donalive.ui.home.ui.explore;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.stream.donalive.R;
import com.stream.donalive.databinding.FragmentExploreBinding;
import com.stream.donalive.databinding.FragmentLiveBinding;
import com.stream.donalive.streaming.activity.LiveAudioRoomActivity;
import com.stream.donalive.streaming.activity.LiveStreamingActivity;
import com.stream.donalive.streaming.internal.ZEGOCallInvitationManager;
import com.stream.donalive.streaming.internal.ZEGOLiveStreamingManager;
import com.stream.donalive.streaming.internal.sdk.ZEGOSDKManager;
import com.stream.donalive.ui.home.ui.explore.adapter.CountryAdapter;
import com.stream.donalive.ui.home.ui.explore.adapter.ExploreAdapter;
import com.stream.donalive.ui.home.ui.explore.models.CountryModel;
import com.stream.donalive.ui.home.ui.home.adapter.ActiveUserAdapter;
import com.stream.donalive.ui.search.activity.SearchUserActivity;
import com.stream.donalive.ui.utill.Constant;

import java.util.ArrayList;
import java.util.Objects;

public class ExploreFragment extends Fragment implements ExploreAdapter.OnActiveUserSelectedListener {

    private static final int LIMIT = 50;
    private FragmentExploreBinding binding;
    private CountryAdapter adapter;
    private ArrayList<CountryModel> countryList;
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private ExploreAdapter mAdapter;

    private String countryNme="Global";
    private String countryImage="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTt6PpzPvDn1dMtgc-FQ-l89Rst-nJIy08iOg&usqp=CAU";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(inflater, container, false);

        // if LiveStreaming,init after user login,may receive pk request.
        ZEGOLiveStreamingManager.getInstance().init();
        // if Call invitation,init after user login,may receive call request.
        ZEGOCallInvitationManager.getInstance().init();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseFirestore.setLoggingEnabled(true);

        // Firestore
        mFirestore = FirebaseFirestore.getInstance();

        if (Objects.equals(countryNme, "Global")){
            mQuery = mFirestore.collection(Constant.LIVE_DETAILS)
                    .orderBy("startTime", Query.Direction.DESCENDING)
//                    .whereNotEqualTo("country","")
                    .limit(LIMIT);
//
//            mAdapter.setQuery(mQuery);

        }

        // RecyclerView
        mAdapter = new ExploreAdapter(mQuery, this) {
            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    binding.recyclerRestaurants.setVisibility(View.GONE);
                    binding.viewEmpty.setVisibility(View.VISIBLE);
                } else {
                    binding.recyclerRestaurants.setVisibility(View.VISIBLE);
                    binding.viewEmpty.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Log.e("FirebaseFirestoreException", "onError: "+e );
            }


        };
        binding.recyclerRestaurants.setAdapter(mAdapter);

        Glide.with(getActivity()).load(countryImage).into(binding.ivFlag);
        binding.txtCountryTitle.setText(countryNme);
        binding.icFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });

        binding.searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getActivity().getApplication(), SearchUserActivity.class);
                startActivity(intent);
            }
        });

    }




    private void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout_country);

        RecyclerView recyclerView = bottomSheetDialog.findViewById(R.id.recycler_country);
        // Create a list of country names (Replace this with your actual list)
        countryList = new ArrayList<>();
        countryList.add(new CountryModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTt6PpzPvDn1dMtgc-FQ-l89Rst-nJIy08iOg&usqp=CAU","Global"));
        countryList.add(new CountryModel("https://www.worldatlas.com/r/w425/img/flag/bd-flag.jpg","Bangladesh"));
        countryList.add(new CountryModel("https://www.worldatlas.com/r/w425/img/flag/af-flag.jpg","Afghanistan"));
        countryList.add(new CountryModel("https://www.worldatlas.com/r/w425/img/flag/kw-flag.jpg","Kuwait"));
        countryList.add(new CountryModel("https://www.worldatlas.com/r/w425/img/flag/qa-flag.jpg","Qatar"));
        countryList.add(new CountryModel("https://www.worldatlas.com/r/w425/img/flag/au-flag.jpg","Australia"));
        countryList.add(new CountryModel("https://www.worldatlas.com/r/w425/img/flag/ir-flag.jpg","Iran"));
        countryList.add(new CountryModel("https://www.worldatlas.com/r/w425/img/flag/in-flag.jpg","India"));
        countryList.add(new CountryModel("https://www.worldatlas.com/r/w425/img/flag/tr-flag.jpg","Turkey"));
        countryList.add(new CountryModel("https://www.worldatlas.com/r/w425/img/flag/uk-flag.jpg","United Kingdom"));

//        countryList.add("Country 2");

        adapter = new CountryAdapter(getActivity(), countryList, new CountryAdapter.Select() {
            @Override
            public void select(String name,String url) {
                Glide.with(getActivity()).load(url).into(binding.ivFlag);
                binding.txtCountryTitle.setText(name);
                if (Objects.equals(name, "Global")){
                    mQuery = mFirestore.collection(Constant.LIVE_DETAILS)
                            .orderBy("startTime", Query.Direction.DESCENDING)
                            .whereNotEqualTo("country",name)
                            .limit(LIMIT);

                    mAdapter.setQuery(mQuery);

                }else {
                    mQuery = mFirestore.collection(Constant.LIVE_DETAILS)
                            .orderBy("startTime", Query.Direction.DESCENDING)
                            .whereEqualTo("country",name)
                            .limit(LIMIT);

                    mAdapter.setQuery(mQuery);
                }
            }
        });
        recyclerView.setAdapter(adapter);

        bottomSheetDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
        binding=null;
    }


    @Override
    public void onActiveUserSelected(DocumentSnapshot user) {
        Log.i("test12345", "onActiveUserSelected: "+user.get("userId"));
        String liveType= (String) user.get("liveType");
        String liveID= (String) user.get("liveID");
        String userId= (String) user.get("userId");
        String username= (String) user.get("username");
        long uid= (long) user.get("uid");
        if (TextUtils.isEmpty(liveID)) {
            return;
        }
        Intent intent;
        if (Objects.equals(liveType, "0")){
            intent = new Intent(getActivity().getApplication(), LiveStreamingActivity.class);
        }else {
            intent = new Intent(getActivity().getApplication(), LiveAudioRoomActivity.class);

        }
        intent.putExtra("host", false);
        intent.putExtra("liveID", liveID);
        intent.putExtra("userId", userId);
        intent.putExtra("username", username);
        intent.putExtra("uid", uid);
        intent.putExtra("country_name", "");
        startActivity(intent);


    }
}