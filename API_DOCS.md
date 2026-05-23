# Travel KSA Chatbot — API Documentation & Android Integration Guide

---

## Base URL

| Environment | URL |
|---|---|
| Local | `http://10.0.2.2:8000` (Android emulator) / `http://localhost:8000` (device on same network) |
| Production | Your Render URL, e.g. `https://travel-ksa.onrender.com` |

---

## Authentication

Every request must include the following header:

```
X-API-Key: TRAVEL_KSA_APP
```

Missing or wrong key returns `403 Forbidden`.

---

## Endpoints

### 1. `GET /health`

Check that the server is running. No auth required.

**Response**
```json
{ "status": "ok" }
```

---

### 2. `POST /chat`

Send a user message and receive an AI answer. The server queries the Knowledge Base and Firestore automatically — the app only ever calls this one endpoint.

**Request headers**
```
Content-Type: application/json
X-API-Key: TRAVEL_KSA_APP
```

**Request body**

| Field | Type | Required | Description |
|---|---|---|---|
| `question` | string | Yes | The user's message |
| `session_id` | string | No | UUID from a previous response. Pass it to maintain conversation context across turns. Omit (or send `null`) to start a new conversation. |

**Example request — first message**
```json
{
  "question": "What are the best hotels in Jeddah?"
}
```

**Example request — follow-up**
```json
{
  "question": "Which one is the cheapest?",
  "session_id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
}
```

**Response body**

| Field | Type | Description |
|---|---|---|
| `status` | string | Always `"answered"` |
| `answer` | string | The AI's reply in Markdown format |
| `session_id` | string | UUID — save this and pass it in the next request to continue the conversation |

**Example response**
```json
{
  "status": "answered",
  "answer": "Jeddah offers a range of excellent hotels:\n\n- **Rosewood Jeddah** — rated 4.8 ⭐, SAR 1,200/night\n- **Park Hyatt Jeddah** — rated 4.7 ⭐, SAR 950/night\n- **Jeddah Hilton** — rated 4.5 ⭐, SAR 750/night",
  "session_id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
}
```

---

## Error Responses

| HTTP Status | Meaning |
|---|---|
| `403` | Missing or invalid `X-API-Key` header |
| `422` | Malformed request body (e.g. missing `question`) |
| `429` | All AI model groups rate-limited — retry after a few seconds |
| `502` | Firestore query failed or AI provider unreachable |

---

## Android Integration

### 1. Add OkHttp (or Retrofit) to `build.gradle`

```kotlin
dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    // Optional: Gson for JSON parsing
    implementation("com.google.code.gson:gson:2.10.1")
}
```

Add internet permission to `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### 2. Create a data model

```java
// ChatRequest.java
public class ChatRequest {
    public String question;
    public String session_id; // null for first message

    public ChatRequest(String question, String sessionId) {
        this.question = question;
        this.session_id = sessionId;
    }
}

// ChatResponse.java
public class ChatResponse {
    public String status;
    public String answer;
    public String session_id;
}
```

### 3. Create a ChatbotClient helper

```java
import okhttp3.*;
import com.google.gson.Gson;
import java.io.IOException;

public class ChatbotClient {

    private static final String BASE_URL = "https://travel-ksa.onrender.com"; // replace with your URL
    private static final String API_KEY  = "TRAVEL_KSA_APP";
    private static final MediaType JSON  = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient http = new OkHttpClient();
    private final Gson gson = new Gson();

    public interface Callback {
        void onSuccess(ChatResponse response);
        void onError(String error);
    }

    public void sendMessage(String question, String sessionId, Callback callback) {
        ChatRequest req = new ChatRequest(question, sessionId);
        String body = gson.toJson(req);

        Request request = new Request.Builder()
                .url(BASE_URL + "/chat")
                .addHeader("X-API-Key", API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(body, JSON))
                .build();

        http.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String raw = response.body().string();
                if (!response.isSuccessful()) {
                    callback.onError("Server error " + response.code() + ": " + raw);
                    return;
                }
                ChatResponse res = gson.fromJson(raw, ChatResponse.class);
                callback.onSuccess(res);
            }
        });
    }
}
```

### 4. Use it in `AiChatActivity`

```java
public class AiChatActivity extends AppCompatActivity {

    private ChatbotClient chatbot = new ChatbotClient();
    private String sessionId = null; // null until first response

    private void onUserSendMessage(String userText) {
        // Show user message in UI...

        chatbot.sendMessage(userText, sessionId, new ChatbotClient.Callback() {
            @Override
            public void onSuccess(ChatResponse response) {
                sessionId = response.session_id; // save for next turn
                runOnUiThread(() -> {
                    // Display response.answer in the chat UI
                    // The answer is Markdown — use a Markdown renderer if desired
                    showBotMessage(response.answer);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> showBotMessage("Sorry, something went wrong. Please try again."));
            }
        });
    }

    private void onNewChatSession() {
        sessionId = null; // reset to start a fresh conversation
    }
}
```

---

## Conversation Sessions

- The first request should have no `session_id` (omit the field or send `null`).
- Every response returns a `session_id` — store it in your Activity/ViewModel.
- Pass the same `session_id` in all follow-up messages so the AI remembers context.
- Sessions expire after **30 minutes** of inactivity. After that, send `null` to start fresh.
- To explicitly start a new chat (e.g. user taps "New Chat"), reset `sessionId = null`.

---

## Answer Rendering

The `answer` field is formatted in **Markdown**. You can render it natively using the [Markwon](https://github.com/noties/Markwon) library:

```kotlin
// build.gradle
implementation("io.noties.markwon:core:4.6.2")
```

```java
Markwon markwon = Markwon.create(context);
markwon.setMarkdown(tvBotMessage, response.answer);
```

---

## City Context (Recommended)

When the user opens a city-specific chat screen, prefix the question with the city name so the AI applies the correct filter automatically:

```java
// Instead of just: "What hotels are available?"
// Send:
String question = "In " + cityName + ": " + userInput;
// e.g. "In Riyadh: What hotels are available?"
```

This ensures Firestore queries are scoped to the right city without requiring the user to type it explicitly.
