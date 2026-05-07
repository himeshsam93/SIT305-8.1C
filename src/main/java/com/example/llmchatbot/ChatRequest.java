
package com.example.llmchatbot;

import java.util.List;

public class ChatRequest {

    private String model;
    private List<MessageBody> messages;

    public ChatRequest(String model,
                       List<MessageBody> messages) {

        this.model = model;
        this.messages = messages;

    }

}