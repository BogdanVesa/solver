package com.example.solver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {


    String eq;
    Context context;
    int duration;
    int ok=0;
    String type;
    ArrayList<String> equations = new ArrayList<String>();
    ArrayList<Question> questions = new ArrayList<Question>();
    TextView question;
    TextView option1;
    TextView option2;
    TextView option3;
    TextView option4;
    Button submit;
    TextView optionSelected;
    int pos = 0;
    int posQ= 0;
    int nrCorrect = 0;
    RecyclerView recyclerView;
    E_Adapter adapter;
    boolean finish = false;

    ArrayList<EandQ> resolve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        question = findViewById(R.id.question);
        option1 =findViewById(R.id.option1);
        option2 =findViewById(R.id.option2);
        option3 =findViewById(R.id.option3);
        option4 =findViewById(R.id.option4);
        submit = findViewById(R.id.btn_submit);

        recyclerView = findViewById(R.id.recycleView);



        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        context = getApplicationContext();
        duration = Toast.LENGTH_SHORT;

        if(b!=null)
        {
            String j =(String) b.get("eq");
            String i =(String) b.get("type");
            Toast toast = Toast.makeText(context, i, duration);
            toast.show();
            eq = j;
            type = i;
        }
        Equation e = new Equation(eq);
        e.setType();
        resolve = e.solve();
        setEandQ();
    }


    public void setEandQ(){
        EandQ previous = null;

        if(pos>resolve.size()-1){
            submit.setText("Mergi inapoi");
            String textFinal= nrCorrect+"/"+questions.size() + "Raspunsuri corecte";
            question.setText(textFinal);
            removeButtons();
            finish = true;
            return;
        }
        EandQ e = resolve.get(pos);
        if(pos>0){
            previous = resolve.get(pos-1);
        }
        if(equations.size()==0){
            equations.add(e.eq);
            adapter = new E_Adapter(this, equations);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        if(previous!=null && !previous.eq.equals(e.eq)){
            equations.add(e.eq);
            adapter.notifyItemInserted(questions.size());
        }
        if(e.q.answer!=null){
            questions.add(e.q);
            addButtons();
            setQuestions();
        }else{
            removeButtons();
            pos+=1;
            setEandQ();
            return;
        }
        pos+=1;
    }

    void removeButtons(){
        option1.setVisibility(View.GONE);
        option2.setVisibility(View.GONE);
        option3.setVisibility(View.GONE);
        option4.setVisibility(View.GONE);
    }
    void addButtons(){
        option1.setVisibility(View.VISIBLE);
        option2.setVisibility(View.VISIBLE);
        option3.setVisibility(View.VISIBLE);
        option4.setVisibility(View.VISIBLE);
    }
    public void setQuestions(){
        if(questions.size()>0){
            question.setText(questions.get(posQ).q);
            option1.setText( questions.get(posQ).option1);
            option2.setText( questions.get(posQ).option2);
            option3.setText( questions.get(posQ).option3);
            option4.setText( questions.get(posQ).option4);
        }
    }

    public void submitBTNPush(View view){
        if(finish && submit.getText().equals("Mergi inapoi")){
            finish();
            return;
        }

        if(ok==0 && posQ < questions.size() && !finish) {

            String answer = optionSelected.getText().toString();
            if (answer.equals(questions.get(posQ).answer)) {
                optionSelected.setBackground(ContextCompat.getDrawable(this, R.drawable.correct_answer));
                nrCorrect+=1;
                posQ+=1;
            }
            else{
                TextView correctAnswer = getCorrectAnswer();
                correctAnswer.setBackground(ContextCompat.getDrawable(this, R.drawable.correct_answer));
                posQ+=1;
            }
            submit.setText("Next question");
            ok=1;
            return;
        }
        else{
                submit.setText("Submit");
                ok=0;
                defaultOption();
                setEandQ();
                return;
            }

    }


    public void defaultOption(){
        option1.setTextColor(getResources().getColor(R.color.tanAccent));
        option1.setBackground(ContextCompat.getDrawable(this, R.drawable.option_button));
        option2.setTextColor(getResources().getColor(R.color.tanAccent));
        option2.setBackground(ContextCompat.getDrawable(this, R.drawable.option_button));
        option3.setTextColor(getResources().getColor(R.color.tanAccent));
        option3.setBackground(ContextCompat.getDrawable(this, R.drawable.option_button));
        option4.setTextColor(getResources().getColor(R.color.tanAccent));
        option4.setBackground(ContextCompat.getDrawable(this, R.drawable.option_button));
    }
    public TextView getCorrectAnswer(){
        if(questions.get(posQ).option1.equals(questions.get(posQ).answer)){
            return option1;
        }
        if(questions.get(posQ).option2.equals(questions.get(posQ).answer)){
            return option2;
        }
        if(questions.get(posQ).option3.equals(questions.get(posQ).answer)){
            return option3;
        }
        if(questions.get(posQ).option4.equals(questions.get(posQ).answer)){
            return option4;
        }
        return null;
    }
    public void showAnswer(View view){
        TextView correctAnswer = getCorrectAnswer();
        correctAnswer.setBackground(ContextCompat.getDrawable(this, R.drawable.correct_answer));
        posQ+=1;
        submit.setText("Next question");
        ok=1;
    }

    public void option1BTNPush(View view){
        defaultOption();
        TextView text = findViewById(R.id.option1);
        optionSelected = text;
        text.setTextColor(Color.BLACK);
        text.setBackground(ContextCompat.getDrawable(this, R.drawable.option_selected));
    }
    public void option2BTNPush(View view){
        defaultOption();
        TextView text = findViewById(R.id.option2);
        optionSelected = text;
        text.setTextColor(Color.BLACK);
        text.setBackground(ContextCompat.getDrawable(this, R.drawable.option_selected));
    }
    public void option3BTNPush(View view){
        defaultOption();
        TextView text = findViewById(R.id.option3);
        optionSelected = text;
        text.setTextColor(Color.BLACK);
        text.setBackground(ContextCompat.getDrawable(this, R.drawable.option_selected));
    }
    public void option4BTNPush(View view){
        defaultOption();
        TextView text = findViewById(R.id.option4);
        optionSelected = text;
        text.setTextColor(Color.BLACK);
        text.setBackground(ContextCompat.getDrawable(this, R.drawable.option_selected));
    }
}