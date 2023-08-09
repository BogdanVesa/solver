package com.example.solver;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private EditText display;
    PopUp newFragment = new PopUp();
    String equation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.displayEditText);

        display.setShowSoftInputOnFocus(false);


        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        openPopup();
    }

    private void updateText(String strToAdd){
        String oldStr = display.getText().toString();

        int cursorPos =display.getSelectionStart();
        String leftStr = oldStr.substring(0 ,cursorPos);
        String rightStr = oldStr.substring(cursorPos);

        display.setText(String.format("%s%s%s", leftStr ,strToAdd, rightStr));
        display.setSelection(cursorPos + strToAdd.length());
    }



    public void zeroBTNPush(View view){
        updateText(getResources().getString(R.string.zeroText));
    }

    public void oneBTNPush(View view){
        updateText(getResources().getString(R.string.oneText));
    }

    public void twoBTNPush(View view){
        updateText(getResources().getString(R.string.twoText));
    }

    public void threeBTNPush(View view){
        updateText(getResources().getString(R.string.threeText));
    }

    public void fourBTNPush(View view){
        updateText(getResources().getString(R.string.fourText));
    }

    public void fiveBTNPush(View view){
        updateText(getResources().getString(R.string.fiveText));
    }

    public void sixBTNPush(View view){
        updateText(getResources().getString(R.string.sixText));
    }

    public void sevenBTNPush(View view){
        updateText(getResources().getString(R.string.sevenText));
    }

    public void eightBTNPush(View view){
        updateText(getResources().getString(R.string.eightText));
    }

    public void nineBTNPush(View view){
        updateText(getResources().getString(R.string.nineText));
    }

    public void multiplyBTNPush(View view){
        updateText(getResources().getString(R.string.multplyDotText));
    }

    public void divideBTNPush(View view){
        updateText(getResources().getString(R.string.divideText));
    }

    public void subtractBTNPush(View view){
        updateText(getResources().getString(R.string.subtractText));
    }
    public void addBTNPush(View view){
        updateText(getResources().getString(R.string.addText));
    }
    public void clearBTNPush(View view){
        display.setText("");
    }
    public void parOpenBTNPush(View view){
        updateText(getResources().getString(R.string.parenthesesOpenText));
    }

    public void parCloseBTNPush(View view){
        updateText(getResources().getString(R.string.parenthesesCloseText));
    }

    public void decimalBTNPush(View view){
        updateText(getResources().getString(R.string.decimalText));
    }

    public void variableBTNPush(View view){
        updateText(getResources().getString(R.string.variable));
    }

    public void variableSquareBTNPush(View view){
        updateText(getResources().getString(R.string.variable)+"^2");
    }

    public void backspaceBTN(View view){
        int cursPos = display.getSelectionStart();
        int textLen = display.getText().length();

        if(cursPos !=0 && textLen !=0){
            SpannableStringBuilder selection = (SpannableStringBuilder) display.getText();
            selection.replace(cursPos-1, cursPos, "");
            display.setText((selection));
            display.setSelection(cursPos-1);


        }
    }

    public void equalBTNPush(View view){
        updateText(getResources().getString(R.string.equalsText));
    }

    public void trigSinBTNPush(View view){
        updateText("sin(");
    }

    public void trigCosBTNPush(View view){
        updateText("cos(");
    }

    public void trigTanBTNPush(View view){
        updateText("tan(");
    }

    public void trigArcSinBTNPush(View view){
        updateText("arcsin(");
    }

    public void trigArcCosBTNPush(View view){
        updateText("arccos(");
    }

    public void trigArcTanBTNPush(View view){
        updateText("arctan(");
    }

    public void naturalLogBTNPush(View view){
        updateText("ln(");
    }

    public void logBTNPush(View view){
        updateText("log(");
    }

    public void sqrtBTNPush(View view){
        updateText("sqrt(");
    }

    public void absBTNPush(View view){
        updateText("abs(");
    }

    public void piBTNPush(View view){
        updateText("pi");
    }

    public void eBTNPush(View view){
        updateText("e");
    }

    public void xSquaredBTNPush(View view){
        updateText("^(2)");
    }

    public void xPowerYBTNPush(View view){
        updateText("^(");
    }

    public void primeBTNPush(View view){
        updateText("ispr(");
    }

    public void sidebarBtnPush(View view){
        DrawerLayout navDrawer = findViewById(R.id.draw);
        // If the navigation drawer is not open then open it, if its already open then close it.
        if(!navDrawer.isDrawerOpen(GravityCompat.START)) navDrawer.openDrawer(GravityCompat.START);
        else navDrawer.closeDrawer(GravityCompat.END);
    }

    public String rewrite(String txt){
        txt = txt.replaceAll(getResources().getString(R.string.variable),"x");
        txt = txt.replaceAll(getResources().getString(R.string.multplyDotText),"*");
        txt = txt.replaceAll(getResources().getString(R.string.divideText),"/");

        return txt;
    }
    public void solveBtnPush(View view){
        Context context = getApplicationContext();
        CharSequence text = newFragment.getSelectedItem();
        int duration = Toast.LENGTH_SHORT;


        Intent intent = new Intent(this, QuizActivity.class);
        String type;
        if(String.valueOf(text).equals("Second degree")){
            type = "second";
        }else {
            type = "first";
        }
        intent.putExtra("type",type);
        intent.putExtra("eq", rewrite(display.getText().toString()));
        startActivity(intent);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.type:
                openPopup();
                break;
            case R.id.gen_eq:
                Intent intent = new Intent(this, GenerateActivity.class);
                startActivity(intent);
                break;
//            case R.id.gen_test:
//                Intent intent1 = new Intent(this, GenerateTestActivity.class);
//                startActivity(intent1);
//                break;
            case R.id.didKnow:
                Intent intent2 = new Intent(this, DidYouKnow.class);
                startActivity(intent2);
                break;

        }
        return true;
    }

    public void openPopup(){
        newFragment.show(getSupportFragmentManager(), "game");
    }

}