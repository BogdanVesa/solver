package com.example.solver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DidYouKnow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_did_you_know);
    }
    public void HomeBTN(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}