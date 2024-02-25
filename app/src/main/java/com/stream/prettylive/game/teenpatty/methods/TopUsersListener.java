package com.stream.prettylive.game.teenpatty.methods;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.stream.prettylive.game.teenpatty.models.UserData;

public class TopUsersListener {

    private FirebaseFirestore db;
    private CollectionReference usersCollection;

    public Select select;

    public interface Select{
        void topUser(UserData userData);
    }

    public TopUsersListener(Select select ) {
        // Initialize Firebase
        this.select = select;
        db = FirebaseFirestore.getInstance();
        usersCollection = db.collection("game_users");
    }

    public void startListeningForTopUsers(int limit) {
        try{
            Query query = usersCollection.orderBy("receiveCoin", Query.Direction.DESCENDING).limit(limit);

            query.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    // Handle errors
                    return;
                }

                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                    switch (documentChange.getType()) {
                        case ADDED:
                            // Handle added document
                            QueryDocumentSnapshot document = documentChange.getDocument();
                            // Convert the document to your UserData model class
                            UserData userData = document.toObject(UserData.class);
                            // Access document data using document.getData()
                            select.topUser(userData);
                            break;
                        case MODIFIED:
                            // Handle modified document
                            break;
                        case REMOVED:
                            // Handle removed document
                            break;
                    }
                }
            });

        }catch (Exception e){

        }
    }
}


