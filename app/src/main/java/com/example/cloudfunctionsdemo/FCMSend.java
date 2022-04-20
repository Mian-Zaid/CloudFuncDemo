package com.example.cloudfunctionsdemo;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FCMSend {
    public static final String BASE_URL = "https://fcm.googleapis.com/fcm/send";
    public static final String SERVER_KEY = "key=AAAAeFLnWTU:APA91bHDXCwmdrIbhRlQxjYbOikCwm3kkbB4i6Kp4MpUUcbHn8yFw-skw_8poyxdjQoDuV8WDXDIVaSsISVnKZjo26wetrXHeoFNo8X_YGrXCWm0s5Jeq829242iMeany12vX9CQg56h";
    private static final String TAG = "FCMSend";

    public static void pushNotification(Context context, String token, String title, String body) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(context);

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("to", token);

            JSONObject notification = new JSONObject();
            notification.put("title", title);
            notification.put("body", body);

            jsonObject.put("notification", notification);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "onResponse: " + response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "onErrorResponse: " + error);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> map = new HashMap<>();
                    map.put("Content-Type", "application/json");
                    map.put("Authorization", SERVER_KEY);

                    return map;

                }
            };

            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            Log.d(TAG, "pushNotification: " + e);
        }
    }

}
