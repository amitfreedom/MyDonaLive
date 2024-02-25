package com.stream.prettylive.game.teenpatty.history;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.stream.prettylive.R;
import com.stream.prettylive.databinding.FragmentBottomSheetGameBinding;
import com.stream.prettylive.databinding.FragmentGameHistoryBinding;
import com.stream.prettylive.game.teenpatty.adapter.HistoryListAdapter;
import com.stream.prettylive.game.teenpatty.models.HistoryList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameHistoryFragment extends BottomSheetDialogFragment {
    private FragmentGameHistoryBinding binding;
    HistoryListAdapter adapter; // Create Object of the Adapter class
    FirebaseFirestore mbase;
    List<HistoryList> HL=new ArrayList<HistoryList>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGameHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        ViewGroup.LayoutParams params = binding.mainView.getLayoutParams();
        params.height = (int) (height/1.18);
        binding.mainView.setLayoutParams(params);
        init();
    }

    void init(){
        mbase= FirebaseFirestore.getInstance();
        CollectionReference Coll = mbase.collection("SpinnerTimerBools").document("TeenPatti").collection("Results");
        Query QC=Coll.orderBy("time", Query.Direction.DESCENDING).limit(10);

        QC.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> DOCS =queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot D:DOCS) {
                    HistoryList TempHL=new HistoryList("Fail",R.drawable.historyfailbg,"Fail",R.drawable.historyfailbg,"Fail",R.drawable.historyfailbg);
                    long Res= (long) D.get("Area");
                    int A=(int)Res;
                    if(1 == A)
                    {
                        TempHL.setB1("Win");
                        TempHL.setWF1(R.drawable.historywinbg);
                    }
                    else  if(2 == A)
                    {
                        TempHL.setB2("Win");
                        TempHL.setWF2(R.drawable.historywinbg);
                    }
                    else  if(3 == A)
                    {
                        TempHL.setB3("Win");
                        TempHL.setWF3(R.drawable.historywinbg);
                    }
                    HL.add(TempHL);
                }
                adapter=new HistoryListAdapter(HL);
                Done();
            }
        });
    }
    void Done()
    {
        binding.historyview.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.historyview.setAdapter(adapter);
    }
}