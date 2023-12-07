package com.stream.donalive.ui.home.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stream.donalive.R;
import com.stream.donalive.databinding.FragmentPopulerBinding;
import com.stream.donalive.ui.home.ui.home.adapter.ImageSliderAdapter;
import com.stream.donalive.ui.home.ui.home.adapter.LiveUserAdapter;
import com.stream.donalive.ui.home.ui.home.models.LiveUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PopulerFragment extends Fragment {

    private FragmentPopulerBinding binding;
    private FirebaseFirestore db;

    private String[] images = {"https://restream.io/blog/content/images/size/w2000/2023/06/how-to-stream-live-video-on-your-website.JPG","https://kingscourtbrampton.org/wp-content/uploads/2022/08/istockphoto-1306922705-612x612-1.jpg","https://wave.video/blog/wp-content/uploads/2021/10/Instagram-Live-Streaming-for-Business-How-to-Get-Started-1.jpg"}; // Replace with your image resource IDs
    private ImageSliderAdapter imageSliderAdapter;
    private LiveUserAdapter liveUserAdapter;
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
//                itemList.add(new LiveUser(itemList.size() + 1, "New Item"));
//                itemList.add(new LiveUser("123"+itemList.size() + 1, "1236q"+itemList.size() + 1,"Amit"+itemList.size() + 1));

//                liveUserAdapter.setItems(itemList);
                testUserEnter();
            }
        });

            liveUserAdapter = new LiveUserAdapter();
//            binding.rvLiveUser.setLayoutManager(new GridLayoutManager(getActivity(),new RecyclerView.LayoutManager()));
            binding.rvLiveUser.setAdapter(liveUserAdapter);

//            itemList.add((new LiveUser("123", "1236q", "Amit")));
//            itemList.add((new LiveUser("1232", "1235q", "Test")));
//            itemList.add((new LiveUser("1233", "1234q", "Amit123")));

            // Set initial data to the adapter
//            liveUserAdapter.setItems(itemList);

            fetChData();

//            fetchWithScroll();




        return root;
    }

    private void testUserEnter() {


        Map<String, Object> user = new HashMap<>();
        user.put("name", "Amit");
        user.put("city", "mohali");
        user.put("category", "demo");
        user.put("photo", "http://");
        user.put("price", 500);
        user.put("numRatings", 105);
        user.put("avgRating", 4.5);

// Add a new document with a generated ID
        db.collection("restaurants")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });
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
                                    LiveUser user = new LiveUser(live,userId, roomId, userName);
                                    itemList.add(user);
                                    liveUserAdapter.setItems(itemList);
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

    private void modifyuser(int position, DocumentSnapshot modifiedUser) {
        String live = modifiedUser.getString("live");
        String userId = modifiedUser.getString("userId");
        String roomId = modifiedUser.getString("roomId");
        String userName = modifiedUser.getString("userName");

        // Create a User model instance and populate it
        LiveUser modifiedLiveUser = new LiveUser(live, userId, roomId, userName);

        // Update the item at the specific position in itemList
        if (position != RecyclerView.NO_POSITION && position < itemList.size()) {
            itemList.set(position, modifiedLiveUser);
            liveUserAdapter.setItems(itemList);
        }
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
        liveUserAdapter.notifyDataSetChanged();
    }
}