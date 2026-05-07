package com.example.llmchatbot;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "messages")
public class Message {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String text;
    public String sender;
    public String timestamp;

    public Message(String text,
                   String sender,
                   String timestamp) {

        this.text = text;
        this.sender = sender;
        this.timestamp = timestamp;

    }
}
