package com.example.llmchatbot;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MessageDao {

    @Insert
    void insertMessage(Message message);

    @Query("SELECT * FROM messages")
    List<Message> getAllMessages();

}
