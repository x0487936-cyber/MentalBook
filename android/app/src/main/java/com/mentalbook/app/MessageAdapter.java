package com.mentalbook.app;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying chat messages with animations
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messages = new ArrayList<>();
    private OnMessageAnimationListener animationListener;
    
    // Typewriter effect settings
    private static final int TYPEWRITER_DELAY = 30; // ms per character
    private Handler typewriterHandler = new Handler(Looper.getMainLooper());

    public interface OnMessageAnimationListener {
        void onMessageAnimationStart(int position);
        void onMessageAnimationEnd(int position);
    }

    public void setOnMessageAnimationListener(OnMessageAnimationListener listener) {
        this.animationListener = listener;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.bind(message, position);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(Message message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    public void addMessageWithAnimation(Message message, int messageType, ViewGroup parent) {
        int position = messages.size();
        messages.add(message);
        notifyItemInserted(position);
        
        // Trigger animation callback
        if (animationListener != null) {
            animationListener.onMessageAnimationStart(position);
        }
    }

    public void clearMessages() {
        messages.clear();
        notifyDataSetChanged();
    }

    /**
     * Apply typewriter effect to bot message
     */
    public void applyTypewriterEffect(TextView textView, String fullText, Runnable onComplete) {
        textView.setText("");
        final int[] currentIndex = {0};
        
        Runnable characterAdder = new Runnable() {
            @Override
            public void run() {
                if (currentIndex[0] < fullText.length()) {
                    textView.setText(fullText.substring(0, currentIndex[0] + 1));
                    currentIndex[0]++;
                    typewriterHandler.postDelayed(this, TYPEWRITER_DELAY);
                } else if (onComplete != null) {
                    onComplete.run();
                }
            }
        };
        
        typewriterHandler.post(characterAdder);
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout userContainer;
        private LinearLayout botContainer;
        private TextView tvUserMessage;
        private TextView tvBotMessage;
        private TextView tvUserTime;
        private TextView tvBotTime;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            userContainer = itemView.findViewById(R.id.userMessageContainer);
            botContainer = itemView.findViewById(R.id.botMessageContainer);
            tvUserMessage = itemView.findViewById(R.id.tvUserMessage);
            tvBotMessage = itemView.findViewById(R.id.tvBotMessage);
            tvUserTime = itemView.findViewById(R.id.tvUserTime);
            tvBotTime = itemView.findViewById(R.id.tvBotTime);
        }

        void bind(Message message, int position) {
            // Apply slide animation based on message type
            Animation animation;
            
            if (message.getType() == Message.TYPE_USER) {
                userContainer.setVisibility(View.VISIBLE);
                botContainer.setVisibility(View.GONE);
                tvUserMessage.setText(message.getText());
                tvUserTime.setText(message.getTime());
                
                // Apply slide in from right animation
                animation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.slide_in_right);
            } else {
                userContainer.setVisibility(View.GONE);
                botContainer.setVisibility(View.VISIBLE);
                tvBotMessage.setText(message.getText());
                tvBotTime.setText(message.getTime());
                
                // Apply slide in from left animation
                animation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.slide_in_left);
            }
            
            // Start the animation
            itemView.startAnimation(animation);
        }
    }
}

