package com.example.travelaks;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travelaks.R;
import com.example.travelaks.data.model.Message;
import java.io.IOException;
import java.util.ArrayList;
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

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private ArrayList<Message> messages;
    private EditText etMessageInput;
    private Button btnSend;
    private TextView tvTitle;

    // 🔑 OpenAI API Key from BuildConfig
    private static final String OPENAI_API_KEY = BuildConfig.OPENAI_API_KEY;

    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_box_layout);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Log to check if API key is loaded correctly
        Log.d("AiChatActivity", "API Key: " + OPENAI_API_KEY);

        RecyclerView recyclerView = findViewById(R.id.recycler_messages);
        EditText etMessageInput = findViewById(R.id.edit_message);
        Button btnSend = findViewById(R.id.btn_send);
        Button btnBack = findViewById(R.id.btn_back);

        String cityName = getIntent().getStringExtra("city_name");
        if (cityName != null && !cityName.isEmpty()) {
            tvTitle.setText("AI " + cityName);
        } else {
            tvTitle.setText("AI CHAT");
        }

        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        btnSend.setOnClickListener(v -> {
            String text = etMessageInput.getText().toString().trim();

            if (!text.isEmpty()) {
                // Add user message
                messages.add(new Message(text, true));
                chatAdapter.notifyItemInserted(messages.size() - 1);
                recyclerView.scrollToPosition(messages.size() - 1);

                etMessageInput.setText("");

                // Send to OpenAI
                sendMessageToOpenAI(text, cityName);
            }
        });

        if (cityName != null) {
            messages.add(new Message("Hello! I am your AI assistant. Ask me anything about " + cityName + " 🌟", false));
            chatAdapter.notifyItemInserted(0);
        }
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

        RequestBody body = RequestBody.create(JSON, json.toString());

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer " + OPENAI_API_KEY)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    messages.add(new Message("❌ Connection error: " + e.getMessage(), false));
                    chatAdapter.notifyItemInserted(messages.size() - 1);
                    recyclerView.scrollToPosition(messages.size() - 1);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.d("AiChatActivity", "Response: " + responseData);

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
                            messages.add(new Message(finalReply, false));
                            chatAdapter.notifyItemInserted(messages.size() - 1);
                            recyclerView.scrollToPosition(messages.size() - 1);
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> {
                            messages.add(new Message("⚠️ Error parsing AI response", false));
                            chatAdapter.notifyItemInserted(messages.size() - 1);
                            recyclerView.scrollToPosition(messages.size() - 1);
                        });
                    }
                } else {
                    runOnUiThread(() -> {
                        messages.add(new Message("❌ Error from OpenAI: " + response.code(), false));
                        chatAdapter.notifyItemInserted(messages.size() - 1);
                        recyclerView.scrollToPosition(messages.size() - 1);
                    });
                }
            }
        });
    }
}
