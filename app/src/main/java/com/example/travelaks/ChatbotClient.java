package com.example.travelaks;

import com.example.travelaks.data.model.ChatRequest;
import com.example.travelaks.data.model.ChatResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatbotClient {

    private static final String BASE_URL = BuildConfig.CHATBOT_BASE_URL;
    private static final String API_KEY  = BuildConfig.CHATBOT_API_KEY;
    private static final MediaType JSON  = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient http = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)   // backend may cold-start on Render free tier
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    private final Gson gson = new Gson();

    public interface ChatCallback {
        void onSuccess(ChatResponse response);
        void onError(String error);
    }

    public void sendMessage(String question, String sessionId, ChatCallback callback) {
        ChatRequest req  = new ChatRequest(question, sessionId);
        String     body = gson.toJson(req);

        Request request = new Request.Builder()
                .url(BASE_URL + "/chat")
                .addHeader("X-API-Key", API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(body, JSON))
                .build();

        http.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String raw = response.body().string();
                if (!response.isSuccessful()) {
                    callback.onError("Server error " + response.code());
                    return;
                }
                ChatResponse res = gson.fromJson(raw, ChatResponse.class);
                callback.onSuccess(res);
            }
        });
    }
}
