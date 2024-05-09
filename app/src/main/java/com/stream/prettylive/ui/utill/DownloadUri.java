package com.stream.prettylive.ui.utill;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class DownloadUri {

    public interface UploadImageCallback {
        void onUploadSuccess(Uri imageUrl);
        void onUploadFailure(String errorMessage);
    }

    public static void uploadImageReturnUrl(Bitmap bitmap, UploadImageCallback callback) {
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
                // Call the callback with the download URL
                callback.onUploadSuccess(uri);
            }).addOnFailureListener(e -> {
                // Call the callback with the error message
                callback.onUploadFailure("Error getting download URL: " + e.getMessage());
            });
        }).addOnFailureListener(e -> {
            // Call the callback with the error message
            callback.onUploadFailure("Error uploading image: " + e.getMessage());
        });
    }

}
