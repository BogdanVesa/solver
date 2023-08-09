package com.example.solver;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Equation {
    String exp;
    Solve type;

    public Equation(String exp){
        this.exp = exp;
    }

    public void setType() {
        if(exp.contains("x^2")){
            this.type = new SecondDegree();
        }else if(exp.contains("x")){
            this.type = new FirstDegree();
        } else{
            this.type = new ZeroDegree();
        }
    }

    public ArrayList<EandQ> solve(){
        return this.type.Solve(this.exp);
    }
}

