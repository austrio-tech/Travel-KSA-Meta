package com.example.travelaks;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelaks.data.model.Message;

import java.util.ArrayList;

import io.noties.markwon.Markwon;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_AI     = 0;
    private static final int TYPE_USER   = 1;
    private static final int TYPE_TYPING = 2;

    private final ArrayList<Message> messages;
    private final Markwon markwon;

    public ChatAdapter(Context context, ArrayList<Message> messages) {
        this.messages = messages;
        this.markwon  = Markwon.create(context);
    }

    @Override
    public int getItemViewType(int position) {
        Message m = messages.get(position);
        if (m.isTyping()) return TYPE_TYPING;
        return m.isUser() ? TYPE_USER : TYPE_AI;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_USER) {
            return new TextViewHolder(inf.inflate(R.layout.item_chat_message_user, parent, false));
        } else if (viewType == TYPE_TYPING) {
            return new TypingViewHolder(inf.inflate(R.layout.item_typing_indicator, parent, false));
        } else {
            return new TextViewHolder(inf.inflate(R.layout.item_chat_message, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TypingViewHolder) {
            ((TypingViewHolder) holder).startAnimation();
        } else if (holder instanceof TextViewHolder) {
            Message m = messages.get(position);
            TextViewHolder tvh = (TextViewHolder) holder;
            if (m.isUser()) {
                // Plain text for user messages
                tvh.messageText.setText(m.getText());
            } else {
                // Render Markdown for AI responses
                markwon.setMarkdown(tvh.messageText, m.getText());
            }
        }
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof TypingViewHolder) {
            ((TypingViewHolder) holder).stopAnimation();
        }
    }

    @Override
    public int getItemCount() { return messages.size(); }

    // ── Text message ViewHolder ──────────────────────────────
    static class TextViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
        }
    }

    // ── Typing indicator ViewHolder ──────────────────────────
    static class TypingViewHolder extends RecyclerView.ViewHolder {
        View dot1, dot2, dot3;
        ObjectAnimator anim1, anim2, anim3;

        TypingViewHolder(@NonNull View itemView) {
            super(itemView);
            dot1 = itemView.findViewById(R.id.dot1);
            dot2 = itemView.findViewById(R.id.dot2);
            dot3 = itemView.findViewById(R.id.dot3);
        }

        void startAnimation() {
            anim1 = bounce(dot1, 0);
            anim2 = bounce(dot2, 150);
            anim3 = bounce(dot3, 300);
        }

        void stopAnimation() {
            if (anim1 != null) anim1.cancel();
            if (anim2 != null) anim2.cancel();
            if (anim3 != null) anim3.cancel();
        }

        private ObjectAnimator bounce(View dot, long delay) {
            ObjectAnimator anim = ObjectAnimator.ofFloat(dot, "translationY", 0f, -14f, 0f);
            anim.setDuration(500);
            anim.setStartDelay(delay);
            anim.setRepeatCount(ObjectAnimator.INFINITE);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.start();
            return anim;
        }
    }
}
