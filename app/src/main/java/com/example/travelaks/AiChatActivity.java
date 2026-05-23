package com.example.travelaks;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelaks.data.model.ChatResponse;
import com.example.travelaks.data.model.Message;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AiChatActivity extends AppCompatActivity {

    private static final String TAG = "AiChatActivity";

    private ChatAdapter chatAdapter;
    private ArrayList<Message> messages;
    private RecyclerView recyclerView;
    private EditText etInput;

    // Backend chatbot client
    private final ChatbotClient chatbot = new ChatbotClient();
    private String sessionId = null; // null = new session; updated on first response

    // City context — prefixed to every user message sent to the backend
    private String cityName;

    // Firestore persistence
    private FirebaseFirestore db;
    private String firestoreSessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_box_layout);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recycler_messages);
        etInput      = findViewById(R.id.edit_message);
        Button btnSend = findViewById(R.id.btn_send);
        Button btnBack = findViewById(R.id.btn_back);
        TextView tvTitle = findViewById(R.id.tv_title);

        cityName = getIntent().getStringExtra("city_name");
        if (tvTitle != null)
            tvTitle.setText(cityName != null ? "AI – " + cityName : "AI Assistant");

        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        // Greeting — not sent to backend, purely UI
        String greeting = cityName != null
            ? "Hello! I'm your AI travel assistant for " + cityName + ". Ask me anything — hotels, attractions, activities, tips, and more! 🌟"
            : "Hello! I'm your Travel KSA assistant. Ask me anything about Saudi Arabia's cities, hotels, attractions, and activities!";
        addMessage(greeting, false);

        createFirestoreSession();

        btnSend.setOnClickListener(v -> {
            String text = etInput.getText().toString().trim();
            if (!text.isEmpty()) {
                etInput.setText("");
                sendMessage(text);
            }
        });

        if (btnBack != null) btnBack.setOnClickListener(v -> finish());
    }

    private void sendMessage(String userText) {
        // Show user message immediately
        addMessage(userText, true);
        saveToFirestore(userText, true);

        // Prefix with city context if applicable
        String question = (cityName != null && !cityName.isEmpty())
                ? "In " + cityName + ": " + userText
                : userText;

        // Call backend — pass sessionId (null on first call = new session)
        chatbot.sendMessage(question, sessionId, new ChatbotClient.ChatCallback() {
            @Override
            public void onSuccess(ChatResponse response) {
                sessionId = response.session_id; // save for next turn
                runOnUiThread(() -> {
                    addMessage(response.answer, false);
                    saveToFirestore(response.answer, false);
                });
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Chatbot error: " + error);
                runOnUiThread(() -> addMessage("⚠️ " + error + ". Please try again.", false));
            }
        });
    }

    private void addMessage(String text, boolean isUser) {
        messages.add(new Message(text, isUser));
        chatAdapter.notifyItemInserted(messages.size() - 1);
        recyclerView.scrollToPosition(messages.size() - 1);
    }

    // ── Firestore persistence ──────────────────────────────────

    private void createFirestoreSession() {
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
    }

    private void saveToFirestore(String text, boolean isUser) {
        if (firestoreSessionId == null) return;

        Map<String, Object> msg = new HashMap<>();
        msg.put("text", text);
        msg.put("isUser", isUser);
        msg.put("timestamp", Timestamp.now());

        db.collection("chatSessions").document(firestoreSessionId)
            .collection("messages").add(msg)
            .addOnFailureListener(e -> Log.e(TAG, "Save message failed: " + e.getMessage()));
    }
}
