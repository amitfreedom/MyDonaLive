package com.stream.prettylive.ui.home.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stream.prettylive.R;
import com.stream.prettylive.databinding.FragmentPopulerBinding;
import com.stream.prettylive.ui.common.GenerateUserId;
import com.stream.prettylive.ui.home.ui.home.adapter.ImageSliderAdapter;
import com.stream.prettylive.ui.home.ui.home.models.LiveUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PopulerFragment extends Fragment {

    private FragmentPopulerBinding binding;
    private FirebaseFirestore db;

    private String[] images = {"https://restream.io/blog/content/images/size/w2000/2023/06/how-to-stream-live-video-on-your-website.JPG","https://kingscourtbrampton.org/wp-content/uploads/2022/08/istockphoto-1306922705-612x612-1.jpg","https://wave.video/blog/wp-content/uploads/2021/10/Instagram-Live-Streaming-for-Business-How-to-Get-Started-1.jpg"}; // Replace with your image resource IDs
    private ImageSliderAdapter imageSliderAdapter;
//    private LiveUserAdapter liveUserAdapter;
    private List<LiveUser> itemList;
//    private NestedScrollView scrollView;
    private boolean isLoading = false;
    private int currentPage = 1; // Keeps track of the current page
    private int totalPages = 10; // Replace this with the total number of pages

    private CollectionReference usersRef;
    private ArrayList<DocumentSnapshot> mSnapshots = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPopulerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        itemList = new ArrayList<>();
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("live");
        imageSliderAdapter = new ImageSliderAdapter(getActivity(), images);
        binding.viewPager.setAdapter(imageSliderAdapter);

        addDotsIndicator(0);

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                addDotsIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        binding.icFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showBottomSheetDialog();
//                testUserEnter();
//                getAllId();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    // Use this userId to fetch user details from Firestore
                    fetchUserDetails("SK7FSe8F65Z9X9ADMuSmo6zbrv72");
                } else {
                    // User is not logged in
                }
            }
        });

//            liveUserAdapter = new LiveUserAdapter();
//            binding.rvLiveUser.setAdapter(liveUserAdapter);

            fetChData();

//            fetchWithScroll();




        return root;
    }

    private void fetchUserDetails(String userId) {
        CollectionReference usersRef = db.collection("test123");

        Query query = usersRef.whereEqualTo("userid", "SK7FSe8F65Z9X9ADMuSmo6zbrv72");

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        String userName = document.getString("name"); // Replace "name" with your field name
//                        String uid = document.getString("uid"); // Replace "email" with your field name

                        // Use the retrieved user details as needed
                        Log.i("MainActivity", "User Name: " + userName);
                    }
                } else {
                    // Handle failure
                    Log.e("MainActivity", "Error getting user document: ", task.getException());
                }
            }
        });
    }

    private void getAllId() {
        GenerateUserId.getLastUserId(db, "test123", new GenerateUserId.UserIdCallback() {
            @Override
            public void onUserIdReceived(int userId) {
                Log.i("MainActivity", "Last user ID: " + userId);

                // Example: Get the next user ID (increment by 1)
                int nextUserId = userId + 1;
                Log.i("MainActivity", "Next user ID: " + nextUserId);

            }

            @Override
            public void onFailure(Exception e) {
                Log.i("MainActivity", "Exception: " + e);
            }
        });
//        db.collection("test123")
//                .orderBy("uid", Query.Direction.DESCENDING) // Assuming "userId" is the field name for user IDs
//                .limit(1)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        // If there are existing users, get the last user ID and increment by 1
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            if (document.contains("uid")) {
//                                int lastUserId = document.getLong("uid").intValue();
//                                Log.i("check_uid", "onComplete: uid ="+lastUserId);
////                                newUserId = lastUserId + 1;
//                            }
//                        }
//                    }
//                });
    }

    private void testUserEnter() {


//        Map<String, Object> user = new HashMap<>();
//        user.put("uid", 1000000);
//        user.put("name", "Amit");
//        db.collection("test123")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Toast.makeText(getActivity(), ""+documentReference.getId(), Toast.LENGTH_SHORT).show();
//                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getActivity(), "Error adding document"+e, Toast.LENGTH_SHORT).show();
//                        Log.w("TAG", "Error adding document", e);
//                    }
//                });
    }

    private void fetChData() {
        // Set up a listener for real-time updates

        usersRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("YourActivity", "Listen failed.", e);
                    return;
                }


                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {


                    for (DocumentChange change : queryDocumentSnapshots.getDocumentChanges()) {
                        int position = change.getOldIndex();
                        switch (change.getType()) {
                            case ADDED:
                                DocumentSnapshot addedUser = change.getDocument();
                                String live = addedUser.getString("live");
                                String userId = addedUser.getString("userId");
                                String roomId = addedUser.getString("roomId");
                                String userName = addedUser.getString("userName");

                                if (Objects.equals(live, "1")){
                                    // Create a User model instance and populate it
//                                    LiveUser user = new LiveUser(live,userId, roomId, userName);
//                                    itemList.add(user);
//                                    liveUserAdapter.setItems(itemList);
                                }

                                break;
                            case MODIFIED:

                                break;
                            case REMOVED:

                                break;
                        }
                    }
                } else {
                    Log.i("YourActivity", "No users found.");
                }
            }
        });
    }

    private void fetchWithScroll() {
        // Set a scroll change listener on the ScrollView
        binding.scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = binding.scrollView.getChildAt(binding.scrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (binding.scrollView.getHeight() + binding.scrollView.getScrollY()));

                if (diff == 0 && !isLoading && currentPage < totalPages) {
                    // Reached the bottom of ScrollView, load more data
                    isLoading = true;
                    loadMoreData();
                }
            }
        });
    }

    private void loadMoreData() {
        Toast.makeText(getActivity(), "Call load more", Toast.LENGTH_SHORT).show();
        // Perform your data loading here
        // Example: Fetch more data for the next page (currentPage + 1)
        // Once data is loaded, update your UI and reset isLoading flag
        // For example:
        // YourDataLoader.loadNextPage(currentPage + 1, new YourDataLoader.DataLoadListener() {
        //     @Override
        //     public void onDataLoaded(List<YourData> newData) {
        //         // Update UI with new data
                 isLoading = false;
                 currentPage++;
        //     }
        // });
    }


    private void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);

        LinearLayout copy = bottomSheetDialog.findViewById(R.id.copyLinearLayout);
        LinearLayout share = bottomSheetDialog.findViewById(R.id.shareLinearLayout);
        LinearLayout download = bottomSheetDialog.findViewById(R.id.download);
        LinearLayout delete = bottomSheetDialog.findViewById(R.id.delete);

        bottomSheetDialog.show();
//        bottomSheetDialog.dismiss();
    }

    private void addDotsIndicator(int position) {
        ImageView[] dots = new ImageView[images.length];
        binding.dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(getResources().getDrawable(
                    i == position ? R.drawable.dot_selected : R.drawable.dot_unselected
            ));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(8, 0, 8, 0);
            binding.dotsLayout.addView(dots[i], params);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
        mSnapshots.clear();
//        liveUserAdapter.notifyDataSetChanged();
    }
}