package com.stream.donalive.ui.utill;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.stream.donalive.global.AppConstants;
import com.stream.donalive.global.ApplicationClass;
import com.stream.donalive.ui.home.ui.profile.activity.UpdateUserDetailsActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageUtils {



    private static final String TAG = "FirestoreImageHandler";
    private FirebaseFirestore firestore;

    public  interface Select {
        void success(int value);
    }

    private Select select;

    public ImageUtils(Select select) {
        this.select = select;
    }

    public void uploadImageToFirestore(Bitmap bitmap) {
        // Access a Cloud Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new image document
        DocumentReference imageRef = db.collection("images").document(); // Create a unique document ID

        // Get the storage reference
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + imageRef.getId());

        // Convert the Bitmap to a byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
        byte[] data = baos.toByteArray();

        // Upload the image to Firebase Storage
        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Image uploaded successfully, now get the download URL
            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                updateLiveStatus(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID),uri);
                Log.d(TAG, "Image URL saved to Firestore: " + uri.toString());
            }).addOnFailureListener(e ->
                    Log.e(TAG, "Error getting download URL: " + e.getMessage()));
        }).addOnFailureListener(e ->
                Log.e(TAG, "Error uploading image: " + e.getMessage()));
    }

    private void updateLiveStatus(String userId, Uri uri) {
        // Reference to the Firestore collection
        firestore = FirebaseFirestore.getInstance();
        CollectionReference liveDetailsRef = firestore.collection(Constant.LOGIN_DETAILS);

        // Create a query to find the document with the given userId
        Query query = liveDetailsRef.whereEqualTo("userId", userId);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get the document ID for the matched document
                    String documentId = document.getId();

                    Map<String, Object> updateDetails = new HashMap<>();
                    updateDetails.put("image", uri.toString());

                    // Update the liveType field from 0 to 1
                    liveDetailsRef.document(documentId)
                            .update(updateDetails)
                            .addOnSuccessListener(aVoid -> {
                                select.success(1);
//                                Toast.makeText(activity, "Profile image has been updated successfully", Toast.LENGTH_SHORT).show();
                                Log.i("UpdateLiveType", "image updated successfully for user with ID: " + userId);
                            })
                            .addOnFailureListener(e -> {
                                select.success(0);
//                                Toast.makeText(activity, "Some things went wrong", Toast.LENGTH_SHORT).show();
                                Log.e("UpdateLiveType", "Error updating liveType for user with ID: " + userId, e);
                            });
                }
            } else {
                Log.e("UpdateLiveType", "Error getting documents: ", task.getException());
            }
        });
    }

    public static Bitmap uriToBitmap(Context context, Uri uri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
            if (parcelFileDescriptor != null) {
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                parcelFileDescriptor.close();
                return image;
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Error accessing file: " + e.getMessage());
        }
        return null;
    }

}

