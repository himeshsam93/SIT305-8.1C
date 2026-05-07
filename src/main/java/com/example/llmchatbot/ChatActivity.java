package com.example.llmchatbot;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText etMessage;
    Button btnSend;

    List<Message> messages = new ArrayList<>();

    MessageAdapter adapter;

    AppDatabase database;

    OpenAIApi api;

    // PUT YOUR API KEY HERE
    String API_KEY = "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";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recyclerView);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);

        adapter = new MessageAdapter(messages);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerView.setAdapter(adapter);

        // ROOM DATABASE

        database = Room.databaseBuilder(
                        getApplicationContext(),
                        AppDatabase.class,
                        "chat_db"
                )
                .allowMainThreadQueries()
                .build();

        // LOAD OLD CHATS

        loadMessages();

        // RETROFIT API

        api = RetrofitClient
                .getClient()
                .create(OpenAIApi.class);

        // SEND BUTTON

        btnSend.setOnClickListener(v -> {

            String userText =
                    etMessage.getText().toString().trim();

            if(!userText.isEmpty()) {

                // USER MESSAGE

                Message userMessage =
                        new Message(
                                userText,
                                "user",
                                getCurrentTime()
                        );

                messages.add(userMessage);

                database.messageDao()
                        .insertMessage(userMessage);

                adapter.notifyDataSetChanged();

                recyclerView.scrollToPosition(
                        messages.size() - 1
                );

                etMessage.setText("");

                // SEND TO OPENAI

                sendMessageToOpenAI();

            }

        });

    }

    // SEND MESSAGE TO OPENAI

    private void sendMessageToOpenAI() {

        List<MessageBody> chatMessages =
                new ArrayList<>();

        // SEND PREVIOUS CHAT HISTORY

        for(Message msg : messages) {

            if(msg.sender.equals("user")) {

                chatMessages.add(
                        new MessageBody(
                                "user",
                                msg.text
                        )
                );

            } else {

                chatMessages.add(
                        new MessageBody(
                                "assistant",
                                msg.text
                        )
                );

            }

        }

        // REQUEST

        ChatRequest request =
                new ChatRequest(
                        "gpt-4o-mini",
                        chatMessages
                );

        // API CALL

        Call<ChatResponse> call =
                api.getChatResponse(
                        "Bearer " + API_KEY,
                        request
                );

        call.enqueue(new Callback<ChatResponse>() {

            @Override
            public void onResponse(
                    Call<ChatResponse> call,
                    Response<ChatResponse> response) {

                try {

                    if(response.isSuccessful()
                            && response.body() != null
                            && response.body().getChoices() != null
                            && !response.body().getChoices().isEmpty()) {

                        String reply =
                                response.body()
                                        .getChoices()
                                        .get(0)
                                        .getMessage()
                                        .getContent();

                        Message botMessage =
                                new Message(
                                        reply,
                                        "bot",
                                        getCurrentTime()
                                );

                        messages.add(botMessage);

                        database.messageDao()
                                .insertMessage(botMessage);

                        adapter.notifyDataSetChanged();

                        recyclerView.scrollToPosition(
                                messages.size() - 1
                        );

                    } else {

                        String error =
                                "API Error: "
                                        + response.code();

                        Message errorMessage =
                                new Message(
                                        error,
                                        "bot",
                                        getCurrentTime()
                                );

                        messages.add(errorMessage);

                        adapter.notifyDataSetChanged();

                        Log.e("OPENAI_ERROR",
                                error);

                    }

                } catch (Exception e) {

                    e.printStackTrace();

                    Message errorMessage =
                            new Message(
                                    "Parsing Error",
                                    "bot",
                                    getCurrentTime()
                            );

                    messages.add(errorMessage);

                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onFailure(
                    Call<ChatResponse> call,
                    Throwable t) {

                t.printStackTrace();

                Message errorMessage =
                        new Message(
                                "Failure: " + t.getMessage(),
                                "bot",
                                getCurrentTime()
                        );

                messages.add(errorMessage);

                adapter.notifyDataSetChanged();

                Log.e("OPENAI_FAILURE",
                        t.getMessage());

            }

        });

    }

    // LOAD OLD MESSAGES

    private void loadMessages() {

        List<Message> savedMessages =
                database.messageDao()
                        .getAllMessages();

        messages.addAll(savedMessages);

        adapter.notifyDataSetChanged();

    }

    // TIME

    private String getCurrentTime() {

        SimpleDateFormat sdf =
                new SimpleDateFormat(
                        "hh:mm a",
                        Locale.getDefault()
                );

        return sdf.format(new Date());

    }

}