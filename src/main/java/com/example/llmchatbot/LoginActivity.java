package com.example.llmchatbot;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername;
    Button btnGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        btnGo = findViewById(R.id.btnGo);

        btnGo.setOnClickListener(v -> {

            String username = etUsername.getText().toString();

            if(username.isEmpty()) {

                Toast.makeText(this,
                        "Enter Username",
                        Toast.LENGTH_SHORT).show();

            } else {

                Intent intent =
                        new Intent(LoginActivity.this,
                                ChatActivity.class);

                intent.putExtra("username", username);

                startActivity(intent);

            }

        });

    }
}