package com.stream.prettylive.ui.search.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.stream.prettylive.R;
import com.stream.prettylive.databinding.ActivityHomeBinding;
import com.stream.prettylive.databinding.ActivitySearchUserBinding;
import com.stream.prettylive.global.AppConstants;
import com.stream.prettylive.global.ApplicationClass;
import com.stream.prettylive.ui.follow.activity.UserInfoActivity;
import com.stream.prettylive.ui.home.ui.profile.models.UserDetailsModel;
import com.stream.prettylive.ui.search.adapter.SearchUserAdapter;

public class SearchUserActivity extends AppCompatActivity implements SearchUserAdapter.OnUserSelectedListener {

   private ActivitySearchUserBinding binding;
    private static final int LIMIT = 50;
    private FirebaseFirestore firestore;
    private CollectionReference usersRef;
    private FirebaseAuth mAuth;
    private UserDetailsModel userDetails;
    private Query mQuery;
    private SearchUserAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firestore = FirebaseFirestore.getInstance();

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);

        // Firestore
        firestore = FirebaseFirestore.getInstance();

        int searchLetter= 1;

        mQuery = firestore.collection("login_details")
                .orderBy("uid")
                .whereNotEqualTo("userId", ApplicationClass.getSharedpref().getString(AppConstants.USER_ID))
                .startAt(searchLetter)
                .endAt(searchLetter + "\uf8ff")
                .limit(LIMIT);


        mAdapter = new SearchUserAdapter(mQuery, this) {
            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    binding.recyclerUser.setVisibility(View.VISIBLE);
//                    binding.viewEmpty.setVisibility(View.VISIBLE);
                } else {
                    binding.recyclerUser.setVisibility(View.VISIBLE);
//                    binding.viewEmpty.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Log.e("FirebaseFirestoreException", "onError: "+e );
                // Show a snackbar on errors
//                Snackbar.make(binding.getRoot(),
//                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }


        };
        binding.recyclerUser.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerUser.setAdapter(mAdapter);

        binding.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.length()>0){
                    long searchLetter= Long.parseLong(s.toString());
//                    int searchLetter= Integer.parseInt(text);
                    Log.i("afterTextChanged", "afterTextChanged: "+searchLetter);
                    mQuery = firestore.collection("login_details")
                            .orderBy("uid")
                            .startAt(searchLetter)
                            .endAt(searchLetter + "\uf8ff")
                            .limit(LIMIT);

                    mAdapter.setQuery(mQuery);
                }else {
                    String searchLetter= "";
                    Log.i("afterTextChanged", "afterTextChanged: "+searchLetter);
                    mQuery = firestore.collection("login_details")
                            .orderBy("username")
                            .startAt(searchLetter)
                            .endAt(searchLetter + "\uf8ff")
                            .limit(LIMIT);

                    mAdapter.setQuery(mQuery);
                }



            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        // Start listening for Firestore updates
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
    protected void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
        binding=null;
    }

    @Override
    public void onUserSelected(DocumentSnapshot user) {
        String userId = user.getString("userId");
        String username = user.getString("username");
        String image = user.getString("image");
        String level = user.getString("level");
        String uid = String.valueOf(user.getLong("uid"));
        if (userId.isEmpty()){
            return;
        }
        Intent intent = new Intent(SearchUserActivity.this, UserInfoActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("username", username);
        intent.putExtra("image", image);
        intent.putExtra("uid", uid);
        intent.putExtra("level", level);
        startActivity(intent);
    }
}