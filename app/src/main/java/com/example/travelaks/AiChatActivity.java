package com.example.travelaks;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AiChatActivity extends AppCompatActivity {

    private static final String TAG = "AiChatActivity";

    private RecyclerView recyclerView;
    private EditText etInput;
    private ChatAdapter chatAdapter;
    private ArrayList<Message> messages;

    private ChatbotClient chatbot;
    private String sessionId = null;
    private String cityName;

    private FirebaseFirestore db;
    private String firestoreSessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_box_layout);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        // Init here — NOT as a field — to avoid class-load issues
        chatbot = new ChatbotClient();
        db      = FirebaseFirestore.getInstance();

        cityName = getIntent().getStringExtra("city_name");

        // Views
        recyclerView   = findViewById(R.id.recycler_messages);
        etInput        = findViewById(R.id.edit_message);
        Button btnSend = findViewById(R.id.btn_send);
        Button btnBack = findViewById(R.id.btn_back);
        TextView tvTitle = findViewById(R.id.tv_title);

        // Title
        if (tvTitle != null) {
            tvTitle.setText(cityName != null ? "AI – " + cityName : "AI Assistant");
        }

        // Chat list
        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, messages);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(chatAdapter);
        }

        // Greeting (displayed only — not sent to backend)
        String greeting = (cityName != null && !cityName.isEmpty())
                ? "Hello! I'm your AI travel assistant for " + cityName
                  + ". Ask me anything — hotels, attractions, activities, and tips! 🌟"
                : "Hello! I'm your Travel KSA assistant. Ask me anything about Saudi Arabia's cities, hotels, attractions, and activities!";
        addMessage(greeting, false);

        // Persist session in Firestore
        createFirestoreSession();

        // Send button
        if (btnSend != null && etInput != null) {
            btnSend.setOnClickListener(v -> {
                String text = etInput.getText().toString().trim();
                if (!text.isEmpty()) {
                    etInput.setText("");
                    sendMessage(text);
                }
            });
        }

        // Back button
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    // ── Sending a message ──────────────────────────────────────

    private void sendMessage(String userText) {
        addMessage(userText, true);
        saveToFirestore(userText, true);
        showTypingIndicator();

        String question = (cityName != null && !cityName.isEmpty())
                ? "In " + cityName + ": " + userText
                : userText;

        chatbot.sendMessage(question, sessionId, new ChatbotClient.ChatCallback() {
            @Override
            public void onSuccess(com.example.travelaks.data.model.ChatResponse response) {
                sessionId = response.session_id;
                runOnUiThread(() -> {
                    removeTypingIndicator();
                    addMessage(response.answer, false);
                    saveToFirestore(response.answer, false);
                });
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Chatbot error: " + error);
                runOnUiThread(() -> {
                    removeTypingIndicator();
                    addMessage("⚠️ Could not reach the assistant. Please check your connection and try again.", false);
                });
            }
        });
    }

    private void showTypingIndicator() {
        messages.add(Message.typingIndicator());
        chatAdapter.notifyItemInserted(messages.size() - 1);
        if (recyclerView != null) recyclerView.scrollToPosition(messages.size() - 1);
    }

    private void removeTypingIndicator() {
        for (int i = messages.size() - 1; i >= 0; i--) {
            if (messages.get(i).isTyping()) {
                messages.remove(i);
                chatAdapter.notifyItemRemoved(i);
                return;
            }
        }
    }

    private void addMessage(String text, boolean isUser) {
        messages.add(new Message(text, isUser));
        chatAdapter.notifyItemInserted(messages.size() - 1);
        if (recyclerView != null) {
            recyclerView.scrollToPosition(messages.size() - 1);
        }
    }

    // ── Firestore persistence ──────────────────────────────────

    private void createFirestoreSession() {
        try {
            String uid = FirebaseAuth.getInstance().getCurrentUser() != null
                    ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                    : "anonymous";

            Map<String, Object> session = new HashMap<>();
            session.put("userId", uid);
            session.put("city", cityName != null ? cityName : "general");
            session.put("startedAt", Timestamp.now());

            db.collection("chatSessions").add(session)
                .addOnSuccessListener(ref -> firestoreSessionId = ref.getId())
                .addOnFailureListener(e -> Log.e(TAG, "Session create failed: " + e.getMessage()));
        } catch (Exception e) {
            Log.e(TAG, "createFirestoreSession error: " + e.getMessage());
        }
    }

    private void saveToFirestore(String text, boolean isUser) {
        if (firestoreSessionId == null) return;
        try {
            Map<String, Object> msg = new HashMap<>();
            msg.put("text", text);
            msg.put("isUser", isUser);
            msg.put("timestamp", Timestamp.now());

            db.collection("chatSessions").document(firestoreSessionId)
                .collection("messages").add(msg)
                .addOnFailureListener(e -> Log.e(TAG, "Save message failed: " + e.getMessage()));
        } catch (Exception e) {
            Log.e(TAG, "saveToFirestore error: " + e.getMessage());
        }
    }
}
