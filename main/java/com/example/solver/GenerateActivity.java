package com.example.solver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class GenerateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinner;
    String equationSelected;
    Button generate;
    ArrayList<String> equations = new ArrayList<String>();
    String[] types;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate2);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.types_equation, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        types = getResources().getStringArray(R.array.types_equation);
        if (savedInstanceState != null) {
            equations = savedInstanceState.getStringArrayList("equations");

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        equationSelected = parent.getItemAtPosition(position).toString();
        int duration = Toast.LENGTH_SHORT;
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, equationSelected, duration);
        toast.show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("it","works");
        outState.putStringArrayList("equations", equations );
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Restore the data
        setEquations();

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        String works = savedInstanceState.getString("it");
        int duration = Toast.LENGTH_SHORT;
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, works, duration);
        toast.show();

        setEquations();

    }

    public void onGenerateBTNPush(View view){
        if(equationSelected.equals(types[0])) {
            GenerateFirst gen1 = new GenerateFirst();
            equations = gen1.generate(10);
        }
        if(equationSelected.equals(types[1])){
            GenerateSecond gen2 = new GenerateSecond();
            equations = gen2.generate(10);
        }
        setEquations();
//        RecyclerView recyclerView = findViewById(R.id.recycleView);
//
//        E_Adapter adapter = new E_Adapter(this, equations);
//
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter.setOnClickListener(new E_Adapter.OnClickListener() {
//            @Override
//            public void onClick(int position,  String eq) {
//                Intent intent = new Intent(GenerateActivity.this, QuizActivity.class);
//                intent.putExtra("eq",eq);
//                if(equationSelected.equals(types[1])){
//                    intent.putExtra("type","second");
//                }else{
//                    intent.putExtra("type","first");
//                }
//                startActivity(intent);
//            }
//        });


    }

    public void setEquations(){
        RecyclerView recyclerView = findViewById(R.id.recycleView);

        E_Adapter adapter = new E_Adapter(this, equations);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnClickListener(new E_Adapter.OnClickListener() {
            @Override
            public void onClick(int position,  String eq) {
                Intent intent = new Intent(GenerateActivity.this, QuizActivity.class);
                intent.putExtra("eq",eq);
                if(equationSelected.equals(types[1])){
                    intent.putExtra("type","second");
                }else{
                    intent.putExtra("type","first");
                }
                startActivity(intent);
            }
        });

    }

    public void HomeBTN(View view){
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
        finish();
    }


}