package com.pm.projetpkmn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launch(View view) {
        Intent intent = new Intent(this, Game.class);
        EditText et = findViewById(R.id.input);
        String x;
        if(!et.getText().toString().equals("")) {
            x = et.getText().toString();
            intent.putExtra("pseudo", x);
        }
        startActivity(intent);
    }
}