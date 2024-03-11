package com.stream.prettylive.game.teenpatty;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.stream.prettylive.R;
import com.stream.prettylive.databinding.FragmentBottomSheetGameBinding;
import com.stream.prettylive.game.teenpatty.history.GameHistoryFragment;
import com.stream.prettylive.game.teenpatty.methods.TopUsersListener;
import com.stream.prettylive.game.teenpatty.models.TopWinnserModel;
import com.stream.prettylive.game.teenpatty.models.UserData;
import com.stream.prettylive.global.AppConstants;
import com.stream.prettylive.global.ApplicationClass;
import com.stream.prettylive.ui.utill.Constant;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;


public class BottomSheetGameFragment extends BottomSheetDialogFragment {
    private FragmentBottomSheetGameBinding binding;
    private FirebaseAuth firebaseAuth;
    boolean TimerDisplay = false;
    boolean ShowCardsBanner = false;
    private long first, second, third = 0;
    boolean BetAllow = false;
    int[][] Trail = {{4, 4, 4}, {1, 1, 1}, {12, 12, 12}};
    int[][] Pure = {{2, 3, 4}, {8, 9, 10}, {1, 2, 3}, {1, 13, 12}, {7, 6, 5}};
    int[][] Sequence = {{12, 11, 10}, {5, 4, 3}, {10, 9, 8}, {4, 5, 6}, {9, 10, 11}, {1, 13, 12}, {13, 12, 11}};
    int[][] Colorss = {{1, 13, 11}, {1, 7, 3}, {6, 4, 2}, {13, 3, 8}, {1, 5, 9}, {10, 5, 12}, {13, 2, 4}, {12, 7, 3}, {1, 10, 4}, {11, 8, 5}};
    int[][] Pair = {{1, 1, 13}, {1, 1, 3}, {6, 6, 9}, {11, 11, 2}, {13, 13, 7}, {3, 3, 6}, {2, 7, 7}, {12, 11, 11}, {10, 4, 4}, {9, 10, 10}, {1, 2, 2}, {4, 8, 8}};
    int[][] HighCard = {{1, 9, 13}, {3, 6, 1}, {12, 9, 10}, {1, 6, 9}, {10, 4, 2}, {3, 8, 10}, {13, 7, 4}, {2, 10, 5}, {1, 8, 11}, {1, 7, 3}, {6, 12, 2}, {13, 10, 8}, {1, 5, 9}, {10, 5, 12}, {13, 2, 4}, {12, 7, 3}, {1, 10, 4}, {11, 8, 5}};

    int[] Cards = {
            R.drawable.c1,
            R.drawable.c2,
            R.drawable.c3,
            R.drawable.c4,
            R.drawable.c5,
            R.drawable.c6,
            R.drawable.c7,
            R.drawable.c8,
            R.drawable.c9,
            R.drawable.c10,
            R.drawable.c11,
            R.drawable.c12,
            R.drawable.c13,
            R.drawable.d1,
            R.drawable.d2,
            R.drawable.d3,
            R.drawable.d4,
            R.drawable.d5,
            R.drawable.d6,
            R.drawable.d7,
            R.drawable.d8,
            R.drawable.d9,
            R.drawable.d10,
            R.drawable.d11,
            R.drawable.d12,
            R.drawable.d13,
            R.drawable.h1,
            R.drawable.h2,
            R.drawable.h3,
            R.drawable.h4,
            R.drawable.h5,
            R.drawable.h6,
            R.drawable.h7,
            R.drawable.h8,
            R.drawable.h9,
            R.drawable.h10,
            R.drawable.h11,
            R.drawable.h12,
            R.drawable.h13,
            R.drawable.s1,
            R.drawable.s2,
            R.drawable.s3,
            R.drawable.s4,
            R.drawable.s5,
            R.drawable.s6,
            R.drawable.s7,
            R.drawable.s8,
            R.drawable.s9,
            R.drawable.s10,
            R.drawable.s11,
            R.drawable.s12,
            R.drawable.s13,
    };

    ImageView[] row1 = {};
    ImageView[] row2 = {};
    ImageView[] row3 = {};
    ImageView[] Bannerss = {};
    TextView[] BannerTextss = {};
    ImageView[] RowMasks = {};
    ImageView[] BirdMasks = {};
    ImageView[] CardPotMasks = {};
    RelativeLayout timerView;
    long UserCoins = 0;
    long PreviousCoins = 0L;
    long DeviceCoins = 0;
    long YourWager = 0;
    long PrizeCoins = 0;
    double MulPrize = 3;
    int RoundNum = 0;
    int Arc = 0;
    int MyPotA = 0;
    int MyPotB = 0;
    int MyPotC = 0;
    long GPotA = 0;
    long GPotB = 0;
    long GPotC = 0;
    boolean SendCoins = false;
    ListenerRegistration listenerRegdoc;
    ListenerRegistration listenerUserReg;
    boolean BetOnGoing = false;
    private String username,userId;
    private List<UserData> userList = new ArrayList<>();
    private AudioPlayerClass audioPlayer;
    private boolean isPlay=true;
    public enum CoinValues {
        None, Coins100, Coins1k, Coins10k, Coins100k;

        private int CoinValues(CoinValues c) {
            switch (c) {
                case None:
                    return 0;
                case Coins100:
                    return 100;
                case Coins1k:
                    return 1000;
                case Coins10k:
                    return 10000;
                case Coins100k:
                    return 100000;
                default:
                    return 0;
            }

        }
    }

    CoinValues CV = CoinValues.None;

    String UID="";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout using view binding
        binding = FragmentBottomSheetGameBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UID = ApplicationClass.getSharedpref().getString(AppConstants.USER_ID);
//        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.TransparentBottomSheetDialog);
        Objects.requireNonNull(getDialog()).setCanceledOnTouchOutside(false);
        audioPlayer = new AudioPlayerClass();
        firebaseAuth = FirebaseAuth.getInstance();
        CheckForTimer();
//        checkStartTimer();

        topUserList();
        row1 = new ImageView[]{binding.card01, binding.card02, binding.card03};
        row2 = new ImageView[]{binding.card04, binding.card05, binding.card06};
        row3 = new ImageView[]{binding.card07, binding.card08, binding.card09};

        Bannerss = new ImageView[]{binding.cardrow01banner, binding.cardrow02banner,binding.cardrow03banner};
        BannerTextss = new TextView[]{binding.cardrow01bannerText, binding.cardrow02bannerText, binding.cardrow03bannerText};


        RowMasks = new ImageView[]{binding.cr01mask, binding.cr02mask, binding.cr03mask};
        BirdMasks = new ImageView[]{binding.chrbmask, binding.chrpmask, binding.chrRmask};
        CardPotMasks = new ImageView[]{binding.CardAmask, binding.CardBmask, binding.CardCmask};

        try {
            for (int i = 0; i < 3; i++) {
                Bannerss[i].setVisibility(View.INVISIBLE);
                BannerTextss[i].setVisibility(View.INVISIBLE);
                RowMasks[i].setVisibility(View.INVISIBLE);
                BirdMasks[i].setVisibility(View.INVISIBLE);
                CardPotMasks[i].setVisibility(View.INVISIBLE);
            }

        }catch (Exception e){

        }
        ImageButton[] ChipButtons = {binding.chip100, binding.chip1000, binding.chip10k, binding.chip100k};

