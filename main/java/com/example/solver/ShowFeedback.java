package com.example.solver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ShowFeedback extends AppCompatActivity {
    TextView text;
    String eq;
    double a;
    double b;
    double c;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_feedback);
        text = findViewById(R.id.numberQuestions);
        Intent in= getIntent();
        Bundle bundle = in.getExtras();
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        if(bundle!=null)
        {
            a =(double) bundle.get("a");
            b =(double) bundle.get("b");
            c =(double) bundle.get("c");
            type = (String) bundle.get("type");
            int correct =(int) bundle.get("correct");
            int questions = (int) bundle.get("questions");
            String number = String.valueOf(correct)+"/"+String.valueOf(questions);
            text.setText(number);
        }
    }

    public void homeBTNPush(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}