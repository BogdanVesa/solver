package com.example.solver;

import java.util.Random;

public class Question {
    String q;
    String answer;
    String option1;
    String option2;
    String option3;
    String option4;

    public Question(){

    }
    public Question(String q, String answer){
        this.q = q;
        this.answer =answer;
        generateOptions();
    }

    private void generateOptions(){
        this.option1 = getOption(answer);
        while (this.option1.equals(answer)){
            this.option1 = getOption(answer);
        }
        this.option2 = getOption(answer);
        while (this.option2.equals(answer)){
            this.option2 = getOption(answer);
        }
        this.option3 = getOption(answer);
        while (this.option3.equals(answer)){
            this.option3 = getOption(answer);
        }
        this.option4 = getOption(answer);
        while (this.option4.equals(answer)){
            this.option4 = getOption(answer);
        }
        Random ran = new Random();
        switch (ran.nextInt(4-1)+1){
            case 1:
                option1 = answer;
                break;
            case 2:
                option2 = answer;
                break;
            case 3:
                option3 = answer;
                break;
            case 4:
                option4 = answer;
        }
    }

    private String getOption(String txt){
        if(txt.equals("x")){
            txt="1"+txt;
        }
        String numbers="1234567890";
        Random ran = new Random();
        char [] eq = txt.toCharArray();
        if(numbers.contains(String.valueOf(eq[0]))){
            eq[0]=Character.forDigit(ran.nextInt(10),10);
        }
        for(int i=1;i<txt.length(); i++){
            if(numbers.contains(String.valueOf(eq[i]))&& eq[i-1]!='^') {
                eq[i]=Character.forDigit(ran.nextInt(10),10);
            }
        }

        return  String.valueOf(eq);
    }


    @Override
    public String toString() {
        return "Question{" +
                "q='" + q + '\'' +
                ", answer='" + answer + '\'' +
                ", option1='" + option1 + '\'' +
                ", option2='" + option2 + '\'' +
                ", option3='" + option3 + '\'' +
                ", option4='" + option4 + '\'' +
                '}';
    }
}

