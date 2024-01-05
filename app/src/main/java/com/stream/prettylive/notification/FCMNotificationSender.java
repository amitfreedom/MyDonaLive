package com.stream.prettylive.notification;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class FCMNotificationSender {

    private static final String FCM_API = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "AAAA73VevaY:APA91bGIomuhpl7CGGkwgCn1IKXwjzvTfu-tXJ6H3zuSqc3o6Pwr8aej3hSv_5zu7YVEZEKkq5kFD3nQ4BTKeIHTL-vG1RgLrpHKu667Oso9ei7sLDRXe1bhdMvFzlIdIszSFKM3L-1e"; // Replace with your server key from Firebase Console

    public static void sendNotificationToDevice(String deviceToken, String notificationTitle, String notib) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        JSONObject notification = new JSONObject();
        JSONObject notificationBody = new JSONObject();

        try {
            notificationBody.put("body", notib);
            notificationBody.put("title", notificationTitle);
//            notification.put("to", deviceToken);
//            notification.put("to", "/topics/" + "weather");
            notification.put("notification", notificationBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(mediaType, notification.toString());

        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer "+SERVER_KEY) // Replace with your server key
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // Handle failure (e.g., logging, error message)
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    // Notification sent successfully, handle response
                } else {
                    // Notification sending failed, handle response code
                }
            }
        });
    }
}