        binding.TPclosebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!BetOnGoing) {
//                    FirebaseFirestore db;
//                    db = FirebaseFirestore.getInstance();
//                    final DocumentReference UserdocRef = db.collection("game_users").document(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));
//                    Map<String, Object> TestDat = new HashMap<>();
//                    TestDat.put("LOG", false);
//                    UserdocRef.update(TestDat);
//                    if (listenerRegdoc != null) {
//                        listenerRegdoc.remove();
//                    }
//                    if (listenerUserReg != null) {
//                        listenerUserReg.remove();
//                    }
                    Objects.requireNonNull(getDialog()).dismiss();
                } else {
                    Toast.makeText(requireActivity(), "Bet is Ongoing please wait !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.chip100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BetAllow) {
                    if (CV != CoinValues.Coins100) {
                        for (int i = 0; i < 4; i++) {
                            if (ChipButtons[i] == binding.chip100) {
                                ChipButtons[i].setImageResource(R.drawable.selected_chip);
                                CV = CoinValues.Coins100;


                            } else {
                                ChipButtons[i].setImageResource(R.drawable.normal_chip);
                            }
                        }
                    } else {
                        binding.chip100.setImageResource(R.drawable.normal_chip);
                        CV = CoinValues.None;

                    }
                } else {
                    binding.chip100.setImageResource(R.drawable.normal_chip);
                    CV = CoinValues.None;

                }
            }
        });

        binding.chip1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BetAllow) {
                    if (CV != CoinValues.Coins1k) {
                        for (int i = 0; i < 4; i++) {
                            if (ChipButtons[i] ==  binding.chip1000) {
                                ChipButtons[i].setImageResource(R.drawable.selected_chip);
                                CV = CoinValues.Coins1k;
                            } else {
                                ChipButtons[i].setImageResource(R.drawable.normal_chip);
                            }
                        }
                    } else {
                        binding.chip1000.setImageResource(R.drawable.normal_chip);
                        CV = CoinValues.None;
                    }
                } else {
                    binding.chip1000.setImageResource(R.drawable.normal_chip);
                    CV = CoinValues.None;
                }


            }
        });

        binding.chip10k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BetAllow) {
                    if (CV != CoinValues.Coins10k) {
                        for (int i = 0; i < 4; i++) {
                            if (ChipButtons[i] == binding.chip10k) {
                                ChipButtons[i].setImageResource(R.drawable.selected_chip);
                                CV = CoinValues.Coins10k;
                            } else {
                                ChipButtons[i].setImageResource(R.drawable.normal_chip);
                            }
                        }
                    } else {
                        binding.chip10k.setImageResource(R.drawable.normal_chip);
                        CV = CoinValues.None;
                    }
                } else {
                    binding.chip10k.setImageResource(R.drawable.normal_chip);
                    CV = CoinValues.None;
                }


            }
        });

        binding.chip100k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BetAllow) {
                    if (CV != CoinValues.Coins100k) {
                        for (int i = 0; i < 4; i++) {
                            if (ChipButtons[i] == binding.chip100k) {
                                ChipButtons[i].setImageResource(R.drawable.selected_chip);
                                CV = CoinValues.Coins100k;
                            } else {
                                ChipButtons[i].setImageResource(R.drawable.normal_chip);
                            }
                        }
                    } else {
                        binding.chip100k.setImageResource(R.drawable.normal_chip);
                        CV = CoinValues.None;
                    }
                } else {
                    binding.chip100k.setImageResource(R.drawable.normal_chip);
                    CV = CoinValues.None;
                }


            }
        });

        binding.CardPotA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BetAllow) {
                    BetArea(0, CV.CoinValues(CV));
                    audioPlayer.playWav(requireActivity(), R.raw.click_tune);
                }

            }
        });
        binding.CardPotB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BetAllow) {
                    BetArea(1, CV.CoinValues(CV));
                    audioPlayer.playWav(requireActivity(), R.raw.click_tune);
                }

            }
        });
        binding.CardPotC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BetAllow) {
                    BetArea(2, CV.CoinValues(CV));
                    audioPlayer.playWav(requireActivity(), R.raw.click_tune);
                }

            }
        });
        binding.startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.soundUp.setVisibility(View.VISIBLE);
                binding.soundOff.setVisibility(View.INVISIBLE);
                binding.timerView.setVisibility(View.VISIBLE);
                StartTimer(binding.startTimer,binding.soundOff,binding.soundUp);
            }
        });


        ShowDoneScreen(0);
        CloseAllRanks(0);
        CloseAllRanks(1);

        binding.rankbutton.setVisibility(View.GONE);
        binding.rankbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnRankButtonClick(0);
            }
        });
        binding.DailyCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseAllRanks(0);
            }
        });


        binding.WeeklyRankbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnRankButtonClick(1);
            }
        });

        binding.WeeklyCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CloseAllRanks(1);
            }
        });
        binding.historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHistorySheetDialog();
            }
        });
        binding.soundUp.setOnClickListener(v -> {
            audioPlayer.stopPlaying();
            binding.soundUp.setVisibility(View.INVISIBLE);
            binding.soundOff.setVisibility(View.VISIBLE);
            isPlay=false;
        });
        binding.soundOff.setOnClickListener(v -> {
            audioPlayer.playMp3(requireActivity(), R.raw.bg_tune);
            binding.soundOff.setVisibility(View.INVISIBLE);
            binding.soundUp.setVisibility(View.VISIBLE);
            isPlay=true;
        });
    }

    private void checkStartTimer() {
        BetStoped(0);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("SpinnerTimerBools").document("TeenPatti");

// Create a SnapshotListener to listen for real-time updates
        docRef.addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                // Handle any errors
                Log.e("FirestoreData", "Listen failed", e);
                return;
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                Boolean timerStart = documentSnapshot.getBoolean("timerstart");
                Boolean betAllowed = documentSnapshot.getBoolean("BetAllowed");
                Boolean isWaiting = documentSnapshot.getBoolean("isWait");
                Long timer123 = documentSnapshot.getLong("timer");
                Log.d("FirestoreData", "timer123: " + timer123);


//                if (timer123 == 20) {
//                    binding.timerView.setVisibility(View.VISIBLE);
////                    GenCards();
//                }
//                if (timer123 == 9) {
//                    binding.timerView.setVisibility(View.INVISIBLE);
////                    GenCards();
//                }
//                if (timer123 == 1) {
//                    SetResults();
//                }
//                if (timer123 == 0) {
//                    binding.timerView.setVisibility(View.INVISIBLE);
//                }
//                if (isWaiting) {
//                    binding.waitingView.setVisibility(View.VISIBLE);
//
//                }
//                if (!isWaiting) {
//                    binding.waitingView.setVisibility(View.INVISIBLE);
//
//                }


            } else {
                // Document does not exist or is null
                Log.d("FirestoreData", "Document does not exist or is null");
            }
        });

    }


    public void showHistorySheetDialog() {
        GameHistoryFragment bottomSheetDialogFragment = new GameHistoryFragment();
        bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    private void topUserList() {
        TopUsersListener topUsersListener = new TopUsersListener(new TopUsersListener.Select() {
            @Override
            public void topUser(UserData userData) {
                // Add userData to the list
                userList.add(userData);
                Log.i("kjhsdvfn", "topUser: "+userData);
                Log.i("kjhsdvfn", "size: "+userList.size());

            }
        });
        int limit = 3; // Adjust the limit based on your requirement
        topUsersListener.startListeningForTopUsers(limit);
    }

//    private void addUser(String userId, String username) {
//        DocumentReference docRef = FirebaseFirestore.getInstance().collection("game_users").document(this.userId);
//        Map<String, Object> TestData1 = new HashMap<>();
//        TestData1.put("MyPotA", 0);
//        TestData1.put("MyPotB", 0);
//        TestData1.put("MyPotC", 0);
//        TestData1.put("LOG", false);
//        TestData1.put("YourWager", 0);
//        TestData1.put("Coins", 150000);
//        TestData1.put("receiveCoin", 0);
//        TestData1.put("userName", username);
//        TestData1.put("image", "https://upload.wikimedia.org/wikipedia/commons/7/7c/Profile_avatar_placeholder_large.png?20150327203541");
//        TestData1.put("uid", "");
//        TestData1.put("userId", userId);
//        docRef.set(TestData1).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
////                Toast.makeText(requireActivity(), "user  created", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(requireActivity(), "user failed to create", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


    private List<Long> coin = new ArrayList<>();
    private List<TopWinnserModel> topWinnserModels = new ArrayList<>();

    private void showTopWiner(int roundNum) {
        FirebaseFirestore Db;
        Db = FirebaseFirestore.getInstance();
        final CollectionReference Coll = Db.collection("SpinnerTimerBools").document("TeenPatti").collection("Results").document(String.valueOf(roundNum)).collection("Game");

        Coll.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> DOCS = queryDocumentSnapshots.getDocuments();
                List<DocumentSnapshot> list = new ArrayList<>();
//                if (!queryDocumentSnapshots.isEmpty()) {
//                    for (DocumentSnapshot D : DOCS) {
//                        list.add(D);
////                        long Res = (long) D.get("WinCoins");
////                        String name = String.valueOf(D.get("username"));
////                        String image = String.valueOf(D.get("userimage"));
//                    }
//                    topWinnserModels.clear();
//                    for (int i = 0; i < list.size(); i++) {
//                        Log.i("onSuccessamit", "path=" + list.get(i).get("username"));
//                        Log.i("onSuccessamit", "path=" + list.get(i).get("budget"));
//                        Log.i("onSuccessamit", "path=" + list.get(i).get("image"));
//                        long Res = (long) list.get(i).get("WinCoins");
//                        coin.add(Res);
//                        TopWinnserModel topWinnserModel = new TopWinnserModel();
//                        topWinnserModel.setCoin(list.get(i).get("WinCoins").toString());
//                        if (list.get(i).get("userimage") != null) {
//                            topWinnserModel.setImage(list.get(i).get("userimage").toString());
//                        } else {
//                            topWinnserModel.setImage("");
//                        }
//                        if (list.get(i).get("username")!= null) {
//                            topWinnserModel.setUserName(list.get(i).get("username").toString());
//                        } else {
//                            topWinnserModel.setUserName("NaNa");
//                        }
//
//                        topWinnserModel.setUserName(list.get(i).get("username").toString());
//                        topWinnserModels.add(topWinnserModel);
//                    }
//
//                    third = first = second = 0;
//                    for (int i = 0; i < list.size(); i++) {
//                        if (coin.get(i) > first) {
//                            third = second;
//                            second = first;
//                            first = coin.get(i);
//                        } else if (coin.get(i) > second) {
//                            third = second;
//                            second = coin.get(i);
//                        } else if (coin.get(i) > third)
//                            third = coin.get(i);
//                    }
//                    if (getWinnerPosition(coin, second) != -1)
//                        Toast.makeText(this, "First Winner " + topWinnserModels.get(getWinnerPosition(coin, first)).getUserName(), Toast.LENGTH_SHORT).show();
//                    if (getWinnerPosition(coin, second) != -1)
//                        Toast.makeText(this, "Second Winner " + topWinnserModels.get(getWinnerPosition(coin, second)).getUserName(), Toast.LENGTH_SHORT).show();
//
//                    if (getWinnerPosition(coin, third) != -1)
//                        Toast.makeText(this, "Third Winner " + topWinnserModels.get(getWinnerPosition(coin, third)).getUserName(), Toast.LENGTH_SHORT).show();
//                }
            }
        });

    }

    private int getWinnerPosition(List<Long> coin, long winnerCoin) {
        int position = -1;
        for (int i = 0; i < coin.size(); i++) {
            if (coin.get(i) == winnerCoin) {
                position = i;
                break;

            }
        }

        return position;
    }


    void CloseAllRanks(int i) {
        try {
            if (i == 0) {
                binding.RankScreenDaily.setVisibility(View.INVISIBLE);
            } else if (i == 1) {
                binding.RankScreenWeekly.setVisibility(View.INVISIBLE);
            }
        }catch (Exception e){

        }
    }

    void OnRankButtonClick(int i) {
        try {
            if (i == 0) {
                binding.RankScreenDaily.setVisibility(View.VISIBLE);
            } else if (i == 1) {
                CloseAllRanks(0);
                binding.RankScreenWeekly.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){

        }
    }

    void BetArea(int Area, int Coins) {

//        FirebaseFirestore Db;
//        Db= FirebaseFirestore.getInstance();
//        DocumentReference Coll = Db.collection("SpinnerTimerBools").document("TeenPatti");
//        DocumentReference UserColl =Db.collection("game_users").document(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID);
        int UCoins = (int) (UserCoins - DeviceCoins);
        if (Area == 0) {
            if (UCoins - Coins >= 0) {
                MyPotA += Coins;
                DeviceCoins += Coins;
                GPotA += Coins;
//                BetCoins(Coins);
//                Map<String, Object> TestData1 = new HashMap<>();
//                Map<String, Object> TestData2 = new HashMap<>();
//                TestData1.put("PotA", FieldValue.increment(Coins));
//                TestData2.put("MyPotA", FieldValue.increment(Coins));
//                Coll.update(TestData1);
//                UserColl.update(TestData2);
            }
        } else if (Area == 1) {
            if (UCoins - Coins >= 0) {
                MyPotB += Coins;
                DeviceCoins += Coins;
                GPotB += Coins;
//                BetCoins(Coins);
//                Map<String, Object> TestData1 = new HashMap<>();
//                Map<String, Object> TestData2 = new HashMap<>();
//                TestData1.put("PotB", FieldValue.increment(Coins));
//                TestData2.put("MyPotB", FieldValue.increment(Coins));
//                Coll.update(TestData1);
//                UserColl.update(TestData2);
            }
        } else if (Area == 2) {
            if (UCoins - Coins >= 0) {
                MyPotC += Coins;
                DeviceCoins += Coins;
                GPotC += Coins;
//                BetCoins(Coins);
//                Map<String, Object> TestData1 = new HashMap<>();
//                Map<String, Object> TestData2 = new HashMap<>();
//                TestData1.put("PotC", FieldValue.increment(Coins));
//                TestData2.put("MyPotC", FieldValue.increment(Coins));
//                Coll.update(TestData1);
//                UserColl.update(TestData2);
            }
        }
        String PotAString = "You: " + prettyCount(MyPotA);
        String PotBString = "You: " + prettyCount(MyPotB);
        String PotCString = "You: " + prettyCount(MyPotC);

        binding.PotAText.setText(PotAString);
        binding.PotBText.setText(PotBString);
        binding.PotCText.setText(PotCString);
    }

    void FinalBets() {
        FirebaseFirestore Db;
        Db = FirebaseFirestore.getInstance();
        DocumentReference Coll = Db.collection("SpinnerTimerBools").document("TeenPatti");
        DocumentReference UserColl = Db.collection("game_users").document(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));

        int MyCoins = MyPotA + MyPotB + MyPotC;
        Map<String, Object> TestData1 = new HashMap<>();
        Map<String, Object> TestData2 = new HashMap<>();
        TestData1.put("PotA", FieldValue.increment(MyPotA));
        TestData2.put("MyPotA", FieldValue.increment(MyPotA));
        TestData1.put("PotB", FieldValue.increment(MyPotB));
        TestData2.put("MyPotB", FieldValue.increment(MyPotB));
        TestData1.put("PotC", FieldValue.increment(MyPotC));
        TestData2.put("MyPotC", FieldValue.increment(MyPotC));
        TestData2.put("Coins", FieldValue.increment(-MyCoins));
        TestData2.put("YourWager", FieldValue.increment(MyCoins));
        Coll.update(TestData1);
        UserColl.update(TestData2);
        MyPotA = MyPotB = MyPotC = 0;
        DeviceCoins = 0;

    }

    void BetCoins(int Coins) {
        int UCoins = (int) UserCoins;
        if (UCoins - Coins >= 0) {
            FirebaseFirestore Db;
            Db = FirebaseFirestore.getInstance();
            DocumentReference Coll = Db.collection("game_users").document(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));
            Map<String, Object> TestData = new HashMap<>();
            TestData.put("Coins", FieldValue.increment(-Coins));
            TestData.put("YourWager", FieldValue.increment(Coins));
            Coll.update(TestData);
            Log.d("CoinsSend", "Coinsa haahahaha");
        }
    }


    void StartTimer(Button button, ImageButton sound_off, ImageButton sound_up) {
        button.setEnabled(false);
        audioPlayer.playMp3(requireActivity(), R.raw.bg_tune);

        FirebaseFirestore Db;
        Db = FirebaseFirestore.getInstance();
        DocumentReference Coll = Db.collection("SpinnerTimerBools").document("TeenPatti");

        Map<String, Object> TestData = new HashMap<>();
        TestData.put("BetAllowed", true);
        TestData.put("timerstart", true);
        Coll.update(TestData);
        int Timerr = 20;
        for (int a = 0; a <= Timerr; a++) {
            Handler handler1 = new Handler();
            int AA = a;
            handler1.postDelayed(new Runnable() {

                @Override
                public void run() {
                    Map<String, Object> TestD = new HashMap<>();
                    TestD.put("timer", Timerr - AA);
                    Coll.update(TestD);
                    if (Timerr - AA == 10) {

                        StopBetting();
                    }
                    if (Timerr - AA == 9) {
                        binding.timerView.setVisibility(View.INVISIBLE);
                        GenCards();
                    }
                    if (Timerr - AA == 0) {
                        binding.timerView.setVisibility(View.INVISIBLE);
                        Map<String, Object> TestData = new HashMap<>();
                        TestData.put("timerstart", false);
                        TestData.put("show", true);
                        Coll.update(TestData);
                        SetResults();
                        button.setEnabled(true);
                        audioPlayer.stopPlaying();
                        sound_up.setVisibility(View.INVISIBLE);
                        sound_off.setVisibility(View.VISIBLE);
                    }

                }
            }, 1000 * a);
        }
    }

    void StopBetting() {
        FirebaseFirestore Db;
        Db = FirebaseFirestore.getInstance();
        DocumentReference docRef = Db.collection("SpinnerTimerBools").document("TeenPatti");
        Map<String, Object> TestData = new HashMap<>();
        TestData.put("BetAllowed", false);
        docRef.update(TestData);
    }

    void SetResults() {
        FirebaseFirestore Db;
        Db = FirebaseFirestore.getInstance();
        final DocumentReference Coll = Db.collection("SpinnerTimerBools").document("TeenPatti");
        Coll.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot != null && snapshot.exists()) {

                        ArrayList<Boolean> Area = new ArrayList<Boolean>();
                        Area.add((Boolean) snapshot.get("A1"));
                        Area.add((Boolean) snapshot.get("A2"));
                        Area.add((Boolean) snapshot.get("A3"));

                        int AR = 0;
                        if (Area.get(0)) {
                            AR = 1;
                        } else if (Area.get(1)) {
                            AR = 2;
                        } else if (Area.get(2)) {
                            AR = 3;
                        }

                        Arc=AR;

//                        Toast.makeText(requireActivity(), "Area =>"+AR, Toast.LENGTH_SHORT).show();

                        long RNum = (long) snapshot.get("RoundNum");
                        RNum++;

                        Map<String, Object> TData = new HashMap<>();
                        TData.put("RoundNum", RNum);
                        Coll.update(TData);

                        DocumentReference NewColl = Db.collection("SpinnerTimerBools").document("TeenPatti").collection("Results").document(String.valueOf(RNum));
                        Map<String, Object> NData = new HashMap<>();
                        NData.put("Area", AR);
                        NData.put("time", Timestamp.now());
                        NewColl.set(NData);

                        for (int a = 0; a < 2; a++) {
                            Handler handler1 = new Handler();
                            int AA = a;
                            handler1.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (AA == 1) {
//                                        Map<String, Object> TestData = new HashMap<>();
//                                        TestData.put("show", false);
////                                        TestData.put("isWait", true);
//                                        Coll.update(TestData);
                                        SetPotValuesDefault(1);

                                        setFalseShow(Coll);
                                    }
//
                                }
                            }, 1000 * a);
