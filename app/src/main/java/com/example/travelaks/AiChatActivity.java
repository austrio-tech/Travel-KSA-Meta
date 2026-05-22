package com.example.travelaks;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travelaks.data.model.Message;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AiChatActivity extends AppCompatActivity {

    private ChatAdapter chatAdapter;
    private ArrayList<Message> messages;
    private RecyclerView recyclerView;

    private static final String OPENAI_API_KEY = BuildConfig.OPENAI_API_KEY;
    private final OkHttpClient client = new OkHttpClient();

    private FirebaseFirestore db;
    private String sessionId;
    private String currentCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_box_layout);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recycler_messages);
        EditText etMessageInput = findViewById(R.id.edit_message);
        Button btnSend = findViewById(R.id.btn_send);
        Button btnBack = findViewById(R.id.btn_back);
        TextView tvTitle = findViewById(R.id.tv_title);

        currentCity = getIntent().getStringExtra("city_name");
        if (tvTitle != null) {
            tvTitle.setText(currentCity != null ? "AI – " + currentCity : "AI CHAT");
        }

        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        if (currentCity != null) {
            addMessage("Hello! I am your AI assistant. Ask me anything about " + currentCity + " 🌟", false);
        }

        createChatSession();

        btnSend.setOnClickListener(v -> {
            String text = etMessageInput.getText().toString().trim();
            if (!text.isEmpty()) {
                addMessage(text, true);
                saveMessageToFirestore(text, true);
                etMessageInput.setText("");
                sendMessageToOpenAI(text, currentCity);
            }
        });

        if (btnBack != null) btnBack.setOnClickListener(v -> finish());
    }

    private void createChatSession() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "anonymous";

        Map<String, Object> session = new HashMap<>();
        session.put("userId", uid);
        session.put("city", currentCity != null ? currentCity : "");
        session.put("startedAt", Timestamp.now());

        db.collection("chatSessions").add(session)
            .addOnSuccessListener(ref -> sessionId = ref.getId())
            .addOnFailureListener(e -> Log.e("AiChat", "Session creation failed: " + e.getMessage()));
    }

    private void saveMessageToFirestore(String text, boolean isUser) {
        if (sessionId == null) return;

        Map<String, Object> msg = new HashMap<>();
        msg.put("text", text);
        msg.put("isUser", isUser);
        msg.put("timestamp", Timestamp.now());

        db.collection("chatSessions").document(sessionId)
            .collection("messages").add(msg)
            .addOnFailureListener(e -> Log.e("AiChat", "Save message failed: " + e.getMessage()));
    }

    private void addMessage(String text, boolean isUser) {
        messages.add(new Message(text, isUser));
        chatAdapter.notifyItemInserted(messages.size() - 1);
        recyclerView.scrollToPosition(messages.size() - 1);
    }

    private void sendMessageToOpenAI(String userMessage, String cityName) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json.put("model", "gpt-3.5-turbo");
            JSONArray messagesArray = new JSONArray();
            JSONObject userObj = new JSONObject();
            userObj.put("role", "user");
            userObj.put("content", userMessage + (cityName != null ? " (about " + cityName + ")" : ""));
            messagesArray.put(userObj);
            json.put("messages", messagesArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .header("Authorization", "Bearer " + OPENAI_API_KEY)
            .post(RequestBody.create(JSON, json.toString()))
            .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String error = "❌ Connection error: " + e.getMessage();
                runOnUiThread(() -> {
                    addMessage(error, false);
                    saveMessageToFirestore(error, false);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseData);
                        JSONArray choices = jsonObject.optJSONArray("choices");
                        String aiReply = "⚠️ No reply from AI";
                        if (choices != null && choices.length() > 0) {
                            JSONObject choice = choices.getJSONObject(0);
                            JSONObject messageObj = choice.optJSONObject("message");
                            if (messageObj != null) {
                                aiReply = messageObj.optString("content", aiReply);
                            }
                        }
                        String finalReply = aiReply;
                        runOnUiThread(() -> {
                            addMessage(finalReply, false);
                            saveMessageToFirestore(finalReply, false);
                        });
                    } catch (JSONException e) {
                        String error = "⚠️ Error parsing AI response";
                        runOnUiThread(() -> {
                            addMessage(error, false);
                            saveMessageToFirestore(error, false);
                        });
                    }
                } else {
                    String error = "❌ Error from OpenAI: " + response.code();
                    runOnUiThread(() -> {
                        addMessage(error, false);
                        saveMessageToFirestore(error, false);
                    });
                }
            }
        });
    }
}
