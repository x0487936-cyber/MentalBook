package com.mentalbook.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Main Activity for MentalBook Android App
 * Connects to web backend for AI responses
 * Includes animations for splash transitions, messages, and loading
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView rvMessages;
    private RecyclerView rvTopics;
    private EditText etMessage;
    private Button btnSend;
    private Button btnNewChat;
    private Button btnClear;
    private FrameLayout loadingOverlay;
    private ProgressBar progressBar;
    private TextView tvTurns;
    private TextView tvMood;
    private View sidebar;
    private View chatArea;
    private View inputArea;

    private MessageAdapter messageAdapter;
    private TopicAdapter topicAdapter;
    
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private int turnCount = 0;
    private String currentMood = "Neutral";
    
    // Typewriter effect settings
    private static final int TYPEWRITER_DELAY = 25;
    private Handler typewriterHandler = new Handler(Looper.getMainLooper());

    // Base URL for web API - change to your server URL
    private static final String BASE_URL = "http://localhost:8080/api/chat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerViews();
        setupClickListeners();
        setupAnimations();
        
        // Show welcome message
        addWelcomeMessage();
    }

    private void initViews() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("VirtualXander");

        rvMessages = findViewById(R.id.rvMessages);
        rvTopics = findViewById(R.id.rvTopics);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        btnNewChat = findViewById(R.id.btnNewChat);
        btnClear = findViewById(R.id.btnClear);
        loadingOverlay = findViewById(R.id.loadingOverlay);
        progressBar = findViewById(R.id.progressBar);
        tvTurns = findViewById(R.id.tvTurns);
        tvMood = findViewById(R.id.tvMood);
        
        // Get animated views
        sidebar = findViewById(R.id.sidebar);
        chatArea = findViewById(R.id.chatArea);
        inputArea = findViewById(R.id.inputArea);
    }

    private void setupRecyclerViews() {
        // Messages RecyclerView
        messageAdapter = new MessageAdapter();
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        rvMessages.setAdapter(messageAdapter);

        // Topics RecyclerView
        topicAdapter = new TopicAdapter();
        rvTopics.setLayoutManager(new LinearLayoutManager(this));
        rvTopics.setAdapter(topicAdapter);
    }

    private void setupClickListeners() {
        btnSend.setOnClickListener(v -> {
            // Button press animation
            animateButton(v);
            sendMessage();
        });
        
        btnNewChat.setOnClickListener(v -> {
            animateButton(v);
            newChat();
        });
        
        btnClear.setOnClickListener(v -> {
            animateButton(v);
            clearChat();
        });
        
        // Send on Enter key
        etMessage.setOnEditorActionListener((v, actionId, event) -> {
            sendMessage();
            return true;
        });
    }

    private void setupAnimations() {
        // Apply entry animations for UI elements
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideFromLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        Animation slideFromRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        
        // Stagger animations
        sidebar.startAnimation(slideFromLeft);
        
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            chatArea.startAnimation(fadeIn);
        }, 150);
        
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            inputArea.startAnimation(slideFromRight);
        }, 300);
    }

    private void animateButton(View v) {
        Animation scaleAnim = AnimationUtils.loadAnimation(this, R.anim.scale_in);
        v.startAnimation(scaleAnim);
    }

    private void addWelcomeMessage() {
        String welcome = "Hello! Welcome to VirtualXander - your friendly mental companion.\n\n" +
                "I'm here to chat, support you, and help with various tasks.\n" +
                "Feel free to share anything on your mind!";
        
        // Add with animation after a short delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            addBotMessageWithAnimation(welcome);
        }, 500);
    }

    private void sendMessage() {
        String message = etMessage.getText().toString().trim();
        if (message.isEmpty()) {
            return;
        }

        // Clear input
        etMessage.setText("");

        // Add user message with animation
        addUserMessageWithAnimation(message);

        // Show loading with animation
        showLoading(true);

        // Send to API
        new Thread(() -> {
            try {
                String response = sendToServer(message);
                mainHandler.post(() -> {
                    showLoading(false);
                    addBotMessageWithAnimation(response);
                    updateStats();
                });
            } catch (Exception e) {
                mainHandler.post(() -> {
                    showLoading(false);
                    addBotMessageWithAnimation("I encountered an error. Please try again.");
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void showLoading(boolean show) {
        if (show) {
            loadingOverlay.setVisibility(View.VISIBLE);
            loadingOverlay.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
            progressBar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pulse));
        } else {
            Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    loadingOverlay.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            loadingOverlay.startAnimation(fadeOut);
        }
    }

    private String sendToServer(String message) throws Exception {
        URL url = new URL(BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonBody = "{\"message\":\"" + message.replace("\"", "\\\"") + "\"}";
        
        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonBody.getBytes());
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();
        } else {
            throw new Exception("Server returned: " + responseCode);
        }
    }

    private void addUserMessage(String message) {
        String time = getCurrentTime();
        messageAdapter.addMessage(new Message(message, Message.TYPE_USER, time));
        rvMessages.scrollToPosition(messageAdapter.getItemCount() - 1);
    }

    private void addUserMessageWithAnimation(String message) {
        String time = getCurrentTime();
        Message msg = new Message(message, Message.TYPE_USER, time);
        messageAdapter.addMessageWithAnimation(msg, Message.TYPE_USER, rvMessages);
        
        // Scroll after animation
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            rvMessages.scrollToPosition(messageAdapter.getItemCount() - 1);
        }, 350);
    }

    private void addBotMessage(String message) {
        String time = getCurrentTime();
        messageAdapter.addMessage(new Message(message, Message.TYPE_BOT, time));
        rvMessages.scrollToPosition(messageAdapter.getItemCount() - 1);
    }

    private void addBotMessageWithAnimation(String message) {
        String time = getCurrentTime();
        Message msg = new Message(message, Message.TYPE_BOT, time);
        messageAdapter.addMessageWithAnimation(msg, Message.TYPE_BOT, rvMessages);
        
        // Scroll after animation
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            rvMessages.scrollToPosition(messageAdapter.getItemCount() - 1);
        }, 350);
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void updateStats() {
        turnCount++;
        tvTurns.setText("Turns: " + turnCount);
        
        // In a real app, this would come from the server
        tvMood.setText("Mood: " + currentMood);
    }

    private void newChat() {
        new AlertDialog.Builder(this)
                .setTitle("New Chat")
                .setMessage("Start a new conversation? This will clear the current chat.")
                .setPositiveButton("Yes", (dialog, which) -> {
                    messageAdapter.clearMessages();
                    topicAdapter.clearTopics();
                    turnCount = 0;
                    updateStats();
                    addWelcomeMessage();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void clearChat() {
        new AlertDialog.Builder(this)
                .setTitle("Clear Chat")
                .setMessage("Clear all messages?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    messageAdapter.clearMessages();
                    addWelcomeMessage();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showAbout() {
        String about = "MentalBook - Your Companion\n\n" +
                "Version 0.2.0.0\n\n" +
                "A friendly virtual companion app.\n\n" +
                "Created by Xander Thompson";
        
        new AlertDialog.Builder(this)
                .setTitle("About MentalBook")
                .setMessage(about)
                .setPositiveButton("OK", null)
                .show();
    }
}