//                            Log.i("jkhgdgdfgdfgdfgg", "onComplete: "+1000 * a);
                        }


                    } else {
                        Log.d("TAG", "No such document");
                    }
                }
            }
        });
    }

    private void setFalseShow(DocumentReference coll) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> TestData = new HashMap<>();
                TestData.put("show", false);
                coll.update(TestData);
            }
        },2000);
    }


    void GenCards() {
        ArrayList<ArrayList<Integer>> Results = genTheCards();
        ArrayList<Integer> Cards = Results.get(0);
        ArrayList<Integer> TypeSeqs = Results.get(1);

        ArrayList<Integer> P1 = new ArrayList<Integer>();
        ArrayList<Integer> P2 = new ArrayList<Integer>();
        ArrayList<Integer> P3 = new ArrayList<Integer>();

        String[] CardTypes = {"Trail", "Pure", "Colour", "Sequence", "Pair", "High"};

        for (int i = 0; i < 9; i++) {
            if (i < 3) {
                P1.add(Cards.get(i));
            } else if (i >= 3 && i < 6) {
                P2.add(Cards.get(i));
            } else if (i >= 6 && i < 9) {
                P3.add(Cards.get(i));
            }
        }


        ArrayList<String> OldCardBanner = new ArrayList<String>();

        ArrayList<Integer> WinnerCardsBanner = new ArrayList<Integer>();

        boolean[] OldArea = {false, false, false};


        for (int j = 0; j < 3; j++) {
            WinnerCardsBanner.add(TypeSeqs.get(j));
            for (int i = 0; i < 6; i++) {
                if (TypeSeqs.get(j) == i) {
                    OldCardBanner.add(CardTypes[i]);
                }
            }
        }

        Collections.sort(WinnerCardsBanner);
        for (int i = 0; i < 3; i++) {
            if (WinnerCardsBanner.get(0) == TypeSeqs.get(i)) {
                OldArea[i] = true;
            } else {
                OldArea[i] = false;
            }
        }


        FirebaseFirestore Db;
        Db = FirebaseFirestore.getInstance();
        final DocumentReference Coll = Db.collection("SpinnerTimerBools").document("TeenPatti");
        Coll.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    try {
                        if (snapshot != null && snapshot.exists()) {


                            long PotA = (long) snapshot.get("PotA");
                            long PotB = (long) snapshot.get("PotB");
                            long PotC = (long) snapshot.get("PotC");

                            ArrayList<Long> Pots = new ArrayList<Long>();
                            ArrayList<Long> UnsortedPots = new ArrayList<Long>();
                            int WinnedPot = 0;
                            boolean[] Area = {false, false, false};

                            Pots.add(PotA);
                            Pots.add(PotB);
                            Pots.add(PotC);
                            UnsortedPots.add(PotA);
                            UnsortedPots.add(PotB);
                            UnsortedPots.add(PotC);

                            Collections.sort(Pots);

                            for (int i = 0; i < 3; i++) {
                                if (Pots.get(i).equals(UnsortedPots.get(i))) {

                                    WinnedPot = i;

                                }
                            }


                            int[] numbers = {1, 2, 3};
                            Random random = new Random();

                            // Generate a random index within the range of the array length
                            int randomIndex = random.nextInt(numbers.length);

//                           for (int i = 0; i < 3; i++) {
//                               if (i == WinnedPot) {
//                                   Area[i] = true;
//                               } else {
//                                   Area[i] = false;
//                               }
//                           }

                            Area[randomIndex] = true;


                            ArrayList<Integer> NewRes = new ArrayList<Integer>();
                            ArrayList<String> NewCardBanner = new ArrayList<String>();

                            if (WinnedPot == 0) {
                                if (OldArea[0]) {

                                    NewCardBanner.add(OldCardBanner.get(0));
                                    NewCardBanner.add(OldCardBanner.get(1));
                                    NewCardBanner.add(OldCardBanner.get(2));

                                    for (int i = 0; i < 3; i++) {
                                        if (i == 0) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P1.get(k));
                                            }
                                        } else if (i == 1) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P2.get(k));
                                            }
                                        } else if (i == 2) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P3.get(k));
                                            }
                                        }

                                    }


                                } else if (OldArea[1]) {
                                    NewCardBanner.add(OldCardBanner.get(1));
                                    NewCardBanner.add(OldCardBanner.get(0));
                                    NewCardBanner.add(OldCardBanner.get(2));

                                    for (int i = 0; i < 3; i++) {
                                        if (i == 0) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P2.get(k));
                                            }
                                        } else if (i == 1) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P1.get(k));
                                            }
                                        } else if (i == 2) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P3.get(k));
                                            }
                                        }

                                    }
                                } else if (OldArea[2]) {
                                    NewCardBanner.add(OldCardBanner.get(2));
                                    NewCardBanner.add(OldCardBanner.get(0));
                                    NewCardBanner.add(OldCardBanner.get(1));

                                    for (int i = 0; i < 3; i++) {
                                        if (i == 0) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P3.get(k));
                                            }
                                        } else if (i == 1) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P1.get(k));
                                            }
                                        } else if (i == 2) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P2.get(k));
                                            }
                                        }

                                    }
                                }
                            } else if (WinnedPot == 1) {
                                if (OldArea[0]) {
                                    NewCardBanner.add(OldCardBanner.get(1));
                                    NewCardBanner.add(OldCardBanner.get(0));
                                    NewCardBanner.add(OldCardBanner.get(2));

                                    for (int i = 0; i < 3; i++) {
                                        if (i == 0) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P2.get(k));
                                            }
                                        } else if (i == 1) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P1.get(k));
                                            }
                                        } else if (i == 2) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P3.get(k));
                                            }
                                        }

                                    }
                                } else if (OldArea[1]) {
                                    NewCardBanner.add(OldCardBanner.get(0));
                                    NewCardBanner.add(OldCardBanner.get(1));
                                    NewCardBanner.add(OldCardBanner.get(2));

                                    for (int i = 0; i < 3; i++) {
                                        if (i == 0) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P1.get(k));
                                            }
                                        } else if (i == 1) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P2.get(k));
                                            }
                                        } else if (i == 2) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P3.get(k));
                                            }
                                        }

                                    }
                                } else if (OldArea[2]) {
                                    NewCardBanner.add(OldCardBanner.get(0));
                                    NewCardBanner.add(OldCardBanner.get(2));
                                    NewCardBanner.add(OldCardBanner.get(1));

                                    for (int i = 0; i < 3; i++) {
                                        if (i == 0) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P1.get(k));
                                            }
                                        } else if (i == 1) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P3.get(k));
                                            }
                                        } else if (i == 2) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P2.get(k));
                                            }
                                        }

                                    }
                                }
                            } else if (WinnedPot == 2) {
                                if (OldArea[0]) {
                                    NewCardBanner.add(OldCardBanner.get(1));
                                    NewCardBanner.add(OldCardBanner.get(2));
                                    NewCardBanner.add(OldCardBanner.get(0));

                                    for (int i = 0; i < 3; i++) {
                                        if (i == 0) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P2.get(k));
                                            }
                                        } else if (i == 1) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P3.get(k));
                                            }
                                        } else if (i == 2) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P1.get(k));
                                            }
                                        }

                                    }
                                } else if (OldArea[1]) {
                                    NewCardBanner.add(OldCardBanner.get(0));
                                    NewCardBanner.add(OldCardBanner.get(2));
                                    NewCardBanner.add(OldCardBanner.get(1));

                                    for (int i = 0; i < 3; i++) {
                                        if (i == 0) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P1.get(k));
                                            }
                                        } else if (i == 1) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P3.get(k));
                                            }
                                        } else if (i == 2) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P2.get(k));
                                            }
                                        }

                                    }
                                } else if (OldArea[2]) {
                                    NewCardBanner.add(OldCardBanner.get(0));
                                    NewCardBanner.add(OldCardBanner.get(1));
                                    NewCardBanner.add(OldCardBanner.get(2));

                                    for (int i = 0; i < 3; i++) {
                                        if (i == 0) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P1.get(k));
                                            }
                                        } else if (i == 1) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P2.get(k));
                                            }
                                        } else if (i == 2) {
                                            for (int k = 0; k < 3; k++) {
                                                NewRes.add(P3.get(k));
                                            }
                                        }

                                    }
                                }
                            }


                            Map<String, Object> NewData = new HashMap<>();

                            NewData.put("cards", NewRes);
                            NewData.put("C11", NewCardBanner.get(0));
                            NewData.put("C22", NewCardBanner.get(1));
                            NewData.put("C33", NewCardBanner.get(2));
                            NewData.put("A1", Area[0]);
                            NewData.put("A2", Area[1]);
                            NewData.put("A3", Area[2]);

                            Coll.update(NewData);

                        } else {
                            Log.d("TAG", "No such document");
                        }
                    }catch (Exception e){

                    }
                }
            }
        });
    }

    void ShowBanner() {
        for (int a = 0; a < 4; a++) {
            Handler handler1 = new Handler();
            int AA = a;
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (AA == 2) {
                        ShowAllCards();
                    }
                }
            }, 1000 * a);
        }
    }

    int SuccesCounter = 0;
    int FailureCounter = 0;


    // need to set user name
    void SetUserBetOnResults() {
        FirebaseFirestore Db;
        Db = FirebaseFirestore.getInstance();
        final DocumentReference Coll = Db.collection("SpinnerTimerBools").document("TeenPatti").collection("Results").document(String.valueOf(RoundNum)).collection("Game").document(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));
        final DocumentReference docRef = Db.collection("game_users").document(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));
        Map<String, Object> TestData = new HashMap<>();
        TestData.put("BetCoins", YourWager);
        TestData.put("WinCoins", PrizeCoins);
        TestData.put("username", "Amit");
        TestData.put("userimage", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ-HM2SXYoFDGDMERjCJ3iRwYKfYGJdlMrVog8Zpufs-Am_9GY6Tm8bXFUkbEwS7xzWEhU&usqp=CAU");

        Coll.set(TestData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                SuccesCounter++;
                if (SuccesCounter % 2 != 0) {
                    showTopWiner(RoundNum);
                    Log.i("onSuccess: ========", "roundNum=" + RoundNum);
                    Log.d("TAG", "DocumentSnapshot successfully written!" + SuccesCounter);
                    //SetPotValuesDefault(0);
                    long C = UserCoins + PrizeCoins;
                    long receiveTotal = PreviousCoins + PrizeCoins;
                    Log.d("Amount", String.valueOf(UserCoins) + "    " + String.valueOf(PrizeCoins));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updateReceiverCoins(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID),receiveTotal,C);
                            Map<String, Object> TestData1 = new HashMap<>();
                            TestData1.put("MyPotA", 0);
                            TestData1.put("MyPotB", 0);
                            TestData1.put("MyPotC", 0);
                            TestData1.put("YourWager", 0);
                            TestData1.put("Coins", C);
                            TestData1.put("receiveCoin", receiveTotal);
                            docRef.update(TestData1);
                            PrizeCoins = 0;
                            GPotA = GPotB = GPotC = 0;
                            BetOnGoing = false;
                            audioPlayer.stopPlaying();
                        }
                    },2000);
                } else {
                    SuccesCounter = 0;
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FailureCounter++;

                if (FailureCounter % 2 != 0) {
                    Log.w("TAG", "Error writing document" + FailureCounter, e);
                    //SetPotValuesDefault(0);
                    long C = UserCoins + PrizeCoins;
                    Map<String, Object> TestData1 = new HashMap<>();
                    TestData1.put("MyPotA", 0);
                    TestData1.put("MyPotB", 0);
                    TestData1.put("MyPotC", 0);
                    TestData1.put("YourWager", 0);
                    TestData1.put("Coins", C);
                    docRef.update(TestData1);
                    PrizeCoins = 0;
                    GPotA = GPotB = GPotC = 0;
                } else {
                    FailureCounter = 0;
                }


            }
        });


    }

    private void updateReceiverCoins(String senderId, long totalCoins, long c) {
        CollectionReference detailsRef = FirebaseFirestore.getInstance().collection(Constant.LOGIN_DETAILS);

        // Create a query to find the document with the given userId
        Query query = detailsRef.whereEqualTo("userId", senderId);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                try {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Get the document ID for the matched document
                        String documentId = document.getId();
                        Map<String, Object> updateDetails = new HashMap<>();
                        updateDetails.put("coins", String.valueOf(c));
                        updateDetails.put("receiveGameCoin", totalCoins);
                        // Update the liveType field from 0 to 1
                        detailsRef.document(documentId)
                                .update(updateDetails)
                                .addOnSuccessListener(aVoid -> {
                                    Log.i("Coinsup", "Coins updated successfully for user with ID: " + senderId);
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("UpdateLiveType", "Error updating liveType for user with ID: " + userId, e);
                                });
                    }
                }catch (Exception e){
                    Log.i("error", "updateReceiverCoins: ");
                }
            } else {
                Log.e("UpdateLiveType", "Error getting documents: ", task.getException());
            }
        });

    }


    void ShowDoneScreen(int i) {

        try {
            binding.WagerText.setText(String.valueOf(YourWager));
            binding.PrizeText.setText(String.valueOf(PrizeCoins));
            if (userList.size() >= 3){
                binding.textUser1.setText(userList.get(0).getUserName());
                binding.txtCoin1.setText(String.valueOf(prettyCount(userList.get(0).getReceiveCoin())));
                binding.textUser2.setText(userList.get(1).getUserName());
                binding.txtCoin2.setText(String.valueOf(prettyCount(userList.get(1).getReceiveCoin())));

                binding.textUser3.setText(userList.get(2).getUserName());
                binding.txtCoin3.setText(String.valueOf(prettyCount(userList.get(2).getReceiveCoin())));

                Glide.with(this).load(userList.get(0).getImage()).into(binding.ivUser1);
                Glide.with(this).load(userList.get(1).getImage()).into(binding.ivUser3);
                Glide.with(this).load(userList.get(2).getImage()).into(binding.ivUser3);
            }
            if (i == 0) {
                binding.roundDoneScreen.setVisibility(View.INVISIBLE);
            } else {
                audioPlayer.playWav(requireActivity(), R.raw.game_win);
                SetUserBetOnResults();
                binding.roundDoneScreen.setVisibility(View.VISIBLE);
            }

        }catch (Exception e){

        }

    }

    void ShowAllCards() {
        FirebaseFirestore Db;
        Db = FirebaseFirestore.getInstance();
        DocumentReference docRef = Db.collection("SpinnerTimerBools").document("TeenPatti");

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot != null && snapshot.exists()) {

                        ArrayList<Long> Cr = (ArrayList<Long>) snapshot.get("cards");
                        ArrayList<String> Typess = new ArrayList<String>();
                        ArrayList<Boolean> Areass = new ArrayList<Boolean>();


                        Areass.add((Boolean) snapshot.get("A1"));
                        Areass.add((Boolean) snapshot.get("A2"));
                        Areass.add((Boolean) snapshot.get("A3"));


                        Typess.add(String.valueOf(snapshot.get("C11")));
                        Typess.add(String.valueOf(snapshot.get("C22")));
                        Typess.add(String.valueOf(snapshot.get("C33")));
                        GetLoopCards(Cards, row1, row2, row3, Cr, Typess, Areass);
                    } else {
                        Log.d("TAG", "No such document");
                    }
                }


            }
        });
    }

    void GetLoopCards(int[] MyCards, ImageView[] r1, ImageView[] r2, ImageView[] r3, ArrayList<Long> r, ArrayList<String> Ty, ArrayList<Boolean> AR) {
        FirebaseFirestore Db;
        Db = FirebaseFirestore.getInstance();
        DocumentReference UserDocRef = Db.collection("game_users").document(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));
        UserDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot != null && snapshot.exists()) {

                        ArrayList<Long> MyPots = new ArrayList<Long>();
                        MyPots.add(snapshot.getLong("MyPotA"));
                        MyPots.add(snapshot.getLong("MyPotB"));
                        MyPots.add(snapshot.getLong("MyPotC"));
                        loppcards(MyCards, r1, r2, r3, r, Ty, AR, MyPots);
                    } else {
                        Log.d("TAG", "No such document");
                    }
                }


            }
        });
    }

    public String prettyCount(Number number) {
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        long numValue = number.longValue();
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.00").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat().format(numValue);
        }
    }

    @SuppressLint("SetTextI18n")
    void SetRandomPots() {
        long PAlong = GetRnNum(1000, 100000);
        long PBlong = GetRnNum(1000, 100000);
        long PClong = GetRnNum(1000, 100000);
        GPotA += PAlong;
        GPotB += PBlong;
        GPotC += PClong;
        binding.AText.setText("Pot:" + prettyCount(GPotA));
        binding.BText.setText("Pot:" + prettyCount(GPotB));
        binding.CText.setText("Pot:" + prettyCount(GPotC));
    }

    void CheckForTimer() {
        try {
            BetStoped(0);
            FirebaseFirestore db;
            db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("SpinnerTimerBools").document("TeenPatti");
            DocumentReference UserdocRef = db.collection("game_users").document(UID);
            Map<String, Object> TestDat = new HashMap<>();
            TestDat.put("LOG", true);
            UserdocRef.update(TestDat);



            listenerUserReg = UserdocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.w("TAGError", String.valueOf(error));
                        return;

                    }
                    if (snapshot != null && snapshot.exists()) {

                        PreviousCoins = 0L; // Initialize with 0

                        if (snapshot.contains("receiveCoin")) {
                            Long receivedCoins = snapshot.getLong("receiveCoin");
                            if (receivedCoins != null) {
                                PreviousCoins = receivedCoins;
                            } else {
                                // Handle the case where "receiveCoin" exists but its value is null
                                Log.i("Firestore", "Received coins field exists but is null");
                            }
                        } else {
                            // Handle the case where "receiveCoin" doesn't exist in the document
                            Log.i("Firestore", "Received coins field does not exist");
                        }


//                    if (snapshot.getLong("receiveCoin") != null) {
//
//
//                        try {
//                            PreviousCoins = snapshot.getLong("receiveCoin");
//                        } catch (Exception e) {
//                            Log.i("error", "error =====>" + e.getMessage());
//                        }
//
//
//                    } else {
//                        PreviousCoins = 0;
//                    }

                        if (snapshot.get("Coins") != null) {


                            try {
                                UserCoins = snapshot.getLong("Coins");
                                binding.CoinsText.setText(prettyCount(UserCoins));
//                            updateCoins(UserCoins);
                            } catch (Exception e) {
                                Log.i("error", "error =====>" + e.getMessage());
                            }


                        } else {
                            UserCoins = 0;
                        }

                        if (snapshot.get("YourWager") != null) {
                            YourWager = snapshot.getLong("YourWager");
                        } else {
                            YourWager = 0;
                        }

                        if (snapshot.getLong("MyPotA") != null && snapshot.getLong("MyPotB") != null && snapshot.getLong("MyPotC") != null) {
                            Long PotA = snapshot.getLong("MyPotA");
                            Long PotB = snapshot.getLong("MyPotB");
                            Long PotC = snapshot.getLong("MyPotC");
                            String PotAString;
                            String PotBString;
                            String PotCString;
                            if (MyPotA != 0) {
                                PotAString = "You: " + prettyCount(MyPotA);

                            } else {
                                PotAString = "You: " + prettyCount(PotA);
                            }
                            if (MyPotB != 0) {

                                PotBString = "You: " + prettyCount(MyPotB);

                            } else {
                                PotBString = "You: " + prettyCount(PotB);
                            }
                            if (MyPotC != 0) {

                                PotCString = "You: " + prettyCount(MyPotC);

                            } else {
                                PotCString = "You: " + prettyCount(PotC);
                            }


                            binding.PotAText.setText(PotAString);
                            binding.PotBText.setText(PotBString);
                            binding.PotCText.setText(PotCString);
                        }
                    } else {
                        Log.d("TagNull", "Current data: null");
                    }
                }
            });


            listenerRegdoc = docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.w("TAGError", String.valueOf(error));
                        return;
                    }

                    try {
                        if (snapshot != null && snapshot.exists()) {
                            boolean TimerStart = (boolean) snapshot.get("timerstart");
                            String Tim = snapshot.get("timer").toString();
                            binding.TimerText.setText(Tim);
                            boolean RoundAlowed = (Boolean) snapshot.get("BetAllowed");

                            if (snapshot.get("RoundNum") != null) {
                                long number = snapshot.getLong("RoundNum");
                                int nm = (int) number;

                                RoundNum = nm;

                                Log.i("onSuccess: ========", "roundNum main=" + RoundNum);

//                        showTopWiner(RoundNum);
                            }
                            if (DeviceCoins != 0) {
                                binding.CoinsText.setText(prettyCount(UserCoins - DeviceCoins));
                                try {
//                            updateCoins(UserCoins - DeviceCoins);
                                } catch (Exception e) {
                                    Log.i("error", "error =====>" + e.getMessage());
                                }
                            } else {
//                            Toast.makeText(requireActivity(), "three call", Toast.LENGTH_SHORT).show();
                                binding.CoinsText.setText(prettyCount(UserCoins));

                                try {
//                            updateCoins(UserCoins);
                                } catch (Exception e) {
                                    Log.i("error", "error =====>" + e.getMessage());
                                }
                            }

                            if (snapshot.get("PotA") != null && snapshot.get("PotB") != null && snapshot.get("PotC") != null) {
//                        TextView PA=requireActivity().findViewById(R.id.AText);
//                        TextView PB=requireActivity().findViewById(R.id.BText);
//                        TextView PC=requireActivity().findViewById(R.id.CText);
//                        long PAlong=snapshot.getLong("PotA");
//                        long PBlong=snapshot.getLong("PotB");
//                        long PClong=snapshot.getLong("PotC");
//                        PA.setText("Pot:"+prettyCount(PAlong));
//                        PB.setText("Pot:"+prettyCount(PBlong));
//                        PC.setText("Pot:"+prettyCount(PClong));
                            }

                            if (snapshot.get("timer") != null) {
                                long number = snapshot.getLong("timer");
                                int nm = (int) number;

                                if (nm==20){
                                    binding.soundUp.setVisibility(View.VISIBLE);
                                    binding.soundOff.setVisibility(View.INVISIBLE);
                                    binding.timerView.setVisibility(View.VISIBLE);
                                    StartTimer(binding.startTimer,binding.soundOff,binding.soundUp);
                                }

                                if (nm <= 0) {
                                    SendCoins = false;
                                    //Log.d("CoinsStop","Cons Bets can be sent now");
                                }
                                if (!SendCoins) {
                                    if (nm == 6) {
                                        DeviceCoins = 0;
                                    }
                                    if (nm == 7) {
                                        Log.d("Coins", "Cons Bets sent ");
                                        SendCoins = true;
                                        FinalBets();
                                    }
                                }
                                if (nm > 0 && nm < 10) {
                                    BetStoped(1);
                                    BetOnGoing = true;
                                } else {

                                    BetStoped(0);
                                }
                            }
                            if (RoundAlowed) {
                                BetAllow = true;
                                SetRandomPots();
                            } else {
                                BetAllow = false;
                            }

                            if (TimerStart) {
                                TimerDisplay = true;
                                TimerDisplayCheck();
                            } else {
                                TimerDisplay = false;
                                TimerDisplayCheck();

                            }
                            boolean ShowBannerCards = (Boolean) snapshot.get("show");
                            if (ShowBannerCards) {
                                ShowBanner();
                                ShowCardsBanner = true;

                                CardsBannerDisplayCheck();
                            } else {
                                ShowCardsBanner = false;
                                CardsBannerDisplayCheck();
                            }




                        } else {
                            Log.d("TagNull", "Current data: null");
                        }
                    }catch (Exception e){
                        Log.i("error", "onEvent: "+e);
                    }
                }
            });
        }catch (Exception e){

        }

    }

