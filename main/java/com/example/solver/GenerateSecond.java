package com.example.solver;

import java.util.ArrayList;
import java.util.Random;

public class GenerateSecond implements Generate{

    @Override
    public ArrayList<String> generate(int number) {
        ArrayList<String> result = new ArrayList<String>();
        for(int i=0;i<number;i++){
            String tmp = generateEquation();
            result.add(tmp);
        }
        return result;
    }

    public String generateEquation(){
        String eq = "";
        int cox2;
        int cox;
        int coe;
        Random ran = new Random();
        switch (ran.nextInt(2)+1){
            case 1:
                cox2 = ran.nextInt(10)+1;
                cox = ran.nextInt(10);
                coe = ran.nextInt(10);

                if(positiveOrNegative().equals("+")){
                    eq = String.valueOf(cox2)+"x^2";
                }
                else{
                    eq = "-"+String.valueOf(cox2)+"x^2";
                }

                eq= eq + positiveOrNegative()+ String.valueOf(cox)+"x";
                eq = eq + positiveOrNegative() + String.valueOf(coe);
                eq = eq + "=0";
                break;
            case 2:
                cox2 = ran.nextInt(100);
                cox = ran.nextInt(100);
                coe = ran.nextInt(100);
                if(positiveOrNegative().equals("+")){
                    eq = String.valueOf(cox2)+"x^2";
                }
                else{
                    eq = "-"+String.valueOf(cox2)+"x^2";
                }

                eq= eq + positiveOrNegative()+ String.valueOf(cox)+"x";
                eq = eq + positiveOrNegative() + String.valueOf(coe);
                eq = eq + "=";

                int n = 2;
                int j;
                boolean first = false;
                for(int i=0;i<n;i++){
                    if(i == 0){
                        for(j=0;j< ran.nextInt(3);i++){
                            cox2 = ran.nextInt(100);
                            if(!first) {
                                eq = eq + String.valueOf(cox2) + "x^2";
                                first = true;
                            }else {
                                eq = eq + positiveOrNegative() + String.valueOf(cox2) + "x^2";
                            }
                        }
                    }
                    if(i==1){
                        for(j=0;j< ran.nextInt(3);i++){
                            cox = ran.nextInt(100);
                            if(!first) {
                                eq = eq + String.valueOf(cox) + "x^2";
                                first = true;
                            }else {
                                eq = eq + positiveOrNegative() + String.valueOf(cox) + "x";
                            }
                        }
                    }
                    if(i==2){
                        for(j=0;j< ran.nextInt(3);i++){
                            coe = ran.nextInt(100);
                            if(!first) {
                                eq = eq + String.valueOf(coe) + "x^2";
                                first = true;
                            }else{
                                eq = eq + positiveOrNegative()+String.valueOf(coe);
                            }
                        }
                    }
                }
        }
        return eq;
    }

    public String positiveOrNegative(){
        String sign;
        Random ran =new Random();
        if(ran.nextInt(10)%2==0){
            sign = "+";
        }else{
            sign ="-";
        }

        return sign;
    }
}
