package com.example.solver;


import java.util.ArrayList;
import java.util.Random;

public class GenerateFirst implements Generate{
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
        String eq="";
        Random ran = new Random();
        switch (ran.nextInt(4-1)+1){
            case 1:
                int coe = ran.nextInt(10-1)+1;
                int con = ran.nextInt(10-1)+1;
                int result = ran.nextInt(50-1)+1;

                eq = String.valueOf(coe) +"x";
                if(ran.nextBoolean()){
                    eq=eq+"+";
                }
                else {
                    eq=eq+"-";
                }
                eq+= String.valueOf(con)+"="+String.valueOf(result);
                break;
            case 2:
                int coe2 = ran.nextInt(10-1)+1;
                int result2 = ran.nextInt(50-1)+1;

                if(ran.nextBoolean()){
                    eq=String.valueOf(coe2)+"*x="+String.valueOf(result2);
                }
                else {
                    eq+="x/"+String.valueOf(coe2)+"="+String.valueOf(result2);
                }
                break;
            case  3:
                int n = ran.nextInt(5-3)+2;
                String semn="";
                eq="";
                for(int i=0;i<n;i++) {
                    if(i==0){
                        eq+=String.valueOf(ran.nextInt(10-1)+1)+"x";
                        continue;
                    }

                    if(ran.nextBoolean()){
                        semn ="+";
                    }
                    else
                    {
                        semn = "-";
                    }
                    if (ran.nextBoolean()) {
                        if (ran.nextBoolean()) {
                            eq += semn+ String.valueOf(ran.nextInt(10 - 1) + 1) + "*" + String.valueOf(ran.nextInt(10 - 1) + 1) + "x";
                        } else {
                            eq += semn+ String.valueOf(ran.nextInt(10 - 1) + 1) + "x";
                        }
                    } else {
                        eq += semn+ String.valueOf(ran.nextInt(10 - 1) + 1);
                    }
                }
                eq+="=";
                for(int i=0;i<n;i++) {

                    if(ran.nextBoolean()){
                        semn ="+";
                    }
                    else
                    {
                        semn = "-";
                    }
                    if (ran.nextBoolean()) {
                        if (ran.nextBoolean()) {
                            eq += semn + String.valueOf(ran.nextInt(10 - 1) + 1) + "*" + String.valueOf(ran.nextInt(10 - 1) + 1) + "x";
                        } else {
                            eq += semn +String.valueOf(ran.nextInt(10 - 1) + 1) + "x";
                        }
                    } else {
                        eq += semn + String.valueOf(ran.nextInt(10 - 1) + 1);
                    }
                }
        }
        return eq;
    }
}
