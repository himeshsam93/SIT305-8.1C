package com.example.llmchatbot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Message> messages;

    int USER = 1;
    int BOT = 2;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {

        if(messages.get(position).sender.equals("user")) {
            return USER;
        } else {
            return BOT;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        if(viewType == USER) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_user,
                            parent,
                            false);

            return new UserViewHolder(view);

        } else {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_bot,
                            parent,
                            false);

            return new BotViewHolder(view);

        }

    }

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder,
            int position) {

        Message message = messages.get(position);

        if(holder instanceof UserViewHolder) {

            ((UserViewHolder) holder).txtMessage
                    .setText(message.text);

            ((UserViewHolder) holder).txtTime
                    .setText(message.timestamp);

        } else {

            ((BotViewHolder) holder).txtMessage
                    .setText(message.text);

            ((BotViewHolder) holder).txtTime
                    .setText(message.timestamp);

        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class UserViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtMessage, txtTime;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            txtMessage =
                    itemView.findViewById(R.id.txtMessage);

            txtTime =
                    itemView.findViewById(R.id.txtTime);
        }
    }

    static class BotViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtMessage, txtTime;

        public BotViewHolder(@NonNull View itemView) {
            super(itemView);

            txtMessage =
                    itemView.findViewById(R.id.txtMessage);

            txtTime =
                    itemView.findViewById(R.id.txtTime);
        }
    }
}