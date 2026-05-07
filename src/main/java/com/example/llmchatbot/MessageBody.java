package com.example.llmchatbot;

public class MessageBody {

    private String role;
    private String content;

    public MessageBody(String role,
                       String content) {

        this.role = role;
        this.content = content;

    }

    public String getContent() {
        return content;
    }

}