//    private void updateCoins(Long coins) {
//        new ProfileMvvm().teenPattiBet(this, userId, String.valueOf(coins)).observe(this, new Observer<TeenPattiBetModel>() {
//            @Override
//            public void onChanged(TeenPattiBetModel teenPattiBetModel) {
//                if (teenPattiBetModel.getSuccess().equalsIgnoreCase("1")) {
//
//                } else {
//                    Toast.makeText(this, "internal server error.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }


    void BetStoped(int n) {
        if (n == 0) {
            binding.StopBetsBanner.setVisibility(View.INVISIBLE);
            binding.StopBetsBannerText.setVisibility(View.INVISIBLE);
        } else if (n == 1) {
            binding.StopBetsBanner.setVisibility(View.VISIBLE);
            binding.StopBetsBannerText.setVisibility(View.VISIBLE);
        }
    }


    void CardsBannerDisplayCheck() {
        if (!ShowCardsBanner) {
            binding.ShowCardsBanner.setVisibility(View.INVISIBLE);
            binding.ShowCardsBannerText.setVisibility(View.INVISIBLE);

        } else {
            binding.ShowCardsBanner.setVisibility(View.VISIBLE);
            binding.ShowCardsBannerText.setVisibility(View.VISIBLE);
        }
    }


    void TimerDisplayCheck() {
        if (!TimerDisplay) {
//            binding.timerView.setVisibility(View.INVISIBLE);
            binding.Timerboard.setVisibility(View.INVISIBLE);
            binding.TimerText.setVisibility(View.INVISIBLE);
        } else {
//            binding.timerView.setVisibility(View.VISIBLE);
            binding.Timerboard.setVisibility(View.VISIBLE);
            binding.TimerText.setVisibility(View.VISIBLE);
        }
    }

    ArrayList<ArrayList<Integer>> genTheCards() {
        int kk = 0;


        int[][][] ChoseCard = {Trail, Pure, Colorss, Sequence, Pair, HighCard};

        int[] Typer = {0, 1, 2, 3, 4, 5};
        ArrayList<Integer> Typ = new ArrayList<Integer>();
        for (int i = 0; i < Typer.length; i++) {
            Typ.add(Typer[i]);
        }

        Collections.shuffle(Typ);
        kk = Typ.get(0);
        Typ.remove(0);
        int Tseq1 = kk;


        Collections.shuffle(Typ);
        kk = Typ.get(0);
        Typ.remove(0);
        int Tseq2 = kk;

        Collections.shuffle(Typ);
        kk = Typ.get(0);
        Typ.remove(0);
        int Tseq3 = kk;

        for (int i = 0; i < Typer.length; i++) {
            Typ.add(Typer[i]);
        }
        int SuiteIndx = GetRnNum(1, 3);
        ArrayList<Integer> TDeck = new ArrayList<Integer>();

        ArrayList<Integer> r1 = SetAllCards(TDeck, ChoseCard, Tseq1, Tseq2, Tseq3, SuiteIndx);
        ArrayList<Integer> Ty = new ArrayList<Integer>();
        Ty.add(Tseq1);
        Ty.add(Tseq2);
        Ty.add(Tseq3);

        ArrayList<ArrayList<Integer>> Result = new ArrayList<ArrayList<Integer>>();
        Result.add(r1);
        Result.add(Ty);

        return Result;

    }

    ArrayList<Integer> Set6Cards(ArrayList<Integer> TMDeck, int[][][] ChoseCard, int TypeSequence2, int TypeSequence3, int SuiteIndx) {
        int[][] SelectedCardType2 = ChoseCard[TypeSequence2];
        int Ranindex2 = GetRnNum(0, SelectedCardType2.length - 1);
        int[] C2 = SelectedCardType2[Ranindex2];

        int[][] SelectedCardType3 = ChoseCard[TypeSequence3];
        int Ranindex3 = GetRnNum(0, SelectedCardType3.length - 1);
        int[] C3 = SelectedCardType3[Ranindex3];

        if (ChangeCard(TMDeck, C2, TypeSequence2, SuiteIndx)) {
            if (ChangeCard(TMDeck, C3, TypeSequence3, SuiteIndx)) {
                return TMDeck;
            } else {
                return Set3Cards(TMDeck, ChoseCard, TypeSequence3, SuiteIndx);
            }
        } else {
            return Set6Cards(TMDeck, ChoseCard, TypeSequence2, TypeSequence3, SuiteIndx);
        }
    }

    ArrayList<Integer> Set3Cards(ArrayList<Integer> TMDeck, int[][][] ChoseCard, int TypeSequence3, int SuiteIndx) {


        int[][] SelectedCardType3 = ChoseCard[TypeSequence3];
        int Ranindex3 = GetRnNum(0, SelectedCardType3.length - 1);
        int[] C3 = SelectedCardType3[Ranindex3];

        if (ChangeCard(TMDeck, C3, TypeSequence3, SuiteIndx)) {
            return TMDeck;
        } else {
            return Set3Cards(TMDeck, ChoseCard, TypeSequence3, SuiteIndx);
        }
    }


    ArrayList<Integer> SetAllCards(ArrayList<Integer> TMDeck, int[][][] ChoseCard, int TypeSequence1, int TypeSequence2, int TypeSequence3, int SuiteIndx) {
        int[][] SelectedCardType1 = ChoseCard[TypeSequence1];
        int[][] SelectedCardType2 = ChoseCard[TypeSequence2];
        int[][] SelectedCardType3 = ChoseCard[TypeSequence3];


        int Ranindex1 = GetRnNum(0, SelectedCardType1.length - 1);
        int[] C1 = SelectedCardType1[Ranindex1];


        int Ranindex2 = GetRnNum(0, SelectedCardType2.length - 1);
        int[] C2 = SelectedCardType2[Ranindex2];


        int Ranindex3 = GetRnNum(0, SelectedCardType3.length - 1);
        int[] C3 = SelectedCardType3[Ranindex3];


        ChangeCard(TMDeck, C1, TypeSequence1, SuiteIndx);

        if (ChangeCard(TMDeck, C2, TypeSequence2, SuiteIndx)) {
            if (ChangeCard(TMDeck, C3, TypeSequence3, SuiteIndx)) {
                return TMDeck;
            } else {
                return Set3Cards(TMDeck, ChoseCard, TypeSequence3, SuiteIndx);
            }
        } else {
            return Set6Cards(TMDeck, ChoseCard, TypeSequence2, TypeSequence3, SuiteIndx);
        }
    }

    boolean ChangeCard(ArrayList<Integer> TMDeck, int[] C, int TypeSequence, int SuiteIndx) {
        int[] j = {0, 1, 2, 3};
        ArrayList<Integer> jk = new ArrayList<Integer>();
        for (int i = 0; i < j.length; i++) {
            jk.add(j[i]);
        }
        ArrayList<Integer> CK = new ArrayList<Integer>();
        for (int i = 0; i < C.length; i++) {
            int kk;
            if (TypeSequence == 1 || TypeSequence == 3) {
                kk = SuiteIndx - 1;
                CK.add(C[i] - 1 + (13 * kk));

            } else {
                Collections.shuffle(jk);
                kk = jk.get(0);

                jk.remove(0);
                CK.add(C[i] - 1 + (13 * kk));

            }

            // if(kk == 0)
            // {
            //   print("Clubs");
            // }
            // else if(kk==1)
            // {
            //   print("Diamond");
            // }
            // else if(kk==2)
            // {
            //   print("Heart");
            // }
            // else if(kk==3)
            // {
            //   print("Spade");
            // }
        }
        if (!TMDeck.contains(CK.get(0)) && !TMDeck.contains(CK.get(1)) && !TMDeck.contains(CK.get(2))) {
            for (int i = 0; i < CK.size(); i++) {
                TMDeck.add(CK.get(i));
            }

            return true;
        } else {
            //print("Not Added to Deck");
            return false;
        }

    }

    int GetRnNum(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    void SetServerPotValDefault() {
        FirebaseFirestore Db;
        Db = FirebaseFirestore.getInstance();
        DocumentReference docRef = Db.collection("SpinnerTimerBools").document("TeenPatti");
        Map<String, Object> TestData = new HashMap<>();
        TestData.put("PotA", 0);
        TestData.put("PotB", 0);
        TestData.put("PotC", 0);
        TestData.put("BetAllowed", false);
        docRef.update(TestData);
        GPotA = GPotB = GPotC = 0;
    }

    void SetPotValuesDefault(int i) {
        FirebaseFirestore Db;
        Db = FirebaseFirestore.getInstance();
        DocumentReference docRef = Db.collection("game_users").document(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));



        if (i == 0) {
//            SetUserBetOnResults();

            long C = UserCoins + PrizeCoins;
            Map<String, Object> TestData1 = new HashMap<>();
            TestData1.put("MyPotA", 0);
            TestData1.put("MyPotB", 0);
            TestData1.put("MyPotC", 0);
            TestData1.put("YourWager", 0);
            TestData1.put("Coins", C);
            docRef.update(TestData1);
            PrizeCoins = 0;
            GPotA = GPotB = GPotC = 0;
        } else if (i == 1) {
            SetServerPotValDefault();
        }
    }


    void loppcards(int[] Cards, ImageView[] r1, ImageView[] r2, ImageView[] r3, ArrayList<Long> r, ArrayList<String> Ty, ArrayList<Boolean> AR, ArrayList<Long> MyPots) {
        ImageView[] Banners = {binding.cardrow01banner, binding.cardrow02banner, binding.cardrow03banner};
        TextView[] BannerTexts = {binding.cardrow01bannerText, binding.cardrow02bannerText, binding.cardrow03bannerText};
        for (int a = 0; a < 10; a++) {
            Handler handler1 = new Handler();
            int AA = a;
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (AA < 3) {
                        Banners[AA].setVisibility(View.VISIBLE);
                        BannerTexts[AA].setVisibility(View.VISIBLE);
                        BannerTexts[AA].setText(Ty.get(AA));
                    }
                    if (AA == 3) {
                        for (int i = 0; i < 3; i++) {
                            if (AR.get(i)) {
                                RowMasks[i].setVisibility(View.INVISIBLE);
                                BirdMasks[i].setVisibility(View.INVISIBLE);
                                CardPotMasks[i].setVisibility(View.INVISIBLE);
                            } else {
                                RowMasks[i].setVisibility(View.VISIBLE);
                                BirdMasks[i].setVisibility(View.VISIBLE);
                                CardPotMasks[i].setVisibility(View.VISIBLE);
                            }
                        }
                    } else if (AA == 6) {
                        for (int i = 0; i < 3; i++) {
                            Banners[i].setVisibility(View.INVISIBLE);
                            BannerTexts[i].setVisibility(View.INVISIBLE);
                            RowMasks[i].setVisibility(View.INVISIBLE);
                            BirdMasks[i].setVisibility(View.INVISIBLE);
                            CardPotMasks[i].setVisibility(View.INVISIBLE);
                        }

                        // need set prize

                        for (int i = 0; i < 3; i++) {
                            if (AR.get(i)) {
                                PrizeCoins = (long) (MyPots.get(i) * MulPrize);

                            }
//                            if (AR.get(Arc)) {
//                                PrizeCoins = (long) (MyPots.get(Arc) * MulPrize);
//
//                            }
                        }

                        ShowDoneScreen(1);
                        for (int i = 0; i < 3; i++) {
                            if (AR.get(i)) {

                                if (i == 0) {
                                    binding.WinningBirdBig.setImageResource(R.drawable.porta);
                                    binding.WinningBird.setImageResource(R.drawable.porta);
                                } else if (i == 1) {
                                    binding.WinningBirdBig.setImageResource(R.drawable.portb1);
                                    binding.WinningBird.setImageResource(R.drawable.portb1);
                                } else if (i == 2) {
                                    binding.WinningBirdBig.setImageResource(R.drawable.portc);
                                    binding.WinningBird.setImageResource(R.drawable.portc);
                                }
                            }

                        }
                    } else if (AA == 9) {
                        // Create a new Handler
                        Handler handler = new Handler();

                        // Create a Runnable that calls the doSomething() method
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                ShowDoneScreen(0);
                            }
                        };

                        // Post the Runnable with a delay
                        handler.postDelayed(runnable, 2000);

//                        ShowDoneScreen(0);
                    }

                    for (int j = 0; j < 3; j++) {
                        if (AA == 0) {
                            r1[j].setImageResource(Cards[r.get(j).intValue()]);
                        } else if (AA == 1) {
                            r2[j].setImageResource(Cards[r.get(j + 3).intValue()]);
                        } else if (AA == 2) {
                            r3[j].setImageResource(Cards[r.get(j + 6).intValue()]);
                        } else if (AA == 6) {
                            r1[j].setImageResource(R.drawable.bcard);
                            r2[j].setImageResource(R.drawable.pcard);
                            r3[j].setImageResource(R.drawable.rcard);
                        }
                    }
                }
            }, 300 * a);
        }
    }
    
    @Override
    public void onStop() {
        super.onStop();
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        final DocumentReference UserdocRef = db.collection("game_users").document(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));
        Map<String, Object> TestDat = new HashMap<>();
        TestDat.put("LOG", false);
        UserdocRef.update(TestDat);
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        final DocumentReference UserdocRef = db.collection("game_users").document(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));
        Map<String, Object> TestDat = new HashMap<>();
        TestDat.put("LOG", true);
        UserdocRef.update(TestDat);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        audioPlayer.stopPlaying();
//        binding = null; // Release the binding when the view is destroyed
    }




}