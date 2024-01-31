package com.stream.prettylive.ui.toplist.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.stream.prettylive.R;
import com.stream.prettylive.databinding.ActivityTopUsersSendReceiveCoinsBinding;
import com.stream.prettylive.global.AppConstants;
import com.stream.prettylive.global.ApplicationClass;
import com.stream.prettylive.ui.home.ui.profile.models.UserDetailsModel;
import com.stream.prettylive.ui.search.adapter.SearchUserAdapter;
import com.stream.prettylive.ui.toplist.adapter.TopUserAdapter;

public class TopUsersSendReceiveCoinsActivity extends AppCompatActivity implements TopUserAdapter.OnUserSelectedListener {
    private static final String TAG = "TopUsersSendReceiveCoin";
    private ActivityTopUsersSendReceiveCoinsBinding binding;
    private static final int LIMIT = 50;
    private FirebaseFirestore firestore;
    private CollectionReference usersRef;
    private FirebaseAuth mAuth;
    private UserDetailsModel userDetails;
    private Query mQuery;
    private TopUserAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityTopUsersSendReceiveCoinsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        FirebaseFirestore.setLoggingEnabled(true);

        mQuery = firestore.collection("login_details")
                .orderBy("receiveCoin", Query.Direction.DESCENDING)
                .whereNotEqualTo("userId", ApplicationClass.getSharedpref().getString(AppConstants.USER_ID))
                .limit(LIMIT);


        mAdapter = new TopUserAdapter(mQuery, this) {
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
        binding.recyclerUser.setAdapter(mAdapter);

//        mAdapter.setQuery(mQuery);

        getDada();
    }

    private void getDada() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

// Query to get user list sorted by "diamond" in descending order
        firestore.collection("login_details")
                .orderBy("receiveCoin", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                // Access data from the document
                                String diamond = document.getString("diamond");
                                String userId = document.getString("userId");
                                String username = document.getString("username");
                                String email = document.getString("email");
                                // ... (other fields)

                                Log.i("kjhdfjkhjkhdf", "Username: "+username+"\n"+"diamond :"+diamond);
//                                // Example: Print user details
//                                System.out.println("User ID: " + userId);
//                                System.out.println("Username: " + username);
//                                System.out.println("Email: " + email);
                                // ... (print other fields)
                            }
                        } else {
                            // Handle the case where the query snapshot is null
                        }
                    } else {
                        // Handle the error
                        Exception exception = task.getException();
                        if (exception != null) {
                            exception.printStackTrace();
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

    }
}