package com.stream.donalive.ui.common;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.stream.donalive.ui.utill.Constant;

import java.util.concurrent.CompletableFuture;

public class GenerateUserId {
    public interface UserIdCallback {
        void onUserIdReceived(int userId);
        void onFailure(Exception e);
    }

    public static void getLastUserId(FirebaseFirestore firestore, String collectionPath, UserIdCallback callback) {
        CollectionReference collection = firestore.collection(collectionPath);
        collection.orderBy(Constant.UID, Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.contains(Constant.UID)) {
                                int lastUserId = document.getLong(Constant.UID).intValue();
                                Log.i("check_uid", "onComplete: uid =" + lastUserId);
                                callback.onUserIdReceived(lastUserId);
                                return;
                            }
                        }
                    }
                    callback.onFailure(task.getException());
                });
    }

//    public static CompletableFuture<Integer> getLastUserIdFuture(FirebaseFirestore firestore, String collectionPath) {
//        CompletableFuture<Integer> future = new CompletableFuture<>();
//
//        getLastUserId(firestore, collectionPath, new UserIdCallback() {
//            @Override
//            public void onUserIdReceived(int userId) {
//                future.complete(userId);
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                future.completeExceptionally(e);
//            }
//        });
//
//        return future;
//    }


}
